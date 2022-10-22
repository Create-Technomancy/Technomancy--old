package dev.Cosmos616.technomancy.content.contraptions.energy.battery;

import com.simibubi.create.AllTileEntities;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.contraptions.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.contraptions.fluids.tank.FluidTankTileEntity;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.tileEntity.ComparatorUtil;
import dev.Cosmos616.technomancy.registry.TMTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SoulBatteryBlock extends Block implements ITE<SoulBatteryTileEntity> {

    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");

    private boolean creative;

    public static SoulBatteryBlock regular(Properties properties) {
        return new SoulBatteryBlock(properties, false);
    }

    public static SoulBatteryBlock creative(Properties properties) {
        return new SoulBatteryBlock(properties, true);
    }

    public SoulBatteryBlock(Properties properties, boolean creative) {
        super(properties);
        this.creative = creative;
        registerDefaultState(defaultBlockState()
                .setValue(TOP, true)
                .setValue(BOTTOM, true));
    }

    public static boolean isBattery(BlockState state) {
        return state.getBlock() instanceof SoulBatteryBlock;
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moved) {
        if (oldState.getBlock() == state.getBlock())
            return;
        if (moved)
            return;
        withTileEntityDo(world, pos, SoulBatteryTileEntity::updateConnectivity);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(TOP, BOTTOM);
    }

//    @Override
//    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
//        SoulBatteryTileEntity batteryAt = ConnectivityHandler.partAt(getTileEntityType(), world, pos);
//        if (batteryAt == null)
//            return 0;
//        SoulBatteryTileEntity controllerTE = batteryAt.getControllerTE();
//        if (controllerTE == null)
//            return 0;
//        return batteryAt.luminosity;
//    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
            BlockEntity te = world.getBlockEntity(pos);
            if (!(te instanceof SoulBatteryTileEntity))
                return;
            SoulBatteryTileEntity batteryTE = (SoulBatteryTileEntity) te;
            world.removeBlockEntity(pos);
            ConnectivityHandler.splitMulti(batteryTE);
        }
    }

//    @Override
//    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
//        BlockEntity te = state.hasBlockEntity() ? worldIn.getBlockEntity(pos) : null;
//        if(te != null) {
//            if(te instanceof SoulBatteryTileEntity) {
//                ((SoulBatteryTileEntity)te).updateCache();
//            }
//        }
//    }

    @Override
    public Class<SoulBatteryTileEntity> getTileEntityClass() {
        return SoulBatteryTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends SoulBatteryTileEntity> getTileEntityType() {
        return creative ? TMTileEntities.CREATIVE_SOUL_BATTERY.get() : TMTileEntities.SOUL_BATTERY.get();
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        return getTileEntityOptional(worldIn, pos).map(SoulBatteryTileEntity::getControllerTE)
                .map(te -> ComparatorUtil.fractionToRedstoneLevel(te.getChargeState()))
                .orElse(0);
    }
}

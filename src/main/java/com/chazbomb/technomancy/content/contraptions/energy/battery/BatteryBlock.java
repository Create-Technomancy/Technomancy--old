package com.chazbomb.technomancy.content.contraptions.energy.battery;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.ComparatorUtil;
import com.simibubi.create.foundation.utility.Lang;
import com.chazbomb.technomancy.registry.TMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
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
public class BatteryBlock extends Block implements IWrenchable, IBE<BatteryBlockEntity> {

    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
    public static final EnumProperty<Shape> SHAPE = EnumProperty.create("shape", Shape.class);

    private boolean creative;

    public static BatteryBlock regular(Properties properties) {
        return new BatteryBlock(properties, false);
    }

    public static BatteryBlock creative(Properties properties) {
        return new BatteryBlock(properties, true);
    }

    public BatteryBlock(Properties properties, boolean creative) {
        super(properties);
        this.creative = creative;
        registerDefaultState(defaultBlockState()
                .setValue(TOP, true)
                .setValue(BOTTOM, true)
                .setValue(SHAPE, Shape.SINGLE));
    }

    public static boolean isBattery(BlockState state) {
        return state.getBlock() instanceof BatteryBlock;
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moved) {
        if (oldState.getBlock() == state.getBlock())
            return;
        if (moved)
            return;
        withBlockEntityDo(world, pos, BatteryBlockEntity::updateConnectivity);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(TOP, BOTTOM, SHAPE);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
            BlockEntity te = world.getBlockEntity(pos);
            if (!(te instanceof BatteryBlockEntity))
                return;
            BatteryBlockEntity batteryTE = (BatteryBlockEntity) te;
            world.removeBlockEntity(pos);
            BatteryConnectivityHandler.splitMulti(batteryTE);
        }
    }

    @Override
    public Class<BatteryBlockEntity> getBlockEntityClass() {
        return BatteryBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BatteryBlockEntity> getBlockEntityType() {
        return creative ? TMBlockEntities.CREATIVE_BATTERY.get() : TMBlockEntities.BATTERY.get();
    }

    public enum Shape implements StringRepresentable {
        SINGLE, NW, SW, NE, SE;

        @Override
        public String getSerializedName() {
            return Lang.asId(name());
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        return getBlockEntityOptional(worldIn, pos).map(BatteryBlockEntity::getControllerBE)
                .map(te -> ComparatorUtil.fractionToRedstoneLevel(te.getChargeState()))
                .orElse(0);
    }
}

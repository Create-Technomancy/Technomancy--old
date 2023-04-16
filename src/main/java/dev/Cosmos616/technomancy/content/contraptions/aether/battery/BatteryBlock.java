package dev.Cosmos616.technomancy.content.contraptions.aether.battery;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.tileEntity.ComparatorUtil;
import com.simibubi.create.foundation.utility.Lang;
import dev.Cosmos616.technomancy.foundation.aether.AetherNetworkBlock;
import dev.Cosmos616.technomancy.registry.TMBlockEntities;
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
public class BatteryBlock extends Block implements AetherNetworkBlock, ITE<BatteryBlockEntity> {

    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
    public static final EnumProperty<Shape> SHAPE = EnumProperty.create("shape", Shape.class);

    private final boolean creative;

    public static BatteryBlock regular(Properties properties) {
        return new BatteryBlock(properties, false);
    }

    public static BatteryBlock creative(Properties properties) {
        return new BatteryBlock(properties, true);
    }
    
    public boolean isCreative() {
        return creative;
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
        withTileEntityDo(world, pos, BatteryBlockEntity::updateConnectivity);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TOP, BOTTOM, SHAPE);
    }
    
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
            BlockEntity te = world.getBlockEntity(pos);
            if (!(te instanceof BatteryBlockEntity))
                return;
            BatteryBlockEntity batteryTE = (BatteryBlockEntity) te;
            world.removeBlockEntity(pos);
            ConnectivityHandler.splitMulti(batteryTE);
        }
    }
    
    @Override
    public Class<BatteryBlockEntity> getTileEntityClass() {
        return BatteryBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BatteryBlockEntity> getTileEntityType() {
        return creative ? TMBlockEntities.CREATIVE_BATTERY.get() : TMBlockEntities.BATTERY.get();
    }

    public enum Shape implements StringRepresentable {
        SINGLE, NW, SW, NE, SE;

        @Override
        public String getSerializedName() {
            return Lang.asId(name());
        }
    }
    
}

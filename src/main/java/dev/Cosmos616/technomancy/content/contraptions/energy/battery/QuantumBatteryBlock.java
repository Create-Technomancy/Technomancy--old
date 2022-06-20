package dev.Cosmos616.technomancy.content.contraptions.energy.battery;

import com.simibubi.create.foundation.block.ITE;
import dev.Cosmos616.technomancy.registry.TMTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class QuantumBatteryBlock extends Block implements ITE<QuantumBatteryTileEntity> {

    public QuantumBatteryBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
            BlockEntity te = level.getBlockEntity(pos);
            if (!(te instanceof QuantumBatteryTileEntity))
                return;
            level.removeBlockEntity(pos);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        BlockEntity te = state.hasBlockEntity() ? worldIn.getBlockEntity(pos) : null;
        if(te != null) {
            if(te instanceof QuantumBatteryTileEntity) {
                ((QuantumBatteryTileEntity)te).updateCache();
            }
        }
    }

    @Override
    public Class<QuantumBatteryTileEntity> getTileEntityClass() {
        return QuantumBatteryTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends QuantumBatteryTileEntity> getTileEntityType() {
        return TMTileEntities.QUANTUM_BATTERY.get();
    }
}

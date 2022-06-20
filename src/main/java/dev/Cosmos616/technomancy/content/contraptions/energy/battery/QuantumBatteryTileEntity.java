package dev.Cosmos616.technomancy.content.contraptions.energy.battery;

import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.tileEntity.IMultiTileContainer;
import dev.Cosmos616.technomancy.content.contraptions.energy.QuantumTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.IEnergyStorage;

public class QuantumBatteryTileEntity extends QuantumTileEntity implements IHaveGoggleInformation, IMultiTileContainer {

    public QuantumBatteryTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 100000, 1000, 1000);
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide())
            return;

        for (Direction d : Direction.values()) {
            IEnergyStorage ies = getCachedEnergy(d);
            if (ies == null)
                continue;
            int ext = quantumStorage.extractEnergy(1000, false);
            int ret = ext - ies.receiveEnergy(ext, false);
            quantumStorage.receiveEnergy(ret, false);
        }
    }

    @Override
    public BlockPos getController() {
        return null;
    }

    @Override
    public boolean isController() {
        return false;
    }

    @Override
    public void setController(BlockPos pos) {

    }

    @Override
    public BlockPos getLastKnownPos() {
        return null;
    }
}

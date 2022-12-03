package dev.Cosmos616.technomancy.content.contraptions.energy.battery;

import dev.Cosmos616.technomancy.foundation.energy.SoulEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CreativeBatteryTileEntity extends BatteryTileEntity {
    public CreativeBatteryTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected SoulEnergyStorage createInventory() {
        return new CreativeSoulEnergyStorage(getCapacityMultiplier());
    }

    public static class CreativeSoulEnergyStorage extends SoulEnergyStorage {

        public CreativeSoulEnergyStorage(int capacity) {
            super(capacity);
        }

        public int receiveEnergy(int maxReceive, boolean simulate)
        {
            return maxReceive;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return maxExtract;
        }
    }
}

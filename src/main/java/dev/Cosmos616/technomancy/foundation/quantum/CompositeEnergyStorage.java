package dev.Cosmos616.technomancy.foundation.quantum;

import net.minecraftforge.energy.IEnergyStorage;

public class CompositeEnergyStorage implements IEnergyStorage {

    protected IEnergyStorage[] energyStorages;
    protected int capacity = 0;
    protected int energy = 0;

    public CompositeEnergyStorage(IEnergyStorage... energyStorages) {
        this.energyStorages = energyStorages;
        for (IEnergyStorage storage : energyStorages) {
            capacity += storage.getMaxEnergyStored();
            energy += storage.getEnergyStored();
        }
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;
        int energyReceived = 0;
        for (IEnergyStorage storage : energyStorages) {
            energyReceived += storage.receiveEnergy(maxReceive, simulate);
            maxReceive -= energyReceived;
            if (maxReceive <= 0)
                break;
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canReceive())
            return 0;
        int energyExtracted = 0;
        for (IEnergyStorage storage : energyStorages) {
            energyExtracted += storage.extractEnergy(maxExtract, simulate);
            maxExtract -= energyExtracted;
            if (maxExtract <= 0)
                break;
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        int totalEnergyStored = 0;
        for (IEnergyStorage storage : energyStorages) {
            totalEnergyStored += storage.getEnergyStored();
        }
        return totalEnergyStored;
    }

    @Override
    public int getMaxEnergyStored() {
        int totalMaxEnergyStored = 0;
        for (IEnergyStorage storage : energyStorages) {
            totalMaxEnergyStored += storage.getMaxEnergyStored();
        }
        return totalMaxEnergyStored;
    }

    @Override
    public boolean canExtract() {
        for (IEnergyStorage storage : energyStorages) {
            if (storage.canExtract())
                return true;
        }
        return false;
    }

    @Override
    public boolean canReceive() {
        for (IEnergyStorage storage : energyStorages) {
            if (storage.canReceive())
                return true;
        }
        return false;
    }
}

package dev.Cosmos616.technomancy.foundation.energy;

import net.minecraftforge.energy.EnergyStorage;

public class SoulEnergyStorage extends EnergyStorage {

    public SoulEnergyStorage(int capacity) {
        super(capacity);
    }

    public SoulEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public SoulEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public SoulEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public int setEnergy(int energy) {
        this.energy = Math.max(this.capacity, energy);
        return this.energy;
    }

    public int setCapacity(int capacity) {
        this.capacity = capacity;
        return this.capacity;
    }

    public int getSpace() {
        return this.capacity - this.energy;
    }
}

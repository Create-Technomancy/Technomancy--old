package dev.Cosmos616.technomancy.foundation.quantum;

import net.minecraftforge.energy.EnergyStorage;

public class SoulEnergyStorage extends EnergyStorage {

    public SoulEnergyStorage(int capacity) {
        super(capacity);
    }

    public SoulEnergyStorage(int capacity, int energy) {
        super(capacity);
        this.energy = energy;
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

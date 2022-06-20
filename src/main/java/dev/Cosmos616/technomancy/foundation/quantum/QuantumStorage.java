package dev.Cosmos616.technomancy.foundation.quantum;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.energy.EnergyStorage;

public class QuantumStorage extends EnergyStorage {


    public QuantumStorage(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public QuantumStorage(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public QuantumStorage(int capacity, int maxReceive, int maxExtract) {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public QuantumStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    /*@Override
    public int receiveEnergy(int maxInsert, boolean simulate) {
        return 0;
    }

    public int receiveEnergyInternal(int maxInsert, boolean simulate) {
        return super.receiveEnergy(maxInsert, simulate);
    }*/

    /*@Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    public int extractEnergyInternal(int maxExtract, boolean simulate) {
        return super.extractEnergy(maxExtract, simulate);
    }*/

    public CompoundTag write(CompoundTag nbt) {
        nbt.putInt("charge", energy);
        return nbt;
    }

    public void read(CompoundTag nbt) {
        this.energy = nbt.getInt("charge");
    }
}

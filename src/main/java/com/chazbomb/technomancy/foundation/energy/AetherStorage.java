package com.chazbomb.technomancy.foundation.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

import java.util.function.Consumer;

public class AetherStorage implements IAetherStorage {

    protected int aether;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;
    private Consumer<Integer> updateCallback;

    public AetherStorage(int capacity, Consumer<Integer> updateCallback)
    {
        this(capacity, capacity, capacity, 0, updateCallback);
    }

    public AetherStorage(int capacity, int maxTransfer, Consumer<Integer> updateCallback)
    {
        this(capacity, maxTransfer, maxTransfer, 0 ,updateCallback);
    }

    public AetherStorage(int capacity, int maxReceive, int maxExtract, Consumer<Integer> updateCallback)
    {
        this(capacity, maxReceive, maxExtract, 0, updateCallback);
    }

    public AetherStorage(int capacity, int maxReceive, int maxExtract, int aether, Consumer<Integer> updateCallback)
    {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.aether = Mth.clamp(aether, 0, capacity);
        this.updateCallback = updateCallback;
    }

    public int generateAether(int toGenerate, boolean simulate) {
        int generated = Math.min(getSpace(), toGenerate);
        if (!simulate) {
            aether += generated;
            onContentsChanged();
        }
        return generated;
    }

    @Override
    public int receiveAether(int toReceive, boolean simulate) {
        return canReceive() ? generateAether(Math.min(toReceive, maxReceive), simulate) : 0;
    }

    public int consumeAether(int toConsume, boolean simulate) {
        int consumed = Math.min(aether, toConsume);
        if (!simulate) {
            aether -= consumed;
            onContentsChanged();
        }
        return consumed;
    }

    @Override
    public int extractAether(int toExtract, boolean simulate) {
        return canExtract() ? consumeAether(Math.min(toExtract, maxExtract), simulate) : 0;
    }

    @Override
    public int getAetherStored() {
        return aether;
    }

    @Override
    public int getAetherCapacity() {
        return capacity;
    }

    public int getSpace() {
        return capacity - aether;
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0;
    }

    public void setAether(int aether) {
        this.aether = Math.min(aether, capacity);
    }

    public void setAetherCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setRetrieval(int maxReceive) {
        this.maxReceive = maxReceive;
    }

    public void setExtraction(int maxExtract) {
        this.maxExtract = maxExtract;
    }

    protected void onContentsChanged() {
        updateCallback.accept(getAetherStored());
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        nbt.putInt("Aether", aether);
        nbt.putInt("Capacity", capacity);
        nbt.putInt("MaxReceive", maxReceive);
        nbt.putInt("MaxExtract", maxExtract);
        return nbt;
    }

    public AetherStorage readFromNBT(CompoundTag nbt) {
        this.aether = nbt.getInt("Aether");
        this.capacity = nbt.getInt("Capacity");
        this.maxReceive = nbt.getInt("MaxReceive");
        this.maxExtract = nbt.getInt("MaxExtract");
        return this;
    }
}

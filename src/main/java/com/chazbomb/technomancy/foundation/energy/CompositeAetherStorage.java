package com.chazbomb.technomancy.foundation.energy;

public class CompositeAetherStorage implements IAetherStorage {

    protected IAetherStorage[] cells;
    protected int aether = 0;
    protected int capacity = 0;

    public CompositeAetherStorage(IAetherStorage... pools) {
        this.cells = pools;
        for (IAetherStorage storage : pools) {
            aether += storage.getAetherStored();
            capacity += storage.getAetherCapacity();
        }
    }

    @Override
    public int receiveAether(int toReceive, boolean simulate) {
        if (!canReceive())
            return 0;
        int received = toReceive;
        for (IAetherStorage storage : cells) {
            toReceive -= storage.receiveAether(toReceive, simulate);
            if (toReceive <= 0)
                break;
        }
        return received - toReceive;
    }

    @Override
    public int extractAether(int toExtract, boolean simulate) {
        if (!canReceive())
            return 0;
        int extracted = toExtract;
        for (IAetherStorage storage : cells) {
            toExtract -= storage.extractAether(toExtract, simulate);
            if (toExtract <= 0)
                break;
        }
        return extracted - toExtract;
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
        for (IAetherStorage storage : cells) {
            if (storage.canExtract())
                return true;
        }
        return false;
    }

    @Override
    public boolean canReceive() {
        for (IAetherStorage storage : cells) {
            if (storage.canReceive())
                return true;
        }
        return false;
    }
}

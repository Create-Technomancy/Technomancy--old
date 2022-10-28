package dev.Cosmos616.technomancy.foundation.energy;

public interface ISoulEnergyStorage {
	int receiveSouls(int retrieval, boolean simulate);
	int extractSouls(int extracted, boolean simulate);
	
	int getStoredSouls();
	int getSoulCapacity();
	
	boolean canExtract();
	boolean canReceive();
}

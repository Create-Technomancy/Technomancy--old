package com.chazbomb.technomancy.foundation.energy;

public interface IAetherStorage {
	int receiveAether(int toReceive, boolean simulate);
	int extractAether(int toExtract, boolean simulate);
	
	int getAetherStored();
	int getAetherCapacity();
	
	boolean canExtract();
	boolean canReceive();
}

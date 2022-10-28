package dev.Cosmos616.technomancy.foundation.energy;

public class SoulStorage implements ISoulEnergyStorage {
	protected int souls;
	protected int capacity;
	
	protected int maxReceive;
	protected int maxExtract;
	
	public SoulStorage(int capacity)
	{
		this(capacity, capacity, capacity, 0);
	}
	
	public SoulStorage(int capacity, int maxTransfer)
	{
		this(capacity, maxTransfer, maxTransfer, 0);
	}
	
	public SoulStorage(int capacity, int maxReceive, int maxExtract)
	{
		this(capacity, maxReceive, maxExtract, 0);
	}
	
	public SoulStorage(int capacity, int maxReceive, int maxExtract, int souls)
	{
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
		this.souls = Math.max(0 , Math.min(capacity, souls));
	}
	
	public int receiveSouls(int receive) {
		return this.receiveSouls(receive, false);
	}
	@Override
	public int receiveSouls(int receive, boolean simulate) {
		if (!this.canReceive())
			return 0;
		
		int soulsReceived = Math.min(this.capacity - this.souls, Math.min(this.maxReceive, receive));
		if (!simulate)
			this.souls += soulsReceived;
		return soulsReceived;
	}
	
	public int extractSouls(int extract) {
		return this.extractSouls(extract, false);
	}
	@Override
	public int extractSouls(int extract, boolean simulate) {
		if (!this.canExtract())
			return 0;
		
		int soulsExtracted = Math.min(this.souls, Math.min(this.maxExtract, extract));
		if (!simulate)
			this.souls -= soulsExtracted;
		return soulsExtracted;
	}
	
	@Override
	public int getStoredSouls() {
		return this.souls;
	}
	@Override
	public int getSoulCapacity() {
		return this.capacity;
	}
	
	@Override
	public boolean canExtract() {
		return this.maxExtract > 0;
	}
	@Override
	public boolean canReceive() {
		return this.maxReceive > 0;
	}
	
	public int setCapacity(int capacity) {
		return (this.capacity = capacity);
	}
	public int setSouls(int souls) {
		return (this.souls = Math.min(souls, this.capacity));
	}
	
	public int getSpace() {
		return this.capacity - this.souls;
	}
}

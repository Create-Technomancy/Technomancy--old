package dev.Cosmos616.technomancy.foundation.energy;

import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.tileEntity.behaviour.BehaviourType;
import dev.Cosmos616.technomancy.Technomancy;
import dev.Cosmos616.technomancy.foundation.TMLang;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;

public class SoulEnergyTileBehavior extends TileEntityBehaviour implements ISoulEnergyStorage {
	public static final BehaviourType<SoulEnergyTileBehavior> TYPE = new BehaviourType<>();
	
	private static final int SYNC_RATE = 8;
	
	protected Runnable energyUpdateCallback = () -> {};
	protected LazyOptional<ISoulEnergyStorage> capability;
	
	protected int capacity;
	protected int souls;
	protected boolean extractionAllowed;
	protected boolean insertionAllowed;
	private int syncCooldown;
	
	public SoulEnergyTileBehavior(SmartTileEntity te, int capacity) {
		super(te);
		this.capacity = capacity;
		this.extractionAllowed = true;
		this.insertionAllowed = true;
		
		this.syncCooldown = 0;
		
		this.capability = LazyOptional.of(() -> this);
	}
	
	public SoulEnergyTileBehavior onEnergyUpdate(Runnable energyUpdateCallback) {
		this.energyUpdateCallback = energyUpdateCallback;
		return this;
	}
	
//	@Override
//	public void tick() {
//		super.tick();
//		if (syncCooldown > 0) {
//			syncCooldown--;
//			updateEnergy();
//		}
//	}
	
	@Override
	public void read(CompoundTag nbt, boolean clientPacket) {
		super.read(nbt, clientPacket);
		insertionAllowed = nbt.getBoolean("AllowInsertion");
		extractionAllowed = nbt.getBoolean("AllowExtraction");
		capacity = nbt.getInt("Capacity");
		souls = nbt.getInt("Souls");
	}
	
	@Override
	public void write(CompoundTag nbt, boolean clientPacket) {
		super.write(nbt, clientPacket);
		nbt.putBoolean("AllowInsertion", insertionAllowed);
		nbt.putBoolean("AllowExtraction", extractionAllowed);
		nbt.putInt("Capacity", capacity);
		nbt.putInt("Souls", souls);
	}
	
	protected void updateEnergy() {
		this.energyUpdateCallback.run();
		this.tileEntity.sendData();
		this.tileEntity.setChanged();
	}
	
	@Override
	public int receiveSouls(int retrieval, boolean simulate) {
		if (!this.canReceive())
			return 0;
		
		int soulsReceived = Math.min(this.capacity - this.souls, retrieval);
		if (!simulate) {
			this.souls += soulsReceived;
			updateEnergy();
		}
		return soulsReceived;
	}
	
	public int generateSouls(int generated) {
		int soulsGenerated = Math.min(this.capacity - this.souls, generated);
		this.souls += soulsGenerated;
		updateEnergy();
		return soulsGenerated;
	}
	
	@Override
	public int extractSouls(int extracted, boolean simulate) {
		if (!this.canExtract())
			return 0;
		
		int soulsExtracted = Math.min(this.souls, extracted);
		if (!simulate) {
			this.souls -= soulsExtracted;
			updateEnergy();
		}
		return soulsExtracted;
	}
	
	@Override
	public int getStoredSouls() {
		return souls;
	}
	
	@Override
	public int getCapacity() {
		return this.capacity;
	}
	
	@Override
	public boolean canExtract() {
		return extractionAllowed;
	}
	
	@Override
	public boolean canReceive() {
		return insertionAllowed;
	}
	
	public SoulEnergyTileBehavior forbidInsertion() {
		this.insertionAllowed = false;
		return this;
	}
	
	public SoulEnergyTileBehavior forbidExtraction() {
		this.extractionAllowed = false;
		return this;
	}
	
	public LazyOptional<ISoulEnergyStorage> getCapability() {
		return capability;
	}
	
	public boolean containedEnergyTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		TMLang.translate("gui.goggles.energy_container")
				.forGoggles(tooltip);
		String energyAmount = "";
		float inc = capacity / 10f;
		for (int x = 1; x <= 10f; x++) {
			float stored = inc * x;
			energyAmount += stored <= souls ? "\u2588" : "\u2592";
		}
		TMLang.lang()
				.text(energyAmount)
				.style(ChatFormatting.GOLD)
				.forGoggles(tooltip, 1);
		return true;
	}
	
	@Override
	public BehaviourType<?> getType() {
		return TYPE;
	}
}

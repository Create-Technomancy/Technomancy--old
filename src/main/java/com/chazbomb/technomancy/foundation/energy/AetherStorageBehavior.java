package com.chazbomb.technomancy.foundation.energy;


import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.chazbomb.technomancy.foundation.TMLang;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class AetherStorageBehavior extends BlockEntityBehaviour {

	public static final BehaviourType<AetherStorageBehavior> TYPE = new BehaviourType<>();

	protected AetherStorage aether;
	protected LazyOptional<IAetherStorage> capability;

	public static AetherStorageBehavior generating(SmartBlockEntity te, int capacity,  Consumer<Integer> updateCallback) {
		return new AetherStorageBehavior(te, capacity, 0, capacity, updateCallback);
	}

	public static AetherStorageBehavior consuming(SmartBlockEntity te, int capacity, Consumer<Integer> updateCallback) {
		return new AetherStorageBehavior(te, capacity, capacity, 0, updateCallback);
	}

	public AetherStorageBehavior(SmartBlockEntity te, int capacity, int maxReceive, int maxExtract,  Consumer<Integer> updateCallback) {
		super(te);
		this.aether = new AetherStorage(capacity, maxReceive, maxExtract, updateCallback);
		this.capability = LazyOptional.of(() -> aether);
	}

//	@Override
//	public void tick() {
//
//	}

	/**
	 * doesnt work for some reason
	 * pls someone deal with this
	 * -DrMangoTea
	 */
	/*
	@Override
	public void remove() {
		super.remove();
		capability.invalidate();
	}

	 */


	public AetherStorage getHandler() {
		return aether;
	}

	public LazyOptional<IAetherStorage> getCapability() {
		return capability;
	}

	@Override
	public void write(CompoundTag nbt, boolean clientPacket) {
		super.write(nbt, clientPacket);
		nbt.put("AetherStorage", aether.writeToNBT(new CompoundTag()));
	}

	@Override
	public void read(CompoundTag nbt, boolean clientPacket) {
		super.read(nbt, clientPacket);
		aether.readFromNBT(nbt.getCompound("AetherStorage"));
	}

	// TODO: Move somewhere else maybe?
	public static boolean containedAetherTooltip(List<Component> tooltip, LazyOptional<IAetherStorage> handler) {
		Optional<IAetherStorage> resolve = handler.resolve();
		if (!resolve.isPresent())
			return false;

		IAetherStorage energy = resolve.get();

		TMLang.translate("gui.goggles.aether_container").forGoggles(tooltip);
		LangBuilder qu = TMLang.translate("generic.unit.aether");
		Lang.builder()
				.add(Lang.number(energy.getAetherStored())
					.style(ChatFormatting.AQUA))
				.text(ChatFormatting.GRAY, " / ")
				.add(Lang.number(energy.getAetherCapacity())
					.space()
					.add(qu)
					.style(ChatFormatting.DARK_GRAY))
				.forGoggles(tooltip, 1);
		return true;
	}
	
	@Override
	public BehaviourType<?> getType() {
		return TYPE;
	}
}

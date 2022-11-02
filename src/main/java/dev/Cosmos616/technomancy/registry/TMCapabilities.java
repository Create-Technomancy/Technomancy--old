package dev.Cosmos616.technomancy.registry;

import dev.Cosmos616.technomancy.foundation.energy.ISoulEnergyStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

// This is to not be initialized before the energy is registered
public class TMCapabilities {
	public static final Capability<ISoulEnergyStorage> SOUL_ENERGY = CapabilityManager.get(new CapabilityToken<>(){});
}

package dev.Cosmos616.technomancy.registry;

import dev.Cosmos616.technomancy.foundation.LEGACYenergy.IAetherStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

// This is to not be initialized before the energy is registered
public class TMCapabilities {
	public static final Capability<IAetherStorage> AETHER = CapabilityManager.get(new CapabilityToken<>(){});
}

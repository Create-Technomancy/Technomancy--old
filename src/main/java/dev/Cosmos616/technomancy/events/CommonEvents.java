package dev.Cosmos616.technomancy.events;

import dev.Cosmos616.technomancy.Technomancy;
import dev.Cosmos616.technomancy.foundation.LEGACYenergy.IAetherStorage;
import dev.Cosmos616.technomancy.foundation.aether.AetherNetwork;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class CommonEvents {

//    @SubscribeEvent
//    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
////        CapabilityChunkController.attach(event);
//        event.getObject()
//    }

    @SubscribeEvent
    public static void attachChunkCapabilities(AttachCapabilitiesEvent<LevelChunk> event){
//        event.addCapability(Technomancy.TMLoc("quantum_field"), new QuantumHandler());
//        QuantumHandler.attach(event);
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event){
        if (event.phase == TickEvent.Phase.START) {
            List<AetherNetwork> invalidNetworks = new ArrayList<>();
            for (AetherNetwork network : AetherNetwork.ALL_NETWORKS) {
                if (network.getNetworkElements().size() == 0)
                    invalidNetworks.add(network);
                else
                    network.tickNetwork();
            }
            invalidNetworks.forEach(AetherNetwork.ALL_NETWORKS::remove);
        }
    }
    
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {

        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
//            event.register(QuantumHandler.class);
            event.register(IAetherStorage.class); // Soul Energy
        }

    }

}

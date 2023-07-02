package com.chazbomb.technomancy.events;


import com.chazbomb.technomancy.foundation.energy.IAetherStorage;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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

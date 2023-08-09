package com.chazbomb.technomancy;

import com.chazbomb.technomancy.content.decoration.palettes.TMPaletteBlocks;
import com.chazbomb.technomancy.registry.*;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.chazbomb.technomancy.foundation.TMLangPartials;
import com.simibubi.create.foundation.data.LangMerger;
import net.minecraft.client.Minecraft;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Technomancy.MOD_ID)
public class Technomancy {
    public static final String MOD_ID = "technomancy";
    public static final String NAME = "Technomancy";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    public Technomancy() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        forgeEventBus.register(this);

        REGISTRATE.registerEventListeners(FMLJavaModLoadingContext.get().getModEventBus());

        TMBlocks.register();
        TMItems.register();
        TMFluids.register();
        TMBlockEntities.register();
        TMBlockPartials.register();
        TMEntities.register();
        TMItemGroups.register();
        TMTags.register();
        TMPackets.registerPackets();
        TMPaletteBlocks.register();

        modEventBus.addListener(EventPriority.LOWEST, Technomancy::gatherData);

        //noinspection ConstantValue - required for datagen to work
        if(Minecraft.getInstance() != null)
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> TechnomancyClient.onClient(modEventBus, forgeEventBus));
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        if (event.includeClient()) {
            gen.addProvider(new LangMerger(gen, MOD_ID, "Technomancy", TMLangPartials.values()));
        }
    }

    public static ResourceLocation asResource(String path) { return new ResourceLocation(MOD_ID, path); }
}

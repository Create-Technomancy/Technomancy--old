package dev.Cosmos616.technomancy;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.Cosmos616.technomancy.foundation.LangMerger;
import dev.Cosmos616.technomancy.foundation.TMLangPartials;
import dev.Cosmos616.technomancy.registry.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
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

    private static final NonNullSupplier<CreateRegistrate> REGISTRATE = CreateRegistrate.lazy(MOD_ID);


    public Technomancy() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        forgeEventBus.register(this);

        TMBlocks.register();
        TMItems.register();
        TMFluids.register();
        TMBlockEntities.register();
        TMBlockPartials.init();
        TMEntities.register();
        TMTags.register();
        TMPackets.registerPackets();
        
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> TechnomancyClient.onClient(modEventBus, forgeEventBus));
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        if (event.includeClient()) {
            gen.addProvider(new LangMerger(gen, MOD_ID, "Technomancy", TMLangPartials.values()));
        }
    }

    public static ResourceLocation TMLoc(String path) { return new ResourceLocation(MOD_ID, path); }

    public static CreateRegistrate getRegistrate() {
        return REGISTRATE.get();
    }
}

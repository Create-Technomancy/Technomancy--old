package dev.Cosmos616.technomancy;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.nullness.NonNullSupplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Technomancy.MOD_ID)
public class Technomancy {
    public static final String MOD_ID = "technomancy";
    private static final Logger LOGGER = LogManager.getLogger();

    private static final NonNullSupplier<CreateRegistrate> REGISTRATE = CreateRegistrate.lazy(MOD_ID);

    public Technomancy() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

//        TMItems.register();
//        TMBlocks.register();
//        TMTileEntities.register();
//        TMBlockPartials.register();

    }

    public static ResourceLocation CTLoc(String path) { return new ResourceLocation(MOD_ID, path); }

    public static CreateRegistrate getRegistrate() {
        return REGISTRATE.get();
    }
}

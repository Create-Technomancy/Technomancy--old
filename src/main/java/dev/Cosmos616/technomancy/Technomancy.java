package dev.Cosmos616.technomancy;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.Cosmos616.technomancy.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
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
    public static final CreativeModeTab BASE_CREATIVE_TAB = new CreativeModeTab("technomancy"){
        @Override
        public ItemStack makeIcon(){
            return new ItemStack(TMBlocks.SOUL_BATTERY_BLOCK.get());
        }};

    public Technomancy() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        TMBlocks.register();
        TMItems.register();
        TMFluids.register();
        TMTileEntities.register();
        TMBlockPartials.init();

    }

    public static ResourceLocation TMLoc(String path) { return new ResourceLocation(MOD_ID, path); }

    public static CreateRegistrate getRegistrate() {
        return REGISTRATE.get();
    }
}

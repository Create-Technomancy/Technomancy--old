package dev.Cosmos616.technomancy.registry;

import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.fluids.VirtualFluid;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import dev.Cosmos616.technomancy.Technomancy;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class TMFluids {

    private static final CreateRegistrate REGISTRATE = Technomancy.getRegistrate();
    
    private static FluidBuilder<ForgeFlowingFluid.Flowing, CreateRegistrate> fluid(String name, NonNullBiFunction<FluidAttributes.Builder, Fluid, FluidAttributes> attributesFactory) {
        return REGISTRATE.fluid(name, new ResourceLocation(REGISTRATE.getModid(), "fluid/" + name + "_flow"), new ResourceLocation(REGISTRATE.getModid(), "fluid/" + name + "_flow"),
                attributesFactory);
    }

    public static final FluidEntry<ForgeFlowingFluid.Flowing> LIQUID_SOULS =
            fluid("liquid_souls", NoColorFluidAttributes::new)
                    .lang(f -> "fluid.technomancy.liquid_souls", "Liquid Souls")
                    .attributes(b -> b.viscosity(2000)
                            .density(1400))
                    .properties(p -> p.levelDecreasePerBlock(2)
                            .tickRate(25)
                            .slopeFindDistance(3)
                            .explosionResistance(100f))
                    .tag(AllTags.forgeFluidTag("liquid_souls"), FluidTags.WATER)
                    .source(ForgeFlowingFluid.Source::new)
                    .bucket()
                    .tag(AllTags.forgeItemTag("buckets/liquid_souls"))
                    .build()
                    .register();

    public static void register() {}

    private static class NoColorFluidAttributes extends FluidAttributes {

        protected NoColorFluidAttributes(Builder builder, Fluid fluid) {
            super(builder, fluid);
        }

        @Override
        public int getColor(BlockAndTintGetter world, BlockPos pos) {
            return 0x00ffffff;
        }

    }
}

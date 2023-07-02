package com.chazbomb.technomancy.registry;

import com.chazbomb.technomancy.Technomancy;
import com.simibubi.create.AllTags;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import static com.chazbomb.technomancy.Technomancy.REGISTRATE;

public class TMFluids {
    private static FluidBuilder<ForgeFlowingFluid.Flowing, CreateRegistrate> fluid(String name, NonNullBiFunction<FluidAttributes.Builder, Fluid, FluidAttributes> attributesFactory,
                                                                                   int viscosity, int density, int levelDecreasePerBlock, int tickRate, int slopeFindDistance,
                                                                                   float explosionResistance) {
        String realName = name.replace('_', ' ').transform(s -> {
            String result = "";
            for(int i = 0; i < s.length(); i++) {
                if(i == 0 || s.charAt(i - 1) == ' ')
                    result += Character.toUpperCase(s.charAt(i));
                else
                    result += s.charAt(i);
            }
            return result;
        });
        return REGISTRATE.fluid(name, Technomancy.asResource("fluid/" + name + "_flow"),
                        Technomancy.asResource("fluid/" + name + "_flow"), attributesFactory)
                .lang(f -> "fluid.technomancy." + name, realName)
                .attributes(b -> b.viscosity(viscosity).density(density))
                .properties(p -> p.levelDecreasePerBlock(levelDecreasePerBlock)
                        .tickRate(tickRate)
                        .slopeFindDistance(slopeFindDistance)
                        .explosionResistance(explosionResistance));
    }
    private static FluidBuilder<ForgeFlowingFluid.Flowing, CreateRegistrate> fluid(String name, NonNullBiFunction<FluidAttributes.Builder, Fluid, FluidAttributes> attributesFactory) {
        return fluid(name, attributesFactory, 2000, 1400, 2, 25, 3, 100f);
    }

    public static final FluidEntry<ForgeFlowingFluid.Flowing> LIQUID_SOULS =
            fluid("liquid_souls", NoColorFluidAttributes::new)
                    .tag(AllTags.forgeFluidTag("liquid_souls"), FluidTags.WATER)
                    .source(ForgeFlowingFluid.Source::new)
                    .bucket()
                    .tag(AllTags.forgeItemTag("buckets/liquid_souls"))
                    .build()
                    .register();

    public static void register() {
        Technomancy.LOGGER.info("Registering fluids!");
    }


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

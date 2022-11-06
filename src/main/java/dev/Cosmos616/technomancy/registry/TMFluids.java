package dev.Cosmos616.technomancy.registry;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import dev.Cosmos616.technomancy.Technomancy;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class TMFluids {

    private static final CreateRegistrate REGISTRATE = Technomancy.getRegistrate();

//    public static final FluidEntry<VirtualFluid> LIQUID_SOULS = REGISTRATE.virtualFluid("liquid_souls")
//            .lang(f -> "fluid.technomancy.liquid_souls", "Souls")
//            .tag(AllTags.forgeFluidTag("liquid_souls"))
//            .register();

//    public static final FluidEntry<ForgeFlowingFluid.Flowing> LIQUID_SOULS =
//            REGISTRATE.standardFluid("liquid_souls", NoColorFluidAttributes::new)
//                    .lang(f -> "fluid.technomancy.liquid_souls", "Nightmare Fuel")
//                    .attributes(b -> b.viscosity(2000)
//                            .density(1400))
//                    .properties(p -> p.levelDecreasePerBlock(2)
//                            .tickRate(25)
//                            .slopeFindDistance(3)
//                            .explosionResistance(100f))
//                    .tag(AllTags.AllFluidTags.HONEY.tag)
//                    .source(ForgeFlowingFluid.Source::new)
//                    .bucket()
//                    .tag(AllTags.forgeItemTag("buckets/liquid_souls"))
//                    .build()
//                    .register();

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

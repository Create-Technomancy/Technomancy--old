package dev.Cosmos616.technomancy.foundation.world.feature;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class PlacedFeatures {
      public static final Holder<PlacedFeature> ZIRCONIUM_ORE_PLACED = PlacementUtils.register("zirconium_ore_placed",
            ConfiguredFeatures.ZIRCONIUM_ORE, OrePlacement.commonOrePlacement(13, // VeinsPerChunk
                    HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(120))));
}

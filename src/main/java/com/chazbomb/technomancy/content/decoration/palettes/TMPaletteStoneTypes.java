package com.chazbomb.technomancy.content.decoration.palettes;

import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.utility.Lang;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;

import static com.chazbomb.technomancy.content.decoration.palettes.PaletteBlockPattern.VANILLA_RANGE;


public enum TMPaletteStoneTypes {

	BLACKSTONE(VANILLA_RANGE, r -> () -> Blocks.BLACKSTONE),
	;

	private Function<CreateRegistrate, NonNullSupplier<Block>> factory;
	private PalettesVariantEntry variants;

	public NonNullSupplier<Block> baseBlock;
	public PaletteBlockPattern[] variantTypes;
	public TagKey<Item> materialTag;

	TMPaletteStoneTypes(PaletteBlockPattern[] variantTypes,
						Function<CreateRegistrate, NonNullSupplier<Block>> factory) {
		this.factory = factory;
		this.variantTypes = variantTypes;
	}

	public NonNullSupplier<Block> getBaseBlock() {
		return baseBlock;
	}

	public PalettesVariantEntry getVariants() {
		return variants;
	}

	public static void register(CreateRegistrate registrate) {
		for (TMPaletteStoneTypes paletteStoneVariants : values()) {
			NonNullSupplier<Block> baseBlock = paletteStoneVariants.factory.apply(registrate);
			paletteStoneVariants.baseBlock = baseBlock;
			String id = Lang.asId(paletteStoneVariants.name());
			paletteStoneVariants.materialTag =
				AllTags.optionalTag(ForgeRegistries.ITEMS, Create.asResource("stone_types/" + id));
			paletteStoneVariants.variants = new PalettesVariantEntry(id, paletteStoneVariants);
		}
	}
}

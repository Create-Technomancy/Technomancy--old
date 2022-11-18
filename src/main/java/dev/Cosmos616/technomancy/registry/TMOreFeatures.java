package dev.Cosmos616.technomancy.registry;

import com.simibubi.create.AllTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.Tags;

public class TMOreFeatures {
    public static final RuleTest BASALT_ORE_REPLACEABLES = new TagMatchTest(TMTags.AllBlockTags.BASALT.tag);
    public static void register() {}
}

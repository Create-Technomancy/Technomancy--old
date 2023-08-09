package com.chazbomb.technomancy.registry;

import com.chazbomb.technomancy.Technomancy;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.recipe.Mods;
import com.simibubi.create.foundation.utility.Lang;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Collections;
import java.util.Objects;

import static com.chazbomb.technomancy.registry.TMTags.NameSpace.MOD;

public class TMTags {
    private static final CreateRegistrate REGISTRATE = Technomancy.REGISTRATE
            .creativeModeTab(() -> TMItemGroups.MAIN_GROUP);

    public static <T extends IForgeRegistryEntry<T>> TagKey<T> optionalTag(IForgeRegistry<T> registry,
                                                                           ResourceLocation id) {
        return Objects.requireNonNull(registry.tags())
                .createOptionalTagKey(id, Collections.emptySet());
    }

    public static <T extends IForgeRegistryEntry<T>> TagKey<T> forgeTag(IForgeRegistry<T> registry, String path) {
        return optionalTag(registry, new ResourceLocation("forge", path));
    }

    public static TagKey<Block> forgeBlockTag(String path) {
        return forgeTag(ForgeRegistries.BLOCKS, path);
    }

    public static TagKey<Item> forgeItemTag(String path) {
        return forgeTag(ForgeRegistries.ITEMS, path);
    }

    public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> axeOrPickaxe() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_AXE)
                .tag(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> axeOnly() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_AXE);
    }

    public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> pickaxeOnly() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, ItemBuilder<BlockItem, BlockBuilder<T, P>>> tagBlockAndItem(
            String... path) {
        return b -> {
            for (String p : path)
                b.tag(forgeBlockTag(p));
            ItemBuilder<BlockItem, BlockBuilder<T, P>> item = b.item();
            for (String p : path)
                item.tag(forgeItemTag(p));
            return item;
        };
    }

    public enum NameSpace {

        MOD(Technomancy.MOD_ID, false, true)

        ;

        public final String id;
        public final boolean optionalDefault;
        public final boolean alwaysDatagenDefault;

        NameSpace(String id) {
            this(id, true, false);
        }

        NameSpace(String id, boolean optionalDefault, boolean alwaysDatagenDefault) {
            this.id = id;
            this.optionalDefault = optionalDefault;
            this.alwaysDatagenDefault = alwaysDatagenDefault;
        }

    }

    public enum AllBlockTags {
        BASALT,
        ;

        public final TagKey<Block> tag;

        AllBlockTags() {
            this(MOD);
        }

        AllBlockTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        AllBlockTags(NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        AllBlockTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        AllBlockTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(ForgeRegistries.BLOCKS, id);
            } else {
                tag = BlockTags.create(id);
            }
            if (alwaysDatagen) {
                REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> prov.tag(tag));
            }
        }

        @SuppressWarnings("deprecation")
        public boolean matches(Block block) {
            return block.builtInRegistryHolder()
                    .is(tag);
        }

        public boolean matches(BlockState state) {
            return state.is(tag);
        }

        public void add(Block... values) {
            REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> prov.tag(tag)
                    .add(values));
        }

        public void addOptional(Mods mod, String... ids) {
            REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
                TagsProvider.TagAppender<Block> builder = prov.tag(tag);
                for (String id : ids)
                    builder.addOptional(mod.asResource(id));
            });
        }

        public void includeIn(TagKey<Block> parent) {
            REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> prov.tag(parent)
                    .addTag(tag));
        }

        public void includeIn(AllBlockTags parent) {
            includeIn(parent.tag);
        }

        public void includeAll(TagKey<Block> child) {
            REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> prov.tag(tag)
                    .addTag(child));
        }

    }



    public static void register() {
        Technomancy.LOGGER.info("Registering tags!");
        AllBlockTags.BASALT.includeAll(AllBlockTags.BASALT.tag);
    }

}

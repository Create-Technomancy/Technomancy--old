package dev.Cosmos616.technomancy.registry;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.Cosmos616.technomancy.Technomancy;
import dev.Cosmos616.technomancy.content.curiosities.tools.AbsolverItem;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import static com.simibubi.create.AllTags.forgeItemTag;

public class TMItems {
    private static final CreateRegistrate REGISTRATE = Technomancy.getRegistrate()
            .creativeModeTab(() -> Technomancy.BASE_CREATIVE_TAB);

    public static final ItemEntry<AbsolverItem> ABSOLVER =
        REGISTRATE.item("absolver", AbsolverItem::new)
            .properties(p -> p.rarity(Rarity.UNCOMMON))
            .model(AssetLookup.itemModelWithPartials())
            .register();

    public static final ItemEntry<Item> ZIRCON_SHARD =
    taggedIngredient("zircon_shard",forgeItemTag("gem/zircon_shard"), forgeItemTag("gem"));

    @SafeVarargs
    private static ItemEntry<Item> taggedIngredient(String name, TagKey<Item>... tags) {
        return REGISTRATE.item(name, Item::new)
                .tag(tags)
                .register();
    }

    public static void register() {}
}

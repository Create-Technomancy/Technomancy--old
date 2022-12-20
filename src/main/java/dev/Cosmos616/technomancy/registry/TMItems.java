package dev.Cosmos616.technomancy.registry;

import com.simibubi.create.content.contraptions.itemAssembly.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.Cosmos616.technomancy.Technomancy;
import dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base.BulletItem;
import dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.repeater.EnergyRepeaterItem;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import static dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base.BulletItem.*;
import static com.simibubi.create.content.AllSections.*;

public class TMItems {
    private static final CreateRegistrate REGISTRATE = Technomancy.getRegistrate()
            .creativeModeTab(() -> Technomancy.BASE_CREATIVE_TAB);

    
    static { REGISTRATE.startSection(MATERIALS); }

    public static final ItemEntry<Item> ZIRCON_SHARD = REGISTRATE.item("zircon_shard",Item::new).register();
    public static final ItemEntry<Item> ZIRCON_DUST = REGISTRATE.item("zircon_dust",Item::new).register();

    public static final ItemEntry<Item> ZIRCONIUM_NUGGET = REGISTRATE.item("zirconium_nugget",Item::new).register();
    public static final ItemEntry<Item> ZIRCONIUM_INGOT = REGISTRATE.item("zirconium_ingot",Item::new).register();
    public static final ItemEntry<Item> ZIRCONIUM_SHEET = REGISTRATE.item("zirconium_sheet",Item::new).register();

    public static final  ItemEntry<Item> CERAMIC_DUST = REGISTRATE.item("ceramic_dust",Item::new).register();
    public static final  ItemEntry<Item> CERAMIC_PLATE = REGISTRATE.item("ceramic_plate",Item::new).register();

    public static final  ItemEntry<Item> SOUL_ROLL = REGISTRATE.item("soul_roll",Item::new)
    .properties(p -> p.food(new FoodProperties.Builder().nutrition(6)
			.saturationMod(0.8F)
			.build()))
            .register();


    static { REGISTRATE.startSection(CURIOSITIES); }

    public static final ItemEntry<BulletItem> HALLOWED_BULLET = REGISTRATE.item("hallowed_bullet", p -> new BulletItem(p, AmmoType.DEFAULT)).register();
    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_HALLOWED_BULLET = REGISTRATE.item("incompleted_hallowed_bullet", SequencedAssemblyItem::new).register();
    public static final ItemEntry<Item> HALLOWED_BULLET_CASING = REGISTRATE.item("hallowed_bullet_casing", Item::new).register();

    public static final ItemEntry<EnergyRepeaterItem> ENERGY_REPEATER = REGISTRATE.item("energy_repeater", EnergyRepeaterItem::new)
            .properties(p -> p.rarity(Rarity.UNCOMMON))
            .model(AssetLookup.itemModelWithPartials())
            .register();

    @SafeVarargs
    private static ItemEntry<Item> taggedIngredient(String name, TagKey<Item>... tags) {
        return REGISTRATE.item(name, Item::new)
                .tag(tags)
                .register();
    }

    public static void register() {}

}

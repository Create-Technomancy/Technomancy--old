package com.chazbomb.technomancy.registry;


import com.chazbomb.technomancy.Technomancy;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.archer.EnergyArcherItem;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.BulletItem;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.repeater.EnergyRepeaterItem;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.revolver.EnergyRevolverItem;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;


public class TMItems {
    private static final CreateRegistrate REGISTRATE = Technomancy.REGISTRATE
            .creativeModeTab(() -> TMItemGroups.MAIN_GROUP);


    /*
    static { REGISTRATE.startSection(MATERIALS); }


     */
    // Zircon crystal
    public static final ItemEntry<Item> ZIRCON_SHARD = REGISTRATE.item("zircon_shard",Item::new).register();
    public static final ItemEntry<Item> ZIRCON_DUST = REGISTRATE.item("zircon_dust",Item::new).register();

    // Zirconium metal
    public static final ItemEntry<Item> ZIRCONIUM_DUST = REGISTRATE.item("zirconium_dust",Item::new).register();
    public static final ItemEntry<Item> ZIRCONIUM_NUGGET = REGISTRATE.item("zirconium_nugget",Item::new).register();
    public static final ItemEntry<Item> ZIRCONIUM_INGOT = REGISTRATE.item("zirconium_ingot",Item::new).register();
    public static final ItemEntry<Item> ZIRCONIUM_SHEET = REGISTRATE.item("zirconium_sheet",Item::new).register();

    // Zircaloy compound
    public static final ItemEntry<Item> ZIRCALOY_DUST = REGISTRATE.item("zircaloy_dust",Item::new).register();
    public static final ItemEntry<Item> ZIRCALOY_NUGGET = REGISTRATE.item("zircaloy_nugget",Item::new).register();
    public static final ItemEntry<Item> ZIRCALOY_INGOT = REGISTRATE.item("zircaloy_ingot",Item::new).register();
    public static final ItemEntry<Item> ZIRCALOY_SHEET = REGISTRATE.item("zircaloy_sheet",Item::new).register();

    // Ceramics
    public static final  ItemEntry<Item> CERAMIC_DUST = REGISTRATE.item("ceramic_dust",Item::new).register();
    public static final  ItemEntry<Item> CERAMIC_PLATE = REGISTRATE.item("ceramic_plate",Item::new).register();

    public static final ItemEntry<Item> ENERGIZED_GOLD_INGOT = REGISTRATE.item("energized_gold_ingot",Item::new).register();
    public static final ItemEntry<Item> ENERGIZED_GOLDEN_SHEET = REGISTRATE.item("energized_golden_sheet",Item::new).register();


    public static final  ItemEntry<Item> SOUL_ROLL = REGISTRATE.item("soul_roll",Item::new)
    .properties(p -> p.food(new FoodProperties.Builder().nutrition(6)
			.saturationMod(0.8F)
			.build()))
            .register();


/*
    static { REGISTRATE.startSection(CURIOSITIES); }

 */

    public static final ItemEntry<BulletItem> HALLOWED_BULLET = REGISTRATE.item("hallowed_bullet", p -> new BulletItem(p, BulletItem.AmmoType.DEFAULT)).register();
    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_HALLOWED_BULLET = REGISTRATE.item("incompleted_hallowed_bullet", SequencedAssemblyItem::new).register();
    public static final ItemEntry<Item> HALLOWED_BULLET_CASING = REGISTRATE.item("hallowed_bullet_casing", Item::new).register();

    public static final ItemEntry<EnergyRepeaterItem> ENERGY_REPEATER = REGISTRATE
            .item("energy_repeater", EnergyRepeaterItem::new)
            .properties(p -> p.rarity(Rarity.RARE))
            .properties(p -> p.stacksTo(1))
            .model(AssetLookup.itemModelWithPartials())
            .register();

    public static final ItemEntry<EnergyRevolverItem> ENERGY_REVOLVER = REGISTRATE
            .item("energy_revolver", EnergyRevolverItem::new)
            .properties(p -> p.rarity(Rarity.RARE))
            .properties(p -> p.stacksTo(1))
            .model(AssetLookup.itemModelWithPartials())
            .register();

  public static final ItemEntry<EnergyArcherItem> ENERGY_ARCHER = REGISTRATE
          .item("energy_archer", EnergyArcherItem::new)
          .properties(p -> p.rarity(Rarity.RARE))
          .properties(p -> p.stacksTo(1))
          .model(AssetLookup.itemModelWithPartials())
          .register();
    @SafeVarargs
    private static ItemEntry<Item> taggedIngredient(String name, TagKey<Item>... tags) {
        return REGISTRATE.item(name, Item::new)
                .tag(tags)
                .register();
    }

    public static void register() {
        Technomancy.LOGGER.info("Registering items!");
    }

}

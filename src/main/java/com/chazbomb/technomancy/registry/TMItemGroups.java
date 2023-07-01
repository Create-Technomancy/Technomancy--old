package com.chazbomb.technomancy.registry;

import com.chazbomb.technomancy.Technomancy;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class TMItemGroups {
    public static CreativeModeTab MAIN_GROUP = new CreativeModeTab("main_group") {
        @Override
        public ItemStack makeIcon() { return TMBlocks.BATTERY_BLOCK.asStack();
        }
    };

    // Tell Registrate to create a lang entry for the item groups
    private static final CreateRegistrate REGISTRATE = Technomancy.getRegistrate().creativeModeTab(() -> MAIN_GROUP, "Technomancy");
    public static void init() {
    }
}

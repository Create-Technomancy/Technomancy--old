package com.chazbomb.technomancy.registry;

import com.chazbomb.technomancy.Technomancy;
import com.chazbomb.technomancy.content.decoration.palettes.TMPaletteStoneTypes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class TMItemGroups {
    public static CreativeModeTab MAIN_GROUP = new CreativeModeTab(Technomancy.MOD_ID + "base") {
        @Override
        public ItemStack makeIcon() { return TMBlocks.BATTERY_BLOCK.asStack();
        }
    };

    public static CreativeModeTab PALETTES_TAB = new CreativeModeTab(Technomancy.MOD_ID + "palettes") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(TMPaletteStoneTypes.BLACKSTONE.getBaseBlock().get().asItem());
        }
    };

    public static void register() {
        Technomancy.LOGGER.info("Registering creative tabs!");
    }
}

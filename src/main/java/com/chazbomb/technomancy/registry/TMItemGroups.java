package com.chazbomb.technomancy.registry;

import com.chazbomb.technomancy.Technomancy;
import com.chazbomb.technomancy.content.decoration.palettes.TMPaletteStoneTypes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class TMItemGroups {
    public static CreativeModeTab MAIN_GROUP = tab("base", TMBlocks.BATTERY_BLOCK::asStack);

    public static CreativeModeTab PALETTES_TAB = tab("palettes", () -> new ItemStack(TMPaletteStoneTypes.BLACKSTONE.getBaseBlock().get().asItem()));

    public static void register() {
        Technomancy.LOGGER.info("Registering creative tabs!");
    }

    public static CreativeModeTab tab(String id, Supplier<ItemStack> icon) {
        return new CreativeModeTab(Technomancy.MOD_ID + '.' + id) {
            @Override
            public @NotNull ItemStack makeIcon() {
                return icon.get();
            }
        };
    }
}

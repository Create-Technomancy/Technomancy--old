package com.chazbomb.technomancy.content.decoration.palettes;

import com.chazbomb.technomancy.registry.TMItemGroups;

import static com.chazbomb.technomancy.Technomancy.REGISTRATE;

public class TMPaletteBlocks {

	public static void register() {
		REGISTRATE.creativeModeTab(() -> TMItemGroups.PALETTES_TAB);
		TMPaletteStoneTypes.register(REGISTRATE);
	}
}

package com.chazbomb.technomancy;

import com.chazbomb.technomancy.content.curiosities.weapons.firearms.archer.ui.EnergyArcherUI;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.FirearmInteractionHandler;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.FirearmRenderHandler;
import com.chazbomb.technomancy.foundation.keys.TMKeys;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.IEventBus;

public class TechnomancyClient {
	public static final FirearmRenderHandler FIREARM_RENDER_HANDLER = new FirearmRenderHandler();
	public static final FirearmInteractionHandler FIREARM_INTERACTION_HANDLER = new FirearmInteractionHandler();

	public static void onClient(IEventBus modEventBus, IEventBus forgeEventBus) {
		FIREARM_RENDER_HANDLER.registerListeners(forgeEventBus);
		forgeEventBus.register(FIREARM_INTERACTION_HANDLER);
		registerOverlays();
		TMKeys.register();
	}
	private static void registerOverlays() {
		// Register overlays in reverse order

		OverlayRegistry.registerOverlayAbove(ForgeIngameGui.EXPERIENCE_BAR_ELEMENT, "Test Energy Archer UI", EnergyArcherUI.OVERLAY);
	}

}

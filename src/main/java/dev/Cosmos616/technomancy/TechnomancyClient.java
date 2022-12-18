package dev.Cosmos616.technomancy;

import dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base.FirearmInteractionHandler;
import dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base.FirearmRenderHandler;
import net.minecraftforge.eventbus.api.IEventBus;

public class TechnomancyClient {
	public static final FirearmRenderHandler FIREARM_RENDER_HANDLER = new FirearmRenderHandler();
	public static final FirearmInteractionHandler FIREARM_INTERACTION_HANDLER = new FirearmInteractionHandler();
	
	public static void onClient(IEventBus modEventBus, IEventBus forgeEventBus) {
		FIREARM_RENDER_HANDLER.registerListeners(forgeEventBus);
		forgeEventBus.register(FIREARM_INTERACTION_HANDLER);
	}
}
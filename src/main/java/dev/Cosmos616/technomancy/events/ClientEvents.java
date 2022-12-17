package dev.Cosmos616.technomancy.events;

import com.simibubi.create.content.contraptions.components.fan.AirCurrent;
import com.simibubi.create.content.contraptions.components.structureMovement.interaction.controls.ControlsHandler;
import com.simibubi.create.content.logistics.item.LinkedControllerClientHandler;
import dev.Cosmos616.technomancy.TechnomancyClient;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
	
	@SubscribeEvent
	public static void onTick(TickEvent.ClientTickEvent event) {
		if (!isGameActive())
			return;
		
		Level world = Minecraft.getInstance().level;
		if (event.phase == TickEvent.Phase.START) {
			LinkedControllerClientHandler.tick();
			ControlsHandler.tick();
			AirCurrent.tickClientPlayerSounds();
			return;
		}
		
		TechnomancyClient.FIREARM_RENDER_HANDLER.tick();
	}
	
	protected static boolean isGameActive() {
		return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
	}
}

package dev.Cosmos616.technomancy.events;

import com.simibubi.create.content.contraptions.components.fan.AirCurrent;
import com.simibubi.create.content.contraptions.components.structureMovement.interaction.controls.ControlsHandler;
import com.simibubi.create.content.contraptions.components.structureMovement.interaction.controls.TrainHUD;
import com.simibubi.create.content.logistics.item.LinkedControllerClientHandler;
import dev.Cosmos616.technomancy.TechnomancyClient;
import dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.archer.ui.EnergyArcherUI;
import dev.Cosmos616.technomancy.registry.TMItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.archer.ui.EnergyArcherUI.toRender;

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


		Minecraft mc = Minecraft.getInstance();
		if(!mc.player.getItemInHand(InteractionHand.MAIN_HAND).is(TMItems.ENERGY_ARCHER.get()))
			EnergyArcherUI.toRender=false;
		EnergyArcherUI.tick();
	}
	
	protected static boolean isGameActive() {
		return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
	}
}

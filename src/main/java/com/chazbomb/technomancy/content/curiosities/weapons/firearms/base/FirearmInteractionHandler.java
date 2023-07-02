package com.chazbomb.technomancy.content.curiosities.weapons.firearms.base;


import com.chazbomb.technomancy.content.curiosities.weapons.firearms.archer.EnergyArcherItem;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.archer.ui.EnergyArcherUI;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.AbstractFirearmItem;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.FirearmShootPacket;
import com.chazbomb.technomancy.registry.TMPackets;
import com.simibubi.create.content.equipment.zapper.ShootableGadgetItemMethods;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class FirearmInteractionHandler {
//	@SubscribeEvent
//	public void onKeyPressed(InputEvent.KeyInputEvent event) {
//
//	}

	/*
@SubscribeEvent(priority = EventPriority.LOWEST)
public void onMouseHold(InputEvent.MouseInputEvent event) {
	}


	 */




	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onMouseClick(InputEvent.ClickInputEvent event) {
		if (event.isCanceled())
			return;


		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		if (player == null)
			return;
		
		ItemStack heldItem = player.getMainHandItem();
		if (!(heldItem.getItem() instanceof AbstractFirearmItem gunItem))
			return;

		event.setSwingHand(false);
		event.setCanceled(true);
		mc.options.keyAttack.setDown(false);

		if(gunItem instanceof EnergyArcherItem) {
			if (!EnergyArcherUI.charged)
				return;
		}


		if (!event.isAttack())
			return;
		
		// Handle firing
		if ((!gunItem.hasAmmo(heldItem) && !player.isCreative()) || player.isSpectator())
			return;
		
		ItemCooldowns cooldownTracker = player.getCooldowns();
		if (cooldownTracker.isOnCooldown(gunItem))
			return;
		if(gunItem instanceof EnergyArcherItem) {
			EnergyArcherUI.charged = false;
			EnergyArcherUI.charge = 0;
			EnergyArcherUI.displayedSpeed.setValue(0);
			((EnergyArcherItem) gunItem).charging = false;
		}
		cooldownTracker.addCooldown(gunItem, gunItem.cooldownTicks());


			TMPackets.channel.sendToServer(new FirearmShootPacket(ShootableGadgetItemMethods.getGunBarrelVec(player, true, gunItem.getBarrelOffset())));
			gunItem.shootWeapon(player, heldItem, true);

	}

//	@SubscribeEvent
//	public void onClientTick(TickEvent.ClientTickEvent event) {
//
//	}
}

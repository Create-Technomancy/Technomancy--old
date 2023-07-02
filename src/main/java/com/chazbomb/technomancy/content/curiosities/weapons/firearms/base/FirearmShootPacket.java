package com.chazbomb.technomancy.content.curiosities.weapons.firearms.base;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class FirearmShootPacket extends SimplePacketBase {
	private final Vec3 barrelVector;
	
	public FirearmShootPacket(Vec3 barrelVec) {
		this.barrelVector = barrelVec;
	}
	public FirearmShootPacket(FriendlyByteBuf buffer) {
		double x = buffer.readDouble();
		double y = buffer.readDouble();
		double z = buffer.readDouble();
		barrelVector = new Vec3(x, y, z);
	}
	
	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeDouble(barrelVector.x);
		buffer.writeDouble(barrelVector.y);
		buffer.writeDouble(barrelVector.z);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean handle(NetworkEvent.Context context) {



		context.enqueueWork(() -> {
			// Get data
			ServerPlayer player = context.getSender();
			if (player == null || !player.isAlive())
				return;
			ServerLevel world = player.getLevel();
			ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
			if (!(heldItem.getItem() instanceof AbstractFirearmItem gunItem))
				return;

			
			// Fire gun
			if ((!gunItem.hasAmmo(heldItem) && !player.isCreative()) || player.isSpectator())
				return;


			gunItem.shootWeapon(player, heldItem, false, barrelVector);
		});

		return true;
	}
	
}

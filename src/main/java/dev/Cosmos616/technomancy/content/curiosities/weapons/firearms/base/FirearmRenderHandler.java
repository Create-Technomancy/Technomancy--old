package dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.curiosities.zapper.ShootableGadgetRenderHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class FirearmRenderHandler extends ShootableGadgetRenderHandler {
	@Override
	protected void playSound(InteractionHand hand, Vec3 position) {
	
	}
	
	@Override
	protected boolean appliesTo(ItemStack stack) {
		return stack.getItem() instanceof AbstractFirearmItem;
	}
	
	@Override
	protected void transformTool(PoseStack ms, float flip, float equipProgress, float recoil, float pt) {
	
	}
	
	@Override
	protected void transformHand(PoseStack ms, float flip, float equipProgress, float recoil, float pt) {
	
	}
}

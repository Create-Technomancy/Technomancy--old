package com.chazbomb.technomancy.content.curiosities.weapons.firearms.base;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import com.simibubi.create.content.equipment.zapper.ShootableGadgetRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class FirearmRenderHandler extends ShootableGadgetRenderHandler {
	protected boolean reloading=false;
	protected float leftHandReload;
	protected float rightHandReload;

	protected int animationProgress;


	@Override
	protected void playSound(InteractionHand hand, Vec3 position) {
	
	}
	public void tick() {
		super.tick();



		leftHandReload *= animationDecay();
		rightHandReload *= animationDecay();
	}
	public void reload(InteractionHand hand, Vec3 location) {
		reloading=true;
		LocalPlayer player = Minecraft.getInstance().player;
		boolean rightHand = hand == InteractionHand.MAIN_HAND ^ player.getMainArm() == HumanoidArm.LEFT;
		if (rightHand) {
			rightHandReload = 9f;
			dontReequipRight = false;
		} else {
			leftHandReload = .0f;
			dontReequipLeft = false;
		}
		playSound(hand, location);
	}
	@Override
	protected boolean appliesTo(ItemStack stack) {
		return stack.getItem() instanceof AbstractFirearmItem;
	}
	
	@Override
	protected void transformTool(PoseStack ms, float flip, float equipProgress, float recoil, float pt) {

		if (reloading) {


			animationProgress+=3;


			if(animationProgress<=90)
				ms.mulPose(Vector3f.YP.rotationDegrees((animationProgress)));



			if(animationProgress>90)
				ms.mulPose(Vector3f.YP.rotationDegrees(flip*(-animationProgress-180)));



				if(animationProgress>180){
					animationProgress=0;
					reloading=false;
				}

		}
	}

	
	@Override
	protected void transformHand(PoseStack ms, float flip, float equipProgress, float recoil, float pt) {
		ms.translate(flip * (0.64F - .1f), -8F , -0.72F - 0.1f);


	}
}

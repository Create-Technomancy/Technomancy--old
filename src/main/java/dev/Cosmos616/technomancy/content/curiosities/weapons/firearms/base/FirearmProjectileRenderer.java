package dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base.FirearmProjectileEntity;
import dev.Cosmos616.technomancy.registry.TMItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class FirearmProjectileRenderer extends EntityRenderer<FirearmProjectileEntity> {
	public FirearmProjectileRenderer(EntityRendererProvider.Context context) {
		super(context);
	}
	
	@Override
	public void render(FirearmProjectileEntity entity, float entityYaw, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light) {
		Minecraft.getInstance()
				.getItemRenderer()
				.renderStatic(TMItems.AMMO.asStack(), ItemTransforms.TransformType.GROUND, light, OverlayTexture.NO_OVERLAY, ms, buffer, 0);
	}
	
	@Override
	public ResourceLocation getTextureLocation(FirearmProjectileEntity entity) {
		return null;
	}
}

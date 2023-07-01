package com.chazbomb.technomancy.content.curiosities.weapons.firearms.repeater;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import com.chazbomb.technomancy.Technomancy;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.AbstractFirearmItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;

public class EnergyRepeaterItemRenderer extends AbstractFirearmItemRenderer {
	protected static final PartialModel COG = new PartialModel(Technomancy.asResource("item/energy_repeater/cog"));
	protected static final PartialModel RAILS = new PartialModel(Technomancy.asResource("item/energy_repeater/rails"));
	@Override
	protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer,
						  ItemTransforms.TransformType transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		Minecraft mc = Minecraft.getInstance();
		float time = mc.player.tickCount + mc.getFrameTime();
		time *= 2;

		renderer.render(model.getOriginalModel(), light);

		ms.pushPose();

		double xo = -0.5;
		double yo = -1;
		double zo = 0;

		ms.translate(xo / 16., yo / 16., zo / 16.);
		ms.mulPose(Vector3f.ZN.rotationDegrees(time));
		ms.translate(-xo / 16., -yo / 16., -zo / 16.);


		renderer.render(COG.get(), light);
		ms.popPose();


		ms.pushPose();

		ms.translate(xo / 16., yo / 16., zo / 16.);
		ms.mulPose(Vector3f.ZN.rotationDegrees(-time * 10));
		ms.translate(-xo / 16., -yo / 16., -zo / 16.);

		renderer.render(RAILS.get(), light);
		ms.popPose();
	}
	/*
	@Override
	public EnergyRepeaterModel createModel(BakedModel originalModel) {
		return new EnergyRepeaterModel(originalModel);
	}

	 */
}

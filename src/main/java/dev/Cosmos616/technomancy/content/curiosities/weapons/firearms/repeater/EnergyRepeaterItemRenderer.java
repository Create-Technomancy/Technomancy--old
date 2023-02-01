package dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.repeater;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base.AbstractFirearmItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;

public class EnergyRepeaterItemRenderer extends AbstractFirearmItemRenderer<EnergyRepeaterModel> {
	@Override
	protected void render(ItemStack stack, EnergyRepeaterModel model, PartialItemModelRenderer renderer,
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


		renderer.render(model.getPartial("cog"), light);
		ms.popPose();


		ms.pushPose();

		ms.translate(xo / 16., yo / 16., zo / 16.);
		ms.mulPose(Vector3f.ZN.rotationDegrees(-time * 10));
		ms.translate(-xo / 16., -yo / 16., -zo / 16.);

		renderer.render(model.getPartial("rails"), light);
		ms.popPose();
	}
	
	@Override
	public EnergyRepeaterModel createModel(BakedModel originalModel) {
		return new EnergyRepeaterModel(originalModel);
	}
}

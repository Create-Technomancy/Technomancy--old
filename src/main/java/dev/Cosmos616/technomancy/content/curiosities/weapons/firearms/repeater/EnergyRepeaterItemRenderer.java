package dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.repeater;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base.AbstractFirearmItemRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;

public class EnergyRepeaterItemRenderer extends AbstractFirearmItemRenderer<EnergyRepeaterModel> {
	@Override
	protected void render(ItemStack stack, EnergyRepeaterModel model, PartialItemModelRenderer renderer,
						  ItemTransforms.TransformType transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		// todo add visuals and whatever
		renderer.render(model.getOriginalModel(), light);
	}
	
	@Override
	public EnergyRepeaterModel createModel(BakedModel originalModel) {
		return new EnergyRepeaterModel(originalModel);
	}
}

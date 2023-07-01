package com.chazbomb.technomancy.content.curiosities.weapons.firearms.archer;

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
    public class EnergyArcherItemRenderer extends AbstractFirearmItemRenderer {
        protected static final PartialModel COG = new PartialModel(Technomancy.asResource("item/energy_archer/cog"));
        @Override
        protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer,
                              ItemTransforms.TransformType transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
            Minecraft mc = Minecraft.getInstance();
            float time = mc.player.tickCount + mc.getFrameTime();
            time *= 2;

            renderer.render(model.getOriginalModel(), light);

            ms.pushPose();

            double xo = 0.5;
            double yo = -4.5;
           double zo = 0;

            ms.translate(xo / 16., yo / 16., zo / 16.);
            ms.mulPose(Vector3f.ZN.rotationDegrees(time));
            ms.translate(-xo / 16., -yo / 16., -zo / 16.);


            renderer.render(COG.get(), light);
            ms.popPose();
        }

        /**
         * looks useless,
         * so i commented it
         * -DrMangoTea
         */
/*
        @Override
        public EnergyArcherModel createModel(BakedModel originalModel) {
            return new EnergyArcherModel(originalModel);
        }


 */
    }


package com.chazbomb.technomancy.content.curiosities.weapons.firearms.archer.shockwave;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.util.Mth;

@OnlyIn(Dist.CLIENT)
public class ShockWaveRenderer extends EntityRenderer<ShockWaveEntity> {
    public static final ResourceLocation SHOCKWAVE_BIG_1 = new ResourceLocation("technomancy:textures/entity/shockwave/1.png");


    public static final ResourceLocation SHOCKWAVE_DEBUG = new ResourceLocation("technomancy:textures/entity/shockwave/shockwave_debug.png");
    public ShockWaveRenderer(EntityRendererProvider.Context p_173917_) {
        super(p_173917_);
    }

    public void render(ShockWaveEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot())));



        pMatrixStack.scale(pEntity.size, pEntity.size, pEntity.size);
      //  pMatrixStack.translate(-4.0D, 0.0D, 0.0D);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(pEntity)));
        PoseStack.Pose posestack$pose = pMatrixStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();

        for(int j = 0; j < 2; ++j) {
                if(j==0)
                    pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
                if(j==1)
                    pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
                this.vertex(matrix4f, matrix3f, vertexconsumer, -3, -3, 0, 1.0F, 1.0F, 0, 1, 0, pPackedLight);
                this.vertex(matrix4f, matrix3f, vertexconsumer, 3, -3, 0, 1F, 0.0F, 0, 1, 0, pPackedLight);
                this.vertex(matrix4f, matrix3f, vertexconsumer, 3, 3, 0, 0F, 0F, 0, 1, 0, pPackedLight);
                this.vertex(matrix4f, matrix3f, vertexconsumer, -3, 3, 0, 0.0F, 1F, 0, 1, 0, pPackedLight);

        }

        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ShockWaveEntity pEntity) {

            return SHOCKWAVE_BIG_1;

    }



    protected int getBlockLightLevel(ShockWaveEntity pEntity, BlockPos p_174147_) {

        return 10;
    }

    public void vertex(Matrix4f pMatrix, Matrix3f pNormals, VertexConsumer pVertexBuilder, int pOffsetX, int pOffsetY, int pOffsetZ, float pTextureX, float pTextureY, int pNormalX, int p_113835_, int p_113836_, int pPackedLight) {
        pVertexBuilder.vertex(pMatrix, (float)pOffsetX, (float)pOffsetY, (float)pOffsetZ).color(255, 255, 255, 255).uv(pTextureX, pTextureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pPackedLight).normal(pNormals, (float)pNormalX, (float)p_113836_, (float)p_113835_).endVertex();
    }


}
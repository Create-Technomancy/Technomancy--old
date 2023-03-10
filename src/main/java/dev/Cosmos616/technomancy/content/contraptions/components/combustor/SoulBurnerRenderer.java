package dev.Cosmos616.technomancy.content.contraptions.components.combustor;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.tileEntity.behaviour.filtering.FilteringRenderer;
import com.simibubi.create.foundation.tileEntity.renderer.SafeTileEntityRenderer;
import dev.Cosmos616.technomancy.registry.TMBlockPartials;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class SoulBurnerRenderer extends SafeTileEntityRenderer<SoulBurnerBlockEntity> {
    public SoulBurnerRenderer(BlockEntityRendererProvider.Context context){ super(); }

    Direction facing;

    @Override
    protected void renderSafe(SoulBurnerBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        FilteringRenderer.renderOnTileEntity(te, partialTicks, ms, buffer, light, overlay);
        this.renderComponents(te, partialTicks, ms, buffer, light, overlay);
    }

    private void renderComponents(SoulBurnerBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        VertexConsumer vb = buffer.getBuffer(RenderType.cutout());
        if (!Backend.canUseInstancing(te.getLevel())) {
        }

        BlockState blockState = te.getBlockState();

        SuperByteBuffer partial = CachedBufferer.partial(TMBlockPartials.SOUL_BURNER_PARTIAL, blockState);
        partial.light(light)
                .translate(0, 1, 0)
                .renderInto(ms, vb);
    }
}


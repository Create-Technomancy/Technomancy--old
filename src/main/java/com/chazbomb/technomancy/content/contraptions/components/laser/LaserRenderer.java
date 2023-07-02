package com.chazbomb.technomancy.content.contraptions.components.laser;

import com.chazbomb.technomancy.registry.TMBlockPartials;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

public class LaserRenderer extends SafeBlockEntityRenderer<LaserBlockEntity> {
	public LaserRenderer(BlockEntityRendererProvider.Context context) { }
	
	// Animation Values
	private boolean transition = false;
	private boolean active = false;
	private int beamLength = 0;
	private int currentLength = 0;
	
	@Override
	protected void renderSafe(LaserBlockEntity tile, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		if (!tile.running)
			return;
		
		// Beam
		VertexConsumer vb = buffer.getBuffer(RenderType.translucent());
		BlockState state = tile.getBlockState();
		Direction facing = state.getValue(DirectionalBlock.FACING);
		
		float rotation = ((tile.beamAnimationTick + partialTicks ) % 20) *18;
		
		float maxLaser = LaserBlockEntity.getMaxLaserDistance();
		
		for (int i = 0; i < (int) tile.getBeamDistance(); i++) {
			float fade = Math.max(Math.min(1, ((maxLaser - (i+1)) / maxLaser) * 4), 0);
			
			float outerTick = (tile.beamAnimationTick + (20 - i) + partialTicks) % 20;
			
			float outerRotation = outerTick * 18;
			float outerPulse = Math.min((20 - outerTick), outerTick) * 0.1f;
			
			int[] colour = new int[] {(int) (150 + (95 * outerPulse)), (int) (200 + (55 * outerPulse)), 255};
			
			transformed(TMBlockPartials.LASER_BEAM_INNER, state, facing)
					.translate(0, i + 1, 0).centre().rotateY(rotation).rotateX(90).unCentre()
					.color(colour[0], colour[1], colour[2], (int) (192 * fade))
					.light(light).renderInto(stack, vb);
			
			transformed(TMBlockPartials.LASER_BEAM_OUTER, state, facing)
					.translate(0, i + 1, 0).centre().rotateY(-outerRotation)
					.scale(1.7f + (outerPulse * fade), 1,
							1.7f + (outerPulse * fade))
					.rotateX(90).unCentre()
					.color(colour[0], colour[1], colour[2], (int) (128 * fade))
					.light(light).renderInto(stack, vb);
		}
	}
	
	private SuperByteBuffer transformed(PartialModel model, BlockState blockState, Direction facing) {
		return CachedBufferer.partial(model, blockState).centre().rotateY(
				AngleHelper.horizontalAngle(facing)).rotateX(AngleHelper.verticalAngle(facing) + 90.0F).unCentre();
	}
	
	@Override
	public boolean shouldRenderOffScreen(LaserBlockEntity tile) {
		return true;
	}
}
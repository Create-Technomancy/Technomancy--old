package dev.Cosmos616.technomancy.content.contraptions.components.laser;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.tileEntity.renderer.SafeTileEntityRenderer;
import com.simibubi.create.foundation.utility.AngleHelper;
import dev.Cosmos616.technomancy.registry.TMBlockPartials;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

public class LaserRenderer extends SafeTileEntityRenderer<LaserBlockEntity> {
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
		for (int i = 0; i < (int)tile.getBeamDistance(); i++) {
			transformed(TMBlockPartials.LASER_BEAM_INNER, state, facing)
					.unCentre().translate(-0.5f, i + 1, -0.5f)
					.centre().rotateX(90.0f)
					.light(light).renderInto(stack, vb);
			transformed(TMBlockPartials.LASER_BEAM_OUTER, state, facing)
					.unCentre().translate(-0.5f, i + 1,-0.5f)
					.centre().rotateX(90.0f)
					.color(255, 255, 255, 128)
					.light(light).renderInto(stack, vb);
		}
	}
	
	private SuperByteBuffer transformed(PartialModel model, BlockState blockState, Direction facing) {
		return CachedBufferer.partial(model, blockState).centre().rotateY(
				AngleHelper.horizontalAngle(facing)).rotateX(AngleHelper.verticalAngle(facing) + 90.0F);
	}
	
	@Override
	public boolean shouldRenderOffScreen(LaserBlockEntity tile) {
		return true;
	}
}
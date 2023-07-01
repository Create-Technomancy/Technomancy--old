package com.chazbomb.technomancy.content.contraptions.energy.tesla_coil.sparkgap;


import com.chazbomb.technomancy.registry.TMBlockPartials;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class SparkGapRenderer extends KineticBlockEntityRenderer {

	public SparkGapRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected SuperByteBuffer getRotatedModel(KineticBlockEntity te, BlockState state) {
		return CachedBufferer.partial(TMBlockPartials.SPARK_GAP_COG, state);
	}

}

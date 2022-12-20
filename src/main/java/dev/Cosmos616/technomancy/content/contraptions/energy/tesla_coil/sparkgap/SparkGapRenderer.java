package dev.Cosmos616.technomancy.content.contraptions.energy.tesla_coil.sparkgap;

import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;

import dev.Cosmos616.technomancy.registry.TMBlockPartials;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class SparkGapRenderer extends KineticTileEntityRenderer {

	public SparkGapRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected SuperByteBuffer getRotatedModel(KineticTileEntity te, BlockState state) {
		return CachedBufferer.partial(TMBlockPartials.SPARK_GAP_COG, state);
	}

}

package dev.Cosmos616.technomancy.content.contraptions.aether.accumulator;

import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;
import dev.Cosmos616.technomancy.registry.TMBlockEntities;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SoulAccumulatorBlock extends Block implements IWrenchable, ITE<SoulAccumulatorBlockEntity> {
	public SoulAccumulatorBlock(Properties properties) {
		super(properties);
	}

	// ITE
	@Override
	public Class<SoulAccumulatorBlockEntity> getTileEntityClass() {
		return SoulAccumulatorBlockEntity.class;
	}
	@Override
	public BlockEntityType<? extends SoulAccumulatorBlockEntity> getTileEntityType() {
		return TMBlockEntities.SOUL_ACCUMULATOR.get();
	}
}

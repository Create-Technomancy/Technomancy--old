package com.chazbomb.technomancy.content.contraptions.energy.accumulator;

import com.chazbomb.technomancy.registry.TMBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SoulAccumulatorBlock extends Block implements IWrenchable, IBE<SoulAccumulatorBlockEntity> {
	public SoulAccumulatorBlock(Properties properties) {
		super(properties);
	}

	// ITE
	@Override
	public Class<SoulAccumulatorBlockEntity> getBlockEntityClass() {
		return SoulAccumulatorBlockEntity.class;
	}
	@Override
	public BlockEntityType<? extends SoulAccumulatorBlockEntity> getBlockEntityType() {
		return TMBlockEntities.SOUL_ACCUMULATOR.get();
	}
}

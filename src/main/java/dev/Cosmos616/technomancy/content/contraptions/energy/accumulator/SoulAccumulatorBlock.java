package dev.Cosmos616.technomancy.content.contraptions.energy.accumulator;

import com.simibubi.create.foundation.block.ITE;
import dev.Cosmos616.technomancy.registry.TMTileEntities;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SoulAccumulatorBlock extends Block implements ITE<SoulAccumulatorTileEntity> {
	public SoulAccumulatorBlock(Properties properties) {
		super(properties);
	}
	
	// ITE
	@Override
	public Class<SoulAccumulatorTileEntity> getTileEntityClass() {
		return SoulAccumulatorTileEntity.class;
	}
	@Override
	public BlockEntityType<? extends SoulAccumulatorTileEntity> getTileEntityType() {
		return TMTileEntities.SOUL_ACCUMULATOR.get();
	}
}

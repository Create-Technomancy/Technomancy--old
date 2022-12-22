package dev.Cosmos616.technomancy.content.contraptions.energy.combustor;

import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;
import dev.Cosmos616.technomancy.registry.TMTileEntities;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SoulCombustorBlock extends Block implements IWrenchable, ITE<SoulCombustorTileEntity> {
	public SoulCombustorBlock(Properties properties) {
		super(properties);
	}
	
	@Override
	public Class<SoulCombustorTileEntity> getTileEntityClass() {
		return SoulCombustorTileEntity.class;
	}
	
	@Override
	public BlockEntityType<? extends SoulCombustorTileEntity> getTileEntityType() {
		return TMTileEntities.SOUL_COMBUSTOR.get();
	}
}

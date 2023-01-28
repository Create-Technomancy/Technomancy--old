package dev.Cosmos616.technomancy.content.contraptions.energy.combustor;

import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;
import dev.Cosmos616.technomancy.registry.TMTileEntities;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SoulBurnerBlock extends Block implements IWrenchable, ITE<SoulBurnerTileEntity> {
	public SoulBurnerBlock(Properties properties) {
		super(properties);
	}
	
	@Override
	public Class<SoulBurnerTileEntity> getTileEntityClass() {
		return SoulBurnerTileEntity.class;
	}
	
	@Override
	public BlockEntityType<? extends SoulBurnerTileEntity> getTileEntityType() {
		return TMTileEntities.SOUL_BURNER.get();
	}
}

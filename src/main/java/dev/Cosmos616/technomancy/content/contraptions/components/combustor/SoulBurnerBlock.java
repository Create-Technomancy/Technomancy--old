package dev.Cosmos616.technomancy.content.contraptions.components.combustor;

import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;
import dev.Cosmos616.technomancy.registry.TMBlockEntities;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SoulBurnerBlock extends Block implements IWrenchable, ITE<SoulBurnerBlockEntity> {
	public SoulBurnerBlock(Properties properties) {
		super(properties);
	}

//	@Override
//	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
//		return TMShapes.SOUL_BURNER.get(Direction.UP);
//	}


	@Override
	public Class<SoulBurnerBlockEntity> getTileEntityClass() {
		return SoulBurnerBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends SoulBurnerBlockEntity> getTileEntityType() {
		return TMBlockEntities.SOUL_BURNER.get();
	}
}

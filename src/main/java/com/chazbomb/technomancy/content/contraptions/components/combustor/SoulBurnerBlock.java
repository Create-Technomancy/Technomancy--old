package com.chazbomb.technomancy.content.contraptions.components.combustor;


import com.chazbomb.technomancy.registry.TMBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SoulBurnerBlock extends Block implements IWrenchable, IBE<SoulBurnerBlockEntity> {
	public SoulBurnerBlock(Properties properties) {
		super(properties);
	}

//	@Override
//	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
//		return TMShapes.SOUL_BURNER.get(Direction.UP);
//	}


	@Override
	public Class<SoulBurnerBlockEntity> getBlockEntityClass() {
		return SoulBurnerBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends SoulBurnerBlockEntity> getBlockEntityType() {
		return TMBlockEntities.SOUL_BURNER.get();
	}
}

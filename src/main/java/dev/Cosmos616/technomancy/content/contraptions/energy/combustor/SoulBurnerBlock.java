package dev.Cosmos616.technomancy.content.contraptions.energy.combustor;

import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;
import dev.Cosmos616.technomancy.registry.TMBlockEntities;
import dev.Cosmos616.technomancy.registry.TMShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

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

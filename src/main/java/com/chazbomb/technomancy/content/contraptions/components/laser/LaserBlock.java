package com.chazbomb.technomancy.content.contraptions.components.laser;


import com.chazbomb.technomancy.registry.TMBlockEntities;
import com.chazbomb.technomancy.registry.TMShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LaserBlock extends DirectionalBlock implements IBE<LaserBlockEntity>, IWrenchable {
	public LaserBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState()
				.setValue(FACING, Direction.UP));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction dir = context.isSecondaryUseActive() ? context.getNearestLookingDirection() : context.getNearestLookingDirection().getOpposite();
		return this.defaultBlockState().setValue(FACING, dir);
	}
	
	@Override
	public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
		updateSignal(pLevel, pPos);
	}
	
	@Override
	public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
		super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
		updateSignal(pLevel, pPos);
	}
	
	private void updateSignal(Level level, BlockPos pos) {
		withBlockEntityDo(level, pos, tile -> {
			tile.updateSignal(level.getBestNeighborSignal(pos));
		});
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return TMShapes.LASER.get(pState.getValue(FACING));
	}

	@Override
	public Class<LaserBlockEntity> getBlockEntityClass() {
		return LaserBlockEntity.class;
	}
	
	@Override
	public BlockEntityType<? extends LaserBlockEntity> getBlockEntityType() {
		return TMBlockEntities.LASER.get();
	}
}

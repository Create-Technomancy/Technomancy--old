package com.chazbomb.technomancy.content.contraptions.energy.tesla_coil.teslastalk;

import com.chazbomb.technomancy.registry.TMBlocks;
import com.google.common.base.Predicates;

import com.simibubi.create.content.equipment.extendoGrip.ExtendoGripItem;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementOffset;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeMod;

import java.util.List;
import java.util.function.Predicate;

public class TeslaStalkPlacementHelper implements IPlacementHelper {

	@Override
	public Predicate<ItemStack> getItemPredicate() {
		return TMBlocks.TESLA_STALK::isIn;
	}

	@Override
	public Predicate<BlockState> getStatePredicate() {
		return Predicates.or(TMBlocks.TESLA_STALK::has, TMBlocks.TESLA_PRIMARY::has);
	}

	private boolean canExtendToward(BlockState state, Direction side) {
		Axis axis = side.getAxis();
		if (state.getBlock() instanceof TeslaStalkBlock) {
			boolean x = state.getValue(TeslaStalkBlock.X);
			boolean z = state.getValue(TeslaStalkBlock.Z);
			if (!x && !z)
				return axis == Axis.Y;
			if (x && z)
				return true;
			return axis == (x ? Axis.X : Axis.Z);
		}

		return false;
	}

	private int attachedPoles(Level world, BlockPos pos, Direction direction) {
		BlockPos checkPos = pos.relative(direction);
		BlockState state = world.getBlockState(checkPos);
		int count = 0;
		while (canExtendToward(state, direction)) {
			count++;
			checkPos = checkPos.relative(direction);
			state = world.getBlockState(checkPos);
		}
		return count;
	}

	private BlockState withAxis(BlockState state, Axis axis) {
		if (state.getBlock() instanceof TeslaStalkBlock)
			return state.setValue(TeslaStalkBlock.X, axis == Axis.X)
				.setValue(TeslaStalkBlock.Z, axis == Axis.Z)
				.setValue(TeslaStalkBlock.AXIS, axis);
		return state;
	}

	@Override
	public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
		List<Direction> directions =
			IPlacementHelper.orderedByDistance(pos, ray.getLocation(), dir -> canExtendToward(state, dir));
		for (Direction dir : directions) {
			int range = AllConfigs.server().equipment.placementAssistRange.get();
			if (player != null) {
				AttributeInstance reach = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
				if (reach != null && reach.hasModifier(ExtendoGripItem.singleRangeAttributeModifier))
					range += 4;
			}
			int poles = attachedPoles(world, pos, dir);
			if (poles >= range)
				continue;

			BlockPos newPos = pos.relative(dir, poles + 1);
			BlockState newState = world.getBlockState(newPos);

			if (!newState.getMaterial()
				.isReplaceable())
				continue;

			return PlacementOffset.success(newPos,
				bState -> Block.updateFromNeighbourShapes(withAxis(bState, dir.getAxis()), world, newPos));
		}

		return PlacementOffset.fail();
	}

}

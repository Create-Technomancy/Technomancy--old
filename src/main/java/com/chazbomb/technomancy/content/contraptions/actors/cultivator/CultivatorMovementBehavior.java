package com.chazbomb.technomancy.content.contraptions.actors.cultivator;

import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoulSandBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;


public class CultivatorMovementBehavior implements MovementBehaviour {

	@Override
	public boolean isActive(MovementContext context) {
		return MovementBehaviour.super.isActive(context)
				&& !VecHelper.isVecPointingTowards(context.relativeMotion, context.state.getValue(CultivatorBlock.FACING)
				.getOpposite());
	}

	@Override
	public void visitNewPosition(MovementContext context, BlockPos pos) {
		MovementBehaviour.super.visitNewPosition(context, pos);
		Level world = context.world;
		if (world.isClientSide)
			return;
		BlockPos below = pos.below();
		if (!world.isLoaded(below))
			return;

		if(world.getBlockState(below).getBlock() instanceof SoulSandBlock) {
			// todo: make particles
			// todo: send souls to internal storage
		}
		Vec3 vec = VecHelper.getCenterOf(pos);
		CultivatorBlock.CultivatorFakePlayer player = getPlayer(context);

		if (player == null)
			return;

		BlockHitResult ray = world.clip(new ClipContext(vec, vec.add(0, -1, 0), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
		if (ray.getType() != HitResult.Type.BLOCK)
			return;

		UseOnContext ctx = new UseOnContext(player, InteractionHand.MAIN_HAND, ray);
		new ItemStack(Items.DIAMOND_HOE).useOn(ctx);
	}

	@Override
	public void stopMoving(MovementContext context) {
		MovementBehaviour.super.stopMoving(context);
		if (context.temporaryData instanceof CultivatorBlock.CultivatorFakePlayer)
			((CultivatorBlock.CultivatorFakePlayer) context.temporaryData).discard();
	}

	private CultivatorBlock.CultivatorFakePlayer getPlayer(MovementContext context) {
		if (!(context.temporaryData instanceof CultivatorBlock.CultivatorFakePlayer) && context.world != null) {
			CultivatorBlock.CultivatorFakePlayer player = new CultivatorBlock.CultivatorFakePlayer((ServerLevel) context.world);
			player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.DIAMOND_HOE));
			context.temporaryData = player;
		}
		//noinspection DataFlowIssue - temporaryData instaceof CultivatorFakePlayer is asserted above
		return (CultivatorBlock.CultivatorFakePlayer) context.temporaryData;
	}
}

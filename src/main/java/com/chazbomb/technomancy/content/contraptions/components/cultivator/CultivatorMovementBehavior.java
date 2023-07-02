package com.chazbomb.technomancy.content.contraptions.components.cultivator;

import com.simibubi.create.content.contraptions.actors.plough.PloughBlock;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import com.chazbomb.technomancy.content.contraptions.components.cultivator.CultivatorBlock.CultivatorFakePlayer;


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

		Vec3 vec = VecHelper.getCenterOf(pos);
		CultivatorFakePlayer player = getPlayer(context);

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
		if (context.temporaryData instanceof CultivatorFakePlayer)
			((CultivatorFakePlayer) context.temporaryData).discard();
	}

	private CultivatorFakePlayer getPlayer(MovementContext context) {
		if (!(context.temporaryData instanceof CultivatorFakePlayer) && context.world != null) {
			CultivatorFakePlayer player = new CultivatorFakePlayer((ServerLevel) context.world);
			player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.DIAMOND_HOE));
			context.temporaryData = player;
		}
		if(context.temporaryData instanceof CultivatorFakePlayer)
			return (CultivatorFakePlayer) context.temporaryData;
		// this is easier to debug than if it throws a ClassCastException
		throw new RuntimeException("Cultivator Fake Player not found! Please report this.");
	}
}

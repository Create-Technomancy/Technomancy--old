package com.chazbomb.technomancy.mixin;

import com.simibubi.create.content.contraptions.actors.plough.PloughMovementBehaviour;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = PloughMovementBehaviour.class, remap = false)
public class PloughMovementBehaviourMixin {
	@Redirect(method = "visitNewPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;"))
	private InteractionResult noHoe(ItemStack instance, UseOnContext pContext) {
		return InteractionResult.PASS;
	}
}

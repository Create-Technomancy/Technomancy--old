package dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.repeater;

import dev.Cosmos616.technomancy.Technomancy;
import dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base.AbstractFirearmItem;
import dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base.AbstractFirearmItemRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.NonNullSupplier;

public class EnergyRepeaterItem extends AbstractFirearmItem {
	public EnergyRepeaterItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public Vec3 getBarrelOffset() {
		return Vec3.ZERO;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
		super.inventoryTick(stack, level, entity, slot, isSelected);
	}
	
	@Override
	protected int getFirerateTicks() {
		return 20;
	}
	
	@Override
	protected int getReloadTicks() {
		return 80;
	}
	
	@Override
	protected int getMaxAmmo() {
		return 8;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public AbstractFirearmItemRenderer<?> getRenderer() {
		return new EnergyRepeaterItemRenderer();
	}
}

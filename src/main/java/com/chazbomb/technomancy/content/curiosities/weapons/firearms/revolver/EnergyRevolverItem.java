package com.chazbomb.technomancy.content.curiosities.weapons.firearms.revolver;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.AbstractFirearmItem;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.AbstractFirearmItemRenderer;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.revolver.EnergyRevolverItemRenderer;
import com.chazbomb.technomancy.registry.TMItems;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.property.Properties;


public class EnergyRevolverItem extends AbstractFirearmItem {
	public EnergyRevolverItem(Properties properties) {
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
		return 10;
	}

	@Override
	protected int cooldownTicks() {
		return 15;
	}

	@Override
	protected Item getItem() {
		return TMItems.ENERGY_REVOLVER.get();
	}

	@Override
	public boolean usesMagazineReload() {
		return true;
	}

	@Override
	protected int getReloadTicks() {
		return 5;
	}

	@Override
	protected int getMaxAmmo() {
		return 8;
	}

	@Override
	public AbstractFirearmItemRenderer getRenderer() {
		return new EnergyRevolverItemRenderer();
	}
}

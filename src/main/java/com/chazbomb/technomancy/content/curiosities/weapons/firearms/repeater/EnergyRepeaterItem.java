package com.chazbomb.technomancy.content.curiosities.weapons.firearms.repeater;
import com.chazbomb.technomancy.TechnomancyClient;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.AbstractFirearmItem;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.AbstractFirearmItemRenderer;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.FirearmProjectileEntity;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.FirearmShootPacket;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.repeater.EnergyRepeaterItemRenderer;
import com.chazbomb.technomancy.registry.TMEntities;
import com.chazbomb.technomancy.registry.TMItems;
import com.chazbomb.technomancy.registry.TMPackets;
import com.simibubi.create.content.equipment.zapper.ShootableGadgetItemMethods;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;



public class EnergyRepeaterItem extends AbstractFirearmItem {
	public EnergyRepeaterItem(Properties properties) {
		super(properties);
	}

	public boolean isShooting = false;
	public FirearmProjectileEntity projectile;
	private int shotTimer=0;


	@Override
	public Vec3 getBarrelOffset() {
		return Vec3.ZERO;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
		super.inventoryTick(stack, level, entity, slot, isSelected);
		if(isShooting){
			shotTimer++;
			if(shotTimer==5||shotTimer>=10){
	shootWeapon((Player) entity,stack,true);
				TMPackets.channel.sendToServer(new FirearmShootPacket(ShootableGadgetItemMethods.getGunBarrelVec((Player) entity, true, ((AbstractFirearmItem)stack.getItem()).getBarrelOffset())));

					if(shotTimer>=10){
						shotTimer=0;
						isShooting=false;

				}
			}

		}

	}



	@Override
	protected int getFirerateTicks() {
		return 5;
	}

	@Override
	protected int cooldownTicks() {
		return 20;
	}

	@Override
	protected Item getItem() {
		return TMItems.ENERGY_REPEATER.get();
	}

	@Override
	public boolean usesMagazineReload() {
		return false;
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
	public void shootWeapon(Player player, ItemStack stack, boolean isClient, Vec3 barrelVec) {
		if (isClient) {
			TechnomancyClient.FIREARM_RENDER_HANDLER.shoot(InteractionHand.MAIN_HAND, barrelVec);
			if(!isShooting)
				isShooting=true;
			return;
		}

		// Summon projectile
		Level world = player.level;
		 projectile = TMEntities.FIREARM_PROJECTILE.create(world);
		if (projectile == null)
			return;
		projectile.setProjectile(getProjectileType(stack));
		Vec3 lookVec = player.getLookAngle();
		Vec3 motion = lookVec.add(barrelVec.subtract(player.position().add(0, player.getEyeHeight(), 0)))
				.normalize().scale(2);
		projectile.setPos(barrelVec.x, barrelVec.y, barrelVec.z);
		projectile.setDeltaMovement(motion);
		projectile.setOwner(player);
		world.addFreshEntity(projectile);



	}



	@Override
	public AbstractFirearmItemRenderer getRenderer() {
		return new EnergyRepeaterItemRenderer();
	}
}

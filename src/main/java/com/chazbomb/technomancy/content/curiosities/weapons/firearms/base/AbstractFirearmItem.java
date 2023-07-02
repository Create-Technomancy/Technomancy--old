package com.chazbomb.technomancy.content.curiosities.weapons.firearms.base;

import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import com.chazbomb.technomancy.TechnomancyClient;
import com.chazbomb.technomancy.foundation.keys.TMKeys;
import com.chazbomb.technomancy.registry.TMEntities;
import com.chazbomb.technomancy.registry.TMItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber
public abstract class AbstractFirearmItem extends Item {

	public AbstractFirearmItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public ItemStack getDefaultInstance() {
		ItemStack firearm = new ItemStack(this);
		CompoundTag tag = firearm.getOrCreateTag();
		tag.putInt("Ammunition", 0);
		return firearm;
	}
	
	public abstract Vec3 getBarrelOffset();
	
	public boolean hasAmmo(ItemStack stack) {
		return getAmmo(stack) > 0;
	}
	
	public int getAmmo(ItemStack stack) {
		return stack.getOrCreateTag().getInt("Ammunition");
	}

	//public abstract boolean isAutomatic();

	protected abstract int getMaxAmmo();
	
	protected abstract int getFirerateTicks();

	protected abstract int cooldownTicks();

	protected abstract Item getItem();
	
	public void shootWeapon(Player player, ItemStack stack, boolean isClient) {
		this.shootWeapon(player, stack, isClient, Vec3.ZERO);
	}
	public void shootWeapon(Player player, ItemStack stack, boolean isClient, Vec3 barrelVec) {
		if (isClient) {
			TechnomancyClient.FIREARM_RENDER_HANDLER.shoot(InteractionHand.MAIN_HAND, barrelVec);
			return;
		}
		stack.getOrCreateTag().putInt("Ammunition", getAmmo(stack) - 1); // Update ammo
		// Summon projectile
		Level world = player.level;
		FirearmProjectileEntity projectile = TMEntities.FIREARM_PROJECTILE.create(world);
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

	protected ProjectileType getProjectileType(ItemStack stack) {
		return ProjectileType.DEFAULT;
	}
	

	@Override
	public boolean isBarVisible(ItemStack stack) {
		if(!usesMagazineReload())
			return false;
		LocalPlayer player = Minecraft.getInstance().player;
		return player != null && !player.isCreative();
	}
	
	private static final int barColor = 0x50dce1;
	@Override
	public int getBarColor(ItemStack stack) {
		return barColor;
	}
	
	@Override
	public int getBarWidth(ItemStack stack) {
		return Math.round((float)stack.getOrCreateTag().getInt("Ammunition") * 13.0F / (float)this.getMaxAmmo());
	}
	public ItemStack getAllSupportedProjectiles() {
		return TMItems.HALLOWED_BULLET.get().getDefaultInstance();
	}
	public ItemStack getAmmunition(ItemStack firearm,Player player){
		ItemStack itemStack = ((AbstractFirearmItem)firearm.getItem()).getAllSupportedProjectiles();
		//ItemStack itemstack = ProjectileWeaponItem.getHeldProjectile(this, predicate);
		//if (!itemstack.isEmpty()) {
		//	return net.minecraftforge.common.ForgeHooks.getProjectile(this, pShootable, itemstack);
		//} else {
			//predicate = ((ProjectileWeaponItem)pShootable.getItem()).getAllSupportedProjectiles();

			for(int i = 0; i < player.getInventory().getContainerSize(); ++i) {
				ItemStack itemstack1 = player.getInventory().getItem(i);
				if (itemstack1.is(itemStack.getItem())) {
					return itemstack1;
				}
			}

			return ItemStack.EMPTY;
			//return net.minecraftforge.common.ForgeHooks.getProjectile(this, pShootable, this.abilities.instabuild ? new ItemStack(Items.ARROW) : ItemStack.EMPTY);
		//}
	}
	public abstract boolean usesMagazineReload();
	protected abstract int getReloadTicks();
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
		if (getAmmo(stack)>getMaxAmmo())
			stack.getOrCreateTag().putInt("Ammunition", 8);

		if(entity instanceof Player  &&((Player)entity).getMainHandItem() == stack) {
			ItemStack itemstack =getAmmunition(stack,(Player) entity);

			if(hasCooldown((Player)entity,stack))
				return;



			if(!usesMagazineReload())
				return;


			if (TMKeys.reload.isDown() && Minecraft.getInstance().screen == null && stack.getOrCreateTag().getInt("Ammunition") != 8) {
				int toReload=8- stack.getOrCreateTag().getInt("Ammunition");
				for(int i =0;i<toReload;i++) {
						itemstack = getAmmunition(stack, (Player) entity);
						if(!itemstack.isEmpty()) {
							stack.getOrCreateTag().putInt("Ammunition", stack.getOrCreateTag().getInt("Ammunition") + 1);
							itemstack.shrink(1);
						}
					}
				if(getAmmo(stack)!=0&&itemstack.getCount()!=0) {
					TechnomancyClient.FIREARM_RENDER_HANDLER.reload(InteractionHand.MAIN_HAND, Vec3.ZERO);
					((Player) entity).getCooldowns().addCooldown(getItem(), cooldownTicks());
				}
				}

			}

		}

	private boolean hasCooldown(Player player,ItemStack firearm) {
		return player.getCooldowns().isOnCooldown(firearm.getItem());
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}
	
	@Override
	public boolean canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
		return false;
	}
	
	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
		return true;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.NONE;
	}


	@OnlyIn(Dist.CLIENT)
	public abstract AbstractFirearmItemRenderer getRenderer();
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		consumer.accept(SimpleCustomRenderer.create(this, this.getRenderer()));
	}

}

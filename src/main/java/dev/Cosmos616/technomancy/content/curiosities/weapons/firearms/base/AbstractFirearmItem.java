package dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base;

import com.simibubi.create.CreateClient;
import com.simibubi.create.content.curiosities.zapper.ShootGadgetPacket;
import com.simibubi.create.content.curiosities.zapper.ShootableGadgetItemMethods;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import com.simibubi.create.foundation.networking.AllPackets;
import dev.Cosmos616.technomancy.Technomancy;
import dev.Cosmos616.technomancy.TechnomancyClient;
import dev.Cosmos616.technomancy.registry.TMEntities;
import dev.Cosmos616.technomancy.registry.TMPackets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Consumer;
import java.util.function.Function;

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
	
	protected abstract int getMaxAmmo();
	
	protected abstract int getFirerateTicks();
	
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
	
	protected abstract int getReloadTicks();
	// true: load all available bullets at once
	// false: load each available bullet individually
	public boolean usesMagazineReload() {
		return true;
	}
	public void reloadWeapon(Player player, ItemStack gun, BulletItem bullet) {
		Technomancy.LOGGER.debug("Reloading");
	}
	
	@Override
	public boolean isBarVisible(ItemStack stack) {
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
	
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
		if (level.isClientSide)
			return;
		
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
	public abstract AbstractFirearmItemRenderer<?> getRenderer();
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		consumer.accept(SimpleCustomRenderer.create(this, this.getRenderer()));
	}
}

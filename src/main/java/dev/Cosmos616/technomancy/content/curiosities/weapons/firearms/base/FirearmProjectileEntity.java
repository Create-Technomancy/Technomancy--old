package dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base;

import com.simibubi.create.content.contraptions.particle.AirParticleData;
import dev.Cosmos616.technomancy.Technomancy;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class FirearmProjectileEntity extends AbstractHurtingProjectile implements IEntityAdditionalSpawnData {
	public FirearmProjectileEntity(EntityType<? extends AbstractHurtingProjectile> type, Level level) {
		super(type, level);
	}
	
	protected ProjectileType projectileType = ProjectileType.DEFAULT;
	protected int leftoverPierce;
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		projectileType = ProjectileType.deserialize(compound);
		leftoverPierce = compound.getInt("currentPierce");
		super.readAdditionalSaveData(compound);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		projectileType.serialize(compound);
		compound.putInt("currentPierce", leftoverPierce);
		super.addAdditionalSaveData(compound);
	}
	
	private static final int UPDATE_MAX_TICKS = 8;
	private int tickUpdate = UPDATE_MAX_TICKS;
	@Override
	public void tick() {
		super.tick();
		
		tickUpdate--;
		if (tickUpdate > 0)
			return;
		this.refreshDimensions();
		tickUpdate = UPDATE_MAX_TICKS;
	}
	
	@Override
	protected void onHitEntity(EntityHitResult hitResult) {
		Entity target = hitResult.getEntity();
		Entity owner = this.getOwner();
		
		if (!target.isAlive())
			return;
		if (owner instanceof LivingEntity livingOwner)
			livingOwner.setLastHurtMob(target);
		
		boolean onServer = !level.isClientSide;
		boolean passes = canPass(projectileType.entityHitPass.test(target));
		if (onServer && !target.hurt(projectileType.damageSource
						.apply(new DamageSource(Technomancy.MOD_ID + ".projectile." + projectileType.toString().toLowerCase()).setProjectile()),
				Mth.lerp(projectileType.damageDrop.apply(this.flyDist), projectileType.closeDamage, projectileType.farDamage))) {
			if (!passes)
				kill();
			return;
		}
		if (!(target instanceof LivingEntity livingTarget)) {
			if (!passes)
				kill();
			return;
		}
		
		if (livingTarget != owner && livingTarget instanceof Player && owner instanceof ServerPlayer serverPlayer
				&& !this.isSilent()) {
			serverPlayer.connection
					.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
		}
		
		if (!passes)
			kill();
	}
	
	@Override
	protected void onHitBlock(BlockHitResult hitResult) {
		super.onHitBlock(hitResult);
		if (!canPass(projectileType.blockHitPass.test(hitResult.getBlockPos())))
			kill();
	}
	
	protected boolean canPass(boolean testedPredicate) {
		if (leftoverPierce <= 0 || !testedPredicate)
			return false;
		leftoverPierce--;
		return true;
	}
	
	@Override
	protected float getInertia() {
		return 2f * projectileType.baseSpeed * projectileType.speedMultiplier.apply(this);
	}
	
	
	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return super.getDimensions(pose).scale(projectileType.baseSize * projectileType.sizeMultiplier.apply(this));
	}
	
	@Override
	protected ParticleOptions getTrailParticle() {
		return new AirParticleData(1, 10);
	}
	
	@Override
	protected boolean shouldBurn() {
		return false;
	}
	
	public FirearmProjectileEntity setProjectile(ProjectileType projectileType) {
		this.projectileType = projectileType;
		this.leftoverPierce = this.projectileType.pierce;
		this.refreshDimensions();
		return this;
	}
	
	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		CompoundTag tag = new CompoundTag();
		addAdditionalSaveData(tag);
		buffer.writeNbt(tag);
	}
	
	@Override
	public void readSpawnData(FriendlyByteBuf buffer) {
		readAdditionalSaveData(buffer.readNbt());
	}
}

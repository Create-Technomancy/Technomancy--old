package com.chazbomb.technomancy.content.curiosities.weapons.firearms.base;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.particle.AirParticleData;
import com.chazbomb.technomancy.Technomancy;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.soul_sparks.SoulSparkEntity;
import com.chazbomb.technomancy.registry.TMEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class FirearmProjectileEntity extends AbstractHurtingProjectile implements IEntityAdditionalSpawnData {
	public FirearmProjectileEntity(EntityType<? extends AbstractHurtingProjectile> type, Level level) {
		super(type, level);
	}
	
	protected ProjectileType projectileType = ProjectileType.DEFAULT;
	protected int leftoverPierce=5;
	
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
		Vec3 vec3 = this.getDeltaMovement();
		if(isInWater()) {
			this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).scale((double) 1.204));


			double d2 = this.getX() + vec3.x;
			double d0 = this.getY() + vec3.y;
			double d1 = this.getZ() + vec3.z;
			this.updateRotation();

			if (!this.isNoGravity()) {
				Vec3 vec31 = this.getDeltaMovement();
				this.setDeltaMovement(vec31.x, vec31.y - (double)this.getGravity(), vec31.z);
			}

			this.setPos(d2, d0, d1);





		}
		if(this.level.isClientSide) {

			this.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, -this.getDeltaMovement().y * 0.5D, this.random.nextGaussian() * 0.05D);
		}






		tickUpdate--;
		if (tickUpdate > 0)
			return;
		this.refreshDimensions();
		tickUpdate = UPDATE_MAX_TICKS;
	}
	protected void explode() {

		if (!this.level.isClientSide) {
		/*	this.level.broadcastEntityEvent(this, (byte) 3);

		 */

			for (int i = 0; i < 10; i++) {
				float x = Create.RANDOM.nextFloat(360);
				float y = Create.RANDOM.nextFloat(360);
				float z = Create.RANDOM.nextFloat(360);
				SoulSparkEntity spark = TMEntities.SOUL_SPARK.create(level);
				spark.moveTo(this.getX(), this.getY(), this.getZ());
				spark.shootFromRotation(this, x, y, z, 0.2f, 1);
				this.level.addFreshEntity(spark);
			}
		//	this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 2.0F, Explosion.BlockInteraction.NONE);

		}
	}
	@Override
	protected void onHitEntity(EntityHitResult hitResult) {

		Entity target = hitResult.getEntity();
		Entity owner = this.getOwner();
		explode();
		if (!target.isAlive())
			return;
		if (owner instanceof LivingEntity livingOwner)
			livingOwner.setLastHurtMob(target);

		boolean onServer = !level.isClientSide;
		//boolean passes = canPass(projectileType.entityHitPass.test(target));
		if (onServer && !target.hurt(projectileType.damageSource
						.apply(new DamageSource(Technomancy.MOD_ID + ".projectile." + projectileType.toString().toLowerCase()).setProjectile()),
				Mth.lerp(projectileType.damageDrop.apply(this.flyDist), projectileType.closeDamage, projectileType.farDamage)*3)) {
		//	if (!passes)
		//		kill();
			return;
		}
		if (!(target instanceof LivingEntity livingTarget)) {
		//	if (!passes)
		//		kill();
			return;
		}
		
		if (livingTarget != owner && livingTarget instanceof Player && owner instanceof ServerPlayer serverPlayer
				&& !this.isSilent()) {
			serverPlayer.connection
					.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
		}
		
	//	if (!passes)
		//	kill();


	}
	
	@Override
	protected void onHitBlock(BlockHitResult hitResult) {
		super.onHitBlock(hitResult);

		BlockPos state = hitResult.getBlockPos();

		if (!canPass(state)) {
			explode();
			this.discard();
		}
	}
	
	protected boolean canPass(BlockPos landingPos) {
		if (leftoverPierce <= 0)
			return false;
		if(
				level.getBlockState(landingPos).is(Blocks.SLIME_BLOCK)||
				level.getBlockState(landingPos).is(Blocks.HONEY_BLOCK)||
				level.getBlockState(landingPos).is(Blocks.ICE)||
				level.getBlockState(landingPos).is(Blocks.PACKED_ICE)
		) {

			if (level.getBlockState(landingPos).is(Blocks.ICE))
				level.destroyBlock(landingPos, false);
			leftoverPierce--;
			return true;
		}
	return false;
	}
	
	@Override
	protected float getInertia() {
		return  projectileType.baseSpeed * projectileType.speedMultiplier.apply(this);
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
		//this.leftoverPierce = this.projectileType.pierce;
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
	protected float getGravity() {
		return 0.01F;
	}

	@SuppressWarnings("unchecked")
	public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
		EntityType.Builder<FirearmProjectileEntity> entityBuilder = (EntityType.Builder<FirearmProjectileEntity>) builder;
		return entityBuilder.sized(.25f, .25f);
	}
}

package dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base;

import com.simibubi.create.content.contraptions.particle.AirParticleData;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.ParametersAreNonnullByDefault;

public class FirearmProjectileEntity extends AbstractHurtingProjectile implements IEntityAdditionalSpawnData {
	public FirearmProjectileEntity(EntityType<? extends AbstractHurtingProjectile> type, Level level) {
		super(type, level);
	}
	
	protected ProjectileType projectileType = ProjectileType.DEFAULT;
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		projectileType = ProjectileType.fromNBT(compound);
		super.readAdditionalSaveData(compound);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		projectileType.serializeNBT(compound);
		super.addAdditionalSaveData(compound);
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
		if (onServer && !target.hurt(projectileType.getSource.apply(target, owner), projectileType.damage)) {
			kill();
			return;
		}
		if (!(target instanceof LivingEntity livingTarget)) {
			kill();
			return;
		}
		
		if (livingTarget != owner && livingTarget instanceof Player && owner instanceof ServerPlayer serverPlayer
				&& !this.isSilent()) {
			serverPlayer.connection
					.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
		}
		
		kill();
	}
	
	@Override
	protected void onHitBlock(BlockHitResult hitResult) {
		super.onHitBlock(hitResult);
		kill();
	}
	
	@Override
	protected float getInertia() {
		return 2f * projectileType.speedMultiplier;
	}
	
	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return super.getDimensions(pose).scale(projectileType.sizeMultiplier);
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

package com.chazbomb.technomancy.content.curiosities.weapons.firearms.base;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;
import java.util.function.Predicate;

public enum ProjectileType {
	DEFAULT("default", source -> source, 4, 0, 1, 1)
	;
	
	private final String id;
	
	// Damage
	public final Function<DamageSource, DamageSource> damageSource; // Gets damage source (target, owner)
	public final float closeDamage;
	public final float farDamage;
	public final Function<Float, Float> damageDrop; // Used as a way to determine how much damage should drop / rise (0 - 1)
	public final int pierce;
	
	// Hit tests (all apply to pierce)
	public final Predicate<BlockPos> blockHitPass; // For block hit effects. Boolean will determine if projectile can pass through
	public final Predicate<Entity> entityHitPass; // For entity hit effects. Boolean will determine if projectile can pass through
	
	// Speed
	public final float baseSpeed;
	public final Function<FirearmProjectileEntity, Float> speedMultiplier; // Speed modifier (0 - 1)
	
	// Size
	public final float baseSize;
	public final Function<FirearmProjectileEntity, Float> sizeMultiplier; // Size modifier (0 - 1)
	
	
	ProjectileType(String id, Function<DamageSource, DamageSource> damageSource, float closeDamage, float farDamage, Function<Float, Float> damageDrop, int pierce,
				   Predicate<BlockPos> blockHitPass, Predicate<Entity> entityHitPass,
				   float baseSpeed, Function<FirearmProjectileEntity, Float> speedMultiplier,
				   float baseSize, Function<FirearmProjectileEntity, Float> sizeMultiplier) {
		this.id = id;
		this.damageSource = damageSource;
		this.closeDamage = closeDamage;
		this.farDamage = farDamage;
		this.damageDrop = damageDrop;
		this.pierce = pierce;
		this.blockHitPass = blockHitPass;
		this.entityHitPass = entityHitPass;
		this.baseSpeed = baseSpeed;
		this.speedMultiplier = speedMultiplier;
		this.baseSize = baseSize;
		this.sizeMultiplier = sizeMultiplier;
	}
	
	ProjectileType(String id, Function<DamageSource, DamageSource> damageSource, float damage, int pierce, float speed, float size) {
		this(id, damageSource, damage, damage, dist -> 1f, pierce, pos -> false, entity -> true, speed, projectile -> 1f, size, projectile -> 1f);
	}
	
	public void serialize(CompoundTag NBT) {
		NBT.putString("ProjectileType", id);
	}
	
	public static ProjectileType deserialize(CompoundTag NBT) {
		String tag = NBT.getString("ProjectileType");
		for (ProjectileType projectile : ProjectileType.values()) {
			if (!projectile.id.equals(tag))
				continue;
			return projectile;
		}
		return DEFAULT;
	}
}

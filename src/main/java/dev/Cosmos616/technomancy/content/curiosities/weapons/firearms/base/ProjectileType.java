package dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base;

import dev.Cosmos616.technomancy.Technomancy;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ProjectileType {
	private static Map<String, ProjectileType> types = new HashMap<>();
	
	public static final ProjectileType
			DEFAULT = new ProjectileType("default", 1, 1, 4);
	
	public ProjectileType(String id, float speed, float size, float damage) {
		this.speedMultiplier = speed;
		this.sizeMultiplier = size;
		this.damage = damage;
		
		this.getSource = (source, trueSource) ->
				new IndirectEntityDamageSource(Technomancy.MOD_ID + ".firearms." + id, source, trueSource)
				.setProjectile();
		
		this.id = id;
		types.put(id, this);
	}
	public float speedMultiplier;
	public float sizeMultiplier;
	public float damage;
	public BiFunction<Entity, Entity, DamageSource> getSource;
	
	protected String id;
	public void serializeNBT(CompoundTag nbt) {
		nbt.putString("ProjectileType", id);
	}
	
	public static ProjectileType fromNBT(CompoundTag nbt) {
		String val = nbt.getString("ProjectileType");
		ProjectileType pType = DEFAULT;
		for (Map.Entry<String, ProjectileType> entry : types.entrySet()) {
			if (!entry.getKey().equals(val))
				continue;
			pType = entry.getValue();
			break;
		}
		return pType;
	}
	
	public static void initialize() { }
}

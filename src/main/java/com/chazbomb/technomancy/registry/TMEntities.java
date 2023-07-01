package com.chazbomb.technomancy.registry;

import com.chazbomb.technomancy.Technomancy;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.archer.shockwave.ShockWaveEntity;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.archer.shockwave.ShockWaveRenderer;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.FirearmProjectileEntity;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.FirearmProjectileRenderer;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.soul_sparks.SoulSparkEntity;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.soul_sparks.SoulSparkRenderer;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateEntityBuilder;
import com.simibubi.create.foundation.utility.Lang;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class TMEntities {

	public static final EntityEntry<FirearmProjectileEntity> FIREARM_PROJECTILE =
			register("firearm_projectile", FirearmProjectileEntity::new, () -> FirearmProjectileRenderer::new,
					MobCategory.MISC, 4, 20, true, true, FirearmProjectileEntity::build).register();


	public static final EntityEntry<SoulSparkEntity> SOUL_SPARK =
			register("soul_spark", SoulSparkEntity::new, () -> SoulSparkRenderer::new,
					MobCategory.MISC, 4, 20, true, true, SoulSparkEntity::build).register();

	public static final EntityEntry<ShockWaveEntity> SHOCKWAVE =
			register("shockwave", ShockWaveEntity::new,
					() -> ShockWaveRenderer::new,
					MobCategory.MISC, 4, 20, true, true, ShockWaveEntity::build).register();


	private static <T extends Entity> CreateEntityBuilder<T, ?> register(String name, EntityType.EntityFactory<T> factory,
																		 NonNullSupplier<NonNullFunction<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer,
																		 MobCategory group, int range, int updateFrequency, boolean sendVelocity, boolean immuneToFire,
																		 NonNullConsumer<EntityType.Builder<T>> propertyBuilder) {
		String id = Lang.asId(name);
		return (CreateEntityBuilder<T, ?>) Create.REGISTRATE
				.entity(id, factory, group)
				.properties(b -> b.setTrackingRange(range)
						.setUpdateInterval(updateFrequency)
						.setShouldReceiveVelocityUpdates(sendVelocity))
				.properties(propertyBuilder)
				.properties(b -> {
					if (immuneToFire)
						b.fireImmune();
				})
				.renderer(renderer);
	}
	public static void register() {
		Technomancy.LOGGER.info("Registering entities!");
	}
}
package dev.Cosmos616.technomancy.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.EntityEntry;
import dev.Cosmos616.technomancy.Technomancy;
import dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base.FirearmProjectileEntity;
import dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base.FirearmProjectileRenderer;
import dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base.soul_sparks.SoulSparkEntity;
import dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base.soul_sparks.SoulSparkRenderer;
import net.minecraft.world.entity.MobCategory;

public class TMEntities {
	private static final CreateRegistrate REGISTRATE = Technomancy.getRegistrate();
	
	public static final EntityEntry<FirearmProjectileEntity> FIREARM_PROJECTILE = REGISTRATE
			.entity("firearm_projectile", FirearmProjectileEntity::new, MobCategory.MISC)
			.renderer(() -> FirearmProjectileRenderer::new)
			.properties(p -> p
					.sized(0.25f, 0.25f)
					.fireImmune())
			.register();
	public static final EntityEntry<SoulSparkEntity> SOUL_SPARK = REGISTRATE
			.entity("soul_spark", SoulSparkEntity::new, MobCategory.MISC)
			.renderer(() -> SoulSparkRenderer::new)
			.properties(p -> p
					.sized(0.25f, 0.25f)
					.fireImmune())
			.register();
	
	public static void register() {}
}
package dev.Cosmos616.technomancy.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.EntityEntry;
import dev.Cosmos616.technomancy.Technomancy;
import dev.Cosmos616.technomancy.content.curiosities.weapons.firearm.FirearmProjectileEntity;
import dev.Cosmos616.technomancy.content.curiosities.weapons.firearm.FirearmProjectileRenderer;
import net.minecraft.world.entity.MobCategory;

public class TMEntities {
	private static final CreateRegistrate REGISTRATE = Technomancy.getRegistrate();
	
	public static final EntityEntry<FirearmProjectileEntity> FIREARM_PROJECTILE = REGISTRATE
			.entity("firearm_projectile", FirearmProjectileEntity::new, MobCategory.MISC)
			.renderer(() -> FirearmProjectileRenderer::new)
			.register();
}
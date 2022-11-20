package dev.Cosmos616.technomancy.content.curiosities.weapons.firearm;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;

@MethodsReturnNonnullByDefault
public class FirearmProjectileEntity extends AbstractHurtingProjectile {
	public FirearmProjectileEntity(EntityType<? extends AbstractHurtingProjectile> type, Level level) {
		super(type, level);
	}
	
	@Override
	public void tick() {
		super.tick();
		
	}
}

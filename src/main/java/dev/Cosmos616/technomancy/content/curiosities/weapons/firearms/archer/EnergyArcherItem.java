package dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.archer;
import dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base.AbstractFirearmItem;
import dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base.AbstractFirearmItemRenderer;

import net.minecraft.world.phys.Vec3;


public class EnergyArcherItem extends AbstractFirearmItem {
    public EnergyArcherItem(Properties properties) {
        super(properties);
    }
    @Override
    public Vec3 getBarrelOffset() {
        return Vec3.ZERO;
    }

    @Override
    protected int getMaxAmmo() {
        return 5;
    }

    @Override
    protected int getFirerateTicks() {
        return 10;
    }

    @Override
    protected int getReloadTicks() {
        return 8;
    }

    @Override
    public AbstractFirearmItemRenderer<?> getRenderer() {
        return new EnergyArcherItemRenderer();
    }
}

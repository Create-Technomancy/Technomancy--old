package com.chazbomb.technomancy.content.curiosities.weapons.firearms.archer;
import com.chazbomb.technomancy.TechnomancyClient;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.archer.shockwave.ShockWaveEntity;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.archer.ui.EnergyArcherUI;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.AbstractFirearmItem;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.AbstractFirearmItemRenderer;

import com.chazbomb.technomancy.foundation.keys.TMKeys;
import com.chazbomb.technomancy.registry.TMItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static com.chazbomb.technomancy.content.curiosities.weapons.firearms.archer.ui.EnergyArcherUI.*;


public class EnergyArcherItem extends AbstractFirearmItem {
    public int chargeLevel=0;
    public boolean charging=false;

    public float power=0;
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slot, isSelected);

        if(TMKeys.reload.consumeClick())
            charging=!charging;
        power = displayedThrottle.getValue()*100;
        power/=10;
        power+=4;


        //if(isSelected)
       //     level.setBlock(entity.blockPosition().west((int)power/3), TMBlocks.ZIRCON_BLOCK.getDefaultState(),1);


        EnergyArcherUI.toRender = ((Player) entity).getMainHandItem().getItem() == TMItems.ENERGY_ARCHER.get();
        if(charging){
            chargeLevel=1;

        }else  {
            chargeLevel=0;
        }
        charge=chargeLevel;
    }
    @Override
    public void shootWeapon(Player player, ItemStack stack, boolean isClient, Vec3 barrelVec) {
        if (isClient) {
            TechnomancyClient.FIREARM_RENDER_HANDLER.shoot(InteractionHand.MAIN_HAND, barrelVec);
            return;
        }

        Level world = player.level;
        ShockWaveEntity projectile = new ShockWaveEntity(player.level,power,player);
       // projectile.powerLevel=13;

      // projectile.setProjectile(getProjectileType(stack));
        Vec3 lookVec = player.getLookAngle();
        Vec3 motion = lookVec.add(barrelVec.subtract(player.position().add(0, player.getEyeHeight(), 0)))
                .normalize().scale(0.8);
        projectile.setPos(barrelVec.x, barrelVec.y-0.2f, barrelVec.z);
        projectile.setDeltaMovement(motion);
        projectile.setOwner(player);
       // projectile.setPowerLevel(power);
        world.addFreshEntity(projectile);
    }

    @Override
    public boolean usesMagazineReload() {
        return false;
    }


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
    protected int cooldownTicks() {
        return 25;
    }

    @Override
    protected Item getItem() {
        return TMItems.ENERGY_ARCHER.get();
    }

    @Override
    protected int getReloadTicks() {
        return 8;
    }

    @Override
    public AbstractFirearmItemRenderer getRenderer() {
        return new EnergyArcherItemRenderer();
    }
}

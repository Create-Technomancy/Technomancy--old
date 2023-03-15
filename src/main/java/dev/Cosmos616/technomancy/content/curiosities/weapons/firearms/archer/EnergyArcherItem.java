package dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.archer;
import com.simibubi.create.content.contraptions.components.structureMovement.interaction.controls.TrainHUD;
import dev.Cosmos616.technomancy.TechnomancyClient;
import dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.archer.ui.EnergyArcherUI;
import dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base.AbstractFirearmItem;
import dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base.AbstractFirearmItemRenderer;

import dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.base.FirearmProjectileEntity;
import dev.Cosmos616.technomancy.foundation.keys.TMKeys;
import dev.Cosmos616.technomancy.registry.TMEntities;
import dev.Cosmos616.technomancy.registry.TMItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.VibrationSignalParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.IIngameOverlay;

import static dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.archer.ui.EnergyArcherUI.*;


public class EnergyArcherItem extends AbstractFirearmItem {
    public int chargeLevel=0;
    public boolean charging=false;
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slot, isSelected);
        if(TMKeys.reload.consumeClick())
            charging=!charging;



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
        super.shootWeapon(player,stack,isClient,barrelVec);

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
    protected int getReloadTicks() {
        return 8;
    }

    @Override
    public AbstractFirearmItemRenderer<?> getRenderer() {
        return new EnergyArcherItemRenderer();
    }
}

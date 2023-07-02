package com.chazbomb.technomancy.content.curiosities.weapons.firearms.archer.shockwave;



import javax.annotation.Nullable;

import com.simibubi.create.Create;

import com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.ProjectileType;
import com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.soul_sparks.SoulSparkEntity;
import com.chazbomb.technomancy.registry.TMEntities;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ShockWaveEntity extends Projectile {

    private static final EntityDataAccessor<Byte> PIERCE_LEVEL = SynchedEntityData.defineId(ShockWaveEntity.class, EntityDataSerializers.BYTE);

    public float powerLevel;
    boolean setSize = false;
    float hitboxSize=0;
    int counter=12;
    public Entity ownerr;

    private SoundEvent soundEvent = this.getDefaultHitGroundSoundEvent();
    public float size =hitboxSize/5f;
    public ShockWaveEntity(EntityType<? extends ShockWaveEntity> p_37027_, Level p_37028_) {
        super(p_37027_, p_37028_);
    }

    public ShockWaveEntity(Level level, float power,Entity Owner) {
        this(TMEntities.SHOCKWAVE.get(), level);
        counter=(int)power;
        ownerr=Owner;

        //this.powerLevel=25;

    }









    public void setPowerLevel(float level){
        this.powerLevel=level;

        //this.counter = (int) (powerLevel / 1.5);
    }


    @Override
    protected void defineSynchedData() {

    }
    private void hurt(List<Entity> pEntities) {
        for(Entity entity : pEntities) {
            if (entity instanceof LivingEntity) {
                if(ownerr!=null)
                    if(!entity.getUUID().equals(ownerr.getUUID()))
                entity.hurt(DamageSource.MAGIC, (int)(hitboxSize*1.3));
                //this.doEnchantDamageEffects(this, entity);
            }
        }

    }
    private void knockBack(List<Entity> pEntities) {

        double d0 = (this.getBoundingBox().minX + this.getBoundingBox().maxX) / 2.0D;
        double d1 = (this.getBoundingBox().minZ + this.getBoundingBox().maxZ) / 2.0D;

        for(Entity entity : pEntities) {
            if(ownerr!=null)
              if(entity.getUUID().equals(ownerr.getUUID()))
                 return;
            if (entity instanceof LivingEntity) {
                double d2 = entity.getX() - d0;
                double d3 = entity.getZ() - d1;
                double d4 = Math.max(d2 * d2 + d3 * d3, 0.1D);
                if(ownerr!=null)
                    if(!entity.getUUID().equals(ownerr.getUUID()))
                entity.push(d2 / d4 * 0.5, (double)0.2F, d3 / d4 * 0.5);
                this.hurt(pEntities);

            }
        }

    }
    public void tick() {
        super.tick();
        if(counter==0)
            this.discard();
        powerLevel = counter;

       // if(powerLevel==0)
         //   return;
        this.makeBoundingBox();
        counter--;


        if(!setSize){

            setSize=true;
        }

            hitboxSize=12-counter;
            hitboxSize/=1.5;
        size=hitboxSize/5;
        refreshDimensions();
       // this.hurt(this.level.getEntities(this, this.getBoundingBox().inflate(1.0D), EntitySelector.NO_CREATIVE_OR_SPECTATOR));

             this.knockBack(this.level.getEntities(this, this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D).move(0.0D, -2.0D, 0.0D), EntitySelector.NO_CREATIVE_OR_SPECTATOR));


        boolean flag = false;
        Vec3 vec3 = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = vec3.horizontalDistance();
            this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

            Vec3 vec32 = this.position();
            Vec3 vec33 = vec32.add(vec3);
            HitResult hitresult = this.level.clip(new ClipContext(vec32, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if (hitresult.getType() != HitResult.Type.MISS) {
                vec33 = hitresult.getLocation();
            }


            vec3 = this.getDeltaMovement();
            double d5 = vec3.x;
            double d6 = vec3.y;
            double d1 = vec3.z;
            double d7 = this.getX() + d5;
            double d2 = this.getY() + d6;
            double d3 = this.getZ() + d1;
            double d4 = vec3.horizontalDistance();

                this.setYRot((float)(Mth.atan2(d5, d1) * (double)(180F / (float)Math.PI)));


            this.setXRot((float)(Mth.atan2(d6, d4) * (double)(180F / (float)Math.PI)));
            this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
            this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
            float f = 0.99F;

            this.setDeltaMovement(vec3.scale((double)f));


            this.setPos(d7, d2, d3);
            this.checkInsideBlocks();

    }
    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        ProjectileType projectileType = ProjectileType.DEFAULT;
        Entity target = hitResult.getEntity();
        Entity owner = this.getOwner();


        if(target.getUUID().equals(ownerr.getUUID()))
            return;
        if (!target.isAlive())
            return;
        explode();
        target.hurt(DamageSource.GENERIC,20);



    }
    protected void explode() {

        if (!this.level.isClientSide) {
            /*	this.level.broadcastEntityEvent(this, (byte) 3);

             */

            for (int i = 0; i < 5; i++) {
                float x = Create.RANDOM.nextFloat(360);
                float y = Create.RANDOM.nextFloat(360);
                float z = Create.RANDOM.nextFloat(360);
                SoulSparkEntity spark = TMEntities.SOUL_SPARK.create(level);
                spark.moveTo(this.getX(), this.getY(), this.getZ());
                spark.shootFromRotation(this, x, y, z, 0.2f, 1);
                this.level.addFreshEntity(spark);
            }
            //	this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 2.0F, Explosion.BlockInteraction.NONE);

        }
    }
    protected void onHitBlock(BlockHitResult p_36755_) {
        super.onHitBlock(p_36755_);
     //   this.discard();
    }


    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.ARROW_HIT;
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
        return ProjectileUtil.getEntityHitResult(this.level, this, pStartVec, pEndVec, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }



    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);




        pCompound.putString("SoundEvent", Registry.SOUND_EVENT.getKey(this.soundEvent).toString());
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);


        if (pCompound.contains("SoundEvent", 8)) {
            this.soundEvent = Registry.SOUND_EVENT.getOptional(new ResourceLocation(pCompound.getString("SoundEvent"))).orElse(this.getDefaultHitGroundSoundEvent());
        }


    }
    @Override
    public EntityDimensions getDimensions(Pose pPose) {
return new EntityDimensions(hitboxSize,0.5f,false);
    }
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    public boolean isAttackable() {
        return false;
    }

    //protected float getEyeHeight(Pose pPose, EntityDimensions pSize) {
      //  return 0.13F;
    //}


    @SuppressWarnings("unchecked")
    public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
        EntityType.Builder<ShockWaveEntity> entityBuilder = (EntityType.Builder<ShockWaveEntity>) builder;
        return entityBuilder.sized(10f, .25f);
    }


}

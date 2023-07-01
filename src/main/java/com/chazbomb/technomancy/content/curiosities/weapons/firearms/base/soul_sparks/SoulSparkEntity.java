package com.chazbomb.technomancy.content.curiosities.weapons.firearms.base.soul_sparks;



import com.simibubi.create.content.equipment.potatoCannon.PotatoProjectileEntity;
import com.simibubi.create.content.trains.CubeParticleData;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class SoulSparkEntity extends ThrowableProjectile {
    public SoulSparkEntity(EntityType<? extends SoulSparkEntity> p_37391_, Level p_37392_) {
        super(p_37391_, p_37392_);

    }


    @Override
    protected float getGravity(){
        return 0.02f;
    }
    @Override
    protected void defineSynchedData() {

    }

    public void tick(){
        super.tick();
        if (this.isInWaterOrRain()) {
            this.discard();
        }
        if(this.level.isClientSide) {

            this.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, -this.getDeltaMovement().y * 0.5D, this.random.nextGaussian() * 0.05D);
            CubeParticleData data =
                    new CubeParticleData(250, 50, 50, 0.1f, 10, true);
            level.addParticle(data, getX(), getY(), getZ(), 0.0D,
                    0.04D, 0.0D);
        }
    }



    private ParticleOptions getParticle() {

        return ParticleTypes.SOUL_FIRE_FLAME;
    }

    public void handleEntityEvent(byte p_37402_) {
        if (p_37402_ == 3) {
            ParticleOptions particleoptions = this.getParticle();

            for(int i = 0; i < 8; ++i) {
                this.level.addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }
    /*
    protected void onHitBlock(BlockHitResult p_37384_) {
        super.onHitBlock(p_37384_);
        if (!this.level.isClientSide) {
            Entity entity = this.getOwner();
            if (!(entity instanceof Mob) || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                BlockPos blockpos = p_37384_.getBlockPos().relative(p_37384_.getDirection());
                if (this.level.isEmptyBlock(blockpos)) {
                    this.level.setBlockAndUpdate(blockpos, SoulFireBlock.getState(this.level, blockpos));
                }
            }

        }
    }

     */
    protected void onHitEntity(EntityHitResult p_37404_) {
        super.onHitEntity(p_37404_);
        Entity entity = p_37404_.getEntity();
        entity.hurt(DamageSource.thrown(this, this.getOwner()), 1);
        if (this.level.isClientSide) {
            this.level.playLocalSound(getX(), getY(), getZ(), SoundEvents.SOUL_ESCAPE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F, false);
        }
    }

    protected void onHitBlock(BlockHitResult p_37406_) {
        super.onHitBlock(p_37406_);

        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)3);


            //this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 2.0F, Explosion.BlockInteraction.NONE);
            this.discard();
        }

    }

    @SuppressWarnings("unchecked")
    public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
        EntityType.Builder<PotatoProjectileEntity> entityBuilder = (EntityType.Builder<PotatoProjectileEntity>) builder;
        return entityBuilder.sized(.25f, .25f);
    }
}
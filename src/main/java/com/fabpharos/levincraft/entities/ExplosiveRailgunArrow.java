package com.fabpharos.levincraft.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import static com.fabpharos.levincraft.setup.Registration.EXPLOSIVERAILGUNAMMO_ITEM;

public class ExplosiveRailgunArrow extends RailgunArrow {

    private static final EntityDataAccessor<Integer> DATA_FUSE_ID = SynchedEntityData.defineId(ExplosiveRailgunArrow.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_LIT_ID = SynchedEntityData.defineId(ExplosiveRailgunArrow.class, EntityDataSerializers.BOOLEAN);
    public ExplosiveRailgunArrow(Level p_36719_, LivingEntity p_36718_) {
        super(p_36719_, p_36718_);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(EXPLOSIVERAILGUNAMMO_ITEM.get());
    }

    @Override
    public byte getPierceLevel() {
        return (byte) 0;
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(DATA_FUSE_ID, 60);
        entityData.define(IS_LIT_ID, false);
        super.defineSynchedData();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putShort("Fuse", (short)this.getFuse());
        tag.putBoolean("Lit", this.getIsLit());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setFuse(tag.getShort("Fuse"));
        this.setIsLitId(tag.getBoolean("Lit"));
    }

    @Override
    protected boolean tryPickup(Player p_150121_) {
        return !getIsLit() && super.tryPickup(p_150121_);
    }

    @Override
    protected void onHitBlock(BlockHitResult p_36755_) {
        super.onHitBlock(p_36755_);
        setIsLitId(Boolean.TRUE);
    }

    @Override
    protected void onHitEntity(EntityHitResult p_36757_) {
        super.onHitEntity(p_36757_);
        explode(1.0F);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.isOnFire()) {
            explode(4.0F);
        }
        if(getIsLit()) {
            BlockPos blockpos = this.blockPosition();
            BlockState blockstate = this.level.getBlockState(blockpos);
            if(this.isInWaterOrRain() || blockstate.is(Blocks.POWDER_SNOW)) {
                setIsLitId(Boolean.FALSE);
            } else {
                int timer = this.getFuse() - 1;
                if(timer <= 0) {
                    explode(4.0F);
                } else {
                    this.setFuse(timer);
                    if (this.level.isClientSide) {
                        this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
                    }
                }
            }
        }
    }

    public void explode(float strength) {
        if(level.isClientSide) return;
        this.level.explode(this, this.getX(), this.getY(), this.getZ(), strength, Explosion.BlockInteraction.BREAK);
        this.discard();
    }

    public void setFuse(int p_32086_) {
        this.entityData.set(DATA_FUSE_ID, p_32086_);
    }

    public int getFuse() {
        return this.entityData.get(DATA_FUSE_ID);
    }

    public void setIsLitId(Boolean p_32086_) {
        this.entityData.set(IS_LIT_ID, p_32086_);
    }

    public Boolean getIsLit() {
        return this.entityData.get(IS_LIT_ID);
    }
}

package com.fabpharos.levincraft.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class ThunderheadEntity extends FlyingMob implements IAnimatable, IAnimationTickable, Enemy {
    BlockPos anchorPoint = BlockPos.ZERO;
    private static final float SPEED = 0.1f;
    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING = SynchedEntityData.defineId(ThunderheadEntity.class, EntityDataSerializers.BOOLEAN);
    private AnimationFactory factory = new AnimationFactory(this);

    public ThunderheadEntity(EntityType<? extends FlyingMob> entityType, Level levelIn) {
        super(entityType, levelIn);
        this.xpReward = 5;
        this.noCulling = true;
        this.moveControl = new ThunderheadEntity.ThunderheadMoveControl(this);
        //this.lookControl = new ThunderheadEntity.ThunderheadLookControl(this);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new ThunderheadEntity.CircleAroundAnchorGoal());
        this.goalSelector.addGoal(7, new ThunderheadEntity.ThunderheadShootGoal(this));
        this.goalSelector.addGoal(7, new ThunderheadLookGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (entity) -> Math.abs(entity.getY() - this.getY()) <= 10.0D));
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.anchorPoint = this.blockPosition().above(5);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    public boolean isCharging() {
        return this.entityData.get(DATA_IS_CHARGING);
    }

    public void setCharging(boolean pAttacking) {
        this.entityData.set(DATA_IS_CHARGING, pAttacking);
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("AX")) {
            this.anchorPoint = new BlockPos(pCompound.getInt("AX"), pCompound.getInt("AY"), pCompound.getInt("AZ"));
        }

    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("AX", this.anchorPoint.getX());
        pCompound.putInt("AY", this.anchorPoint.getY());
        pCompound.putInt("AZ", this.anchorPoint.getZ());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_CHARGING, false);
    }

    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return true;
    }

    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    //TODO: replace with custom sounds from somewhere
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    protected float getSoundVolume() {
        return 1.0F;
    }

    public boolean canAttackType(EntityType<?> pType) {
        return true;
    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return 0.5F;
    }

    //Geckolib animation stuff

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        //event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.thunderhead.jawopen", false));
        //event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.thunderhead.jawclose", false));
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.thunderhead.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public int tickTimer() {
        return tickCount;
    }

    //AI stuff

    public class CircleAroundAnchorGoal extends Goal {
        private float angle;
        private float distance;
        private float height;
        private float clockwise;

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return ThunderheadEntity.this.getTarget() != null;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.distance = 5.0F + ThunderheadEntity.this.random.nextFloat() * 10.0F;
            this.height = -4.0F + ThunderheadEntity.this.random.nextFloat() * 9.0F;
            this.clockwise = ThunderheadEntity.this.random.nextBoolean() ? 1.0F : -1.0F;
            this.selectNext();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (ThunderheadEntity.this.random.nextInt(this.adjustedTickDelay(350)) == 0) {
                this.height = -4.0F + ThunderheadEntity.this.random.nextFloat() * 9.0F;
            }

            if (ThunderheadEntity.this.random.nextInt(this.adjustedTickDelay(250)) == 0) {
                ++this.distance;
                if (this.distance > 15.0F) {
                    this.distance = 5.0F;
                    this.clockwise = -this.clockwise;
                }
            }

            if (ThunderheadEntity.this.random.nextInt(this.adjustedTickDelay(450)) == 0) {
                this.angle = ThunderheadEntity.this.random.nextFloat() * 2.0F * (float)Math.PI;
                this.selectNext();
            }

            if (ThunderheadEntity.this.moveControl.getWantedY() < ThunderheadEntity.this.getY() && !ThunderheadEntity.this.level.isEmptyBlock(ThunderheadEntity.this.blockPosition().below(1))) {
                this.height = Math.max(1.0F, this.height);
                this.selectNext();
            }

            if (ThunderheadEntity.this.moveControl.getWantedX() > ThunderheadEntity.this.getY() && !ThunderheadEntity.this.level.isEmptyBlock(ThunderheadEntity.this.blockPosition().above(1))) {
                this.height = Math.min(-1.0F, this.height);
                this.selectNext();
            }

        }

        private void selectNext() {
            if (BlockPos.ZERO.equals(ThunderheadEntity.this.anchorPoint)) {
                ThunderheadEntity.this.anchorPoint = ThunderheadEntity.this.blockPosition();
            }

            this.angle += this.clockwise * 15.0F * ((float)Math.PI / 180F);
            if(getTarget() != null)
                ThunderheadEntity.this.moveControl.setWantedPosition(
                        getTarget().getX() + this.distance * Mth.cos(this.angle),
                        getTarget().getY() + 4.0F + this.height,
                        getTarget().getZ() + this.distance * Mth.sin(this.angle), 0.1f);
                //ThunderheadEntity.this.moveTargetPoint = getTarget().position().add(this.distance * Mth.cos(this.angle), 4.0F + this.height, this.distance * Mth.sin(this.angle));

            else
                ThunderheadEntity.this.moveControl.setWantedPosition(
                        ThunderheadEntity.this.anchorPoint.getX() + this.distance * Mth.cos(this.angle),
                        ThunderheadEntity.this.anchorPoint.getY() + 4.0F + this.height,
                        ThunderheadEntity.this.anchorPoint.getZ() + this.distance * Mth.sin(this.angle), 0.1f);
                //ThunderheadEntity.this.moveTargetPoint = Vec3.atLowerCornerOf(ThunderheadEntity.this.anchorPoint).add(this.distance * Mth.cos(this.angle), -4.0F + this.height, this.distance * Mth.sin(this.angle));

        }
    }

    static class ThunderheadMoveControl extends MoveControl {
        private final ThunderheadEntity thunderhead;
        private int floatDuration;

        public ThunderheadMoveControl(ThunderheadEntity p_32768_) {
            super(p_32768_);
            this.thunderhead = p_32768_;
        }

        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                if (this.floatDuration-- <= 0) {
                    this.floatDuration += this.thunderhead.getRandom().nextInt(5) + 2;
                    Vec3 vec3 = new Vec3(this.wantedX - this.thunderhead.getX(), this.wantedY - this.thunderhead.getY(), this.wantedZ - this.thunderhead.getZ());
                    double d0 = vec3.length();
                    vec3 = vec3.normalize();
                    if (this.canReach(vec3, Mth.ceil(d0))) {
                        this.thunderhead.setDeltaMovement(this.thunderhead.getDeltaMovement().add(vec3.scale(0.1D)));
                    } else {
                        this.operation = MoveControl.Operation.WAIT;
                    }
                }

            }
        }

        private boolean canReach(Vec3 p_32771_, int p_32772_) {
            AABB aabb = this.thunderhead.getBoundingBox();

            for(int i = 1; i < p_32772_; ++i) {
                aabb = aabb.move(p_32771_);
                if (!this.thunderhead.level.noCollision(this.thunderhead, aabb)) {
                    return false;
                }
            }

            return true;
        }
    }

    static class ThunderheadShootGoal extends Goal {
        private final ThunderheadEntity thunderhead;
        public int chargeTime;

        public ThunderheadShootGoal(ThunderheadEntity p_32776_) {
            this.thunderhead = p_32776_;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return this.thunderhead.getTarget() != null;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.chargeTime = 0;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            this.thunderhead.setCharging(false);
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity livingentity = this.thunderhead.getTarget();
            if (livingentity != null) {
                double d0 = 64.0D;
                if (livingentity.distanceToSqr(this.thunderhead) < 4096.0D && this.thunderhead.hasLineOfSight(livingentity)) {
                    Level level = this.thunderhead.level;
                    //++this.chargeTime;
                    if (this.chargeTime == 10 && !this.thunderhead.isSilent()) {
                        level.levelEvent((Player)null, 1015, this.thunderhead.blockPosition(), 0);
                    }
                    if (this.chargeTime == 20) {
                        if (!this.thunderhead.isSilent()) {
                            level.levelEvent((Player)null, 1016, this.thunderhead.blockPosition(), 0);
                        }
                        double d2 = livingentity.getX() - (this.thunderhead.getX());
                        double d3 = livingentity.getY(0.5D) - (0.5D + this.thunderhead.getY(0.5D));
                        double d4 = livingentity.getZ() - (this.thunderhead.getZ());
                        LargeFireball largefireball = new LargeFireball(level, this.thunderhead, d2, d3, d4, 1);
                        largefireball.setPos(this.thunderhead.getX(), this.thunderhead.getY(0.5D) + 0.5D, largefireball.getZ());
                        level.addFreshEntity(largefireball);
                        this.chargeTime = -40;
                    }
                } else if (this.chargeTime > 0) {
                    --this.chargeTime;
                }

                this.thunderhead.setCharging(this.chargeTime > 10);
            }
        }
    }

    static class ThunderheadLookGoal extends Goal {
        private final ThunderheadEntity thunderhead;

        public ThunderheadLookGoal(ThunderheadEntity p_32762_) {
            this.thunderhead = p_32762_;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return true;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (this.thunderhead.getTarget() == null) {
                Vec3 vec3 = this.thunderhead.getDeltaMovement();
                this.thunderhead.setYRot(-((float)Mth.atan2(vec3.x, vec3.z)) * (180F / (float)Math.PI));
                this.thunderhead.yBodyRot = this.thunderhead.getYRot();
            } else {
                LivingEntity livingentity = this.thunderhead.getTarget();
                double d0 = 64.0D;
                if (livingentity.distanceToSqr(this.thunderhead) < 4096.0D) {
                    double d1 = livingentity.getX() - this.thunderhead.getX();
                    double d2 = livingentity.getZ() - this.thunderhead.getZ();
                    this.thunderhead.setYRot(-((float)Mth.atan2(d1, d2)) * (180F / (float)Math.PI));
                    this.thunderhead.yBodyRot = this.thunderhead.getYRot();
                }
            }

        }
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, SPEED)
                .add(Attributes.FOLLOW_RANGE, 50.0);
    }
}

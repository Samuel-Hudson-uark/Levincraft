package com.fabpharos.levincraft.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class ThunderheadEntity extends FlyingMob implements IAnimatable, IAnimationTickable {
    Vec3 moveTargetPoint = Vec3.ZERO;
    BlockPos anchorPoint = BlockPos.ZERO;
    private AnimationFactory factory = new AnimationFactory(this);

    public ThunderheadEntity(EntityType<? extends FlyingMob> entityType, Level levelIn) {
        super(entityType, levelIn);
        this.xpReward = 5;
        this.noCulling = true;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 50.0f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new ThunderheadEntity.CircleAroundAnchorGoal());
        this.goalSelector.addGoal(1, new ThunderheadEntity.AttackPlayerTargetGoal());
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.anchorPoint = this.blockPosition().above(5);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    protected boolean shouldDespawnInPeaceful() {
        return true;
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

    //Geckolib animation stuff

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.thunderhead.jawopen", false));
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.thunderhead.jawclose", false));
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

    public class CircleAroundAnchorGoal extends ThunderheadEntity.MoveTargetGoal {
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

            if (this.touchingTarget()) {
                this.selectNext();
            }

            if (ThunderheadEntity.this.moveTargetPoint.y < ThunderheadEntity.this.getY() && !ThunderheadEntity.this.level.isEmptyBlock(ThunderheadEntity.this.blockPosition().below(1))) {
                this.height = Math.max(1.0F, this.height);
                this.selectNext();
            }

            if (ThunderheadEntity.this.moveTargetPoint.y > ThunderheadEntity.this.getY() && !ThunderheadEntity.this.level.isEmptyBlock(ThunderheadEntity.this.blockPosition().above(1))) {
                this.height = Math.min(-1.0F, this.height);
                this.selectNext();
            }

        }

        private void selectNext() {
            if (BlockPos.ZERO.equals(ThunderheadEntity.this.anchorPoint)) {
                ThunderheadEntity.this.anchorPoint = ThunderheadEntity.this.blockPosition();
            }

            this.angle += this.clockwise * 15.0F * ((float)Math.PI / 180F);
            ThunderheadEntity.this.moveTargetPoint = Vec3.atLowerCornerOf(ThunderheadEntity.this.anchorPoint).add((double)(this.distance * Mth.cos(this.angle)), (double)(-4.0F + this.height), (double)(this.distance * Mth.sin(this.angle)));
        }
    }

    class AttackPlayerTargetGoal extends Goal {
        private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0D);
        private int nextScanTick = reducedTickDelay(20);

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (this.nextScanTick > 0) {
                --this.nextScanTick;
            } else {
                this.nextScanTick = reducedTickDelay(60);
                List<Player> list = ThunderheadEntity.this.level.getNearbyPlayers(this.attackTargeting, ThunderheadEntity.this, ThunderheadEntity.this.getBoundingBox().inflate(16.0D, 64.0D, 16.0D));
                if (!list.isEmpty()) {
                    list.sort(Comparator.<Entity, Double>comparing(Entity::getY).reversed());

                    for(Player player : list) {
                        if (ThunderheadEntity.this.canAttack(player, TargetingConditions.DEFAULT)) {
                            ThunderheadEntity.this.setTarget(player);
                            setAnchorAboveTarget();
                            return true;
                        }
                    }
                }

            }
            return false;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            LivingEntity livingentity = ThunderheadEntity.this.getTarget();
            return livingentity != null && ThunderheadEntity.this.canAttack(livingentity, TargetingConditions.DEFAULT);
        }

        @Override
        public void tick() {
            super.tick();
        }

        private void setAnchorAboveTarget() {
            ThunderheadEntity.this.anchorPoint = ThunderheadEntity.this.getTarget().blockPosition().above(20 + ThunderheadEntity.this.random.nextInt(20));
            if (ThunderheadEntity.this.anchorPoint.getY() < ThunderheadEntity.this.level.getSeaLevel()) {
                ThunderheadEntity.this.anchorPoint = new BlockPos(ThunderheadEntity.this.anchorPoint.getX(), ThunderheadEntity.this.level.getSeaLevel() + 1, ThunderheadEntity.this.anchorPoint.getZ());
            }

        }
    }

    abstract class MoveTargetGoal extends Goal {
        public MoveTargetGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        protected boolean touchingTarget() {
            return ThunderheadEntity.this.moveTargetPoint.distanceToSqr(ThunderheadEntity.this.getX(), ThunderheadEntity.this.getY(), ThunderheadEntity.this.getZ()) < 4.0D;
        }
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.FOLLOW_RANGE, 50.0);
    }
}

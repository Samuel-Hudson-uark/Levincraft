package com.fabpharos.levincraft.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import static com.fabpharos.levincraft.setup.Registration.RAILGUNAMMO_ITEM;

public class RailgunArrow extends AbstractArrow {

    public RailgunArrow(Level p_36719_, LivingEntity p_36718_) {
        super(EntityType.ARROW, p_36718_, p_36719_);
        this.setBaseDamage(4.0f);
        this.setPierceLevel((byte)3);
        this.setNoGravity(true);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(RAILGUNAMMO_ITEM.get());
    }

    @Override
    protected void onHitBlock(BlockHitResult p_36755_) {
        super.onHitBlock(p_36755_);
        setNoGravity(false);
    }
}

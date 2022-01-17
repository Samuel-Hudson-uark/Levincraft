package com.fabpharos.levincraft.items;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class ChargableItem extends Item {
    private final Supplier<Integer> chargeRateSupplier;
    private final Supplier<Integer> maxEnergySupplier;

    public ChargableItem(Supplier<Integer> maxEnergySupplier, Supplier<Integer> chargeRateSupplier, Properties p_41383_) {
        super(p_41383_.stacksTo(1));
        this.chargeRateSupplier = chargeRateSupplier;
        this.maxEnergySupplier = maxEnergySupplier;
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return pStack.getOrCreateTag().getInt("charge") != 0;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        return (int) Math.round(13.0F - getEnergyRatio(pStack) * 13.0F);
    }

    double getEnergyRatio(ItemStack stack) {
        int charge = stack.getOrCreateTag().getInt("charge");
        return (double) charge / (double) getMaxEnergy();
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        return 9830550;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack,level,tooltip,flag);
        int charge = stack.getOrCreateTag().getInt("charge");
        tooltip.add(new TranslatableComponent("message.chargableitem", charge, getMaxEnergy()));
    }

    public int getMaxEnergy() {
        return maxEnergySupplier.get();
    }

    public int getChargeRate() {
        return chargeRateSupplier.get();
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return oldStack.getItem() != newStack.getItem();
    }
}

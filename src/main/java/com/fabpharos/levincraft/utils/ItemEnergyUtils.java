package com.fabpharos.levincraft.utils;

import com.fabpharos.levincraft.items.ChargableItem;
import net.minecraft.world.item.ItemStack;

public class ItemEnergyUtils {
    public static int sendPower(ItemStack stack, int powerRequested) {
        if(stack.getItem() instanceof ChargableItem) {
            int charge = stack.getOrCreateTag().getInt("charge");
            boolean enoughCharge = charge > powerRequested;
            stack.getTag().putInt("charge", enoughCharge ? charge-powerRequested : 0);
            return enoughCharge ? powerRequested : charge;
        }
        return 0;
    }

    public static int receivePower(ItemStack stack, int powerIn) {
        if(stack.getItem() instanceof ChargableItem) {
            int chargeRate = ((ChargableItem) stack.getItem()).getChargeRate();
            int maxEnergy = ((ChargableItem) stack.getItem()).getMaxEnergy();
            int charge = stack.getOrCreateTag().getInt("charge");
            powerIn = Math.min(powerIn, chargeRate);
            boolean enoughSpace = (charge + powerIn) <= maxEnergy;
            stack.getTag().putInt("charge", enoughSpace ? charge + powerIn : maxEnergy);
            return enoughSpace ? 0 : charge + powerIn - maxEnergy;
        }
        return powerIn;
    }

    public static boolean isFull(ItemStack stack) {
        if(stack.getItem() instanceof ChargableItem) {
            int maxEnergy = ((ChargableItem) stack.getItem()).getMaxEnergy();
            int charge = stack.getOrCreateTag().getInt("charge");
            return charge >= maxEnergy;
        }
        return true;
    }
}

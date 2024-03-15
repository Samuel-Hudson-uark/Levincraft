package com.fabpharos.levincraft.items;

import com.fabpharos.levincraft.setup.Config;
import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.world.item.ItemStack;

public class LightningGunItem extends GunItem {
    public LightningGunItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        if (stack.isEnchanted()) {
            return Config.Client.enableGunEnchantmentGlint.get();
        }
        else {
            return false;
        }
    }
}

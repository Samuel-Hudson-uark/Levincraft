package com.fabpharos.levincraft.items;

import com.fabpharos.levincraft.setup.ModSetup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CrystalTuner extends Item {
    public CrystalTuner() {
        super(new Properties().stacksTo(1).tab(ModSetup.ITEM_GROUP));
    }

    public BlockPos getBlockPos(ItemStack stack) {
        int x = stack.hasTag() ? stack.getTag().getInt("saved_x") : 0;
        int y = stack.hasTag() ? stack.getTag().getInt("saved_y") : 0;
        int z = stack.hasTag() ? stack.getTag().getInt("saved_z") : 0;
        return new BlockPos(x,y,z);
    }

    public void setTag(ItemStack stack, BlockPos pos) {
        stack.getOrCreateTag().putInt("saved_x", pos.get(Direction.Axis.X));
        stack.getOrCreateTag().putInt("saved_y", pos.get(Direction.Axis.Y));
        stack.getOrCreateTag().putInt("saved_z", pos.get(Direction.Axis.Z));
    }
}

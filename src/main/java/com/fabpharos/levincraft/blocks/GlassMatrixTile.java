package com.fabpharos.levincraft.blocks;

import com.fabpharos.levincraft.items.ChargableItem;
import com.fabpharos.levincraft.utils.ItemEnergyUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.fabpharos.levincraft.setup.Registration.GLASSMATRIXBLOCK_TILE;


public class GlassMatrixTile extends EnergyStoringTile {

    private final int inventorySize = 2;

    private final ItemStackHandler itemHandler = createHandler();

    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public GlassMatrixTile(BlockPos pos, BlockState state) {
        super(GLASSMATRIXBLOCK_TILE.get(), pos, state);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        handler.invalidate();
    }

    public void tickServer(BlockState state) {
        //Send energy to item in inventory
        ItemStack stack = itemHandler.getStackInSlot(0);
        if(stack.getItem() instanceof ChargableItem) {
            int energyToSend = 200;
            getEnergyStorage().consumeEnergy(energyToSend - ItemEnergyUtils.receivePower(stack, energyToSend));
            if(ItemEnergyUtils.isFull(stack) && itemHandler.getStackInSlot(1).isEmpty()) {
                itemHandler.insertItem(1,itemHandler.extractItem(0,stack.getCount(),false),false);
            }
            setChanged();
        }
    }

    @Override
    public void load(CompoundTag tag) {
        itemHandler.deserializeNBT(tag.getCompound("inv"));
        super.load(tag);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.put("inv", itemHandler.serializeNBT());
        return super.save(tag);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(inventorySize) {

            @Override
            protected void onContentsChanged(int slot) {
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

                return stack.getItem() instanceof ChargableItem;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (!(stack.getItem() instanceof ChargableItem) || slot != 0) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return getEnergy().cast();
        }
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }
    @Override
    public int getMaxEnergy() {
        return 100000;
    }
    @Override
    public int getMaxTransfer() {
        return 10000;
    }

}

package com.fabpharos.levincraft.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

import static com.fabpharos.levincraft.Registration.PYLON_BLOCK_ENTITY;

public class PylonBlockEntity extends BlockEntity {

    public static final String ITEMS_TAG = "Inventory";
    public static final String ATTUNEMENT_SLOTS_TAG = "Connected Blocks";

    public static int SLOT_COUNT = 1;
    public static int SLOT = 0;
    public static int MAX_ATTUNED_PYLONS = 4;

    private final ItemStackHandler items = createItemHandler();
    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> items);

    //TODO: Do not allow an already existing position to enter list.
    public final ArrayList<BlockPos> registeredPylons = new ArrayList<>();

    public boolean addToPylonList(BlockPos pos) {
        boolean result = registeredPylons.add(pos);
        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        return result;
    }

    public boolean CanAddToPylonList(BlockPos pos) {
        for(BlockPos blockPos : registeredPylons) {
            if(blockPos.toString().equals(pos.toString())) {
                return false;
            }
        }
        return true;
    }

    public PylonBlockEntity(BlockPos pos, BlockState state) {
        super(PYLON_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
    }

    @Nonnull
    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(SLOT_COUNT) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        };
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemHandler.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        saveClientData(tag);
    }

    private void saveClientData(CompoundTag tag) {
        tag.put(ITEMS_TAG, items.serializeNBT());
        ListTag tags = new ListTag();
        for(BlockPos blockPos : registeredPylons) {
            tags.add(NbtUtils.writeBlockPos(blockPos));
            System.out.println(blockPos);
        }
        System.out.println("Saving data tag!");
        tag.put(ATTUNEMENT_SLOTS_TAG, tags);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        loadClientData(tag);
    }

    private void loadClientData(CompoundTag tag) {
        if (tag.contains(ITEMS_TAG)) {
            items.deserializeNBT(tag.getCompound(ITEMS_TAG));
        }
        if(tag.contains(ATTUNEMENT_SLOTS_TAG)) {
            System.out.println("Handling data tag!");
            ListTag slots = (ListTag) tag.get(ATTUNEMENT_SLOTS_TAG);
            for(int i = 0; i < slots.size(); i++) {
                registeredPylons.add(NbtUtils.readBlockPos(slots.getCompound(i)));
                System.out.println(NbtUtils.readBlockPos(slots.getCompound(i)));
            }
        }
    }

    // The getUpdateTag()/handleUpdateTag() pair is called whenever the client receives a new chunk
    // it hasn't seen before. i.e. the chunk is loaded
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveClientData(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        if (tag != null) {
            loadClientData(tag);
        }
    }

    // The getUpdatePacket()/onDataPacket() pair is used when a block update happens on the client
    // (a blockstate change or an explicit notificiation of a block update from the server). It's
    // easiest to implement them based on getUpdateTag()/handleUpdateTag()
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        // This is called client side
        CompoundTag tag = pkt.getTag();
        // This will call loadClientData()
        if (tag != null) {
            handleUpdateTag(tag);
        }
    }

    public void tickServer() {
    }
}
package com.fabpharos.levincraft.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
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
import java.util.Random;

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
    private final ArrayList<LightningBeamDataHolder> BeamData = new ArrayList<>();
    private final ArrayList<Vec3> beamCache = new ArrayList<>();

    public boolean addToPylonList(BlockPos pos) {
        boolean result = registeredPylons.add(pos);
        //This data is only saved on the server and needs to be sent to the client.
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

    public ArrayList<Vec3> getBeamCache() {
        return beamCache;
    }

    public void tick() {
        assert level != null;
        if(level.isClientSide) {
            clientTick();
        } else {
            serverTick();
        }
    }

    private void clientTick() {
        //Later replace the if statement with if power was sent between blocks this tick.
        if (!registeredPylons.isEmpty() && BeamData.isEmpty()) {
            BeamData.add(new LightningBeamDataHolder(registeredPylons.get(0)));
        }
        beamCache.clear();
        for(LightningBeamDataHolder holder : BeamData) {
            beamCache.addAll(holder.tick());
            //At this point delete holder if it is invalid.
        }
    }
    private void serverTick() {
        //Handle power transfer
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
            //System.out.println(blockPos);
        }
        //System.out.println("Saving data tag!");
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
            //System.out.println("Handling data tag!");
            ListTag slots = (ListTag) tag.get(ATTUNEMENT_SLOTS_TAG);
            for(int i = 0; i < slots.size(); i++) {
                registeredPylons.add(NbtUtils.readBlockPos(slots.getCompound(i)));
                //System.out.println(NbtUtils.readBlockPos(slots.getCompound(i)));
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

    public class LightningBeamDataHolder {
        private static final int LIGHTNING_BOLT_ANIMATION_DELAY = 4;
        private int lightningBoltAnimationTimer = 0;
        private static final int LIGHTNING_BOLT_ANIMATION_REPEATS = 4;
        private int lightningBoltAnimationCount = 0;
        private final BlockPos destination;
        private final Vec3 direction;
        private final ArrayList<Vec3> beamCache;

        //TODO: Will this block also transport items? If so, create function for % complete of animation to render an item in the midway point.
        //Animation takes LIGHTNING_BOLT_ANIMATION_DELAY*LIGHTNING_BOLT_ANIMATION_REPEATS ticks to complete
        
        private boolean valid = true;
        public LightningBeamDataHolder(BlockPos destination) {
            this.destination = destination;
            this.beamCache = new ArrayList<>();
            BlockPos diff = destination.subtract(getBlockPos());
            //don't normalize
            this.direction = new Vec3(diff.getX(), diff.getY(), diff.getZ()).normalize();
            generateLightningBeams();
        }

        public ArrayList<Vec3> tick() {
            lightningBoltAnimationTimer++;
            if(lightningBoltAnimationTimer >= LIGHTNING_BOLT_ANIMATION_DELAY) {
                lightningBoltAnimationTimer = 0;
                lightningBoltAnimationCount++;
                if(lightningBoltAnimationCount >= LIGHTNING_BOLT_ANIMATION_REPEATS) {
                    valid = false;
                    //Have the animation repeat only this many times, clear the beam cache after
                }
                generateLightningBeams();
            }
            return beamCache;
        }

        public void generateLightningBeams() {
            //Generate lightning beams
            Random random = new Random();
            beamCache.clear();
            Vec3 coreStart = new Vec3(0, 0, 0);
            //Keep the length the same
            //Vec3 distance = Vec3(destination.subtract(getBlockPos()));
            int coreLength = random.nextInt(3) + 7;
            for (int core = 0; core < coreLength; core++) {
                //Figure out how to face the beams the right way
                //Replace the first added vector with distance * float(core / core length)
                //Keep the second random vector
                //Multiply the third multiplying vector by distance.normalize?
                Vec3 coreEnd = coreStart.add(0, 0, 1).add(randomVector(.3f).multiply(2.5, 1, 2.5));
                beamCache.add(coreStart);
                beamCache.add(coreEnd);
                coreStart = coreEnd;

                int branchSegments = random.nextInt(3) + 1;
                beamCache.addAll(generateBranch(coreEnd, branchSegments, 0.5f, 1));
            }
            //beamCache.add(coreStart);
            //beamCache.add(distance);
        }

        public static List<Vec3> generateBranch(Vec3 origin, int maxLength, float splitChance, int recursionCount) {
            List<Vec3> branchSegements = new ArrayList<>();
            Random random = new Random();
            int branches = random.nextInt(maxLength + 1);
            Vec3 branchStart = origin;
            int dir = random.nextBoolean() ? 1 : -1;
            float branchLength = .75f / (recursionCount + 1);
            for (int i = 0; i < branches; i++) {
                Vec3 branchEnd = branchStart.add(dir * branchLength, 0, branchLength).add(randomVector(.3f));
                branchSegements.add(branchStart);
                branchSegements.add(branchEnd);
                if (random.nextFloat() <= splitChance)
                    branchSegements.addAll(generateBranch(branchEnd, maxLength - 1, splitChance * 1.2f, recursionCount + 1));
                branchStart = branchEnd;
            }
            return branchSegements;
        }

        public static Vec3 randomVector(float radius) {
            double x = Math.random() * 2 * radius - radius;
            double y = Math.random() * 2 * radius - radius;
            double z = Math.random() * 2 * radius - radius;
            return new Vec3(x, y, z);
        }

        public boolean isValid() {
            return valid;
        }
    }
}

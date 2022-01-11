package com.fabpharos.levincraft.blocks;

import com.fabpharos.levincraft.tools.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static com.fabpharos.levincraft.blocks.PylonBlock.MODE;
import static com.fabpharos.levincraft.setup.Registration.PYLONBLOCK_TILE;

public class PylonTile extends EnergyStoringTile {

    private int counter;
    private int[] array;

    private Mode mode = Mode.MODE_NONE;

    private final ArrayList<PylonTile> tiles = new ArrayList<>();

    public PylonTile(BlockPos pos, BlockState state) {
        super(PYLONBLOCK_TILE.get(), pos, state);
    }

    public Mode getMode() {
        return mode;
    }

    public void toggleMode() {
        switch (mode) {
            case MODE_NONE -> mode = Mode.MODE_INPUT;
            case MODE_INPUT -> mode = Mode.MODE_OUTPUT;
            case MODE_OUTPUT -> mode = Mode.MODE_NONE;
        }
        updateState();
    }

    private void updateState() {
        BlockState state = level.getBlockState(worldPosition);
        level.setBlockAndUpdate(worldPosition,state.setValue(MODE, mode));
        setChanged();
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(getBlockPos(), getBlockPos().offset(1, 2, 1));
    }

    public void addPylonToList(PylonTile tile) {
        if(!tiles.contains(tile)) {
            tiles.add(tile);
        }
    }

    public void updateTiles() {
        assert level != null;
        for(int i = 0; i < array.length; i += 3) {
            if(level.getBlockEntity(new BlockPos(array[i], array[i+1], array[i+2])) instanceof PylonTile tile)
                addPylonToList(tile);
        }
    }

    public void tickServer(BlockState state) {
        moveEnergyWithPylons();
        transferToConnectedEntity(state);
    }

    public void moveEnergyWithPylons() {
        if(this.getMode() != Mode.MODE_INPUT) {
            counter++;
            if(counter >= 20) {
                counter = 0;
                setChanged();
                if(array != null && tiles.isEmpty() && !level.isClientSide)
                    updateTiles();
                CustomEnergyStorage energyStorage = getEnergyStorage();
                AtomicInteger capacity = new AtomicInteger(energyStorage.getEnergyStored());
                tiles.sort(Comparator.comparingInt(o -> o.getMode().ordinal()));
                for(PylonTile tile : tiles) {
                    boolean doContinue = tile.getCapability(CapabilityEnergy.ENERGY).map(handler -> {
                        if(tile.getMode() != Mode.MODE_OUTPUT && handler.canReceive()) {
                            int received = handler.receiveEnergy(Math.min(capacity.get(), getMaxTransfer()), false);
                            capacity.addAndGet(-received);
                            energyStorage.consumeEnergy(received);
                            return capacity.get() > 0;
                        }
                        return true;
                    }).orElse(true);
                    if(!doContinue) {
                        tiles.remove(tile);
                        tiles.add(tile);
                        return;
                    }
                }
            }
        }
    }

    public void transferToConnectedEntity(BlockState state) {
        CustomEnergyStorage energyStorage = getEnergyStorage();
        AtomicInteger capacity = new AtomicInteger(energyStorage.getEnergyStored());
        if(mode != Mode.MODE_NONE) {
            Direction direction = state.getValue(BlockStateProperties.FACING);
            assert level != null;
            BlockEntity entity = level.getBlockEntity(worldPosition.relative(direction));
            if(entity != null) {
                boolean doContinue = entity.getCapability(CapabilityEnergy.ENERGY, direction).map(handler -> {
                    if(mode == Mode.MODE_INPUT && handler.canReceive()) {
                        int received = handler.receiveEnergy(Math.min(capacity.get(), getMaxTransfer()), false);
                        capacity.addAndGet(-received);
                        energyStorage.consumeEnergy(received);
                        setChanged();
                        return capacity.get() > 0;
                    } else if(mode == Mode.MODE_OUTPUT && handler.canExtract())  {
                        int extracted = handler.extractEnergy(Math.min(Math.min(getMaxTransfer(), handler.getEnergyStored()), getMaxEnergy()-energyStorage.getEnergyStored()), false);
                        capacity.addAndGet(extracted);
                        energyStorage.addEnergy(extracted);
                        setChanged();
                        return capacity.get() <= getMaxEnergy();
                    } else {return true;}
                }).orElse(true);
                if(!doContinue) {
                }
            }
        }
    }

    @Override
    public void load(CompoundTag tag) {
        if(tag.contains("counter"))
            counter = tag.getInt("counter");
        if(tag.contains("m0"))
            mode = Mode.values()[tag.getByte("m0")];
        if(tag.contains("blockentities")) {
            array = tag.getIntArray("blockentities");
        }
        super.load(tag);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("counter", counter);
        tag.putByte("m0",(byte)mode.ordinal());
        int[] positions = new int[tiles.size()*3];
        for(int i = 0; i<tiles.size(); i++) {
            BlockPos pos = tiles.get(i).getBlockPos();
            positions[i*3] = pos.getX();
            positions[(i*3)+1] = pos.getY();
            positions[(i*3)+2] = pos.getZ();
        }
        tag.putIntArray("blockentities", positions);
        return super.save(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return getEnergy().cast();
        }
        return super.getCapability(cap, side);
    }

    public int getMaxEnergy() {
        return 10000;
    }

    public int getMaxTransfer() {
        return 2000;
    }

    public enum Mode implements StringRepresentable {
        MODE_INPUT("input"),
        MODE_NONE("none"),
        MODE_OUTPUT("output");

        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }


        @Override
        public String toString() {
            return getSerializedName();
        }
    }

}

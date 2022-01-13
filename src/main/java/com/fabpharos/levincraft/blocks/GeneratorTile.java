package com.fabpharos.levincraft.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.fabpharos.levincraft.setup.Registration.GENERATORBLOCK_TILE;

public class GeneratorTile extends EnergyStoringTile{

    private int counter = 0;

    public GeneratorTile(BlockPos p_155229_, BlockState p_155230_) {
        super(GENERATORBLOCK_TILE.get(), p_155229_, p_155230_);
    }

    public void tickServer() {
        if(counter > 0) {
            counter--;
            getEnergyStorage().addEnergy(40);
            setChanged();
        }
    }

    @Override
    public void load(CompoundTag tag) {
        counter = tag.getInt("counter");
        super.load(tag);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("counter", counter);
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

    public void fillCounter() {
        counter = 2500;
        setChanged();
    }

    @Override
    public int getMaxEnergy() {
        return 100000;
    }

    @Override
    public int getMaxTransfer() {
        return 1000;
    }
}

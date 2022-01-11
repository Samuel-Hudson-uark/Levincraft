package com.fabpharos.levincraft.blocks;

import com.fabpharos.levincraft.tools.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyStoringTile extends BlockEntity {

    private final CustomEnergyStorage energyStorage = createEnergy();
    private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);

    public EnergyStoringTile(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        energy.invalidate();
    }

    @Override
    public void load(CompoundTag tag) {
        energyStorage.setEnergy(tag.getInt("energy"));
        super.load(tag);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("energy", energyStorage.getEnergyStored());
        return super.save(tag);
    }

    private CustomEnergyStorage createEnergy() {

        return new CustomEnergyStorage(getMaxEnergy(), getMaxTransfer()) {
            @Override
            protected void onEnergyChanged() {
                setChanged();
            }
        };
    }

    public int getMaxEnergy() {
        return 0;
    }

    public int getMaxTransfer() {
        return 0;
    }

    public LazyOptional<IEnergyStorage> getEnergy() {
        return energy;
    }

    public CustomEnergyStorage getEnergyStorage() {
        return energyStorage;
    }
}

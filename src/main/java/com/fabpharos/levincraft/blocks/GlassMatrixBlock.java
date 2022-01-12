package com.fabpharos.levincraft.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;

public class GlassMatrixBlock extends Block implements EntityBlock {

    public GlassMatrixBlock() {
        super(Properties
                .of(Material.GLASS)
                .sound(SoundType.GLASS)
                .strength(0.3f)
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

        return new GlassMatrixTile(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }
        return (level1, blockPos, blockState, t) -> {
            if (t instanceof GlassMatrixTile tile) {
                tile.tickServer(blockState);
            }
        };
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(player.getItemInHand(hand).isEmpty()) {
            BlockEntity entity = level.getBlockEntity(blockPos);
            if(entity instanceof GlassMatrixTile) {
                entity.getCapability(CapabilityEnergy.ENERGY).map(handler -> {
                    player.displayClientMessage(new TranslatableComponent("message.chargableitem", handler.getEnergyStored(), handler.getMaxEnergyStored()), true);
                    return InteractionResult.SUCCESS;
                });
            }
        }
        return super.use(blockState, level, blockPos, player, hand, hitResult);
    }
}

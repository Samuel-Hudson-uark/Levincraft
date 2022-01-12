package com.fabpharos.levincraft.blocks;

import com.fabpharos.levincraft.entities.GeneratorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.List;

import static com.fabpharos.levincraft.setup.Registration.GENERATOR_ENTITY;

public class GeneratorBlock extends Block implements EntityBlock {
    public GeneratorBlock() {
        super(Properties
                .of(Material.METAL)
                .sound(SoundType.METAL)
                .strength(2.0f)
                .requiresCorrectToolForDrops());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GeneratorTile(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }
        return (level1, blockPos, blockState, t) -> {
            if (t instanceof GeneratorTile tile) {
                tile.tickServer();
            }
        };
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(player.getItemInHand(hand).isEmpty()) {
            BlockEntity entity = level.getBlockEntity(blockPos);
            if(entity instanceof GeneratorTile) {
                entity.getCapability(CapabilityEnergy.ENERGY).map(handler -> {
                    player.displayClientMessage(new TranslatableComponent("message.chargableitem", handler.getEnergyStored(), handler.getMaxEnergyStored()), true);
                    return InteractionResult.SUCCESS;
                });
            }
        }
        return super.use(blockState, level, blockPos, player, hand, hitResult);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState state, boolean b) {
        super.onPlace(blockState, level, pos, state, b);
        if(!level.isClientSide) {
            GeneratorEntity entity = new GeneratorEntity(GENERATOR_ENTITY.get(), level);
            System.out.println(pos.getX());
            System.out.println(pos.getY());
            System.out.println(pos.getZ());
            entity.setPos(pos.getX(), pos.getY(), pos.getZ());
            level.addFreshEntity(entity);
        }
    }

    @Override
    public void destroy(LevelAccessor accessor, BlockPos pos, BlockState state) {
        if(!accessor.isClientSide()) {
            Vec3 vec3 = new Vec3(pos.getX(), pos.getY(), pos.getZ());
            List<GeneratorEntity> entities = accessor.getEntitiesOfClass(GeneratorEntity.class, new AABB(vec3, vec3).inflate(1));
            for(GeneratorEntity entity : entities) {
                if(entity != null && entity.distanceToSqr(vec3) == 0) {
                    entity.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }
        super.destroy(accessor, pos, state);
    }
}

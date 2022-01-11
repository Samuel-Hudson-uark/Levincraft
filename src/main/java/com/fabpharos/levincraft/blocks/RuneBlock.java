package com.fabpharos.levincraft.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RuneBlock extends Block {

    private static final VoxelShape SHAPE = Block.box(4, 4, 4, 12, 12, 12);

    public static final EnumProperty<PylonTile.Mode> MODE = EnumProperty.create("mode",PylonTile.Mode.class);

    public RuneBlock() {
        super(Properties
                .of(Material.AMETHYST)
                .sound(SoundType.AMETHYST)
                .strength(0.5f));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MODE);
    }

}

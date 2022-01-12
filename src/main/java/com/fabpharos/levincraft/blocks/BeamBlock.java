package com.fabpharos.levincraft.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

import static com.fabpharos.levincraft.setup.Registration.BEAMBLOCK_ITEM;
import static com.fabpharos.levincraft.setup.Registration.PYLONBLOCK_ITEM;

public class BeamBlock extends Block {

    private static final VoxelShape SHAPE = Block.box(6, 0, 6, 10, 16, 10);
    private static final VoxelShape BOTTOM_SHAPE = Shapes.join(Block.box(5, 0, 5, 11, 1, 11), Block.box(6, 1, 6, 10, 16, 10), BooleanOp.OR);


    public BeamBlock() {
        super(Properties
                .of(Material.STONE)
                .sound(SoundType.STONE)
                .strength(1.0f)
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        return state.getValue(BlockStateProperties.ATTACHED) ? SHAPE : BOTTOM_SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Block belowBlock = context.getLevel().getBlockState(context.getClickedPos().below()).getBlock();
        return defaultBlockState()
                //.setValue(BlockStateProperties.FACING, context.getClickedFace().getOpposite())
                .setValue(BlockStateProperties.ATTACHED, belowBlock instanceof BeamBlock || belowBlock instanceof AirBlock);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.ATTACHED, BlockStateProperties.FACING);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if(itemInHand.is(BEAMBLOCK_ITEM.get()) || itemInHand.is(PYLONBLOCK_ITEM.get())) {
            if(!level.isClientSide) {
                BlockPos placementPos = pos.above();
                BlockState blockToPlace = Block.byItem(itemInHand.getItem()).defaultBlockState().setValue(BlockStateProperties.FACING, Direction.DOWN);
                while(level.getBlockState(placementPos).getBlock() instanceof BeamBlock)
                    placementPos = placementPos.above();
                if(level.setBlockAndUpdate(placementPos, blockToPlace)) {
                    if(!player.isCreative())
                        itemInHand.shrink(1);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.use(state, level, pos, player, hand, result);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState blockState, LevelAccessor accessor, BlockPos pos, BlockPos blockPos) {
        return direction == Direction.DOWN && !this.canSurvive(state, accessor, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, blockState, accessor, pos, blockPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        return canSupportCenter(reader, pos.below(), Direction.UP);
    }

}

package com.fabpharos.levincraft.blocks;

import com.fabpharos.levincraft.items.CrystalTuner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

public class PylonBlock extends Block implements EntityBlock {

    private static final Hashtable<Direction, VoxelShape> SHAPES = new Hashtable<Direction, VoxelShape>();
    static {
        SHAPES.put(Direction.DOWN, Block.box(5, 1, 5, 11, 2, 11));
        SHAPES.put(Direction.UP, Block.box(5, 14, 5, 11, 16, 11));
        SHAPES.put(Direction.NORTH, Block.box(5, 5, 0, 11, 11, 2));
        SHAPES.put(Direction.SOUTH, Block.box(5, 5, 14, 11, 11, 16));
        SHAPES.put(Direction.EAST, Block.box(14, 5, 5, 16, 11, 11));
        SHAPES.put(Direction.WEST, Block.box(0, 5, 5, 2, 11, 11));

    }

    public static final EnumProperty<PylonTile.Mode> MODE = EnumProperty.create("mode",PylonTile.Mode.class);

    public PylonBlock() {
        super(Properties
                .of(Material.STONE)
                .sound(SoundType.STONE)
                .strength(1.0f)
        );
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(BlockStateProperties.FACING, context.getClickedFace().getOpposite());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }
        return (level1, blockPos, blockState, t) -> {
            if (t instanceof PylonTile tile) {
                tile.tickServer(blockState);
            }
        };
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(BlockStateProperties.FACING));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState blockState, LevelAccessor accessor, BlockPos pos, BlockPos blockPos) {
        return direction == state.getValue(BlockStateProperties.FACING) && !this.canSurvive(state, accessor, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, blockState, accessor, pos, blockPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        Direction direction = state.getValue(BlockStateProperties.FACING);
        return canSupportCenter(reader, pos.offset(direction.getNormal()), direction.getOpposite());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PylonTile(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MODE, BlockStateProperties.FACING);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!world.isClientSide && hand.equals(InteractionHand.MAIN_HAND)) {
            ItemStack stack = player.getItemInHand(hand);
            BlockEntity entity = world.getBlockEntity(pos);
            if(entity instanceof PylonTile pylonEntity) {
                if (stack.isEmpty()) {
                    pylonEntity.toggleMode();
                    player.displayClientMessage(new TextComponent(pylonEntity.getMode().getSerializedName()), true);
                    return InteractionResult.SUCCESS;
                } else if (stack.getItem() instanceof CrystalTuner tunerStack) {
                    BlockPos saved = tunerStack.getBlockPos(stack);
                    tunerStack.setTag(stack, pos);
                    if (!Objects.equals(saved, new BlockPos(0, 0, 0)) && !Objects.equals(saved, pos) && world.getBlockEntity(saved) instanceof PylonTile savedPylonEntity) {
                        savedPylonEntity.addPylonToList(pylonEntity);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return super.use(state, world, pos, player, hand, result);
    }
}

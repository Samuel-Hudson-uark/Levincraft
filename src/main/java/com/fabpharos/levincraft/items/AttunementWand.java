package com.fabpharos.levincraft.items;

import com.fabpharos.levincraft.blocks.PylonBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AttunementWand extends Item {
    public AttunementWand() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        BlockPos blockClicked = pContext.getClickedPos();
        Player player = pContext.getPlayer();
        BlockEntity be = pContext.getLevel().getBlockEntity(blockClicked);
        if(be instanceof PylonBlockEntity) {
            if(pContext.getItemInHand().hasTag()) {
                int[] targetBlockPos = pContext.getItemInHand().getTag().getIntArray("levincraft.target_block");
                BlockPos target = new BlockPos(targetBlockPos[0], targetBlockPos[1], targetBlockPos[2]);
                if(((PylonBlockEntity) be).CanAddToPylonList(target)) {
                    if(pContext.getLevel().isClientSide()) {
                        player.displayClientMessage(Component.translatable("item.levincraft.attunement_wand.load"), true);
                    } else {
                        ((PylonBlockEntity) be).addToPylonList(target);
                        pContext.getItemInHand().setTag(new CompoundTag());
                    }
                } else if(pContext.getLevel().isClientSide()) {
                    player.displayClientMessage(Component.translatable("item.levincraft.attunement_wand.fail_load"), true);
                }
            } else {
                int[] targetBlockPos = {blockClicked.getX(), blockClicked.getY(), blockClicked.getZ()};
                if(pContext.getLevel().isClientSide()) {
                    player.displayClientMessage(Component.translatable("item.levincraft.attunement_wand.save"), true);
                } else {
                    pContext.getItemInHand().getOrCreateTag().putIntArray("levincraft.target_block", targetBlockPos);
                }
            }
        }

        return super.useOn(pContext);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(pStack.hasTag()) {
            int[] targetBlockPos = pStack.getTag().getIntArray("levincraft.target_block");
            pTooltipComponents.add(Component.literal(String.format("Position: %d, %d, %d", targetBlockPos[0], targetBlockPos[1], targetBlockPos[2])));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}

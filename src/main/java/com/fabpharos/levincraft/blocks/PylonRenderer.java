package com.fabpharos.levincraft.blocks;

import com.fabpharos.levincraft.setup.Registration;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.EmptyModelData;

import static com.fabpharos.levincraft.blocks.PylonBlock.MODE;
import static com.fabpharos.levincraft.setup.Registration.RUNEBLOCK;

public class PylonRenderer implements BlockEntityRenderer<PylonTile> {

    private static final BlockState runeState = RUNEBLOCK.get().defaultBlockState();

    public PylonRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(PylonTile tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {

        long time = System.currentTimeMillis();
        int speed = 25;
        float angle = (time / speed) % 360;
        Quaternion rotation = Vector3f.YP.rotationDegrees(angle);

        matrixStack.pushPose();
        matrixStack.translate(.5,0,.5);
        matrixStack.mulPose(rotation);
        matrixStack.translate(0,.5,0);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(45));
        matrixStack.mulPose(Vector3f.ZN.rotationDegrees((float) 35.2644));
        matrixStack.translate(-.5,-.5,-.5);

        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        BlockState state = runeState.setValue(MODE, tile.getBlockState().getValue(MODE));
        blockRenderer.renderSingleBlock(state, matrixStack, buffer, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
        matrixStack.popPose();

    }

    public static void register() {
        BlockEntityRenderers.register(Registration.PYLONBLOCK_TILE.get(), PylonRenderer::new);
    }
}

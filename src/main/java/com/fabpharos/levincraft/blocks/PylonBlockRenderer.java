package com.fabpharos.levincraft.blocks;

import com.fabpharos.levincraft.Registration;
import com.fabpharos.levincraft.utils.LightningBoltRenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class PylonBlockRenderer implements BlockEntityRenderer<PylonBlockEntity> {
    public PylonBlockRenderer(BlockEntityRendererProvider.Context context) {

    }
    @Override
    public void render(PylonBlockEntity pylonBlockEntity, float v, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        ArrayList<BlockPos> registeredPylons = pylonBlockEntity.registeredPylons;
        int brightness = LightTexture.FULL_BRIGHT;
        float width = .25f;
        float height = width;

        poseStack.pushPose();
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();
        //Translate to the middle of the block
        //Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        //poseStack.mulPose(Vector3f.YP.rotationDegrees(-camera.getYRot()));
        //poseStack.mulPose(Vector3f.XP.rotationDegrees(camera.getXRot()));

        boolean isHoldingConfigurator = Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND).is(Registration.CONFIGURATOR.get());
        VertexConsumer buffer = bufferSource.getBuffer(isHoldingConfigurator ? RenderType.LINES : LightningBoltRenderType.LIGHTNING_LINES);

        BlockPos start = pylonBlockEntity.getBlockPos();
        poseStack.translate(0.5,0.5,0.5);
        for (BlockPos end : registeredPylons) {
            BlockPos distance = end.subtract(start);
            if (isHoldingConfigurator) {
                buffer.vertex(matrix, 0, 0, 0).color(1f, 0f, 1f, 0.3f).uv(1, 1).uv2(brightness).normal(1, 1, 0).endVertex();
                buffer.vertex(matrix, distance.getX(), distance.getY(), distance.getZ()).color(1f, 0f, 1f, 0.3f).uv(1, 1).uv2(brightness).normal(1, 1, 0).endVertex();

            } else {
                List<Vec3> segments = pylonBlockEntity.getBeamCache();
                //Quaternion direction = Quaternion.fromXYZ(distance.getX(), distance.getY(), distance.getZ());
                //direction.normalize();
                //poseStack.mulPose(direction);
                for (int i = 0; i < segments.size() - 1; i += 2) {
                    var from = segments.get(i);
                    var to = segments.get(i + 1);
                    drawHull(from, to, width, height, pose, buffer, 0, 156, 255, 30);
                    drawHull(from, to, width * .55f, height * .55f, pose, buffer, 0, 226, 255, 30);
                }
                //direction.mul(-1);
                //poseStack.mulPose(direction);
            }
        }
        poseStack.popPose();
    }

    public void drawHull(Vec3 from, Vec3 to, float width, float height, PoseStack.Pose pose, VertexConsumer consumer, int r, int g, int b, int a) {
        //Bottom
        drawQuad(from.subtract(0, height * .5f, 0), to.subtract(0, height * .5f, 0), width, 0, pose, consumer, r, g, b, a);
        //Top
        drawQuad(from.add(0, height * .5f, 0), to.add(0, height * .5f, 0), width, 0, pose, consumer, r, g, b, a);
        //Left
        drawQuad(from.subtract(width * .5f, 0, 0), to.subtract(width * .5f, 0, 0), 0, height, pose, consumer, r, g, b, a);
        //Right
        drawQuad(from.add(width * .5f, 0, 0), to.add(width * .5f, 0, 0), 0, height, pose, consumer, r, g, b, a);
    }

    public void drawQuad(Vec3 from, Vec3 to, float width, float height, PoseStack.Pose pose, VertexConsumer consumer, int r, int g, int b, int a) {
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        //to = new Vec3(1, 0, 10);
        float halfWidth = width * .5f;
        float halfHeight = height * .5f;
        //float height = (float) (Math.random() * .25f) + .25f;
        consumer.vertex(poseMatrix, (float) from.x - halfWidth, (float) from.y - halfHeight, (float) from.z).color(r, g, b, a).uv(0f, 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, (float) from.x + halfWidth, (float) from.y + halfHeight, (float) from.z).color(r, g, b, a).uv(1f, 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, (float) to.x + halfWidth, (float) to.y + halfHeight, (float) to.z).color(r, g, b, a).uv(1f, 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, (float) to.x - halfWidth, (float) to.y - halfHeight, (float) to.z).color(r, g, b, a).uv(0f, 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0f, 1f, 0f).endVertex();
    }

    public static void register() {
        BlockEntityRenderers.register(Registration.PYLON_BLOCK_ENTITY.get(), PylonBlockRenderer::new);
    }
}

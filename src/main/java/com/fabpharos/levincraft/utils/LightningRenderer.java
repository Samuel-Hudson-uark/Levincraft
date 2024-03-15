package com.fabpharos.levincraft.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;

//@Mod.EventBusSubscriber(modid = MODID, bus=Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class LightningRenderer {
    static int b1 = LightTexture.FULL_BRIGHT;

    //@SubscribeEvent
    public static void renderLineInWorld(RenderLevelStageEvent event) {
        if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            Vector3f start3f = new Vector3f(20,60,10);
            Vector3f end3f = new Vector3f(10,60,10);
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.lines());
            PoseStack stack = event.getPoseStack();
            Vec3 cam = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

            stack.pushPose();
            stack.translate(-cam.x, -cam.y, -cam.z);
            Matrix4f mat = stack.last().pose();

            vertexConsumer.vertex(mat, start3f.x(), start3f.y(), start3f.z()).color(0, 255, 255, 150).uv2(b1).normal(1, 0, 0).endVertex();
            vertexConsumer.vertex(mat,end3f.x(), end3f.y(), end3f.z()).color(0, 255, 255, 150).uv2(b1).normal(1, 0, 0).endVertex();
            stack.popPose();
        }
    }

    private class Lightning {
        public final Vector3f start;
        public final Vector3f end;
        public int ticks;
        Lightning(Vector3f start, Vector3f end, int ticks) {
            this.start = start;
            this.end = end;
            this.ticks = ticks;
        }
    }
}

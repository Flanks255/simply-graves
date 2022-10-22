package com.flanks255.simplygraves.render;

import com.flanks255.simplygraves.GraveEntity;
import com.flanks255.simplygraves.config.CommonConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;

public class GraveTextRender implements BlockEntityRenderer<GraveEntity> {
    private static final SimpleDateFormat format = new SimpleDateFormat("mm:ss");
    public GraveTextRender() {
    }

    @Override
    public void render(@Nonnull GraveEntity graveEntity, float pPartialTick, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (Minecraft.getInstance().player == null)
            return;

        matrixStack.pushPose();
        matrixStack.translate(0.5,0.5,0.5);
        matrixStack.scale(1, -1, 1);
        matrixStack.scale(0.03F, 0.03F, 0.03F);


        Vec3 cameraPos = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();
        BlockPos gravePos = graveEntity.getBlockPos();
        boolean isMine = graveEntity.getPlayer().compareTo(Minecraft.getInstance().player.getUUID()) == 0;
        double angle = Mth.atan2(cameraPos.z - (gravePos.getZ() + 0.5f), cameraPos.x - (gravePos.getX() + 0.5f));
        matrixStack.mulPose(Vector3f.YP.rotation((float) ((Math.PI / 2) - (float) angle)));

        Font font = Minecraft.getInstance().font;
        Screen.drawCenteredString(matrixStack, font, graveEntity.getPlayerName(), 0,-25, 0xffffff);

        long timeRemaining = 0;
        if (!isMine) {
            timeRemaining = (graveEntity.getDeathTime() + (CommonConfig.DELAY_TO_PUBLIC.get() * 1000)) - System.currentTimeMillis();
            if (timeRemaining > 0) {
                matrixStack.translate(0, -15, 0);
                matrixStack.scale(0.25f, 0.25f, 0.25f);
                Screen.drawCenteredString(matrixStack, font, format.format(timeRemaining), 0, 0, 0xffff00);
            }
        }
        if (isMine || timeRemaining == 0) {
            matrixStack.translate(0, -15, 0);
            matrixStack.scale(0.25f, 0.25f, 0.25f);
            Screen.drawCenteredString(matrixStack, font, "Right-click to collect.", 0, 0, 0xffffff);
        }

        matrixStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 10;
    }
}

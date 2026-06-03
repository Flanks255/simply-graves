package com.flanks255.simplygraves.render;

import com.flanks255.simplygraves.GraveEntity;
import com.flanks255.simplygraves.config.CommonConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.UUID;

public class GraveTextRender implements BlockEntityRenderer<GraveEntity, GraveTextRender.GraveRenderState> {
    private static final SimpleDateFormat format = new SimpleDateFormat("mm:ss");
    private final Font font;

    public GraveTextRender(BlockEntityRendererProvider.Context context) {
        this.font = context.font();
    }

    @Override
    public GraveRenderState createRenderState() {
        return new GraveRenderState();
    }

    @Override
    public void extractRenderState(GraveEntity graveEntity, GraveRenderState state, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(graveEntity, state, partialTick, cameraPosition, breakProgress);
        state.playerName = graveEntity.getPlayerName();
        LocalPlayer player = Minecraft.getInstance().player;
        UUID gravePlayer = graveEntity.getPlayer();
        state.isMine = gravePlayer != null && player != null && gravePlayer.compareTo(player.getUUID()) == 0;
        state.timeRemaining = (graveEntity.getDeathTime() + (CommonConfig.DELAY_TO_PUBLIC.get() * 1000L)) - System.currentTimeMillis();
    }

    @Override
    public void submit(GraveRenderState state, @NotNull PoseStack matrixStack, @NotNull SubmitNodeCollector nodeCollector, @NotNull CameraRenderState camera) {
        renderFace(state, matrixStack, nodeCollector, 0f);
        renderFace(state, matrixStack, nodeCollector, 180f);
    }

    private void renderFace(GraveRenderState state, PoseStack matrixStack, SubmitNodeCollector nodeCollector, float yaw) {
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.mulPose(Axis.YP.rotationDegrees(yaw));
        matrixStack.translate(0, 0, 0.07);
        matrixStack.scale(1, -1, 1);
        matrixStack.scale(0.025F, 0.025F, 0.025F);

        drawCenteredString(matrixStack, nodeCollector, Component.literal(state.playerName), -8, 0xffffff);

        Component.literal("test").getVisualOrderText();

        matrixStack.popPose();
    }

    private void drawCenteredString(PoseStack matrixStack, SubmitNodeCollector nodeCollector, Component text, int y, int color) {
        FormattedCharSequence sequence = text.getVisualOrderText();
        float x = -font.width(sequence) / 2.0f;
        Font.PreparedText prepared = font.prepareText(sequence, x, y, color | 0xFF000000, false, false, 0);
        RenderType[] renderType = {null};
        prepared.visit(new Font.GlyphVisitor() {
            @Override
            public void acceptGlyph(TextRenderable.Styled glyph) {
                if (renderType[0] == null) {
                    renderType[0] = glyph.renderType(Font.DisplayMode.NORMAL, false);
                }
            }
        });
        if (renderType[0] == null) {
            return;
        }
        nodeCollector.submitCustomGeometry(matrixStack, renderType[0], (pose, consumer) -> prepared.visit(new Font.GlyphVisitor() {
            @Override
            public void acceptGlyph(TextRenderable.Styled glyph) {
                glyph.render(pose.pose(), consumer, 15728880, false);
            }

            @Override
            public void acceptEffect(TextRenderable effect) {
                effect.render(pose.pose(), consumer, 15728880, false);
            }
        }));
    }

    @Override
    public int getViewDistance() {
        return 10;
    }

    public static class GraveRenderState extends BlockEntityRenderState {
        public String playerName = "";
        public boolean isMine;
        public long timeRemaining;
    }
}

package com.oitsjustjose.geolosys.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.oitsjustjose.geolosys.common.config.ClientConfig;
import com.oitsjustjose.geolosys.common.items.ProPickItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class ProPickOverlay {
    @SubscribeEvent
    public static void onDrawScreen(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.options.renderDebug || mc.options.renderDebugCharts) {
            return;
        }

        if (mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ProPickItem || mc.player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof ProPickItem) {
            GlStateManager._enableBlend();
            GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            int seaLvl = mc.player.level().getSeaLevel();
            int level = (int) (seaLvl - mc.player.getY());
            if (level < 0) {
                event.getGuiGraphics().drawString(mc.font, I18n.get("geolosys.pro_pick.depth.above", Math.abs(level)), ClientConfig.PROPICK_HUD_X.get(), ClientConfig.PROPICK_HUD_Y.get(), 0xFFFFFFFF);
            } else if (level == 0) {
                event.getGuiGraphics().drawString(mc.font, I18n.get("geolosys.pro_pick.depth.at"), ClientConfig.PROPICK_HUD_X.get(), ClientConfig.PROPICK_HUD_Y.get(), 0xFFFFFFFF);
            } else {
                event.getGuiGraphics().drawString(mc.font, I18n.get("geolosys.pro_pick.depth.below", Math.abs(level)), ClientConfig.PROPICK_HUD_X.get(), ClientConfig.PROPICK_HUD_Y.get(), 0xFFFFFFFF);
            }
        }
    }
}

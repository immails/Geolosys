package com.oitsjustjose.geolosys.common.config;

import java.nio.file.Path;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;

public class ClientConfig {
    public static final ForgeConfigSpec CLIENT_CONFIG;
    private static final Builder CLIENT_BUILDER = new Builder();
    private static final String CATEGORY_CLIENT = "client";
    public static ForgeConfigSpec.DoubleValue MANUAL_FONT_SCALE;
    public static ForgeConfigSpec.IntValue PROPICK_HUD_X;
    public static ForgeConfigSpec.IntValue PROPICK_HUD_Y;
    public static ForgeConfigSpec.BooleanValue ENABLE_TAG_DEBUG;

    static {
        init();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

        configData.load();
        spec.setConfig(configData);
    }

    public static void init() {
        CLIENT_BUILDER.comment("Client-Side Settings").push(CATEGORY_CLIENT);

        MANUAL_FONT_SCALE = CLIENT_BUILDER.comment("Defines the scale factor of the font for the Field Manual").defineInRange("manualFontScale", 0.75D, 0.1D, 3.0D);
        PROPICK_HUD_X = CLIENT_BUILDER.comment("The X-position of the Prospector's Pickaxe Depth HUD").defineInRange("propickHUDX", 2, 0, Integer.MAX_VALUE);
        PROPICK_HUD_Y = CLIENT_BUILDER.comment("The Y-position of the Prospector's Pickaxe Depth HUD").defineInRange("propickHUDY", 2, 0, Integer.MAX_VALUE);
        ENABLE_TAG_DEBUG = CLIENT_BUILDER.comment("Enable Minecraft object tag tooltip with Advanced Tooltips on (F3+\"H\")").define("enableTagDebug", true);
        CLIENT_BUILDER.pop();
    }
}

package com.oitsjustjose.geolosys.client.network;

import java.util.function.Supplier;

import com.oitsjustjose.geolosys.common.network.PacketStackSurface;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class PacketStackClientSurface {
    public static void handleClient(PacketStackSurface msg, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.get().enqueueWork(() -> {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player != null) {
                    sendProspectingMessage(mc.player, msg);
                }
            });
        }
        context.get().setPacketHandled(true);
    }

    private static void sendProspectingMessage(LocalPlayer player, PacketStackSurface msg) {
        MutableComponent component = Component.empty();
        for (String block : msg.blocks) {
            component.append(Component.translatable("block." + block));
        }
        player.displayClientMessage(
            MutableComponent.create(
                new TranslatableContents("geolosys.pro_pick.tooltip.found_surface", null, new Object[]{
                    component.withStyle(ChatFormatting.GOLD), 
            })).withStyle(ChatFormatting.GRAY), true
        );
    }
}

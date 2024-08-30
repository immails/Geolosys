package com.oitsjustjose.geolosys.common.network;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class PacketStackSurface {

    public HashSet<String> blocks;

    public PacketStackSurface(FriendlyByteBuf buf) {
        this.blocks = new HashSet<String>(Arrays.asList(buf.readUtf().split(",")));
    }

    public PacketStackSurface(HashSet<String> d1) {
        this.blocks = d1;
    }

    public static PacketStackSurface decode(FriendlyByteBuf buf) {
        return new PacketStackSurface(buf);
    }

    public static void encode(PacketStackSurface msg, FriendlyByteBuf buf) {
        buf.writeUtf(String.join(",", msg.blocks));
    }

    public void handleServer(Supplier<NetworkEvent.Context> context) {
        context.get().setPacketHandled(true);
    }
}

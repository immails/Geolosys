package com.oitsjustjose.geolosys.common.network;

import net.minecraft.network.FriendlyByteBuf;
import java.util.HashSet;

public class PacketStackUnderground extends PacketStackSurface {
    public Integer direction;

    public PacketStackUnderground(FriendlyByteBuf buf) {
        super(buf);
        this.direction = buf.readInt();
    }

    public PacketStackUnderground(HashSet<String> d1, Integer direction) {
        super(d1);
        this.direction = direction;
    }

    public static PacketStackUnderground decode(FriendlyByteBuf buf) {
        return new PacketStackUnderground(buf);
    }

    public static void encode(PacketStackUnderground msg, FriendlyByteBuf buf) {
        PacketStackSurface.encode(msg, buf);
        buf.writeInt(msg.direction);
    }
}

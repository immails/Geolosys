package com.oitsjustjose.geolosys.common;

import com.oitsjustjose.geolosys.common.network.NetworkManager;
import com.oitsjustjose.geolosys.common.network.PacketStackSurface;
import com.oitsjustjose.geolosys.common.network.PacketStackUnderground;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashSet;

public class CommonProxy {
    public static NetworkManager networkManager = new NetworkManager();
    public static int discriminator = 0;

    public void init() {
        networkManager.networkWrapper.registerMessage(CommonProxy.discriminator++, PacketStackSurface.class,
                PacketStackSurface::encode, PacketStackSurface::decode, PacketStackSurface::handleServer);
        networkManager.networkWrapper.registerMessage(CommonProxy.discriminator++, PacketStackUnderground.class,
                PacketStackUnderground::encode, PacketStackUnderground::decode, PacketStackUnderground::handleServer);
    }

    public void sendProspectingMessage(Player player, HashSet<BlockState> blocks, @Nullable Direction direction) {
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        HashSet<String> blockSet = new HashSet<String>();
        for (BlockState blockState : blocks) {
            blockSet.add(ForgeRegistries.BLOCKS.getKey(blockState.getBlock()).toLanguageKey());
        }
        if (direction != null) {
            PacketStackUnderground msg = new PacketStackUnderground(blockSet, direction.ordinal());
            networkManager.networkWrapper.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), msg);
        } else {
            PacketStackSurface msg = new PacketStackSurface(blockSet);
            networkManager.networkWrapper.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), msg);
        }
    }

    public void registerClientSubscribeEvent(Object o) {
    }
}

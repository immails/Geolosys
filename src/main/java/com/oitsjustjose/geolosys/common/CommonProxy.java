package com.oitsjustjose.geolosys.common;

import java.util.HashSet;

import javax.annotation.Nullable;

import com.oitsjustjose.geolosys.common.network.NetworkManager;
import com.oitsjustjose.geolosys.common.network.PacketStackSurface;
import com.oitsjustjose.geolosys.common.network.PacketStackUnderground;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

public class CommonProxy {
    public static NetworkManager networkManager = new NetworkManager();
    public static int discriminator = 0;

    public void init() {
        networkManager.networkWrapper.registerMessage(CommonProxy.discriminator++, PacketStackSurface.class,
                PacketStackSurface::encode, PacketStackSurface::decode, PacketStackSurface::handleServer);
        networkManager.networkWrapper.registerMessage(CommonProxy.discriminator++, PacketStackUnderground.class,
                PacketStackUnderground::encode, PacketStackUnderground::decode, PacketStackUnderground::handleServer);
    }

    protected HashSet<String> convertBlockStates(HashSet<BlockState> blocks) {
        HashSet<String> blockSet = new HashSet<String>();
        for (BlockState blockState : blocks) {
            blockSet.add(ForgeRegistries.BLOCKS.getKey(blockState.getBlock()).toLanguageKey());
        }
        return blockSet;
    }

    public void sendProspectingMessage(Player player, HashSet<BlockState> blocks, @Nullable Direction direction) {
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        if (direction != null) {
            PacketStackUnderground msg = new PacketStackUnderground(convertBlockStates(blocks), direction.ordinal());
            networkManager.networkWrapper.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), msg);
        } else {
            PacketStackSurface msg = new PacketStackSurface(convertBlockStates(blocks));
            networkManager.networkWrapper.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), msg);
        }
    }

    public void registerClientSubscribeEvent(Object o) {
    }
}

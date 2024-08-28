package com.oitsjustjose.geolosys;

import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.capability.deposit.DepositCapability;
import com.oitsjustjose.geolosys.capability.deposit.IDepositCapability;
import com.oitsjustjose.geolosys.capability.player.IPlayerCapability;
import com.oitsjustjose.geolosys.capability.player.PlayerCapability;
import com.oitsjustjose.geolosys.capability.world.ChunkGennedCapability;
import com.oitsjustjose.geolosys.capability.world.IChunkGennedCapability;
import com.oitsjustjose.geolosys.client.ClientProxy;
import com.oitsjustjose.geolosys.client.GeolosysClient;
import com.oitsjustjose.geolosys.client.patchouli.processors.PatronProcessor;
import com.oitsjustjose.geolosys.common.CommonProxy;
import com.oitsjustjose.geolosys.common.config.ClientConfig;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.data.WorldGenDataLoader;
import com.oitsjustjose.geolosys.common.datagen.WorldGenProvider;
import com.oitsjustjose.geolosys.common.event.ManualGifting;
import com.oitsjustjose.geolosys.common.items.CoalItem;
import com.oitsjustjose.geolosys.common.utils.Constants;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@Mod(Constants.MODID)
public class Geolosys {
    private static Geolosys instance;
    public final Registry REGISTRY;

    public static CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    public Logger LOGGER = LogManager.getLogger();

    public Geolosys() {
        instance = this;
        LOGGER.info("Preparing to take over the worldgen");

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        REGISTRY = new Registry(bus);

        // Register the setup method for modloading
        bus.addListener(this::setup);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> GeolosysClient::setup);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ManualGifting());

        this.configSetup();
    }

    public static Geolosys getInstance() {
        return instance;
    }

    private void configSetup() {
        ModLoadingContext.get().registerConfig(Type.CLIENT, ClientConfig.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.COMMON_CONFIG);
        CommonConfig.loadConfig(CommonConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("geolosys-common.toml"));
    }


    public void setup(final FMLCommonSetupEvent event) {
        GeolosysAPI.init();
        PatronProcessor.fetchPatrons();
        proxy.init();
    }

    @SubscribeEvent
    public void onSlashReload(AddReloadListenerEvent evt) {
        evt.addListener(new WorldGenDataLoader());
    }

    @SubscribeEvent
    public void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(DepositCapability.class);
        event.register(PlayerCapability.class);
        event.register(ChunkGennedCapability.class);
    }

    @SubscribeEvent
    public void attachWorldCaps(AttachCapabilitiesEvent<Level> event) {
        if (event.getObject().isClientSide()) {
            return;
        }

        try {
            final LazyOptional<IDepositCapability> inst = LazyOptional.of(DepositCapability::new);
            final ICapabilitySerializable<CompoundTag> provider = new ICapabilitySerializable<>() {
                @Override
                public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
                    return DepositCapability.CAPABILITY.orEmpty(cap, inst);
                }

                @Override
                public CompoundTag serializeNBT() {
                    IDepositCapability cap = this.getCapability(DepositCapability.CAPABILITY).orElseThrow(RuntimeException::new);
                    return cap.serializeNBT();
                }

                @Override
                public void deserializeNBT(CompoundTag nbt) {
                    IDepositCapability cap = this.getCapability(DepositCapability.CAPABILITY).orElseThrow(RuntimeException::new);
                    cap.deserializeNBT(nbt);
                }
            };
            event.addCapability(Constants.DEPOSIT_CAPABILITY_NAME, provider);
            event.addListener(inst::invalidate);
        } catch (Exception e) {
            LOGGER.error("Geolosys has faced a fatal error. The game will crash...");
            throw new RuntimeException(e);
        }

        try {
            final LazyOptional<IPlayerCapability> inst = LazyOptional.of(PlayerCapability::new);
            final ICapabilitySerializable<CompoundTag> provider = new ICapabilitySerializable<>() {
                @Override
                public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
                    return PlayerCapability.CAPABILITY.orEmpty(cap, inst);
                }

                @Override
                public CompoundTag serializeNBT() {
                    IPlayerCapability cap = this.getCapability(PlayerCapability.CAPABILITY).orElseThrow(RuntimeException::new);
                    return cap.serializeNBT();
                }

                @Override
                public void deserializeNBT(CompoundTag nbt) {
                    IPlayerCapability cap = this.getCapability(PlayerCapability.CAPABILITY).orElseThrow(RuntimeException::new);
                    cap.deserializeNBT(nbt);
                }
            };
            event.addCapability(Constants.PLAYER_CAPABILITY_NAME, provider);
            event.addListener(inst::invalidate);
        } catch (Exception e) {
            LOGGER.error("Geolosys has faced a fatal error. The game will crash...");
            throw new RuntimeException(e);
        }

        try {
            final LazyOptional<IChunkGennedCapability> inst = LazyOptional.of(ChunkGennedCapability::new);
            final ICapabilitySerializable<CompoundTag> provider = new ICapabilitySerializable<>() {
                @Override
                public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
                    return ChunkGennedCapability.CAPABILITY.orEmpty(cap, inst);
                }

                @Override
                public CompoundTag serializeNBT() {
                    IChunkGennedCapability cap = this.getCapability(ChunkGennedCapability.CAPABILITY).orElseThrow(RuntimeException::new);
                    return cap.serializeNBT();
                }

                @Override
                public void deserializeNBT(CompoundTag nbt) {
                    IChunkGennedCapability cap = this.getCapability(ChunkGennedCapability.CAPABILITY).orElseThrow(RuntimeException::new);
                    cap.deserializeNBT(nbt);
                }
            };
            event.addCapability(Constants.CHUNKGEN_CAPABILITY_NAME, provider);
            event.addListener(inst::invalidate);
        } catch (Exception e) {
            LOGGER.error("Geolosys has faced a fatal error. The game will crash...");
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public void onFuelRegistry(FurnaceFuelBurnTimeEvent fuelBurnoutEvent) {
        if (fuelBurnoutEvent.getItemStack().getItem() instanceof CoalItem c) {
            fuelBurnoutEvent.setBurnTime(c.getBurnTime());
        }
    }
}

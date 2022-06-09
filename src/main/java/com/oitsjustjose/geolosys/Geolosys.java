package com.oitsjustjose.geolosys;

import java.util.Collection;

import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.client.ClientProxy;
import com.oitsjustjose.geolosys.client.render.Cutouts;
import com.oitsjustjose.geolosys.common.CommonProxy;
import com.oitsjustjose.geolosys.common.blocks.ModBlocks;
import com.oitsjustjose.geolosys.common.config.ClientConfig;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.config.ModItemsParser;
import com.oitsjustjose.geolosys.common.data.WorldGenDataLoader;
import com.oitsjustjose.geolosys.common.data.modifiers.OsmiumDropModifier;
import com.oitsjustjose.geolosys.common.data.modifiers.QuartzDropModifier;
import com.oitsjustjose.geolosys.common.data.modifiers.SulfurDropModifier;
import com.oitsjustjose.geolosys.common.data.modifiers.YelloriumDropModifier;
import com.oitsjustjose.geolosys.common.event.ManualGifting;
import com.oitsjustjose.geolosys.common.items.ModItems;
import com.oitsjustjose.geolosys.common.utils.Constants;
import com.oitsjustjose.geolosys.common.world.capability.Deposit.DepositCapProvider;
import com.oitsjustjose.geolosys.common.world.capability.Deposit.DepositCapStorage;
import com.oitsjustjose.geolosys.common.world.capability.Deposit.DepositCapability;
import com.oitsjustjose.geolosys.common.world.capability.Deposit.IDepositCapability;
import com.oitsjustjose.geolosys.common.world.capability.Chunk.ChunkGennedCapProvider;
import com.oitsjustjose.geolosys.common.world.capability.Chunk.ChunkGennedCapStorage;
import com.oitsjustjose.geolosys.common.world.capability.Chunk.ChunkGennedCapability;
import com.oitsjustjose.geolosys.common.world.capability.Chunk.IChunkGennedCapability;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Constants.MODID)
public class Geolosys {
    private static Geolosys instance;
    public static CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    public Logger LOGGER = LogManager.getLogger();

    public Geolosys() {
        instance = this;

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ManualGifting());
        MinecraftForge.EVENT_BUS.register(new ModItemsParser());

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

    private void clientSetup(final FMLClientSetupEvent event) {
        Cutouts.init();
    }

    public void setup(final FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(IDepositCapability.class, new DepositCapStorage(), DepositCapability::new);
        CapabilityManager.INSTANCE.register(IChunkGennedCapability.class, new ChunkGennedCapStorage(), ChunkGennedCapability::new);

        GeolosysAPI.init();
        proxy.init();
    }

    @SubscribeEvent
    public void onSlashReload(AddReloadListenerEvent evt) {
        evt.addListener(new WorldGenDataLoader());
    }

    @SubscribeEvent
    public void attachCap(AttachCapabilitiesEvent<World> event) {
        String dimName = event.getObject().getDimensionKey().getLocation().toString();

        event.addCapability(new ResourceLocation(Constants.MODID, "pluton"), new DepositCapProvider());
        event.addCapability(new ResourceLocation(Constants.MODID, "generated_chunks"), new ChunkGennedCapProvider());
        LOGGER.info("Geolosys Capabilities attached for {}", dimName);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onHover(ItemTooltipEvent event) {
        if (!ClientConfig.ENABLE_TAG_DEBUG.get()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();

        if (mc.gameSettings.advancedItemTooltips) {
            Collection<ResourceLocation> tags = ItemTags.getCollection().getOwningTags(event.getItemStack().getItem());
            if (tags.size() > 0) {
                for (ResourceLocation tag : tags) {
                    event.getToolTip().add(new StringTextComponent("\u00A78#" + tag.toString() + "\u00A7r"));
                }
            }
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            ModBlocks.getInstance().register(blockRegistryEvent);
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
            ModBlocks.getInstance().registerItemBlocks(itemRegistryEvent);
            ModItems.getInstance().register(itemRegistryEvent);
        }

        @SubscribeEvent
        public static void onModifierSerializersRegistry(
                final RegistryEvent.Register<GlobalLootModifierSerializer<?>> evt) {
            evt.getRegistry().register(new OsmiumDropModifier.Serializer()
                    .setRegistryName(new ResourceLocation(Constants.MODID, "osmium")));
            evt.getRegistry().register(new QuartzDropModifier.Serializer()
                    .setRegistryName(new ResourceLocation(Constants.MODID, "quartzes")));
            evt.getRegistry().register(new YelloriumDropModifier.Serializer()
                    .setRegistryName(new ResourceLocation(Constants.MODID, "yellorium")));
            evt.getRegistry().register(new SulfurDropModifier.Serializer()
                    .setRegistryName(new ResourceLocation(Constants.MODID, "sulfur")));
        }
    }
}

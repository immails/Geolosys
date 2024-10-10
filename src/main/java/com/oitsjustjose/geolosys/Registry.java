package com.oitsjustjose.geolosys;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.oitsjustjose.geolosys.common.blocks.OreBlock;
import com.oitsjustjose.geolosys.common.blocks.PeatBlock;
import com.oitsjustjose.geolosys.common.blocks.PlantBlock;
import com.oitsjustjose.geolosys.common.blocks.SampleBlock;
import com.oitsjustjose.geolosys.common.items.CoalItem;
import com.oitsjustjose.geolosys.common.items.DepthMeterItem;
import com.oitsjustjose.geolosys.common.items.ProPickItem;
import com.oitsjustjose.geolosys.common.utils.Constants;
import com.oitsjustjose.geolosys.common.world.feature.DepositFeature;
import com.oitsjustjose.geolosys.common.world.feature.RemoveVeinsFeature;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vazkii.patchouli.api.PatchouliAPI;

public class Registry {
    public final DeferredRegister<Block> BlockRegistry = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);
    public final DeferredRegister<Item> ItemRegistry = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
    public final DeferredRegister<Feature<?>> FeatureRegistry = DeferredRegister.create(Registries.FEATURE, Constants.MODID);

    public final RegistryObject<DepositFeature> deposits = FeatureRegistry.register(
        "deposits", () -> new DepositFeature(NoneFeatureConfiguration.CODEC));
    public final RegistryObject<RemoveVeinsFeature> remove_veins = FeatureRegistry.register(
        "remove_veins", () -> new RemoveVeinsFeature(NoneFeatureConfiguration.CODEC));

    // Here because cutouts and coloring
    public final RegistryObject<Block> peat = BlockRegistry.register("peat", PeatBlock::new);
    public final RegistryObject<Block> rhododendron = BlockRegistry.register("rhododendron", () -> new PlantBlock(false, peat));


    public Registry(IEventBus bus) {
        final List<RegistryObject<Block>> NeedItemBlocks = RegisterBlocks();
        final DeferredRegister<CreativeModeTab> CreativeModeTabRegistry = RegisterItems(NeedItemBlocks);

        BlockRegistry.register(bus);
        ItemRegistry.register(bus);
        FeatureRegistry.register(bus);
        CreativeModeTabRegistry.register(bus);
    }

    private <I extends Item> RegistryObject<I> RegisterItem(final List<RegistryObject<? extends Item>> queue, final String name, final Supplier<I> sup) {
        final RegistryObject<I> item = ItemRegistry.register(name, sup);
        queue.add(item);
        return item;
    }

    private final List<RegistryObject<Block>> RegisterBlocks() {
        final List<RegistryObject<Block>> NeedItemBlocks = Lists.newArrayList();
        final HashMap<String, Integer> UniversalMaterials = new HashMap<>() {{
            put("anthracite_coal", 2);
            put("autunite", 0);
            put("azurite", 0);
            put("bauxite", 0);
            put("beryl", 7);
            put("bituminous_coal", 2);
            put("cassiterite", 0);
            put("cinnabar", 0);
            put("coal", 2);
            put("galena", 0);
            put("gold", 0);
            put("hematite", 0);
            put("kimberlite", 7);
            put("lapis", 5);
            put("lignite", 2);
            put("limonite", 0);
            put("malachite", 0);
            put("platinum", 0);
            put("quartz", 5);
            put("sphalerite", 0);
            put("teallite", 0);
        }};

        BlockBehaviour.Properties baseProps = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(5.0F, 10F).sound(SoundType.STONE).requiresCorrectToolForDrops();
        BlockBehaviour.Properties dsProps = BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).strength(7.5F, 10F).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops();
        BlockBehaviour.Properties netherProps = BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).strength(7.5F, 10F).requiresCorrectToolForDrops();

        // Non-standard blocks
        NeedItemBlocks.add(this.peat);
        NeedItemBlocks.add(this.rhododendron);

        UniversalMaterials.forEach((name, xp) -> {
            NeedItemBlocks.add(BlockRegistry.register(name + "_ore", () -> new OreBlock(baseProps, xp)));
            NeedItemBlocks.add(BlockRegistry.register("deepslate_" + name + "_ore", () -> new OreBlock(dsProps, xp)));
            NeedItemBlocks.add(BlockRegistry.register(name + "_ore_sample", SampleBlock::new));
        });

        // Ores that don't need a deepslate variant
        NeedItemBlocks.add(BlockRegistry.register("ancient_debris_ore", () -> new OreBlock(netherProps.sound(SoundType.ANCIENT_DEBRIS), 0)));
        NeedItemBlocks.add(BlockRegistry.register("ancient_debris_ore_sample", SampleBlock::new));
        NeedItemBlocks.add(BlockRegistry.register("nether_gold_ore", () -> new OreBlock(netherProps.sound(SoundType.NETHER_GOLD_ORE), 1)));
        NeedItemBlocks.add(BlockRegistry.register("nether_gold_ore_sample", SampleBlock::new));


        return NeedItemBlocks;
    }

    private DeferredRegister<CreativeModeTab> RegisterItems(final List<RegistryObject<Block>> NeedItemBlocks) {
        DeferredRegister<CreativeModeTab> CreativeTabRegisty = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MODID);
        final Item.Properties baseProps = new Item.Properties().stacksTo(64);
        final List<RegistryObject<? extends Item>> TabListQueue = Lists.newArrayList(); 

        // Register Block Items
        NeedItemBlocks.forEach(
            ItemBlock -> RegisterItem(
                TabListQueue, 
                ItemBlock.getId().getPath(), 
                () -> new BlockItem(ItemBlock.get(), baseProps)
            )
        );

        // Special Items
        final RegistryObject<ProPickItem> prospector_pick = RegisterItem(TabListQueue, "prospectors_pick", ProPickItem::new);
        RegisterItem(TabListQueue, "depth_meter", () -> new DepthMeterItem(new Item.Properties().stacksTo(1)));

        // Coals
        RegisterItem(TabListQueue, "anthracite_coal", () -> new CoalItem(20));
        RegisterItem(TabListQueue, "bituminous_coal", () -> new CoalItem(16));
        RegisterItem(TabListQueue, "lignite_coal", () -> new CoalItem(12));
        RegisterItem(TabListQueue, "peat_coal", () -> new CoalItem(6));
        RegisterItem(TabListQueue, "bituminous_coal_coke", () -> new CoalItem(32));
        RegisterItem(TabListQueue, "lignite_coal_coke", () -> new CoalItem(24));

        // Ingots
        RegisterItem(TabListQueue, "aluminum_ingot", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "lead_ingot", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "nickel_ingot", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "platinum_ingot", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "silver_ingot", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "tin_ingot", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "zinc_ingot", () -> new Item(baseProps));

        // Nuggets
        RegisterItem(TabListQueue, "aluminum_nugget", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "copper_nugget", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "lead_nugget", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "nickel_nugget", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "platinum_nugget", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "silver_nugget", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "tin_nugget", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "zinc_nugget", () -> new Item(baseProps));

        // Clusters
        RegisterItem(TabListQueue, "aluminum_cluster", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "ancient_debris_cluster", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "copper_cluster", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "gold_cluster", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "iron_cluster", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "lead_cluster", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "nether_gold_cluster", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "nickel_cluster", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "osmium_cluster", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "platinum_cluster", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "silver_cluster", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "tin_cluster", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "uranium_cluster", () -> new Item(baseProps));
        RegisterItem(TabListQueue, "zinc_cluster", () -> new Item(baseProps));

        // Others
        
        CreativeTabRegisty.register("geolosys", 
            () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.geolosys.name"))
                .icon(() -> new ItemStack(prospector_pick.get()))
                .displayItems((params, output) -> {
                    for(RegistryObject<? extends Item> TabListItem : TabListQueue) {
                        output.accept(TabListItem.get());
                    }
                    output.accept(PatchouliAPI.get().getBookStack(new ResourceLocation(Constants.MODID, "field_manual")));
                })
                .build()
        ); //At this moment no class requires the creative tab reference. 

        return CreativeTabRegisty;
    }
}
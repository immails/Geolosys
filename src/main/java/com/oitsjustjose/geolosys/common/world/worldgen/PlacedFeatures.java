package com.oitsjustjose.geolosys.common.world.worldgen;

import java.util.List;

import com.google.common.collect.Lists;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.utils.Constants;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

public class PlacedFeatures {

	private ConfiguredFeatures ConfiguredFeaturesInstance;

	public PlacedFeatures(ConfiguredFeatures _ConfiguredFeaturesInstance) {
		ConfiguredFeaturesInstance = _ConfiguredFeaturesInstance;
		Geolosys.getInstance().LOGGER.info("HIIIIIIIII");
	}

    public final ResourceKey<PlacedFeature> deposits_placed = registerKey("deposits_placed");
    public final ResourceKey<PlacedFeature> remove_veins_placed = registerKey("remove_veins_placed");

    public void bootstrap(BootstapContext<PlacedFeature> context) {
		Geolosys.getInstance().LOGGER.info("Bootstrap of PlacedFeatures has been called");

        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        List<PlacementModifier> placement = Lists.newArrayList(HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(320)));

		context.register(deposits_placed, new PlacedFeature(configuredFeatures.getOrThrow(ConfiguredFeaturesInstance.DEPOSITS_KEY), List.copyOf(placement)));
		context.register(remove_veins_placed, new PlacedFeature(configuredFeatures.getOrThrow(ConfiguredFeaturesInstance.REMOVE_VEINS_KEY), List.copyOf(placement)));
    }


    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Constants.MODID, name));
    }
}

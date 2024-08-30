package com.oitsjustjose.geolosys.common.datagen;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.oitsjustjose.geolosys.common.utils.Constants;
import com.oitsjustjose.geolosys.common.world.worldgen.ConfiguredFeatures;
import com.oitsjustjose.geolosys.common.world.worldgen.PlacedFeatures;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

public class WorldGenProvider extends DatapackBuiltinEntriesProvider {
    public WorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, getBuilder(), Set.of(Constants.MODID));
    }

	private static RegistrySetBuilder getBuilder() {
		// It's beter when it's not in the memory all the runtime, i guess
		final ConfiguredFeatures ConfiguredFeaturesInstance = new ConfiguredFeatures();
		final PlacedFeatures PlacedFeaturesInstance = new PlacedFeatures(ConfiguredFeaturesInstance);
		return new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, ConfiguredFeaturesInstance::bootstrap)
			.add(Registries.PLACED_FEATURE, PlacedFeaturesInstance::bootstrap);
	}
}

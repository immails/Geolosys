package com.oitsjustjose.geolosys.common.world.worldgen;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.utils.Constants;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/** Instanced by {@link com.oitsjustjose.geolosys.common.datagen.WorldGenProvider#getBuilder() WorldGenProvider#getBuilder()}. Very mindful, very demure. Trying to be memory efficient.*/
public class ConfiguredFeatures {

	public final ResourceKey<ConfiguredFeature<?, ?>> DEPOSITS_KEY = registerKey("deposits");
	public final ResourceKey<ConfiguredFeature<?, ?>> REMOVE_VEINS_KEY = registerKey("remove_veins");

    public void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
		Geolosys.getInstance().LOGGER.info("Bootstrap of ConfiguredFeatures has been called");

		context.register(DEPOSITS_KEY, new ConfiguredFeature<>(Geolosys.getInstance().REGISTRY.deposits.get(), NoneFeatureConfiguration.INSTANCE));
		context.register(REMOVE_VEINS_KEY, new ConfiguredFeature<>(Geolosys.getInstance().REGISTRY.remove_veins.get(), NoneFeatureConfiguration.INSTANCE));
    }

	public ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Constants.MODID, name));
    }
}

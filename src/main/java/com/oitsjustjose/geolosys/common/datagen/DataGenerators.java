package com.oitsjustjose.geolosys.common.datagen;

import com.oitsjustjose.geolosys.common.utils.Constants;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();

		generator.addProvider(
			event.includeServer(), 
			new WorldGenProvider(generator.getPackOutput(), event.getLookupProvider())
		);
	}
}
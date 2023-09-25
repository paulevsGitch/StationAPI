package net.modificationstation.sltest.worldgen;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.biome.Forest;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.biome.BiomeRegisterEvent;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.ClimateBiomeProvider;
import net.modificationstation.stationapi.impl.worldgen.BiomeProviderRegistryEvent;

public class TestWorldgenListener {
	private Biome testBiome1;
	private Biome testBiome2;
	private Biome testBiome3;
	private Biome[] climateTest;
	
	@EventListener
	public void registerBiomes(BiomeRegisterEvent event) {
		SLTest.LOGGER.info("Register test biomes");
		testBiome1 = new Forest();
		testBiome2 = new Forest();
		testBiome3 = new Forest();
		testBiome1.grassColour = 0xFFFF0000;
		testBiome2.grassColour = 0xFFFFFF00;
		testBiome3.grassColour = 0xFFFF00FF;
		
		climateTest = new Biome[8];
		for (int i = 0; i < climateTest.length; i++) {
			climateTest[i] = new Forest();
			int r = i * 255 / climateTest.length;
			climateTest[i].grassColour = 0xFF000000 | r << 16 | r << 8 | 255;
		}
	}
	
	@EventListener
	public void registerRegions(BiomeProviderRegistryEvent event) {
		SLTest.LOGGER.info("Register test biome regions");
		
		// Add biome directly into default region
		BiomeAPI.addOverworldBiome(testBiome1, 0.3F, 0.7F, 0.3F, 0.7F);
		// Fancy borders example
		BiomeAPI.addOverworldBiome(testBiome3, 0.28F, 0.72F, 0.28F, 0.72F);
		
		// Simple climate provider with biomes by temperature
		ClimateBiomeProvider provider = new ClimateBiomeProvider();
		for (int i = 0; i < climateTest.length; i++) {
			float t1 = (float) i / climateTest.length;
			float t2 = (float) (i + 1) / climateTest.length;
			provider.addBiome(climateTest[i], t1, t2, 0, 1);
		}
		BiomeAPI.addOverworldBiomeProvider(StationAPI.MODID.id("climate_provider"), provider);
	}
}

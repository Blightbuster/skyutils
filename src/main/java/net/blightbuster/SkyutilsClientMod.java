package net.blightbuster;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.blightbuster.kiln.KilnScreen;

public class SkyutilsClientMod implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ScreenRegistry.register(SkyutilsMod.KILN_SCREEN_HANDLER, KilnScreen::new);
	}
}

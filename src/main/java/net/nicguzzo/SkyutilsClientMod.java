package net.nicguzzo;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.nicguzzo.kiln.KilnScreen;

public class SkyutilsClientMod implements ClientModInitializer {
	public static boolean skyblock = false;
	public static RegistryKey<ChunkGeneratorSettings> SKYBLOCK_FLOATING_ISLANDS;

	@Override
	public void onInitializeClient() {
		ScreenRegistry.register(SkyutilsMod.KILN_SCREEN_HANDLER, KilnScreen::new);
	}
}

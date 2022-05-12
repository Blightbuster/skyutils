package net.blightbuster;

import net.blightbuster.kiln.KilnScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class SkyutilsClientMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(SkyutilsMod.KILN_SCREEN_HANDLER, KilnScreen::new);
    }
}

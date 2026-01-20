package com.fredoseep;

import me.voidxwalker.autoreset.Atum;
import net.fabricmc.api.ClientModInitializer;

public class TreasureCheckerMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Atum.setSeedProvider(new TreasureSeedProvider());
    }
}
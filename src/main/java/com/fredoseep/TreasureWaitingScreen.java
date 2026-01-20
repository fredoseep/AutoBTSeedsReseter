package com.fredoseep;

import me.voidxwalker.autoreset.api.seedprovider.AtumWaitingScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TreasureWaitingScreen extends AtumWaitingScreen {

    protected TreasureWaitingScreen(Text title) {
        super(title);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.drawCenteredString(matrices, this.textRenderer, "Filtering a bt seed from random seeds", this.width / 2, this.height / 2 - 50, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
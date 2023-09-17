package dev.hilligans.client.graphics.screens;

import dev.hilligans.Settings;
import dev.hilligans.client.graphics.GlUtils;
import dev.hilligans.client.graphics.MatrixStack;
import dev.hilligans.client.graphics.Textures;

public class WaitingScreen extends Screen {

    @Override
    public void render(GlUtils glUtils, MatrixStack matrixStack) {
        matrixStack.push();
        matrixStack.setColor(1, 1f, 1);
        glUtils.stringRenderer.drawCenteredStringInternal(window, matrixStack, "Waiting for opponent to select an upgrade", (int) (this.window.getWindowWidth() / 2), 400, Settings.guiScale / 3f);
        matrixStack.pop();
    }
}

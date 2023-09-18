package dev.hilligans.client.graphics.screens;

import dev.hilligans.Main;
import dev.hilligans.Settings;
import dev.hilligans.client.graphics.GlUtils;
import dev.hilligans.client.graphics.MatrixStack;

public class InfoScreen extends Screen {



    @Override
    public void render(GlUtils glUtils, MatrixStack matrixStack) {
        matrixStack.push();

        glUtils.stringRenderer.drawCenteredStringInternal(window, matrixStack, "Loading", (int) (this.window.getWindowWidth() / 2), 0, Settings.guiScale / 4f);

        glUtils.stringRenderer.drawCenteredStringInternal(window, matrixStack, "WASD to move", (int) (this.window.getWindowWidth() / 2), 200, Settings.guiScale / 4f);
        glUtils.stringRenderer.drawCenteredStringInternal(window, matrixStack, "Mouse to point", (int) (this.window.getWindowWidth() / 2), 250, Settings.guiScale / 4f);
        glUtils.stringRenderer.drawCenteredStringInternal(window, matrixStack, "Space to shoot", (int) (this.window.getWindowWidth() / 2), 300, Settings.guiScale / 4f);
        glUtils.stringRenderer.drawCenteredStringInternal(window, matrixStack, "Shift to dash", (int) (this.window.getWindowWidth() / 2), 350, Settings.guiScale / 4f);


        glUtils.stringRenderer.drawCenteredStringInternal(window, matrixStack, "Tables and chairs can be shot through but not walked though", (int) (this.window.getWindowWidth() / 2), 500, Settings.guiScale / 4f);
        glUtils.stringRenderer.drawCenteredStringInternal(window, matrixStack, "Vending machines can neither be shot through or walked through", (int) (this.window.getWindowWidth() / 2), 550, Settings.guiScale / 4f);

        glUtils.stringRenderer.drawCenteredStringInternal(window, matrixStack, "You are", (int) (this.window.getWindowWidth() / 2), 700, Settings.guiScale / 4f);
        String name;
        if(Main.getClient().playerID == 1) {
            matrixStack.setColor(1f, 0, 0);
            name = "red";
        } else {
            matrixStack.setColor(0, 0, 1f);
            name = "blue";
        }
        glUtils.stringRenderer.drawCenteredStringInternal(window, matrixStack, name, (int) (this.window.getWindowWidth() / 2), 750, Settings.guiScale / 4f);


        matrixStack.pop();
    }
}

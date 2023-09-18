package dev.hilligans.client.graphics.screens;

import dev.hilligans.Main;
import dev.hilligans.Settings;
import dev.hilligans.client.graphics.*;
import dev.hilligans.client.graphics.Widgets.UpgradeWidget;
import dev.hilligans.upgrades.Upgrade;
import dev.hilligans.upgrades.Upgrades;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;

import java.util.ArrayList;

public class UpgradeSelectScreen extends Screen {

    UpgradeWidget widget = new UpgradeWidget(10, 10, 48, 64, Upgrades.GARLIC);
    UpgradeWidget widget1 = new UpgradeWidget(10, 10, 48, 64, Upgrades.CARROT);

    public UpgradeSelectScreen() {
        window = Main.main.window;

        Upgrade[] upgrades = Upgrades.getRandomUpgrades();
        int xoffset = (int) ((1920 - (upgrades.length * 80 * Settings.guiScale)) / 2);
        for(int x= 0; x < upgrades.length; x++) {
            Widget widget2 = new UpgradeWidget((int) (x * 80 * Settings.guiScale) + xoffset, (int) (window.getWindowHeight() / 2 - 128), 48, 64, upgrades[x]);
            addWidget(widget2);
        }
    }

    @Override
    public void render(GlUtils glUtils, MatrixStack matrixStack) {
        matrixStack.push();
        matrixStack.setColor(0.7f, 0, 0.7f);
        glUtils.stringRenderer.drawCenteredStringInternal(window, matrixStack, "Select an upgrade", (int) (this.window.getWindowWidth() / 2), 0, Settings.guiScale / 4f);
        matrixStack.pop();

        for(Widget upgradeWidget : widgets) {
            upgradeWidget.render(glUtils, matrixStack, 0, 0);
        }
    }
}

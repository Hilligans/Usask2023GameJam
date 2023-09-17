package dev.hilligans.client.graphics.screens;

import dev.hilligans.Settings;
import dev.hilligans.client.graphics.GlUtils;
import dev.hilligans.client.graphics.MatrixStack;
import dev.hilligans.client.graphics.Textures;
import dev.hilligans.client.graphics.Widget;
import dev.hilligans.client.graphics.Widgets.UpgradeWidget;
import dev.hilligans.upgrades.Upgrade;
import dev.hilligans.upgrades.Upgrades;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;

import java.util.ArrayList;

public class UpgradeSelectScreen extends Screen {

    ArrayList<UpgradeWidget> upgrades = new ArrayList<>();
    UpgradeWidget widget = new UpgradeWidget(10, 10, 48, 64, Upgrades.GARLIC);
    UpgradeWidget widget1 = new UpgradeWidget(10, 10, 48, 64, Upgrades.CARROT);

    public UpgradeSelectScreen() {
        for(int x= 0; x < 5; x++) {
            upgrades.add(new UpgradeWidget(0, 0, 48, 64, Upgrades.GARLIC));
        }
    }



    @Override
    public void render(GlUtils glUtils, MatrixStack matrixStack) {

        Matrix4f rotMatrix = new Matrix4f().translate(0, 0, 0).rotateZ(-0.2f);
        matrixStack.push();
        //matrixStack.translate(0, -50, 0);
        int x = (int) (window.getWindowWidth() / 2 - 64 * Settings.guiScale * 2);
        for(UpgradeWidget upgradeWidget : upgrades) {
            matrixStack.push();
            matrixStack.matrix4f.mul(rotMatrix);
            upgradeWidget.render(glUtils, matrixStack, x, 400);
            x += 64 * Settings.guiScale;
            rotMatrix.rotateLocalZ(0.1f);
            rotMatrix.translate(0, -60, 0);
            matrixStack.pop();;
        }
        matrixStack.pop();
        //Textures.ARROW.drawTexture(glUtils, matrixStack, 0, 0, 50, 50);

    }
}

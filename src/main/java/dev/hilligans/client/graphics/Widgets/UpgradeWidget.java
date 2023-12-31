package dev.hilligans.client.graphics.Widgets;

import dev.hilligans.Main;
import dev.hilligans.Settings;
import dev.hilligans.client.graphics.*;
import dev.hilligans.client.graphics.screens.WaitingScreen;
import dev.hilligans.network.Packet.Client.CSelectUpgradePacket;
import dev.hilligans.upgrades.Upgrade;

import java.util.ArrayList;

public class UpgradeWidget extends Widget {

    public Upgrade upgrade;

    public UpgradeWidget(int x, int y, int width, int height, Upgrade upgrade) {
        super(x, y, width, height);
        this.upgrade = upgrade;
    }


    @Override
    public void render(GlUtils glUtils, MatrixStack matrixStack, int xOffset, int yOffset) {
        super.render(glUtils, matrixStack, xOffset, yOffset);
        float guiScale = Settings.guiScale;

        Textures.UPGRADE_BACKGROUND.drawTexture(glUtils, matrixStack, x + xOffset, y + yOffset, (int) (width * Settings.guiScale), (int) (height * Settings.guiScale));
        matrixStack.push();
        matrixStack.setColor(0.7f, 0, 0.7f);
        glUtils.stringRenderer.drawCenteredStringInternal(window, matrixStack, upgrade.upgradeName, (int) (x + xOffset + width * guiScale / 2), y + yOffset- 64, guiScale/4);
        matrixStack.pop();
        int yy = (int) (this.y + yOffset + 4 * guiScale);
        upgrade.texture.drawTexture(glUtils, matrixStack, (int) (x + xOffset + 16 * guiScale), yy, (int) ((width / 2) * guiScale / 1.5f), (int) ((height / 2) * guiScale / 1.5f));
        yy += ((height / 2) * guiScale / 1.5f);
        Textures.DIVIDER.drawTexture(glUtils, matrixStack, (int) (x + xOffset + 3 * guiScale), yy, (int) ((width - 6) * guiScale), (int) (1 * guiScale / 2));
        yy += (int) (1 * guiScale / 2);
        matrixStack.push();
        matrixStack.setColor(1f, 0.3f, 0.3f);
        ArrayList<String> lines = upgrade.getLines();
        for(String s : lines) {
            glUtils.stringRenderer.drawCenteredStringInternal(window, matrixStack, s, (int) (x + xOffset + width * guiScale / 2), yy, guiScale / 12);
            yy += 4 * guiScale;
        }
        matrixStack.pop();
    }

    @Override
    public void activate(int x, int y) {
        Main.main.renderer.openScreen = new WaitingScreen();
        Main.getClient().network.sendPacket(new CSelectUpgradePacket(upgrade, Main.getClient().playerID));
    }
}

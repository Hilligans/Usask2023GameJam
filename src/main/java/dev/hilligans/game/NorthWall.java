package dev.hilligans.game;

import dev.hilligans.Settings;
import dev.hilligans.client.graphics.GlUtils;
import dev.hilligans.client.graphics.MatrixStack;
import dev.hilligans.client.graphics.Textures;
import dev.hilligans.client.graphics.screens.GameScreen;

import static dev.hilligans.client.graphics.screens.GameScreen.gameHeight;

public class NorthWall extends Entity {

    int w = (int) (GameScreen.gameWidth * 16 * Settings.guiScale);
    int h = 256;

    public NorthWall() {
        this.x = -w / 2f;
        this.z = -((gameHeight * Settings.guiScale * 16 - 500) + 256);

    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(x, z, w, h);
    }

    @Override
    public void render(GlUtils glUtils, MatrixStack matrixStack) {
        Textures.NORTH_WALL.drawTexture1(glUtils, matrixStack, (int) x, (int) y, (int) z, w, h);
    }

    @Override
    public int getEntityType() {
        return -1;
    }

    @Override
    boolean isImmoveable() {
        return true;
    }
}

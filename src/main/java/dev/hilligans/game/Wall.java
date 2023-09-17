package dev.hilligans.game;

import dev.hilligans.client.graphics.GlUtils;
import dev.hilligans.client.graphics.MatrixStack;
import dev.hilligans.client.graphics.Textures;
import dev.hilligans.client.graphics.Window;

public class Wall extends Entity {

    public Wall() {

    }

    public Wall(int x, int z) {
        this.x = x * 64;
        this.z = z * 64;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(x, z, 64, 64);
    }

    @Override
    public int getEntityType() {
        return 1;
    }

    @Override
    boolean isImmoveable() {
        return true;
    }

    @Override
    public void render(GlUtils glUtils, MatrixStack matrixStack) {
        Textures.WALL.drawTexture1(glUtils, matrixStack, (int) x, (int) y, (int) z, size, size);
    }
}

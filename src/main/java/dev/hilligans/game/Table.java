package dev.hilligans.game;

import dev.hilligans.Settings;
import dev.hilligans.client.graphics.GlUtils;
import dev.hilligans.client.graphics.MatrixStack;
import dev.hilligans.client.graphics.Texture;
import dev.hilligans.client.graphics.Textures;

public class Table extends Entity {

    Texture texture;
    boolean fullySolid;

    public Table(Texture texture, boolean fullySolid) {
        this.texture = texture;
        this.fullySolid = fullySolid;
    }

    public Table(Texture texture) {
        this.texture = texture;
        this.fullySolid = false;
    }

    float getW() {
        return (int) (texture.width * Settings.guiScale);
    }

    float getH() {
        return (int) (texture.height * Settings.guiScale);
    }

    float getX() {
        return (int) (x - getW()/2);
    }

    float getZ() {
        return (int) (z - getH()/2);
    }

    @Override
    boolean primary() {
        return false;
    }

    @Override
    boolean secondary() {
        return true;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(getX(), getZ(), getW(), getH());
    }

    @Override
    public void render(GlUtils glUtils, MatrixStack matrixStack) {
        texture.drawTexture1(glUtils, matrixStack, (int) getX(), (int) y, (int) getZ(), (int) getW(), (int) getH());
    }

    @Override
    public int getEntityType() {
        return -1;
    }

    @Override
    boolean isImmoveable() {
        return fullySolid;
    }

    @Override
    boolean isSolid() {
        return true;
    }
}

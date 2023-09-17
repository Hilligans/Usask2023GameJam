package dev.hilligans.game;

import dev.hilligans.Settings;
import dev.hilligans.client.graphics.GlUtils;
import dev.hilligans.client.graphics.MatrixStack;
import dev.hilligans.client.graphics.Texture;
import dev.hilligans.network.PacketData;

import java.util.ArrayList;

public class LifetimeEntity extends Entity {

    public int lifetime;
    public Texture texture;

    public LifetimeEntity(Texture texture, int lifetime) {
        this.texture = texture;
        this.lifetime = lifetime;
    }


    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(0,0,0,0);
    }

    @Override
    public int getEntityType() {
        return -2;
    }

    @Override
    public void write(PacketData packetData) {
        packetData.writeString(texture.getIdentifierName());
    }

    @Override
    public void tick(Game game, ArrayList<Entity> movedEntities) {
        lifetime -= 1;
        if(lifetime < 0) {
            dead = true;
        }
    }

    @Override
    public void render(GlUtils glUtils, MatrixStack matrixStack) {
        int factor = (int) Settings.guiScale;
        texture.drawTexture1(glUtils, matrixStack, (int) x, (int) y, (int) z, texture.width * factor, texture.height * factor);
    }
}

package dev.hilligans.game;

import dev.hilligans.client.graphics.GlUtils;
import dev.hilligans.client.graphics.MatrixStack;
import dev.hilligans.client.graphics.Texture;
import dev.hilligans.network.PacketData;

import java.util.ArrayList;

public abstract class Entity {

    public float x;
    public float y;
    public float z;

    public float velX;
    public float velY;
    public float velZ;

    public int size = 64;

    public int entityID;

    public boolean dead;

    public Entity setPos(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Entity setVelocity(float velX, float velZ) {
        this.velX = velX;
        this.velZ = velZ;
        return this;
    }

    public abstract BoundingBox getBoundingBox();

    public abstract int getEntityType();

    public void write(PacketData packetData) {}

    boolean isImmoveable() {
        return false;
    }

    boolean isSolid() {
        return false;
    }

    boolean primary() {
        return true;
    }

    boolean secondary() {
        return false;
    }

    public void render(GlUtils glUtils, MatrixStack matrixStack) {
    }

    public void tick(Game game, ArrayList<Entity> movedEntities) {
    }
}

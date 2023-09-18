package dev.hilligans.game;

import dev.hilligans.Main;
import dev.hilligans.Settings;
import dev.hilligans.client.graphics.GlUtils;
import dev.hilligans.client.graphics.MatrixStack;
import dev.hilligans.client.graphics.Texture;
import dev.hilligans.network.Packet.Server.SPlayerInfoPacket;
import dev.hilligans.network.PacketData;
import dev.hilligans.upgrades.Upgrade;
import dev.hilligans.upgrades.Upgrades;

import java.util.ArrayList;

public class Projectile extends Entity {

    Player owner;
    Texture texture;
    public int upgradeID;
    int life = 100;

    public Projectile(Player owner, int upgradeID) {
        this.owner = owner;
        this.upgradeID = upgradeID;
        texture = Upgrades.getUpgrade(upgradeID).texture;
    }

    public Projectile(PacketData packetData, Game game) {
        this(game.getPlayer(packetData.readVarInt()), packetData.readVarInt());
    }

    @Override
    public void write(PacketData packetData) {
        packetData.writeVarInt(owner.playerID);
        packetData.writeVarInt(upgradeID);
    }

    @Override
    public BoundingBox getBoundingBox() {
       return new BoundingBox(x + 8, z + 8, 48, 48);
        // return Texture.;
    }

    @Override
    public int getEntityType() {
        return 2;
    }

    @Override
    public void render(GlUtils glUtils, MatrixStack matrixStack) {
        Texture texture1 = owner.icon.texture;
        int factor = (int) Settings.guiScale;
        texture.drawTexture1(glUtils, matrixStack, (int) x, (int) y, (int) z, texture1.width * factor, texture1.height * factor);
    }

    public static final float slowdown = 0.98f;

    @Override
    public void tick(Game game, ArrayList<Entity> movedEntities) {
        life -= 1;
        if(life < 0) {
            dead = true;
            return;
        }

        x += velX;
        y += velY;
        z += velZ;

        velX *= slowdown;
        velY *= slowdown;
        velZ *= slowdown;

        Entity entity = collidesWithObject(game, this);
        if(entity != null) {
            if(entity instanceof Player player) {
                player.health -= Math.max(1, owner.damage);
                game.sendPacketToAll(new SPlayerInfoPacket(player));
                owner.onHit(game, player);
                dead = true;
            } else if(entity.isImmoveable()) {
                //destroy
                dead = true;
            } else {
                if(velX != 0 || velY != 0 || velZ != 0) {
                    movedEntities.add(this);
                }
            }
        } else {
            if(velX != 0 || velY != 0 || velZ != 0) {
                movedEntities.add(this);
            }
        }
    }

    public Entity collidesWithObject(Game game, Entity entity) {
        BoundingBox boundingBox = entity.getBoundingBox();
        synchronized (game.entityList) {
            for (Entity entity1 : game.entityList) {
                if (entity1 == owner) {
                    continue;
                }
                if(entity == entity1) {
                    continue;
                }
                if (entity1.getBoundingBox().intersects(boundingBox)) {
                    return entity1;
                }
            }
        }
        return null;
    }

}

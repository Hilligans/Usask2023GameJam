package dev.hilligans.game;

import dev.hilligans.Settings;
import dev.hilligans.client.graphics.GlUtils;
import dev.hilligans.client.graphics.MatrixStack;
import dev.hilligans.client.graphics.Texture;
import dev.hilligans.client.graphics.Textures;
import dev.hilligans.network.Network;
import dev.hilligans.network.PacketBase;
import dev.hilligans.network.PacketData;
import dev.hilligans.upgrades.Upgrade;
import dev.hilligans.upgrades.Upgrades;
import io.netty.channel.ChannelHandlerContext;
import org.joml.Vector2f;

import java.util.ArrayList;

public class Player extends Entity {

    public int playerID;
    public boolean female = true;
    public boolean flipped;
    public int health;

    public ArrayList<Upgrade> upgrades = new ArrayList<>();
    public Upgrade icon;

    public int damage;
    public float speed = 1;

    public int projectileCooldown = 0;



    ChannelHandlerContext ctx;

    public Player(int playerID) {
        addUpgrade(Upgrades.TOMATO);
        this.playerID = playerID;
        health = 100;
    }

    public Vector2f calculateShootAngle(float mouseX, float mouseY) {
        float difX = (int) (this.x - mouseX) + 5 * Settings.guiScale;
        float difY = this.z - mouseY + 10 * Settings.guiScale;
        double val = Math.atan(difY / difX);

        if(difX < 0) {
            return new Vector2f((float) Math.cos(val), (float) Math.sin(val));
        } else {
            return new Vector2f((float) -Math.cos(val), (float) -Math.sin(val));
        }
    }

    public Player setNetwork(ChannelHandlerContext network) {
        this.ctx = network;
        return this;
    }


    public Player addUpgrade(Upgrade upgrade) {
        this.upgrades.add(upgrade);
        this.damage += upgrade.damage;
        this.health += upgrade.health;
        this.speed *= upgrade.speed;

        this.icon = upgrade;
        return this;
    }

    @Override
    public void tick(Game game, ArrayList<Entity> movedEntities) {
        if(projectileCooldown > 0) {
            projectileCooldown -= 1;
        }
    }

    public Entity getProjectile(float velX, float velZ) {
        if(projectileCooldown != 0) {
            return null;
        }
        projectileCooldown = 20;
        float factor = Settings.guiScale * 6;
        return new Projectile(this).setVelocity(velX * factor, velZ * factor).setPos(x, y, z);
    }


    public void setFromPlayer(Player player) {
        this.x = player.x;
        this.y = player.y;
        this.z = player.z;

        this.health = player.health;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(x, z + 4 * Settings.guiScale, 11 * Settings.guiScale, 64 + 4 * Settings.guiScale);
    }

    @Override
    public int getEntityType() {
        return 0;
    }

    public void render(GlUtils glUtils, MatrixStack matrixStack) {
        Texture texture;
        int factor = (int) Settings.guiScale;
        float x = this.x;
        if(!female) {
            texture = Textures.MALE_HEAD;
        } else {
            texture = Textures.FEMALE_HEAD;
            if(!flipped) {
                x -= 3 * factor;
            }
        }
        if(flipped) {
            texture.drawTexture1(glUtils, matrixStack, (int) x, (int) y, (int) z, texture.width * factor, texture.height * factor);
        } else {
            texture.drawTexture1f(glUtils, matrixStack, (int) x, (int) y, (int) z, texture.width * factor, texture.height * factor);
        }

        if(playerID == 1) {
            texture = Textures.RED_BODY;
        } else {
            texture = Textures.BLUE_BODY;
        }
        if(flipped) {
            texture.drawTexture1(glUtils, matrixStack, (int) this.x, (int) y, (int) z + 64, texture.width * factor, texture.height * factor);
        } else {
            texture.drawTexture1f(glUtils, matrixStack, (int) this.x, (int) y, (int) z + 64, texture.width * factor, texture.height * factor);
        }

        Textures.RED.drawTexture1(glUtils, matrixStack, (int) this.x, (int) y, (int) (z + factor), texture.width * factor, factor);
        Textures.GREEN.drawTexture1(glUtils, matrixStack, (int) this.x, (int) y, (int) (z + factor), (int) ((texture.width * factor) * (health / 100f)), factor);


        //Textures.ARROW.drawTexture1(glUtils, matrixStack, (int) x, (int) y, (int) z, 64, 64);
    }

    public void sendPacket(PacketBase packetBase) {
        ctx.channel().writeAndFlush(new PacketData(packetBase));
    }
}

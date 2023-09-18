package dev.hilligans.game;

import dev.hilligans.Main;
import dev.hilligans.Settings;
import dev.hilligans.client.graphics.GlUtils;
import dev.hilligans.client.graphics.MatrixStack;
import dev.hilligans.client.graphics.Texture;
import dev.hilligans.client.graphics.Textures;
import dev.hilligans.client.graphics.screens.GameScreen;
import dev.hilligans.network.Network;
import dev.hilligans.network.Packet.Server.SPlayerInfoPacket;
import dev.hilligans.network.Packet.Server.SSpecialEffect;
import dev.hilligans.network.PacketBase;
import dev.hilligans.network.PacketData;
import dev.hilligans.upgrades.Upgrade;
import dev.hilligans.upgrades.Upgrades;
import io.netty.channel.ChannelHandlerContext;
import org.joml.Vector2f;

import java.util.ArrayList;

public class Player extends Entity {

    public int playerID;
    public boolean female = false;
    public boolean flipped;
    public int maxHealth = 100;
    public int health;

    public int maxShots = 3;
    public int shots;
    public float reloadSpeed = 1;
    public int reloadTime = 0;

    public ArrayList<Upgrade> upgrades = new ArrayList<>();
    public Upgrade icon;

    public int damage;
    public float speed = 1;
    public float temporarySpeedModifierUpgrade = 1;

    public int projectileCooldown = 0;

    public float temporarySpeedModifier = 1;


    public static final int TIME_TO_RELOAD = 50;

    ChannelHandlerContext ctx;

    public Player(int playerID) {
        addUpgrade(Upgrades.TOMATO);
        this.playerID = playerID;
        health = maxHealth;
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
        this.maxHealth += upgrade.health;
        this.health = this.maxHealth;
        this.speed *= upgrade.speed;
        this.temporarySpeedModifierUpgrade *= upgrade.temporarySpeedModifier;
        this.temporarySpeedModifierUpgrade = Math.max(0.2f, temporarySpeedModifierUpgrade);
        this.maxShots += upgrade.reloadCount;
        this.reloadSpeed *= upgrade.reloadSpeed;

        if(this.icon == null || upgrade.tier > icon.tier) {
            this.icon = upgrade;
        }
        return this;
    }

    public void onHit(Game game, Player hitPlayer) {
        if(temporarySpeedModifierUpgrade != 1) {
            game.sendPacketToAll(new SSpecialEffect(hitPlayer.playerID, temporarySpeedModifierUpgrade));
        }
    }

    @Override
    public void tick(Game game, ArrayList<Entity> movedEntities) {
        if(projectileCooldown > 0) {
            projectileCooldown -= 1;
        }
        if(shots != getMaxShots()) {
            reloadTime++;
            if(reloadTime > reloadSpeed * TIME_TO_RELOAD) {
                reloadTime = 0;
                shots++;
                game.sendPacketToAll(new SPlayerInfoPacket(this));
            }
        }
    }

    public int getMaxShots() {
        return maxShots;
    }

    public Entity getProjectile(float velX, float velZ) {
        float factor = Settings.guiScale * 6;
        return new Projectile(this, icon.upgradeID).setVelocity(velX * factor, velZ * factor).setPos(x, y, z);
    }


    public void setFromPlayer(Player player, boolean server) {
        if(!server) {
            if(Main.main.renderer.openScreen instanceof GameScreen gameScreen) {
                if(gameScreen.started) {
                    if(player.playerID != Main.getClient().playerID) {
                        this.x = player.x;
                        this.y = player.y;
                        this.z = player.z;
                    }
                } else {
                    this.x = player.x;
                    this.y = player.y;
                    this.z = player.z;
                }
            } else {
                this.x = player.x;
                this.y = player.y;
                this.z = player.z;
            }
        } else {
            this.x = player.x;
            this.y = player.y;
            this.z = player.z;
        }
        if(player.shots != 0) {
            this.shots = player.shots;
        }

        if(player.health != -1000) {
            this.health = player.health;
        }

        if(player.maxHealth != 0) {
            this.maxHealth = player.maxHealth;
        }
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
        Textures.GREEN.drawTexture1(glUtils, matrixStack, (int) this.x, (int) y, (int) (z + factor), (int) ((texture.width * factor) * (health / (float)maxHealth)), factor);

        Texture texture1;
        if(playerID == 1) {
            texture = Textures.RED_SHOT_FULL;
            texture1 = Textures.RED_SHOT_EMPTY;
        } else {
            texture = Textures.BLUE_SHOT_FULL;
            texture1 = Textures.BLUE_SHOT_EMPTY;
        }

        int width = texture.width * factor / 4;
        int count = getMaxShots();
        int r = shots;
        int z = -1;
        while(count > 0) {
            for (int a = 0; a < 6; a++) {
                if (r != 0) {
                    texture.drawTexture1(glUtils, matrixStack, (int) this.x + width * a, (int) y, (int) (this.z + factor * z), width, texture.height * factor / 4);
                    r--;
                    count--;
                } else if(count != 0) {
                    texture1.drawTexture1(glUtils, matrixStack, (int) this.x + width * a, (int) y, (int) (this.z + factor * z), width, texture.height * factor / 4);
                    count--;
                }
            }
            z--;
        }
        //Textures.ARROW.drawTexture1(glUtils, matrixStack, (int) x, (int) y, (int) z, 64, 64);
    }

    public void sendPacket(PacketBase packetBase) {
        ctx.channel().writeAndFlush(new PacketData(packetBase));
    }
}

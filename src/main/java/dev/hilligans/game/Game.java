package dev.hilligans.game;

import dev.hilligans.Main;
import dev.hilligans.Settings;
import dev.hilligans.client.graphics.GlUtils;
import dev.hilligans.client.graphics.MatrixStack;
import dev.hilligans.client.graphics.Textures;
import dev.hilligans.network.Packet.Client.CUpdatePositionPacket;
import dev.hilligans.network.Packet.Server.SPlayerInfoPacket;
import dev.hilligans.network.Packet.Server.SRemoveEntityPacket;
import dev.hilligans.network.Packet.Server.SSendEntityPositionPacket;
import dev.hilligans.network.Packet.Server.SSendTimePacket;
import dev.hilligans.network.PacketBase;
import dev.hilligans.network.PacketData;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {

    public Player player1 = new Player(1);
    public Player player2 = new Player(2);

    public AtomicInteger entityID = new AtomicInteger(2);

    public final Int2ObjectOpenHashMap<Entity> entities = new Int2ObjectOpenHashMap<>();
    public final ArrayList<Entity> entityList = new ArrayList<>();

    public int time;

    public static int TICKS_PER_SECOND = 50;
    public static int MATCH_TIME = 60;

    public Game() {
        player1.setPos(-800, 0, 0);
        player2.setPos(800, 0, 0);
        synchronized (entityList) {
            entityList.add(player1);
            entityList.add(player2);
        }
        synchronized (entities) {
            entities.put(0, player1);
            entities.put(1, player2);
        }
        addEntity(new NorthWall());
        addEntity(new Table(Textures.HORIZONTAL_TABLE));

        addEntity(new Table(Textures.CHAIR).setPos(0, 0, 250));

        addEntity(new Table(Textures.VENDING_MACHINE, true).setPos(-500, 0, 56));
        addEntity(new Table(Textures.VENDING_MACHINE, true).setPos(500, 0, 56));

        addEntity(new Table(Textures.CHAIR, false).setPos(500, 0,325));

        int width = (int) (22 * Settings.guiScale);
        int height = (int) (32 * Settings.guiScale);

        addEntity(new Table(Textures.VENDING_MACHINE, true).setPos(500 + width, 0, 325 + height));


        addEntity(new Table(Textures.CHAIR).setPos(-500 - width * 4, 0, 56 - height * 2));
        addEntity(new Table(Textures.CHAIR).setPos(-500 - width * 3, 0, 56 - height));


        addEntity(new Wall1(-1020, -600, 60, 1200));
        addEntity(new Wall1(960, -600, 60, 1200));
        addEntity(new Wall1(-1100, 500, 2200, 100));
    }

    public Player getPlayer(int id) {
        if(id == 1) {
            return player1;
        } else if(id == 2) {
            return player2;
        } else {
            throw new RuntimeException();
        }
    }


    /*
    0 normal
    1 player1 died
    2 player2 died
    3 draw
     */
    public int checkState() {
        int ret = player1.health > 0 ? 0 : 1;
        ret += player2.health > 0 ? 0 : 2;
        return ret;
    }

    public boolean time() {
        time += 1;
        if(time > TICKS_PER_SECOND * MATCH_TIME) {
            return true;
        }
        if(time % TICKS_PER_SECOND == 0) {
            sendPacketToAll(new SSendTimePacket(time));
        }
        return false;
    }

    public void reset() {
        time = 0;
        sendPacketToAll(new SSendTimePacket(0));

        ArrayList<Entity> entities1 = new ArrayList<>();
        synchronized (entities) {
            synchronized (entityList) {
                entityList.removeIf(entity -> {
                    if (entity instanceof Projectile) {
                        entities.remove(entity.entityID);
                        entities1.add(entity);
                        entities1.add(entity);
                        return true;
                    }
                    return false;
                });
            }
        }
        if(entities1.size() != 0) {
            sendPacketToAll(new SRemoveEntityPacket(entities1));
        }
    }

    public void tickEntities() {
        ArrayList<Entity> movedEntities = new ArrayList<>();
        ArrayList<Entity> entities1 = new ArrayList<>();
        synchronized (entities) {
            synchronized (entityList) {
                for (Entity entity : entityList) {
                    entity.tick(this, movedEntities);
                }
                entityList.removeIf(entity -> {
                    if (entity.dead) {
                        entities.remove(entity.entityID);
                        entities1.add(entity);
                        return true;
                    }
                    return false;
                });
            }
        }
        if(entities1.size() != 0) {
            sendPacketToAll(new SRemoveEntityPacket(entities1));
        }
        if(movedEntities.size() != 0) {
            sendPacketToAll(new SSendEntityPositionPacket(movedEntities));
        }
    }

    public void sendPacketToAll(PacketBase packetBase) {
        if(player1.ctx != null) {
            player1.ctx.channel().writeAndFlush(new PacketData(packetBase));
        }

        if(player2.ctx != null) {
            player2.ctx.channel().writeAndFlush(new PacketData(packetBase));
        }
    }

    public void addEntity(Entity entity) {
        int id = entityID.getAndIncrement();
        entity.entityID = id;
        synchronized (entities) {
            entities.put(id, entity);
        }
        synchronized (entityList) {
            entityList.add(entity);
        }
    }

    public void addEntityDirect(Entity entity) {
        synchronized (entities) {
            entities.put(entity.entityID, entity);
        }
        synchronized (entityList) {
            entityList.add(entity);
        }
    }

    public void removeEntity(int entityID) {
        synchronized (entities) {
            synchronized (entityList) {
                entityList.remove(entities.remove(entityID));
            }
        }
    }

    public void updatePlayer(Player newData, boolean server) {
        if(newData.playerID == 1) {
            player1.setFromPlayer(newData, server);
            if(server) {
                if(player2.ctx != null) {
                    player2.ctx.channel().writeAndFlush(new PacketData(new SPlayerInfoPacket(player1)));
                }
            }
        } else if(newData.playerID == 2) {
            player2.setFromPlayer(newData, server);
            if(server) {
                player1.ctx.channel().writeAndFlush(new PacketData(new SPlayerInfoPacket(player2)));
            }
        } else {
            throw new RuntimeException("Player " + newData.playerID + " just tried to move");
        }
    }

    public void renderEntities(GlUtils glUtils, MatrixStack matrixStack) {
        synchronized (entities) {
            for (Entity entity : entities.values()) {
                if(entity.secondary()) {
                    entity.render(glUtils, matrixStack);
                }
            }
            for (Entity entity : entities.values()) {
                if(entity.primary()) {
                    entity.render(glUtils, matrixStack);
                }
            }
        }
    }

    public Entity collidesWithObject(Entity entity) {
        BoundingBox boundingBox = entity.getBoundingBox();
        synchronized (entityList) {
            for (Entity entity1 : entityList) {
                if(entity1 == entity) {
                    continue;
                }
                if (entity1.isSolid() && entity1.getBoundingBox().intersects(boundingBox)) {
                    return entity1;
                }
            }
        }
        return null;
    }

    public void movePlayer(int playerID, float x, float z) {

        Player player = null;
        if(playerID == 1) {
            player = player1;
        } else if(playerID == 2) {
            player = player2;
        }
        if(player != null) {
            player.x += x;
            player.z += z;
            if (collidesWithObject(player) != null) {
                player.x -= x;
                player.z -= z;
            } else {
                Main.getClient().network.sendPacketDirect(new CUpdatePositionPacket(player));
            }
            if (x > 0) {
                player.flipped = false;
            } else if (x < 0) {
                player.flipped = true;
            }
        }
    }
}

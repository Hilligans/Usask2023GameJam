package dev.hilligans.network.Packet.Server;

import dev.hilligans.GameInstance;
import dev.hilligans.Main;
import dev.hilligans.game.Entity;
import dev.hilligans.game.Game;
import dev.hilligans.game.LifetimeEntity;
import dev.hilligans.game.Projectile;
import dev.hilligans.network.PacketBase;
import dev.hilligans.network.PacketData;

public class SCreateEntity extends PacketBase {

    public Entity entity;

    public SCreateEntity() {
        super(10);
    }

    public SCreateEntity(Entity entity) {
        this();
        this.entity = entity;
    }


    @Override
    public void encode(PacketData packetData) {
        packetData.writeVarInt(entity.getEntityType());
        packetData.writeVarInt(entity.entityID);

        entity.write(packetData);

        packetData.writeFloat(entity.x);
        packetData.writeFloat(entity.y);
        packetData.writeFloat(entity.z);
    }

    @Override
    public void decode(PacketData packetData) {
        int type = packetData.readVarInt();
        int id = packetData.readVarInt();

        if(type == 2) {
            entity = new Projectile(packetData, Main.getClient().game);
        } else if(type == -2) {
            entity = new LifetimeEntity(Main.main.gameInstance.TEXTURES.get(packetData.readString()), 0);
        } else {
            System.out.println("Entity Type: " + type);
            System.out.println("ID " + id);
            throw new RuntimeException();
        }

        entity.entityID = id;
        entity.setPos(packetData.readFloat(), packetData.readFloat(), packetData.readFloat());

    }

    @Override
    public void handle() {
        Main.getClient().game.addEntityDirect(entity);
    }
}

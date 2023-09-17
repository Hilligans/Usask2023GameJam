package dev.hilligans.network.Packet.Server;

import dev.hilligans.Main;
import dev.hilligans.game.Entity;
import dev.hilligans.network.PacketBase;
import dev.hilligans.network.PacketData;

import java.util.ArrayList;

public class SRemoveEntityPacket extends PacketBase {

    public ArrayList<Entity> entities;

    public int[] entitiesArr;

    public SRemoveEntityPacket() {
        super(7);
    }

    public SRemoveEntityPacket(ArrayList<Entity> entities) {
        this();
        this.entities = entities;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeVarInt(entities.size());
        for(Entity entity : entities) {
            packetData.writeVarInt(entity.entityID);
        }
    }

    @Override
    public void decode(PacketData packetData) {
        entitiesArr = new int[packetData.readVarInt()];
        for(int x = 0; x < entitiesArr.length; x++) {
            entitiesArr[x] = packetData.readVarInt();
        }
    }

    @Override
    public void handle() {
        for(int a : entitiesArr) {
            Main.getClient().game.removeEntity(a);
        }
    }
}

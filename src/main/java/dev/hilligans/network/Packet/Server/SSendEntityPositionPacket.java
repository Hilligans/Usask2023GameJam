package dev.hilligans.network.Packet.Server;

import dev.hilligans.Main;
import dev.hilligans.game.Entity;
import dev.hilligans.game.Game;
import dev.hilligans.network.PacketBase;
import dev.hilligans.network.PacketData;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;

public class SSendEntityPositionPacket extends PacketBase {

    public ArrayList<Entity> entities;

    public SSendEntityPositionPacket() {
        super(8);
    }

    public SSendEntityPositionPacket(ArrayList<Entity> entities) {
        this();
        this.entities = entities;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeVarInt(entities.size());
        for(Entity entity : entities) {
            packetData.writeVarInt(entity.entityID);
            packetData.writeFloat(entity.x);
            packetData.writeFloat(entity.y);
            packetData.writeFloat(entity.z);
        }
    }

    @Override
    public void decode(PacketData packetData) {
        int length = packetData.readVarInt();
        Game game = Main.getClient().game;
        Int2ObjectOpenHashMap<Entity> entities = game.entities;
        synchronized (entities) {
        for(int x = 0; x < length; x++) {
                //System.out.println("yes");
                entities.get(packetData.readVarInt()).setPos(packetData.readFloat(), packetData.readFloat(), packetData.readFloat());
            }
        }
    }

    @Override
    public void handle() {
    }
}

package dev.hilligans.network.Packet.Server;

import dev.hilligans.Main;
import dev.hilligans.network.PacketBase;
import dev.hilligans.network.PacketData;

public class SSpecialEffect extends PacketBase {


    int type = 0;
    int playerID = 0;

    float speed = 1;

    public SSpecialEffect() {
        super(16);

    }

    public SSpecialEffect(int playerID, float speedEffect) {
        this();
        type = 1;
        this.playerID = playerID;
        this.speed = speedEffect;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeVarInt(type);
        packetData.writeVarInt(playerID);
        if(type == 1) {
            packetData.writeFloat(speed);
        }
    }

    @Override
    public void decode(PacketData packetData) {
        type = packetData.readVarInt();
        playerID = packetData.readVarInt();
        if(type == 1) {
            speed = packetData.readFloat();
        }
    }

    @Override
    public void handle() {
        if(type == 1) {
            Main.main.client.game.getPlayer(playerID).temporarySpeedModifier = speed;
        }
    }
}

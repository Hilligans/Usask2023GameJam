package dev.hilligans.network.Packet.Server;

import dev.hilligans.GameInstance;
import dev.hilligans.Main;
import dev.hilligans.network.PacketBase;
import dev.hilligans.network.PacketData;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class SHandshakePacket extends PacketBase {

    public int playerId;

    public SHandshakePacket() {
        super(2);
    }

    public SHandshakePacket(int playerId) {
        this();
        this.playerId = playerId;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeInt(playerId);
    }

    @Override
    public void decode(PacketData packetData) {
        playerId = packetData.readInt();
    }

    @Override
    public void handle() {
        Main.getClient().playerID = playerId;
        //ClientMain.getClient().playerId = playerId;
        //ClientMain.getClient().valid = true;
    }
}

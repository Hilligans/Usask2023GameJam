package dev.hilligans.network.Packet.Server;

import dev.hilligans.Main;
import dev.hilligans.network.PacketBase;
import dev.hilligans.network.PacketData;

public class SSendTimePacket extends PacketBase {

    public int time;

    public SSendTimePacket() {
        super(12);
    }

    public SSendTimePacket(int time) {
        this();
        this.time = time;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeVarInt(time);
    }

    @Override
    public void decode(PacketData packetData) {
        time = packetData.readVarInt();
    }

    @Override
    public void handle() {
        Main.getClient().game.time = time;
    }
}

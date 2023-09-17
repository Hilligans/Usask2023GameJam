package dev.hilligans.network.Packet.Server;

import dev.hilligans.network.PacketBase;
import dev.hilligans.network.PacketData;

public class SAwardWinPacket extends PacketBase {

    public byte winner;

    public SAwardWinPacket() {
        super(11);
    }

    public SAwardWinPacket(int winner) {
        this.winner = (byte)winner;
    }



    @Override
    public void encode(PacketData packetData) {
        packetData.writeByte(winner);
    }

    @Override
    public void decode(PacketData packetData) {
        winner = packetData.readByte();
    }

    @Override
    public void handle() {

    }
}

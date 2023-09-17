package dev.hilligans.network.Packet;

import dev.hilligans.network.PacketBase;
import dev.hilligans.network.PacketData;

public class InvalidFormatPacket extends PacketBase {

    public InvalidFormatPacket() {
        super(0);
    }

    @Override
    public void encode(PacketData packetData) {}

    @Override
    public void decode(PacketData packetData) {}

    @Override
    public void handle() {
        System.err.println("Received an invalid packet");
    }
}

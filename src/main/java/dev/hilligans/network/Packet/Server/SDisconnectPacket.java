package dev.hilligans.network.Packet.Server;

import dev.hilligans.network.PacketBase;
import dev.hilligans.network.PacketData;

public class SDisconnectPacket extends PacketBase {

    public SDisconnectPacket() {
        super(3);
    }

    String disconnectReason;

    public SDisconnectPacket(String disconnectReason) {
        this();
        this.disconnectReason = disconnectReason;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeString(disconnectReason);
    }

    @Override
    public void decode(PacketData packetData) {
        disconnectReason = packetData.readString();
    }

    @Override
    public void handle() {
      //  ClientMain.getClient().openScreen(new DisconnectScreen(ClientMain.getClient(),disconnectReason));
    }
}

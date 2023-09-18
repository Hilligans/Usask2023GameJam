package dev.hilligans.network.Packet.Server;

import dev.hilligans.Main;
import dev.hilligans.client.graphics.screens.GameScreen;
import dev.hilligans.network.PacketBase;
import dev.hilligans.network.PacketData;

public class SGameStartPacket extends PacketBase {

    public SGameStartPacket() {
        super(4);
    }


    @Override
    public void encode(PacketData packetData) {

    }

    @Override
    public void decode(PacketData packetData) {

    }

    @Override
    public void handle() {
        if(Main.main.renderer.openScreen instanceof GameScreen screen) {
            screen.started = true;
        } else {
            throw new RuntimeException("Something exploded");
        }
    }
}

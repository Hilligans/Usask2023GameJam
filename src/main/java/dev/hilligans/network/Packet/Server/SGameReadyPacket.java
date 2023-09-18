package dev.hilligans.network.Packet.Server;

import dev.hilligans.Main;
import dev.hilligans.client.graphics.screens.GameScreen;
import dev.hilligans.client.graphics.screens.InfoScreen;
import dev.hilligans.client.graphics.screens.Screen;
import dev.hilligans.network.PacketBase;
import dev.hilligans.network.PacketData;

public class SGameReadyPacket extends PacketBase {

    int screen;

    public SGameReadyPacket() {
        super(14);
    }

    public SGameReadyPacket(int screen) {
        this();
        this.screen = screen;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeVarInt(screen);
    }

    @Override
    public void decode(PacketData packetData) {
        screen = packetData.readVarInt();
    }

    @Override
    public void handle() {
        if(screen == 0) {
            Main.main.renderer.openScreen = new GameScreen();
        } else if(screen == 1) {
            Main.main.renderer.openScreen = new InfoScreen();
        }
    }
}

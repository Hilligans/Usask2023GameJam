package dev.hilligans.network.Packet.Server;

import dev.hilligans.Main;
import dev.hilligans.client.graphics.screens.UpgradeSelectScreen;
import dev.hilligans.client.graphics.screens.WaitingScreen;
import dev.hilligans.network.PacketBase;
import dev.hilligans.network.PacketData;

public class SAwardWinPacket extends PacketBase {

    public byte winner;

    public SAwardWinPacket() {
        super(11);
    }

    public SAwardWinPacket(int winner) {
        this();
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
        if(winner == 3 || winner == Main.getClient().playerID) {
            Main.main.renderer.openScreen = new UpgradeSelectScreen();
        } else {
            Main.main.renderer.openScreen = new WaitingScreen();
        }
    }
}

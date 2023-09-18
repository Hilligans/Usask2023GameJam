package dev.hilligans.network.Packet.Client;

import dev.hilligans.Main;
import dev.hilligans.game.Player;
import dev.hilligans.network.PacketBase;
import dev.hilligans.network.PacketData;

public class CUpdatePositionPacket extends PacketBase {

    Player player;


    public CUpdatePositionPacket() {
        super(6);
    }

    public CUpdatePositionPacket(Player player) {
        this();
        this.player = player;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeVarInt(player.playerID);
        packetData.writeFloat(player.x);
        packetData.writeFloat(player.y);
        packetData.writeFloat(player.z);
    }

    @Override
    public void decode(PacketData packetData) {
        player = new Player(packetData.readVarInt());
        player.x = packetData.readFloat();
        player.y = packetData.readFloat();
        player.z = packetData.readFloat();
        player.health = -1000;
    }

    @Override
    public void handle() {
        Main.getServer().game.updatePlayer(player, true);
    }


}

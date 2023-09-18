package dev.hilligans.network.Packet.Server;

import dev.hilligans.Main;
import dev.hilligans.game.Player;
import dev.hilligans.network.PacketBase;
import dev.hilligans.network.PacketData;

public class SPlayerInfoPacket extends PacketBase {

    Player player;


    public SPlayerInfoPacket() {
        super(5);
    }

    public SPlayerInfoPacket(Player player) {
        this();
        this.player = player;
    }


    @Override
    public void encode(PacketData packetData) {
        packetData.writeVarInt(player.playerID);
        packetData.writeFloat(player.x);
        packetData.writeFloat(player.y);
        packetData.writeFloat(player.z);

        packetData.writeVarInt(player.health);
        packetData.writeVarInt(player.shots);
        packetData.writeVarInt(player.maxHealth);
    }

    @Override
    public void decode(PacketData packetData) {
        player = new Player(packetData.readVarInt());
        player.x = packetData.readFloat();
        player.y = packetData.readFloat();
        player.z = packetData.readFloat();

        player.health = packetData.readVarInt();
        player.shots = packetData.readVarInt();
        player.maxHealth = packetData.readVarInt();
    }

    @Override
    public void handle() {
        Main.getClient().game.updatePlayer(player, false);
    }
}

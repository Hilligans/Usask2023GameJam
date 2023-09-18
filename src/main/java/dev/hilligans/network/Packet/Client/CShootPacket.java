package dev.hilligans.network.Packet.Client;

import dev.hilligans.Main;
import dev.hilligans.game.Entity;
import dev.hilligans.game.Game;
import dev.hilligans.game.Player;
import dev.hilligans.network.Packet.Server.SCreateEntity;
import dev.hilligans.network.Packet.Server.SPlayerInfoPacket;
import dev.hilligans.network.PacketBase;
import dev.hilligans.network.PacketData;

public class CShootPacket extends PacketBase {

    public int playerID;
    public float velX;
    public float velZ;

    public CShootPacket() {
        super(9);
    }

    public CShootPacket(Player player, float velX, float velZ) {
        this();
        this.playerID = player.playerID;
        this.velX = velX;
        this.velZ = velZ;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeVarInt(playerID);
        packetData.writeFloat(velX);
        packetData.writeFloat(velZ);
    }

    @Override
    public void decode(PacketData packetData) {
        playerID = packetData.readVarInt();
        velX = packetData.readFloat();
        velZ = packetData.readFloat();
    }

    @Override
    public void handle() {
        Player player = Main.getServer().game.getPlayer(playerID);
        if(player.shots != 0) {
            player.shots--;
            Entity entity = player.getProjectile(velX, velZ);
            if (entity != null) {
                Main.getServer().game.addEntity(entity);
                Main.getServer().sendPacket(new SCreateEntity(entity));
            }
            //very race condition
            Main.getServer().sendPacket(new SPlayerInfoPacket(player));
        }
    }
}

package dev.hilligans.network.Packet.Client;

import dev.hilligans.Main;
import dev.hilligans.Server;
import dev.hilligans.game.Player;
import dev.hilligans.network.Packet.Server.SSelectUpgrade;
import dev.hilligans.network.PacketBase;
import dev.hilligans.network.PacketData;
import dev.hilligans.upgrades.Upgrade;
import dev.hilligans.upgrades.Upgrades;

public class CSelectUpgradePacket extends PacketBase {

    public int upgradeID;
    public int playerID;

    public CSelectUpgradePacket() {
        super(13);
    }

    public CSelectUpgradePacket(Upgrade upgrade, int player) {
        this();
        upgradeID = upgrade.upgradeID;
        this.playerID = player;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeVarInt(upgradeID);
        packetData.writeVarInt(playerID);
    }

    @Override
    public void decode(PacketData packetData) {
        upgradeID = packetData.readVarInt();
        playerID = packetData.readVarInt();
    }

    @Override
    public void handle() {
        Upgrade upgrade = Upgrades.getUpgrade(upgradeID);
        Server server = Main.getServer();
        Player player = server.game.getPlayer(playerID);
        if(player != null) {
            player.addUpgrade(upgrade);
            server.handleUpgrade(playerID);
            server.sendPacket(new SSelectUpgrade(upgrade, playerID));
        }
    }
}

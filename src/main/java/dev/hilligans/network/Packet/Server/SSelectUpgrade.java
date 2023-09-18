package dev.hilligans.network.Packet.Server;

import dev.hilligans.Main;
import dev.hilligans.Server;
import dev.hilligans.game.Player;
import dev.hilligans.network.PacketBase;
import dev.hilligans.network.PacketData;
import dev.hilligans.upgrades.Upgrade;
import dev.hilligans.upgrades.Upgrades;

public class SSelectUpgrade extends PacketBase {


    public int upgradeID;
    public int playerID;

    public SSelectUpgrade() {
        super(15);
    }

    public SSelectUpgrade(Upgrade upgrade, int player) {
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
        Player player = Main.getClient().game.getPlayer(playerID);
        if(player != null) {
            player.addUpgrade(upgrade);
        }
    }
}

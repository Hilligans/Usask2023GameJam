package dev.hilligans.network.Packet.Client;

import dev.hilligans.Main;
import dev.hilligans.network.Packet.SChatMessage;
import dev.hilligans.network.Packet.Server.SHandshakePacket;
import dev.hilligans.network.PacketData;
import dev.hilligans.network.PacketBase;
import dev.hilligans.network.ServerNetworkHandler;
import dev.hilligans.server.ServerPlayerData;
import io.netty.channel.ChannelHandlerContext;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class CHandshakePacket extends PacketBase {

    public int id;
    public String name;
    public String authToken;
    public long version;

    public CHandshakePacket() {
        super(1);
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeInt(0);
        packetData.writeString(Main.getClient().getUsername());
    }

    @Override
    public void decode(PacketData packetData) {
        id = packetData.readInt();
        name = packetData.readString();
    }

    @Override
    public void handle() {
        System.out.println("HANDLING PLAYER");
        handlePlayer(name, version, ctx, name);
    }

    public static AtomicInteger ids = new AtomicInteger();

    public static synchronized void handlePlayer(String name, long version, ChannelHandlerContext ctx, String identifier) {
        int playerId = ids.incrementAndGet();
        System.out.println("Player " + playerId + " just joined the network");
        if(playerId == 1) {
            Main.getServer().game.player1.setNetwork(ctx);
        } else if(playerId == 2) {
            Main.getServer().game.player2.setNetwork(ctx);
        }

        ServerPlayerData serverPlayerData = ServerPlayerData.loadOrCreatePlayer(identifier);

        ServerNetworkHandler.playerData.put(playerId, serverPlayerData);
        ServerNetworkHandler.mappedChannels.put(playerId,ctx.channel().id());
        ServerNetworkHandler.mappedId.put(ctx.channel().id(),playerId);
        ServerNetworkHandler.mappedName.put(ctx.channel().id(),name);
        ServerNetworkHandler.nameToChannel.put(name, ctx.channel().id());


        ServerNetworkHandler.sendPacket(new SHandshakePacket(playerId),ctx);

       // Main.getServer().sendPacket(new SChatMessage(name + " has joined the game"));
    }

    public static final String alphanum = "ABCDEFGHIJKLMNOPQRSTUVQXYZabcdefghijklmnopqrstuvwxyz1234567890`!@#$%^&*()-_=+~[]\\;',./{}|:\"<>?;";
    private static final char[] symbols = alphanum.toCharArray();
    static Random random = new SecureRandom();

    public static char getChar(int index) {
        return symbols[index % symbols.length];
    }
}

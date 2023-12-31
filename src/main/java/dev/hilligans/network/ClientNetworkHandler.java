package dev.hilligans.network;

import dev.hilligans.Main;
import dev.hilligans.network.Packet.Client.CHandshakePacket;
import io.netty.channel.*;

public class ClientNetworkHandler extends NetworkHandler {

    public static ClientNetworkHandler clientNetworkHandler;

    public ClientNetwork network;

    public ClientNetworkHandler(ClientNetwork network) {
        this.network = network;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        network.client.network.sendPacket(new CHandshakePacket());
      //  network.client.renderWorld = true;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("DISCONNECTED FROM SERVER");
    //    network.client.renderWorld = false;
     //   network.client.valid = false;
     //   network.client.clientWorld = new ClientWorld(network.client);
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PacketData msg) throws Exception {
        Main.connect = false;
        PacketBase packetBase = msg.createPacket(network.receiveProtocol);
        packetBase.handle();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        cause.printStackTrace();
    //    network.client.clientWorld = new ClientWorld(network.client);
    //    network.client.openScreen(new DisconnectScreen(network.client,cause.getMessage()));
    }

}

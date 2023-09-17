package dev.hilligans.network.Packet;

import dev.hilligans.network.PacketBase;
import dev.hilligans.network.PacketData;
import dev.hilligans.network.ServerNetworkHandler;

public class CSendMessage extends PacketBase {

    public String message;

    public CSendMessage() {
        super(11);
    }

    public CSendMessage(String message) {
        this();
        this.message = message;
    }


    @Override
    public void encode(PacketData packetData) {
        packetData.writeString(message);
    }

    @Override
    public void decode(PacketData packetData) {
        message = packetData.readString();
    }

    @Override
    public void handle() {
        if(!message.equals("")) {
            String name = ServerNetworkHandler.mappedName.get(ctx.channel().id());
            String[] args = message.split(" ");
            System.out.println(name + ": " + message);
            //ServerMain.getServer().sendPacket(new SChatMessage(name + ": " + message));
        }
    }


}

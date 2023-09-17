package dev.hilligans.network;

import dev.hilligans.network.Packet.Client.CHandshakePacket;
import dev.hilligans.network.Packet.Client.CShootPacket;
import dev.hilligans.network.Packet.Client.CUpdatePositionPacket;
import dev.hilligans.network.Packet.InvalidFormatPacket;
import dev.hilligans.network.Packet.Server.*;

public class Protocols {

    public static final Protocol PROTOCOL = new Protocol("main");


    public static void setupProtocol() {
        PROTOCOL.register(InvalidFormatPacket::new);
        PROTOCOL.register(CHandshakePacket::new);
        PROTOCOL.register(SHandshakePacket::new);
        PROTOCOL.register(SDisconnectPacket::new);
        PROTOCOL.register(SGameStartPacket::new);
        PROTOCOL.register(SPlayerInfoPacket::new);
        PROTOCOL.register(CUpdatePositionPacket::new);
        PROTOCOL.register(SRemoveEntityPacket::new);
        PROTOCOL.register(SSendEntityPositionPacket::new);
        PROTOCOL.register(CShootPacket::new);
        PROTOCOL.register(SCreateEntity::new);
        PROTOCOL.register(SAwardWinPacket::new);
    }




}

package dev.hilligans.server;

import dev.hilligans.save.WorldLoader;
import dev.hilligans.tag.CompoundNBTTag;

public class ServerPlayerData {

    public String id;
    public static String path = "world/" + "world" + "/player-data/";

    public ServerPlayerData(String id) {
        this.id = id;
    }

    public ServerPlayerData(String id, CompoundNBTTag compoundNBTTag) {
        this.id = id;
    }

    public static ServerPlayerData loadOrCreatePlayer(String id) {
        CompoundNBTTag tag = WorldLoader.loadTag(path + id + ".dat");
        if(tag == null) {
            return new ServerPlayerData(id);
        } else {
            return new ServerPlayerData(id,tag);
        }
    }

    public void save() {

    }

}

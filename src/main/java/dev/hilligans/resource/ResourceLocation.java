package dev.hilligans.resource;

import dev.hilligans.Main;

public class ResourceLocation {

    public String path;
    public String sSource;

    public ResourceLocation(String path) {
        this.path = path;
    }

    public ResourceLocation(String path, String source) {
        this.path = path;
        this.sSource = source;
    }

    public String getSource() {
        return Main.PROJECT_NAME;
    }

    @Override
    public String toString() {
        return "ResourceLocation{" +
                "path='" + path + '\'' +
                ", source=" + getSource() +
                '}';
    }

    public String identifier() {
        return getSource() + ":" + path;
    }
}

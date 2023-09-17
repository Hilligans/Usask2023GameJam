package dev.hilligans.registry;

import dev.hilligans.GameInstance;
import dev.hilligans.client.graphics.GlUtils;
import dev.hilligans.resource.ResourceLocation;

public interface IRegistryElement {

    default void load(GameInstance gameInstance) {}

    default void cleanup() {}

    String getResourceName();

    default String getIdentifierName() {
        return "ourcraft:" + getResourceName();
    }

    String getUniqueName();

    default ResourceLocation getResourceLocation() {
        return new ResourceLocation(getResourceName());
    }

    default void setUniqueID(int id) {}

    default void loadGraphics(GlUtils glUtils) {}

    default void cleanupGraphics(GlUtils glUtils) {}

    //default void loadGraphics(IGraphicsEngine<?,?,?> graphicsEngine, GraphicsContext graphicsContext) {}

    //default void cleanupGraphics(IGraphicsEngine<?,?,?> graphicsEngine, GraphicsContext graphicsContext) {}
}

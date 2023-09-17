package dev.hilligans;

import dev.hilligans.client.graphics.*;
import dev.hilligans.registry.IRegistryElement;
import dev.hilligans.registry.Registry;
import dev.hilligans.resource.IBufferAllocator;
import dev.hilligans.resource.ResourceLocation;
import dev.hilligans.resource.UniversalResourceLoader;
import dev.hilligans.resource.dataloader.DataLoader;
import dev.hilligans.resource.dataloader.FolderResourceDirectory;
import dev.hilligans.resource.loaders.ImageLoader;
import dev.hilligans.resource.loaders.JsonLoader;
import dev.hilligans.resource.loaders.ResourceLoader;
import dev.hilligans.resource.loaders.StringLoader;
import dev.hilligans.upgrades.Upgrade;
import dev.hilligans.upgrades.Upgrades;

import java.io.File;
import java.nio.ByteBuffer;

public class GameInstance {

    public static final GameInstance GAME_INSTANCE = new GameInstance();

    public final DataLoader DATA_LOADER = new DataLoader();
    public final UniversalResourceLoader RESOURCE_LOADER = new UniversalResourceLoader();

    public final Registry<Registry<?>> REGISTRIES = new Registry<>(this);

    public GameInstance() {
        REGISTRIES.put(Main.PROJECT_NAME + ":" + "vertex_formats", VERTEX_FORMATS);
        REGISTRIES.put(Main.PROJECT_NAME + ":" + "shaders", SHADERS);
        REGISTRIES.put(Main.PROJECT_NAME + ":" + "textures", TEXTURES);
        REGISTRIES.put(Main.PROJECT_NAME + ":" + "resource_loaders", RESOURCE_LOADERS);
        REGISTRIES.put(Main.getFormattedName("upgrades"), UPGRADES);
    }

    public void registerContent() {
        Textures.loadTextures(this);
        Upgrades.loadUpgrades(this);

        RESOURCE_LOADER.add(new JsonLoader());
        RESOURCE_LOADER.add(new ImageLoader());
        RESOURCE_LOADER.add(new StringLoader());
        VERTEX_FORMATS.put(position_texture);
        SHADERS.put(new ShaderSource("position_texture", "ourcraft:position_texture", "shaders/PositionTexture.vsh", "shaders/PositionTexture.fsh").withUniform("transform", "4fv").withUniform("color", "4f"));

    }

    public static final VertexFormat position_texture = new VertexFormat("ourcraft", "position_texture", VertexFormat.TRIANGLES)
            .addPart("position", VertexFormat.FLOAT,3)
            .addPart("texture", VertexFormat.FLOAT, 2);

    public void load() {

        if(new File("Usask2023GameJam-1.0-SNAPSHOT-jar-with-dependencies.jar").exists()) {
            DATA_LOADER.addJar("Usask2023GameJam-1.0-SNAPSHOT-jar-with-dependencies.jar", Main.PROJECT_NAME);
        } else {
            DATA_LOADER.addFolder("target/classes/", Main.PROJECT_NAME);
        }

        for(Registry<?> registry : REGISTRIES.ELEMENTS) {
            for(Object o : registry.ELEMENTS) {
                if(o instanceof IRegistryElement) {
                    ((IRegistryElement) o).load(this);
                }
            }
        }
    }

    public void loadGraphics(GlUtils glUtils) {
        for(Registry<?> registry : REGISTRIES.ELEMENTS) {
            for(Object o : registry.ELEMENTS) {
                if(o instanceof IRegistryElement) {
                    ((IRegistryElement) o).loadGraphics(glUtils);
                }
            }
        }
    }

    public final Registry<VertexFormat> VERTEX_FORMATS = new Registry<>(this, VertexFormat.class);
    public final Registry<ShaderSource> SHADERS = new Registry<>(this, ShaderSource.class);
    public final Registry<Texture> TEXTURES = new Registry<>(this, Texture.class);
    public final Registry<ResourceLoader<?>> RESOURCE_LOADERS = new Registry<>(this, ResourceLoader.class);
    public final Registry<Upgrade> UPGRADES = new Registry<>(this, Upgrade.class);

    public ByteBuffer getResource(ResourceLocation resourceLocation) {
        return DATA_LOADER.get(resourceLocation);
    }

    public ByteBuffer getResourceDirect(ResourceLocation resourceLocation) {
        return DATA_LOADER.getDirect(resourceLocation);
    }

    public ByteBuffer getResource(ResourceLocation resourceLocation, IBufferAllocator allocator) {
        return DATA_LOADER.get(resourceLocation, allocator);
    }

}

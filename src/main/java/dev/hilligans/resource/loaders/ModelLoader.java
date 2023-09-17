package dev.hilligans.resource.loaders;

import dev.hilligans.client.graphics.Model;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;

import java.nio.ByteBuffer;

public class ModelLoader extends ResourceLoader<Model> {

    public ModelLoader() {
        super("model_loader", "model");
    }

    @Override
    public Model read(ByteBuffer buffer) {
        try(AIScene scene = Assimp.aiImportFileFromMemory(buffer, 0, "")) {



        }

        return null;
    }

    @Override
    public ByteBuffer write(Model iModel) {
        return null;
    }
}

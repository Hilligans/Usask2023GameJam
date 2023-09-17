package dev.hilligans.client.graphics.screens;

import dev.hilligans.client.graphics.GlUtils;
import dev.hilligans.client.graphics.MatrixStack;
import dev.hilligans.client.graphics.Window;
import org.joml.Matrix4fStack;

public abstract class Screen {

    public Window window;

    public abstract void render(GlUtils glUtils, MatrixStack matrixStack);


}

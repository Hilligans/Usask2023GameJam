package dev.hilligans.client.graphics;

import dev.hilligans.client.graphics.screens.Screen;
import org.joml.Matrix4d;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class Renderer {

    public Screen openScreen;

    public Window window;
    public GlUtils glUtils;

    public Renderer(GlUtils glUtils, Window window) {
        this.glUtils = glUtils;
        this.window = window;
    }

    public void render(GlUtils glUtils) {
        if(openScreen != null) {
            openScreen.window = window;
            openScreen.render(glUtils, getScreenStack());
        }
    }

    public void renderLoop() {
        while (!window.shouldClose()) {

            glClearColor(window.clearColor.x, window.clearColor.y, window.clearColor.z, window.clearColor.w);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            render(glUtils);
            rot += 0.1;
            window.swapBuffers();
        }
        System.out.println("Done");
        GLFW.glfwTerminate();
        System.exit(1);
    }

    public static float rot = 0;


    MatrixStack getScreenStack() {
        MatrixStack matrixStack = getScreenStack(1, 1, 0 ,0);
        return matrixStack;
    }

    MatrixStack getMatrix() {
        Matrix4d perspective = new Matrix4d().perspective(Math.toRadians(90), window.getAspectRatio(), 0.01f, 100000f);
        //perspective.mul(getView());
        return new MatrixStack(perspective);
    }

    MatrixStack getScreenStack(int W, int H, int x, int y) {
        return new MatrixStack(new Matrix4d().ortho(0, window.getWindowWidth(), window.getWindowHeight(),0,-1,20000));
    }
}

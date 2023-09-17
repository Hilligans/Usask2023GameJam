package dev.hilligans.client.graphics;

import dev.hilligans.Client;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    public long window;
    public Client client;
    public boolean shouldClose = false;
    public boolean mouseLocked = false;
    public boolean windowFocused = true;
    public float width;
    public float height;
    public Vector4f clearColor = new Vector4f();

    public double mouseX;
    public double mouseY;

    public Window(Client client, String name, int width, int height) {
        window = glfwCreateWindow(width,height,name,NULL,NULL);
        this.width = width;
        this.height = height;
        if(window == NULL) {
            throw new RuntimeException("Failed to create window");
        }
        glfwMakeContextCurrent(window);
        this.client = client;
        registerCallbacks();
    }

    public void setup() {
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    public long getWindowID() {
        return window;
    }

    public void close() {
        shouldClose = true;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void swapBuffers() {
        //glfwSwapInterval(0);
        glfwSwapBuffers(window);
        glfwPollEvents();
       // tick();
    }

    public Client getClient() {
        return client;
    }

    public float getWindowWidth() {
        return width;
    }

    public float getWindowHeight() {
        return height;
    }

    public float getAspectRatio() {
        return getWindowWidth() / getWindowHeight();
    }

    public boolean isWindowFocused() {
        return windowFocused;
    }

    public String getWindowingName() {
        return "glfw";
    }

    public void setClearColor(float r, float g, float b, float a) {
        this.clearColor.set(r,g,b,a);
    }

    public void registerCallbacks() {
        glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
            mouseX = xpos -= width /2f;
            mouseY = ypos -= height / 2f;
            //System.out.println(mouseX);
            if(mouseLocked) {
                double halfWindowX = (double) getWindowWidth() / 2;
                double halfWindowY = (double) getWindowHeight() / 2;

                double deltaX = xpos - halfWindowX;
                double deltaY = ypos - halfWindowY;

               // glfwSetCursorPos(window, halfWindowX, halfWindowY);
            }
        });
        glfwSetWindowSizeCallback(window, (window, w, h) -> {
            width = w;
            height = h;
            GL30.glViewport(0,0,w,h);
        });

        glfwSetWindowFocusCallback(window, (window, focused) -> windowFocused = focused);
      //  MouseHandler mouseHandler = new MouseHandler(client);
      //  glfwSetMouseButtonCallback(window, mouseHandler::invoke);
        //inputHandler.add((IInputProvider) mouseHandler);
    }

}

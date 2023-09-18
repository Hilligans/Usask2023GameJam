package dev.hilligans.client.graphics.screens;

import dev.hilligans.Main;
import dev.hilligans.client.graphics.GlUtils;
import dev.hilligans.client.graphics.MatrixStack;
import dev.hilligans.client.graphics.Widget;
import dev.hilligans.client.graphics.Window;
import org.joml.Matrix4fStack;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

public abstract class Screen {

    public Window window;
    public ArrayList<Widget> widgets = new ArrayList<>();

    public abstract void render(GlUtils glUtils, MatrixStack matrixStack);

    public void addWidget(Widget widget) {
        widgets.add(widget);
        widget.screenBase = this;
        widget.window = Main.main.window;
    }

    public void mouseClick(int x, int y, int mouseButton) {
        if(mouseButton == GLFW_MOUSE_BUTTON_1) {
            for (Widget widget : widgets) {
                widget.isFocused = false;
                if (widget.isInBounds(x, y)) {
                    widget.activate(x - widget.x, y - widget.y);
                }
            }
        }
    }
}

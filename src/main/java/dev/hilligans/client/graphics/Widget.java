package dev.hilligans.client.graphics;

import dev.hilligans.Settings;
import dev.hilligans.client.graphics.screens.Screen;

public class Widget {

    public int x;
    public int y;
    public int width;
    public int height;

    public float percentX = -1;
    public float percentY = -1;


    public short widgetId = -1;
    public String name;

    public int minY;

    public boolean isFocused = false;
    public boolean shouldRender = true;
    public boolean enabled = true;

   // public IntegerWrapper yOffset = new IntegerWrapper(0);

    public Screen screenBase;

    public Window window;

    public Widget(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Widget(float percentX, float percentY, int width, int height) {
        setPercentages(percentX,percentY);
        this.width = width;
        this.height = height;
    }

   //public Widget addOffset(IntegerWrapper integerWrapper) {
   //     this.yOffset = integerWrapper;
   //     return this;
   // }

    public Widget setPercentages(float x, float y) {
        this.percentX = x / 100f;
        this.percentY = y / 100f;
        return this;
    }

    public void addSource(Window renderWindow) {
        this.window = renderWindow;
    }

    public void render(GlUtils glUtils, MatrixStack matrixStack, int xOffset, int yOffset) {}

    public void screenClose() {}

    public boolean isInBounds(int x, int y) {
        return x > this.getX() && x < this.getX() + this.width * Settings.guiScale && y > this.getY() && y < this.getY() + this.height * Settings.guiScale;
    }

    public boolean isInBoundsX(int x) {
        return x > this.getX() && x < this.getX() + this.width;
    }

    public void activate(int x, int y) {
        isFocused = true;
    }

    public void mouseScroll(int x, int y, float amount) {}

    public int getX() {
        return (int) (x - window.getWindowWidth() / 2);
    }

    public int getY() {
        return (int) (y - window.getWindowHeight() / 2);
    }

    public boolean isActive() {
        return this.getY() >= minY;
    }

    public void onScreenResize(int sizeX, int sizeY) {
        if(percentX != -1) {
            this.x = (int) (sizeX * percentX) - width / 2;
            this.y = (int) (sizeY * percentY) - height / 2;
        }
    }
}

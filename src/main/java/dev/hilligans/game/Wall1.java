package dev.hilligans.game;

public class Wall1 extends Entity {

    public int x;
    public int y;
    public int w;
    public int h;


    public Wall1(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(x, y, w, h);
    }

    @Override
    public int getEntityType() {
        return -1;
    }


    @Override
    boolean isSolid() {
        return true;
    }

    @Override
    boolean isImmoveable() {
        return true;
    }
}

package dev.hilligans.game;

public class BoundingBox {

    public float minX;
    public float minZ;
    public float maxX;
    public float maxZ;

    public BoundingBox(float minX, float minZ, float width, float height) {
        this.minX = minX;
        this.minZ = minZ;

        this.maxX = minX + width;
        this.maxZ = minZ + height;
    }

    public boolean intersects(BoundingBox boundingBox) {
        return !(boundingBox.minX > maxX
                || boundingBox.maxX < minX
                || boundingBox.minZ > maxZ
                || boundingBox.maxZ < minZ);
    }
}

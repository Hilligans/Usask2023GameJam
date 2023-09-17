package dev.hilligans.client.graphics.camera;

import dev.hilligans.client.graphics.MatrixStack;
import dev.hilligans.client.graphics.Window;
import org.joml.*;

import java.lang.Math;

public class WorldCamera implements ICamera {

    public Vector3d pos = new Vector3d(1, 5, 0);
   // public double x;
   // public double y = Chunk.terrain + 5;
  //  public double z;

    public Vector3f gravityVector = new Vector3f(0, -1, 0);
    public Matrix3f moveMatrix = new Matrix3f();
    public Vector3f moveVector = new Vector3f();
    public Quaternionf f = new Quaternionf();

    public Vector3f forwardVector = new Vector3f(1,0,0);
    public Vector3f leftVector = new Vector3f(0, 0, 1);

    public float pitch;
    public float yaw;
    public float roll;
    public float roller;

    public float maxPitch = 3.1415f / 2;
    public float minPitch = -3.1415f / 2;
    public double maxYaw = 6.283;
    public double minYaw = -6.283;

    public int pitchSign = 1;


    public float velX;
    public float velY;
    public float velZ;

    public float fov = 90;

    public Vector3d savedPosition;

    public Window window;

    /**
     0 = normal camera
     1 = camera behind player
     2 = camera looking at player
     */
    public int thirdPersonMode = 0;
    public float cameraZoom = 0;

    @Override
    public void move(float x, float y, float z) {
        moveVector.set(x, y, z).mul(moveMatrix);
        pos.add(moveVector);
    }


    @Override
    public Window getWindow() {
        return window;
    }

    @Override
    public void setPosition(double x, double y, double z) {
        pos.set(x,y,z);
    }

    @Override
    public Vector3d getPosition() {
        return pos.get(new Vector3d());
    }

    @Override
    public void setRotation(float pitch, float yaw) {
        this.pitch = pitch;
        this.yaw = yaw;
    }

    @Override
    public Vector2f getRotation() {
        return new Vector2f(pitch,yaw);
    }

    //TODO fix packet not being sent
    @Override
    public void addRotation(float pitch, float yaw) {
        this.pitch += pitch;
        this.yaw += yaw;

        /*if(this.pitch > maxPitch) {
            this.pitch = maxPitch;
        }

        if(this.pitch < minPitch) {
            this.pitch = minPitch;
        }

         */

        //if(this.yaw > 6.283) {
        //    this.yaw -= 6.283f * 2;
        //} else if(this.yaw < -6.283) {
        //    this.yaw += 6.283f * 2;
        //}
        this.yaw = (this.yaw + 6.283f) % 6.283f;
    }

    @Override
    public float getPitch() {
        return pitch;
    }

    @Override
    public float getYaw() {
        return yaw;
    }

    @Override
    public void tick() {
        if(Math.abs(roll) < 0.05) {
            roll = 0;
        } else
        if(roll != 0) {
            //roll -= 0.008 * (roll > 0 ? 1 : -1);
        }
    }

    @Override
    public float getSensitivity() {
        return 1;
    }

    @Override
    public void setMotion(float velX, float velY, float velZ) {
        this.velX = velX;
        this.velY = velY;
        this.velZ = velZ;
    }

    @Override
    public void addMotion(float velX, float velY, float velZ) {

    }

    @Override
    public Vector3f getMotion() {
        return new Vector3f(velX,velY,velZ);
    }

    public Vector3d getLookVector() {
        return new Vector3d((Math.cos(yaw) * Math.cos(pitch)), (Math.sin(pitch)), (Math.sin(yaw) * Math.cos(pitch))).mul(moveMatrix);
    }

    @Override
    public MatrixStack getMatrixStack(int W, int H, int x, int y) {
        Matrix4d perspective = getPerspective(W, H, x, y,  fov, getWindow().getAspectRatio(), 0.01f, 100000f);
        perspective.mul(getView());
        Vector3d cameraPos = getPosition();
        savePosition(cameraPos);
        if(thirdPersonMode == 2) {
            perspective.lookAt(getSavedPosition(), cameraPos.add(getLookVector().negate()), cameraUp());
        } else {
            perspective.lookAt(getSavedPosition(), cameraPos.add(getLookVector()), cameraUp());
        }
        perspective.translate(0,0.15f,0);
        return new MatrixStack(perspective);
    }

    public MatrixStack get() {
        Vector3d cameraPos = getPosition();
        savePosition(cameraPos);
        return new MatrixStack(new Matrix4d().ortho(-getWindowWidth()/2, getWindowWidth()/2,-getWindowHeight()/2,getWindowHeight()/2,0.01,20000).mul(getView()).lookAt(getSavedPosition(), cameraPos.add(getLookVector()), cameraUp()));
    }

    public Vector3d cameraUp() {
        return new Vector3d(1, 1, 1).mul(Math.sin(roll), Math.cos(roll), 0).mul(moveMatrix);
    }

    @Override
    public Matrix4d getView() {
        Matrix4d view = new Matrix4d();
        if(thirdPersonMode != 0) {
            view.translate(0,0,1 + getViewLength() * -1);
        }
        return view;
    }

    public float getViewLength() {
        return cameraZoom;
    }


    @Override
    public void savePosition(Vector3d vector3d) {
        this.savedPosition = vector3d.get(new Vector3d());
    }

    @Override
    public Vector3d getSavedPosition() {
        return savedPosition;
    }


    /**
     * x = 1
     * y = 2
     * z = 3
     */
    public static int getDirection(int current, int old) {
        String code = current + "" + old;
        return switch (code) {
            case "-1-2", "2-1", "12", "-21",    "3-2", "23", "-32", "-2-3",   "-13", "-3-1", "1-3", "31" -> -1;
            default -> 1;
        };
    }

}

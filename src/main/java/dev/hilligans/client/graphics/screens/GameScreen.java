package dev.hilligans.client.graphics.screens;

import dev.hilligans.Client;
import dev.hilligans.Main;
import dev.hilligans.Settings;
import dev.hilligans.client.graphics.*;
import dev.hilligans.client.graphics.camera.WorldCamera;
import dev.hilligans.game.Game;
import dev.hilligans.network.Packet.Client.CShootPacket;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL30;

public class GameScreen extends Screen {

    public long mesh;
    public long length;
    public ShaderSource shaderSource;


    Game game = Main.getClient().game;
    Client client;

    WorldCamera worldCamera = new WorldCamera();

    boolean switch1 = false;

    boolean switch2 = false;

    public static int gameHeight = 12;
    public static int gameWidth = 30;

    public GameScreen() {
        game.player1.x = 2 * 64;
        game.player1.z = 2 * 64;

        client = Main.getClient();


        switch1 = true;
        worldCamera.addRotation((float) (-Math.PI/2f), (float) Math.PI/2);

        worldCamera.setPosition(0, 1, 0);
    }

    @Override
    public void render(GlUtils glUtils, MatrixStack matrixStack) {
        float moveAmount = 1f;
        float rotSpeed = 0.01f;
        int playerMoveSpeed1 = 3;
        if(GLFW.glfwGetKey(window.window, GLFW.GLFW_KEY_UP) == GLFW.GLFW_PRESS) {
            worldCamera.addRotation(rotSpeed, 0);
        }

        if(GLFW.glfwGetKey(window.window, GLFW.GLFW_KEY_DOWN) == GLFW.GLFW_PRESS) {
            worldCamera.addRotation(-rotSpeed, 0);
        }
        if(GLFW.glfwGetKey(window.window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            if(switch2) {
                game.movePlayer(client.playerID, 0, -playerMoveSpeed1);
            } else {
                worldCamera.moveForward(moveAmount);
            }
        }
        if(GLFW.glfwGetKey(window.window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            if(switch2) {
                game.movePlayer(client.playerID, 0, playerMoveSpeed1);
            } else {
                worldCamera.moveBackward(moveAmount);
            }
        }


        if(GLFW.glfwGetKey(window.window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            if(switch2) {
                game.movePlayer(client.playerID, playerMoveSpeed1, 0);
            } else {
                worldCamera.moveRight(moveAmount);
            }
        }
        if(GLFW.glfwGetKey(window.window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            if(switch2) {
                game.movePlayer(client.playerID, -playerMoveSpeed1, 0);
            } else {
                worldCamera.moveLeft(moveAmount);
            }
        }
        if(GLFW.glfwGetKey(window.window, GLFW.GLFW_KEY_LEFT) == GLFW.GLFW_PRESS) {
            worldCamera.addRotation(0, -rotSpeed);
        }
        if(GLFW.glfwGetKey(window.window, GLFW.GLFW_KEY_RIGHT) == GLFW.GLFW_PRESS) {
            worldCamera.addRotation(0, rotSpeed);
        }
        if(GLFW.glfwGetKey(window.window, GLFW.GLFW_KEY_Q) == GLFW.GLFW_PRESS) {
            switch1 = true;
        }

        if(GLFW.glfwGetKey(window.window, GLFW.GLFW_KEY_Z) == GLFW.GLFW_PRESS) {
            switch2 = true;
        }
        if(GLFW.glfwGetKey(window.window, GLFW.GLFW_KEY_X) == GLFW.GLFW_PRESS) {
            switch2 = false;
        }

        if(GLFW.glfwGetKey(window.window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS) {
            Vector2f vector2f = client.game.getPlayer(client.playerID).calculateShootAngle((float) window.mouseX, (float) window.mouseY);

            Main.getClient().network.sendPacket(new CShootPacket(client.game.getPlayer(client.playerID), vector2f.x, vector2f.y));
        }

        if(mesh == 0) {
            makeMesh(glUtils);
            worldCamera.window = Main.main.window;
        }
        if(!switch1) {
            matrixStack = worldCamera.getMatrix();
        } else {
            matrixStack = worldCamera.get();
        }



        matrixStack.push();
        //matrixStack.matrix4f.lookAt(new Vector3f(x, y, z), new Vector3f(1, 0, 1), new Vector3f(0, 1, 0));
       // matrixStack.matrix4f.translate(0, 10, 0).rotateX(-rot).rotateZ(-rotz).translate(0, 0, z);
       // rot -= 0.02f;

        glUtils.bindPipeline(shaderSource.program);
        glUtils.bindTexture(Textures.TILE.textureId);
        glUtils.uploadMatrix(matrixStack, shaderSource);
        glUtils.drawMesh(matrixStack, mesh, 0L, (int) length);
        matrixStack.pop();

        game.renderEntities(glUtils, matrixStack);
    }

    public void makeMesh(GlUtils glUtils) {
        shaderSource = glUtils.gameInstance.SHADERS.get(Main.getFormattedName("position_texture"));

        PrimitiveBuilder primitiveBuilder = new PrimitiveBuilder(shaderSource.vertexFormat);
        GL30.glDisable(GL30.GL_CULL_FACE);
        int startx = 0;
        int startz = 0;
        int yy = 0;
        int size = 64;
        int zoffset = (int) -(gameHeight * Settings.guiScale * 16 - window.height/2);
        int xoffset = (int) -(gameWidth * Settings.guiScale * 16 - window.width/2);
        //int xoffset = 0;
        //int zoffset = 0;
        for(int x = startx; x < gameWidth + startx; x++) {
            for (int y = startz; y < startz + gameHeight; y++) {
                float minX = (float) 0;
                float minY = (float) 0;
                float maxX = (float) 1;
                float maxY = (float) 1;
                float[] vertices = new float[]{x * size + xoffset, yy, y * size + zoffset, minX, minY, x * size + xoffset, yy, (y + 1) * size + zoffset, minX, maxY, (x + 1) * size + xoffset, yy, y * size + zoffset, maxX, minY, (x + 1) * size + xoffset, yy, (y + 1) * size + zoffset, maxX, maxY};
                primitiveBuilder.addQuad(vertices);
            }
        }
        //primitiveBuilder.buildQuad(startx * size + xoffset, yy, startz * size, 0, 0, startx * size + 64, yy + 64, 1, 1);
        //primitiveBuilder.toVertexMesh();
        length = primitiveBuilder.indices.size();
        mesh = glUtils.createMesh(primitiveBuilder.toVertexMesh());
    }
}

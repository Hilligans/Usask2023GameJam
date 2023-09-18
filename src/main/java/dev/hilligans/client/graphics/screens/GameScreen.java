package dev.hilligans.client.graphics.screens;

import dev.hilligans.Client;
import dev.hilligans.Main;
import dev.hilligans.Settings;
import dev.hilligans.client.graphics.*;
import dev.hilligans.client.graphics.camera.WorldCamera;
import dev.hilligans.game.Game;
import dev.hilligans.game.Player;
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

    public boolean started = false;

    public static int gameHeight = 12;
    public static int gameWidth = 30;

    int countdown = 60 * 3;

    public GameScreen() {
        client = Main.getClient();

        worldCamera.addRotation((float) (-Math.PI/2f), (float) Math.PI/2);

        worldCamera.setPosition(0, 1, 0);
    }

    public int renderCount = 0;
    public long time = System.currentTimeMillis();

    public int dashTime = 0;

    public int projectileCooldown = 0;

    @Override
    public void render(GlUtils glUtils, MatrixStack matrixStack) {
        long t = System.currentTimeMillis();
        if(t > time + 1000) {
            time = t;
            renderCount = 0;
        }
        if(projectileCooldown > 0) {
            projectileCooldown--;
        }
        renderCount++;
        Player player = game.getPlayer(client.playerID);
        float playerMoveSpeed1 = 3 * player.temporarySpeedModifier * player.speed;

        if(dashTime > 0) {
            dashTime--;
        }

        if(Math.abs(player.temporarySpeedModifier - 1) < 0.01) {
            player.temporarySpeedModifier = 1;
        } else if(player.temporarySpeedModifier < 1) {
            player.temporarySpeedModifier *= 1.05;
        } else if(player.temporarySpeedModifier > 1) {
            player.temporarySpeedModifier *= 0.95;
        }

        if(started) {
            if (GLFW.glfwGetKey(window.window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
                game.movePlayer(client.playerID, 0, -playerMoveSpeed1);
            }
            if (GLFW.glfwGetKey(window.window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
                game.movePlayer(client.playerID, 0, playerMoveSpeed1);
            }
            if (GLFW.glfwGetKey(window.window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
                game.movePlayer(client.playerID, playerMoveSpeed1, 0);
            }
            if (GLFW.glfwGetKey(window.window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
                game.movePlayer(client.playerID, -playerMoveSpeed1, 0);
            }

            if (GLFW.glfwGetKey(window.window, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS) {
                if(dashTime == 0) {
                    player.temporarySpeedModifier += 3;
                    dashTime = 90;
                }
            }
            if (GLFW.glfwGetKey(window.window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS) {
                if(player.shots != 0) {
                    if (projectileCooldown == 0) {
                        Vector2f vector2f = client.game.getPlayer(client.playerID).calculateShootAngle((float) window.mouseX, (float) window.mouseY);
                        projectileCooldown = 10;
                        player.shots--;
                        Main.getClient().network.sendPacket(new CShootPacket(client.game.getPlayer(client.playerID), vector2f.x, vector2f.y));
                    }
                }
            }
        }

        if (mesh == 0) {
            makeMesh(glUtils);
            worldCamera.window = Main.main.window;
        }

        matrixStack = worldCamera.get();


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

        int time = Game.MATCH_TIME - game.time / Game.TICKS_PER_SECOND;
        Texture texture = Textures.NUMBERS[time % 10];
        int height = getHeightOffset();
        height -= 43 * Settings.guiScale;
        texture.drawTexture1(glUtils, matrixStack, 16, 0, height, (int) (texture.width * Settings.guiScale * 2), (int) (texture.height * Settings.guiScale * 2));

        time /= 10;
        texture = Textures.NUMBERS[time % 10];
        texture.drawTexture1(glUtils, matrixStack, -64 - 16, 0, height, (int) (texture.width * Settings.guiScale * 2), (int) (texture.height * Settings.guiScale * 2));

        if(!started) {
            countdown--;
            if(countdown >= 0) {
                texture = Textures.NUMBERS[((countdown / 60) % 10) + 1];
            } else {
                texture = Textures.NUMBERS[0];
            }
            height = 0;
            texture.drawTexture1(glUtils, matrixStack, -32, 0, height, (int) (texture.width * Settings.guiScale * 2), (int) (texture.height * Settings.guiScale * 2));
        }

    }

    public int getHeightOffset() {
        return (int) -(gameHeight * Settings.guiScale * 16 - window.height/2);
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

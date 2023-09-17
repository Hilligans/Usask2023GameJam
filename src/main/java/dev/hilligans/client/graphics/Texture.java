package dev.hilligans.client.graphics;

import dev.hilligans.GameInstance;
import dev.hilligans.Main;
import dev.hilligans.registry.IRegistryElement;
import dev.hilligans.resource.ResourceLocation;

public class Texture implements IRegistryElement {

    public static float guiSize = 4.0f;


    public String path;

    public int width;
    public int height;

    public long textureId = -1;

    public Image texture;
    public ShaderSource shaderSource;

    public int program;

    public Texture(String path) {
        this.path = path;
    }

    public Texture(String path, Image texture) {
        this.path = path;
        width = texture.getWidth();
        height = texture.getHeight();
        this.texture = texture;
    }

    public void drawTexture(GlUtils glUtils, MatrixStack matrixStack, int x, int y, int width, int height, int startX, int startY, int endX, int endY) {
        float minX = (float)startX / this.width;
        float minY = (float)startY / this.height;
        float maxX = (float)endX / this.width;
        float maxY = (float)endY / this.height;
        float[] vertices = new float[] {x,y,0,minX,minY,x,y + height,0,minX,maxY,x + width,y,0,maxX,minY,x + width,y + height,0,maxX,maxY};
        int[] indices = new int[] {0,1,2,2,1,3};

        VertexMesh mesh = new VertexMesh(shaderSource.vertexFormat);

        mesh.addData(indices, vertices);

        //GL11.glDisable(GL11.GL_DEPTH_TEST);
        glUtils.bindPipeline(shaderSource.program);
        glUtils.bindTexture(textureId);
        glUtils.uploadMatrix(matrixStack, shaderSource);
        glUtils.drawAndDestroyMesh(matrixStack,mesh);
    }

    public void drawTexture1(GlUtils glUtils, MatrixStack matrixStack, int x, int y, int z, int width, int height, int startX, int startY, int endX, int endY) {
        float minX = (float)startX / this.width;
        float minY = (float)startY / this.height;
        float maxX = (float)endX / this.width;
        float maxY = (float)endY / this.height;
        float[] vertices = new float[] {x,y,z,minX,minY,x,y,z + height,minX,maxY,x + width,y,z,maxX,minY,x + width,y,z + height,maxX,maxY};
        int[] indices = new int[] {0,1,2,2,1,3};

        VertexMesh mesh = new VertexMesh(shaderSource.vertexFormat);

        mesh.addData(indices, vertices);

        //GL11.glDisable(GL11.GL_DEPTH_TEST);
        glUtils.bindPipeline(shaderSource.program);
        glUtils.bindTexture(textureId);
        glUtils.uploadMatrix(matrixStack, shaderSource);
        glUtils.drawAndDestroyMesh(matrixStack,mesh);
    }

    public void drawTexture1(GlUtils glUtils, MatrixStack matrixStack, int x, int y, int z, int width, int height) {
        drawTexture1(glUtils, matrixStack,x,y,z,width,height,0,0,this.width,this.height);
    }

    public void drawTexture1f(GlUtils glUtils, MatrixStack matrixStack, int x, int y, int z, int width, int height) {
        drawTexture1(glUtils, matrixStack,x,y,z,width,height,this.width,0,0,this.height);
    }

    public void drawTexture(GlUtils glUtils, MatrixStack matrixStack, int x, int y, int width, int height) {
        drawTexture(glUtils, matrixStack,x,y,width,height,0,0,this.width,this.height);
    }

    public void drawTexture(GlUtils glUtils, MatrixStack matrixStack, int x, int y, int startX, int startY, int endX, int endY) {
        drawTexture(glUtils, matrixStack,x,y,(int)((endX - startX) * guiSize),(int) ((endY - startY) * guiSize),startX,startY,endX,endY);
    }

    @Override
    public String getResourceName() {
        return path;
    }

    @Override
    public String getIdentifierName() {
        return Main.PROJECT_NAME + ":" + path;
    }

    @Override
    public String getUniqueName() {
        return "texture." + Main.PROJECT_NAME + "." + path;
    }

    @Override
    public void load(GameInstance gameInstance) {
        shaderSource = gameInstance.SHADERS.get(Main.getFormattedName("position_texture"));
        if(shaderSource == null) {
            throw new RuntimeException("Failed to load shader source for rendering textures");
        }
        texture = (Image) gameInstance.RESOURCE_LOADER.getResource(new ResourceLocation(path));
        if(texture == null) {
            System.err.println(path);
            return;
        }
        width = texture.getWidth();
        height = texture.getHeight();
    }

    @Override
    public void loadGraphics(GlUtils glUtils) {
        IRegistryElement.super.loadGraphics(glUtils);
        textureId = glUtils.createTexture(texture);
    }

    @Override
    public void cleanupGraphics(GlUtils glUtils) {
        IRegistryElement.super.cleanupGraphics(glUtils);
        glUtils.destroyTexture(textureId);
    }
}

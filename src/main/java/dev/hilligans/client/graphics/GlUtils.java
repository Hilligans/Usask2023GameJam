package dev.hilligans.client.graphics;

import dev.hilligans.GameInstance;
import dev.hilligans.resource.ResourceLocation;
import dev.hilligans.resource.UnknownResourceException;
import dev.hilligans.util.Tuple;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class GlUtils {

    public Int2ObjectOpenHashMap<Tuple<Integer, Integer>> meshData = new Int2ObjectOpenHashMap<>();
    public Int2IntOpenHashMap textureTypes = new Int2IntOpenHashMap();
    public Int2LongOpenHashMap vertexArrayObjects = new Int2LongOpenHashMap();
    public Int2ObjectOpenHashMap<VertexMesh> meshReferences = new Int2ObjectOpenHashMap<>();

    public long boundTexture = -1;
    public long boundProgram = -1;

    public GameInstance gameInstance;

    public StringRenderer stringRenderer;


    public GlUtils(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public void setupStringRenderer() {
        stringRenderer = new StringRenderer(this);
        stringRenderer.buildChars();
    }

    public void drawMesh(MatrixStack matrixStack, long meshID, long indicesIndex, int length) {
        Tuple<Integer, Integer> data = meshData.get((int)meshID);
        if(data == null) {
            return;
        }
        glBindVertexArray((int)meshID);
        GL20.glDrawElements(data.typeA, length, data.typeB, indicesIndex);
    }

    public long createMesh(VertexMesh mesh) {
        if(mesh.vertexFormat == null) {
            mesh.vertexFormat = getFormat(mesh.vertexFormatName);
        }

        int VAO = glGenVertexArrays();
        int VBO = glGenBuffers();
        int EBO = glGenBuffers();

        glBindVertexArray(VAO);


        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, mesh.vertices, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, mesh.indices, GL_STATIC_DRAW);

        int x = 0;
        int stride = mesh.vertexFormat.getStride();
        int pointer = 0;
        for(VertexFormat.VertexPart part : mesh.vertexFormat.parts) {
            glVertexAttribPointer(x, part.primitiveCount, getGLPrimitive(part.primitiveType), part.normalized, stride, pointer);
            glEnableVertexAttribArray(x);
            pointer += part.getSize();
            x++;
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        mesh.destroy();

        meshReferences.put(VAO, mesh);
        meshData.put(VAO, new Tuple<>(GL_TRIANGLES, GL_UNSIGNED_INT));
        vertexArrayObjects.put(VAO, ((long)VBO << 32) | (long)EBO);
        return VAO;
    }

    public void destroyMesh(long mesh) {
        long array = vertexArrayObjects.get((int)mesh);
        meshData.remove((int)mesh);
        if((int)array != 0) {
            glDeleteBuffers((int) array);
        }
        glDeleteBuffers((int)(array >> 32));
        glDeleteBuffers((int)mesh);
    }

    private int getGLPrimitive(int type) {
        return type + 0x1400;
    }

    public long createTexture(Image image) {
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        int format = image.format == 4 ? GL_RGBA : GL_RGB;
        glTexImage2D(GL_TEXTURE_2D, 0, format, image.width, image.height, 0, format, GL_UNSIGNED_BYTE, image.buffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        textureTypes.put(texture, GL_TEXTURE_2D);
        return texture;
    }

    public long createTexture(ByteBuffer buffer, int width, int height, int format) {
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        format = format == 4 ? GL_RGBA : GL_RGB;
        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        textureTypes.put(texture, GL_TEXTURE_2D);
        return texture;
    }

    public void destroyTexture(long texture) {
        glDeleteTextures((int)texture);
        textureTypes.remove((int)texture);
    }

    public void drawAndDestroyMesh(MatrixStack matrixStack, VertexMesh mesh) {
        if(mesh.vertexFormat == null) {
            mesh.vertexFormat = getFormat(mesh.vertexFormatName);
        }
        glDisable(GL_DEPTH_TEST);

        int VAO = glGenVertexArrays();
        int VBO = glGenBuffers();
        int EBO = glGenBuffers();

        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, mesh.vertices, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, mesh.indices, GL_STATIC_DRAW);

        int x = 0;
        int stride = mesh.vertexFormat.getStride();
        int pointer = 0;
        for(VertexFormat.VertexPart part : mesh.vertexFormat.parts) {
            glVertexAttribPointer(x,part.primitiveCount, getGLPrimitive(part.primitiveType),part.normalized,stride,pointer);
            glEnableVertexAttribArray(x);
            pointer += part.getSize();
            x++;
        }

        glDrawElements(mesh.vertexFormat.primitiveType,mesh.indices.limit(),GL_UNSIGNED_INT,0);

        glDeleteBuffers(VBO);
        glDeleteBuffers(EBO);
        glDeleteBuffers(VAO);
    }

    public void bindTexture(long texture) {
        if(texture != boundTexture) {
            GL20.glBindTexture(textureTypes.get((int)texture), (int)texture);
            boundTexture = texture;
        }
    }

    public void bindPipeline(long pipeline) {
        if(pipeline != boundProgram){
            GL20.glUseProgram((int) pipeline);
            boundProgram = pipeline;
        }
    }

    public void setState(PipelineState state) {
        //if(graphicsContext.pipelineStateSet) {
        //    throw new RuntimeException("Graphics state was already set by the render task and cannot be reset inside the render task");
        //}
        if(state.depthTest) {
            glEnable(GL_DEPTH_TEST);
        } else {
            glDisable(GL_DEPTH_TEST);
        }
    }

    public long createProgram(ShaderSource shaderSource) {

        String vertex = gameInstance.RESOURCE_LOADER.getString(new ResourceLocation(shaderSource.vertexShader));
        String fragment = gameInstance.RESOURCE_LOADER.getString(new ResourceLocation(shaderSource.fragmentShader));
        String geometry = shaderSource.geometryShader == null ? null :  gameInstance.RESOURCE_LOADER.getString(new ResourceLocation(shaderSource.geometryShader));

        int shader;
        if(geometry == null) {
            shader = ShaderManager.registerShader(vertex, fragment);
        } else {
            shader = ShaderManager.registerShader(vertex, geometry, fragment);
        }

        if(shaderSource.uniformNames != null) {
            shaderSource.uniformIndexes = new int[shaderSource.uniformNames.size()];
            for(int x = 0; x < shaderSource.uniformNames.size(); x++) {
                shaderSource.uniformIndexes[x] = glGetUniformLocation(shader, shaderSource.uniformNames.get(x));
            }
        }

        return shader;
    }

    public void uploadData(FloatBuffer data, long index, String type, long program, ShaderSource shaderSource) {
        //if(program != boundProgram) {
        GL20.glUseProgram((int)program);
        boundProgram = program;
        // }
        if ("4fv".equals(type)) {
            GL33.glUniformMatrix4fv((int) index, false, data);
        } else if("4f".equals(type)) {
            GL33.glUniform4fv((int) index, data);
        } else {
            throw new RuntimeException();
        }
    }

    public void uploadMatrix(MatrixStack matrixStack, ShaderSource shaderSource) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            uploadData(memoryStack.floats(matrixStack.color.x, matrixStack.color.y, matrixStack.color.z, matrixStack.color.w), shaderSource.uniformIndexes[1], "4f", shaderSource.program, shaderSource);
            uploadData(matrixStack.matrix4f.get(memoryStack.mallocFloat(16)), shaderSource.uniformIndexes[0], "4fv", shaderSource.program, shaderSource);
        }
    }

    VertexFormat[] cache = new VertexFormat[2];

    public synchronized VertexFormat getFormat(String name) {
        if(cache[0] != null && cache[0].formatName.equals(name)) {
            return cache[0];
        }
        if(cache[1] != null && cache[1].formatName.equals(name)) {
            VertexFormat f = cache[0];
            cache[0] = cache[1];
            cache[1] = f;
            return cache[0];
        }
        for(VertexFormat vertexFormat : gameInstance.VERTEX_FORMATS.ELEMENTS) {
            if(vertexFormat.formatName.equals(name)) {
                cache[1] = vertexFormat;
                return vertexFormat;
            }
        }

        throw new UnknownResourceException("Failed to find resource in the registry by name: " + name, gameInstance.VERTEX_FORMATS, name);
    }
}

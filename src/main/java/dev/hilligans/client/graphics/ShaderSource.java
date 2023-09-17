package dev.hilligans.client.graphics;

import dev.hilligans.GameInstance;
import dev.hilligans.Main;
import dev.hilligans.registry.IRegistryElement;

import java.util.ArrayList;

public class ShaderSource implements IRegistryElement {

    public String format;
    public String name;
    public String vertexShader;
    public String fragmentShader;
    public String geometryShader;

    public VertexFormat vertexFormat;

    public int program;

    public ArrayList<String> uniformNames = new ArrayList<>(4);
    public ArrayList<String> uniformTypes = new ArrayList<>(4);
    public int[] uniformIndexes;

    public ShaderSource(String name, String format, String vertexShader, String fragmentShader) {
        this.name = name;
        this.format = format;
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }

    public ShaderSource(String name, String format, String vertexShader, String geometryShader, String fragmentShader) {
        this.name = name;
        this.format = format;
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
        this.geometryShader = geometryShader;
    }

    public ShaderSource withUniform(String name, String type) {
        uniformNames.add(name);
        uniformTypes.add(type);
        return this;
    }

    @Override
    public void load(GameInstance gameInstance) {
        vertexFormat = gameInstance.VERTEX_FORMATS.get(format);
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getIdentifierName() {
        return Main.PROJECT_NAME + ":" + name;
    }

    @Override
    public String getUniqueName() {
        return "shader." + Main.PROJECT_NAME + "." + name;
    }


    @Override
    public void loadGraphics(GlUtils glUtils) {
        program = (int) glUtils.createProgram(this);
    }

    @Override
    public String toString() {
        return "ShaderSource{" +
                "name='" + name + '\'' +
                ", program=" + program +
                '}';
    }
}

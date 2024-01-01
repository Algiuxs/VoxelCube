package com.renderer.Shaders;

import org.joml.Matrix4f;

public class StaticShader extends ShaderProgram {
    private static final String vertexFile = "src/main/java/com/renderer/res/shaders/VShader.glsl";
    private static final String fragmentFile = "src/main/java/com/renderer/res/shaders/FShader.glsl";

    int loacation_transformationMatrix;

    public StaticShader() {
        super(vertexFile, fragmentFile);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute("position", 0);
        super.bindAttribute("textureCoords", 1);
    }

    @Override
    protected void getAllUniformLoactions() {
        loacation_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(loacation_transformationMatrix, matrix);
    }
}

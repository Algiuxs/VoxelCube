package com.renderer.ToolBox;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Maths {
    public static Matrix4f createTransformationMatrix(Vector3f translation, float rotX, float rotY, float rotZ, float scale){
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
    
        matrix.translate(translation, matrix);
        matrix.rotate(rotX, new Vector3f(1, 0, 0), matrix);
        matrix.rotate(rotY, new Vector3f(0, 1, 0), matrix);
        matrix.rotate(rotZ, new Vector3f(0, 0, 1), matrix);
        matrix.scale(new Vector3f(scale, scale, scale));
        return matrix;
    }
}


package com.renderer.RenderEngine;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import com.renderer.Models.RawModel;

public class Loader {
    static List<Integer> vaos = new ArrayList<Integer>();
    static List<Integer> vbos = new ArrayList<Integer>();
    static List<Integer> textures = new ArrayList<Integer>();

    public RawModel loadToVao(float[] vertices, int[] indices, float[] uv) {
        int vaoID = createVAO();
        storeDataInAttributeList(vertices, 0, 3);
        storeDataInAttributeList(uv, 1, 2);
        bindIndicesBuffer(indices);
        GL30.glBindVertexArray(0);
        return new RawModel(vaoID, indices.length);
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    /*public int loadTexture(String fileName, String fileFormatNoDot){
        Texture tex = null;
        try {
            tex = TextureLoader.getTexture(fileFormatNoDot, new FileInputStream(new File("src/main/java/com/renderer/res/textures/"+ fileName + "." + fileFormatNoDot)));//TODO res folder location here
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Texture couldnt be loaded");
        }
        int textureID = tex.getTextureID();
        textures.add(textureID);
        return textureID;
    }*/

        public int loadTexture(String filename) {
            int textureId = 0;
            try {
                BufferedImage image = ImageIO.read(new File("src/main/java/com/renderer/res/textures/"+filename));
                if (image == null) {
                    System.err.println("Image is null");
                    System.exit(-1);
                } 
                int width = image.getWidth();
                int height = image.getHeight();
                if(width <= 0 || height <= 0){
                    throw new IOException("Invalid image dimensions: " + width + "x" + height);
                }
                int[] pixels_raw = new int [width * height * 4];
                pixels_raw = image.getRGB(0, 0, width, height, null, 0, width);
                ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
		    	for(int w = 0; w < width; w++) {
		    		for(int h = 0; h < height; h++) {
		    			int pixel = pixels_raw[w * width + h];
		    			pixels.put((byte)((pixel >> 16) & 0xFF));//Red
		    			pixels.put((byte)((pixel >> 8) & 0xFF));//Green
		    			pixels.put((byte)(pixel & 0xFF));		//Blue
		    			pixels.put((byte)((pixel >> 24) & 0xFF));//Alpha
		    		}
		    	}
                pixels.flip();
                textureId = GL11.glGenTextures();
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
                // Use nearest neighbor filtering 
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

                // Disable mipmaps
                GL14.glTexParameteri(GL14.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL14.GL_FALSE); 

                // Disable anti-aliasing
                GL11.glDisable(GL13.GL_MULTISAMPLE);




                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
                int error = GL11.glGetError();
                if(error != GL11.GL_NO_ERROR){
                    throw new RuntimeException("OpenGL error: " + error);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Could not load texture " + filename + ": " + e.getMessage());
                System.exit(-1);
            }
        return textureId;
    }

    private void storeDataInAttributeList(float[] data, int attributeNumber, int dimensions) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = StoreDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, dimensions, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
    private void bindIndicesBuffer(int[] indices){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }
    IntBuffer storeDataInIntBuffer(int[] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer StoreDataInFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public void cleanUp(){
        vaos.forEach(GL30::glDeleteVertexArrays);
        vbos.forEach(GL15::glDeleteBuffers);
        textures.forEach(GL11::glDeleteTextures);
    }
}

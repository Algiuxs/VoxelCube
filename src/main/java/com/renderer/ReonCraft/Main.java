package com.renderer.ReonCraft;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import com.renderer.Entities.Entity;
import com.renderer.Models.RawModel;
import com.renderer.Models.TexturedModel;
import com.renderer.RenderEngine.DisplayManager;
import com.renderer.RenderEngine.Loader;
import com.renderer.RenderEngine.MasterRenderer;
import com.renderer.Shaders.StaticShader;
import com.renderer.Textures.ModelTexture;

public class Main {

	public static Loader loader1 = null;
	public static StaticShader shader1 = null;

	public Main() {
		DisplayManager.setCallbacks();
		DisplayManager Display = new DisplayManager();
		MasterRenderer renderer = new MasterRenderer();
		
		

		if(!GLFW.glfwInit()) {
			throw new IllegalStateException("Failed to initialize GLFW");
		}
		Display.setSize(900, 600);
		Display.createWindow("Engine");
		GL.createCapabilities();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		Loader loader = new Loader();
		loader1 = loader;
		StaticShader shader = new StaticShader(); 
		shader1 = shader;

		float[] vertices = new float[] {
			-0.5f, 0.5f, 0,
			-0.5f, -0.5f, 0,
			0.5f,-0.5f, 0,
			0.5f, 0.5f, 0
		};
		int[] indices = new int[] {
			0,1,2,
			2,3,0
		};
		float[] UV = new float[] {
			0,0,
			0,1,
			1,1,
			1,0
		};

		RawModel model = loader.loadToVao(vertices, indices, UV);
		ModelTexture texture = new ModelTexture(loader.loadTexture("dirtTex.png"));
		TexturedModel texModel = new TexturedModel(model, texture);
		Entity entity = new Entity(texModel, new Vector3f(0, 0, 0), 0, 0, 0, 1);

		while (!Display.shouldClose()) {
			renderer.draw();
			shader.start();
			renderer.render(entity, shader);
			shader.stop();
			Display.updateWindow();
		}
		Display.closeWindow();
	}
	public static void main(String[] args) {
		new Main();
	}

}

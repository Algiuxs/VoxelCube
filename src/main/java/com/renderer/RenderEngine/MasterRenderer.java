package com.renderer.RenderEngine;

import org.lwjgl.opengl.GL11;

import com.renderer.Entities.Entity;
import com.renderer.Shaders.StaticShader;

public class MasterRenderer {
	
	public void draw() {
		GL11.glClearColor(0.4f, 0.7f, 1.0f, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	public void render(Entity entity, StaticShader shader){
		EntityRenderer.render(entity, shader);
	}

}

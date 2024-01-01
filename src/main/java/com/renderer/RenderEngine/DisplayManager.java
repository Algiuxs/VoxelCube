package com.renderer.RenderEngine;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;

import com.renderer.IO.Input;
import com.renderer.ReonCraft.Main;

public class DisplayManager {

	private long windowID;
	private boolean fulscreen;
	private int width;
	private int height;
	private Input input;
	private int FPS_CAP = 120; //TODO implement fps lock
	
	public static void setCallbacks() {
		GLFW.glfwSetErrorCallback(new GLFWErrorCallback() {
			@Override
			public void invoke(int err, long desc) {
				throw new IllegalStateException(GLFWErrorCallback.getDescription(desc));
			}
			
		});

	}

	public DisplayManager() {
		setSize(640, 480);
		setFulscreen(false);

	}

	public void createWindow(String Title) {
		windowID = GLFW.glfwCreateWindow(width, height, Title, fulscreen ? GLFW.glfwGetPrimaryMonitor() : 0, 0);
		
		if(windowID == MemoryUtil.NULL) {
			throw new IllegalStateException("Could not Create GLFW window");
		}
		if(!fulscreen) {
			GLFWVidMode vid = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

			GLFW.glfwSetWindowPos(windowID, (vid.width()-width)/2, (vid.height()-height)/2);
		}
		GLFW.glfwShowWindow(windowID);
		GLFW.glfwMakeContextCurrent(windowID);
		input = new Input(windowID);
	}

	public void closeWindow() {
		Main.loader1.cleanUp();
		Main.shader1.cleanUp();
		Callbacks.glfwFreeCallbacks(windowID);
		GLFW.glfwDestroyWindow(windowID);
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();	
	}

	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(windowID);
	}
	
	public void updateWindow() {
		if(getInput().isKeyReleased(GLFW.GLFW_KEY_ESCAPE)) {
			GLFW.glfwSetWindowShouldClose(windowID, true);
		}
		update();
		GLFW.glfwSwapBuffers(windowID);
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void setFpsCap(int fps) {
		FPS_CAP = fps;
	}
	public void setFulscreen(boolean fulscreen) {
		this.fulscreen = fulscreen;
	}

	public void update(){
		input.update();
		GLFW.glfwPollEvents();
	}

	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}
	public boolean isFulscreen() {
		return fulscreen;
	}
	public long getWindowID() {
		return windowID;
	}
	public int getFpsCap(){
		return FPS_CAP;
	}
	public Input getInput() {
		return input;
	}
}

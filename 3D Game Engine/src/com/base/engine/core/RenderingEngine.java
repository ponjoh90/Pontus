package com.base.engine.core;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

import com.base.engine.rendering.BasicShader;
import com.base.engine.rendering.Camera;
import com.base.engine.rendering.Shader;
import com.base.engine.rendering.Window;

public class RenderingEngine {
	private Camera mainCamera;
	
	public RenderingEngine(){
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		glFrontFace(GL_CW); //CLOCKWISE
		glCullFace(GL_BACK); //Inte rita baksidan
		glEnable(GL_CULL_FACE); //inte rita baksidan
		glEnable(GL_DEPTH_TEST); //h�ller koll p� djupet...typ

		glEnable(GL_DEPTH_CLAMP);
		
		glEnable(GL_TEXTURE_2D);
		
		mainCamera = new Camera((float)Math.toRadians(70.0f), (float)Window.getWidth()/(float)Window.getHeight(), 0.01f, 1000);
	}
	
	public void input(float delta){
		mainCamera.input(delta);
	}
	
	public void render(GameObject object){
		clearScreen();
		
		Shader shader = BasicShader.getInstance();
		shader.setRenderingEngine(this);
		
		object.render(BasicShader.getInstance());
	}
	
	public static void clearScreen(){
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	private static void setTextures(boolean enabled){
		if(enabled)
			glEnable(GL_TEXTURE_2D);
		else
			glDisable(GL_TEXTURE_2D);
	}
	
	private static void unbindTextures(){
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	private static void setClearColor(Vector3f color){
		glClearColor(color.getX(), color.getY(), color.getZ(), 1.0f);
	}
	
	public static String getOpenGLVersion(){
		return glGetString(GL_VERSION);
	}

	public Camera getMainCamera() {
		return mainCamera;
	}

	public void setMainCamera(Camera mainCamera) {
		this.mainCamera = mainCamera;
	}
}

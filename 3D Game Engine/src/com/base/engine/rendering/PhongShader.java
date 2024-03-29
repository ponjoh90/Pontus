package com.base.engine.rendering;

import static org.lwjgl.opengl.GL20.glUniform1i;

import com.base.engine.core.Matrix4f;
import com.base.engine.core.ResourceLoader;
import com.base.engine.core.Transform;
import com.base.engine.core.Vector3f;


public class PhongShader extends Shader{
	private static final int MAX_POINT_LIGHTS = 4;
	private static final int MAX_SPOT_LIGHTS = 4;
	
	private static final PhongShader instance = new PhongShader();
	
	public static PhongShader getInstance()
	{
		return instance;
	}
	
	private static Vector3f ambientLight = new Vector3f(0.1f, 0.1f, 0.1f);
	private static DirectionalLight directionalLight = new DirectionalLight(new BaseLight(new Vector3f(1, 1, 1), 0), new Vector3f(0, 0, 0));
	private static PointLight[] pointLights = new PointLight[]{};
	private static SpotLight[] spotLights = new SpotLight[]{};
	
	public PhongShader(){
		super();
		
		addVertexShader(ResourceLoader.loadShader("phongVertex.vs"));
		addFragmentShader(ResourceLoader.loadShader("phongFragment.fs"));
		compileShader();
		
		addUniform("transform");
		addUniform("transformProjected");
		addUniform("baseColor");
		addUniform("ambientLight");
		
		addUniform("specularIntensity");
		addUniform("specularPower");
		addUniform("eyePos");
		
		addUniform("directionalLight.base.color");
		addUniform("directionalLight.base.intensity");
		addUniform("directionalLight.direction");
		
		for(int i = 0; i < MAX_POINT_LIGHTS; i++){
			addUniform("pointLights[" + i + "].base.color");
			addUniform("pointLights[" + i + "].base.intensity");
			addUniform("pointLights[" + i + "].attenuation.constant");
			addUniform("pointLights[" + i + "].attenuation.linear");
			addUniform("pointLights[" + i + "].attenuation.exponent");
			addUniform("pointLights[" + i + "].position");
			addUniform("pointLights[" + i + "].range");
		}
		
		for(int i = 0; i < MAX_SPOT_LIGHTS; i++){
			addUniform("spotLights[" + i + "].pointLight.base.color");
			addUniform("spotLights[" + i + "].pointLight.base.intensity");
			addUniform("spotLights[" + i + "].pointLight.attenuation.constant");
			addUniform("spotLights[" + i + "].pointLight.attenuation.linear");
			addUniform("spotLights[" + i + "].pointLight.attenuation.exponent");
			addUniform("spotLights[" + i + "].pointLight.position");
			addUniform("spotLights[" + i + "].pointLight.range");
			addUniform("spotLights[" + i + "].direction");
			addUniform("spotLights[" + i + "].cutoff");
		}
	}
	
	public void updateUniform(Transform transform, Material material){
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f projectedMatrix = getRenderingEngine().getMainCamera().getViewProjection().mul(worldMatrix);
		material.getTexture().bind();
		
		setUniform("transformProjected", projectedMatrix);
		setUniform("transform", worldMatrix);
		setUniform("baseColor", material.getColor());
		
		setUniform("ambientLight", ambientLight);
		setUniform("directionalLight", directionalLight);
		for(int i = 0; i < pointLights.length; ++i){
			setUniform("pointLights[" + i + "]", pointLights[i]);
		}
		
		for(int i = 0; i < spotLights.length; ++i){
			setUniform("spotLights[" + i + "]", spotLights[i]);
		}
		
		setUniformf("specularIntensity", material.getSpecualIntensity());
		setUniformf("specularPower", material.getSpecularPower());
		
		setUniform("eyePos", getRenderingEngine().getMainCamera().getPos());
	}

	public void setUniform(String uniformName, BaseLight baseLight){
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
	}
	
	public void setUniform(String uniformName, DirectionalLight directionalLight){
		setUniform(uniformName + ".base", directionalLight.getBase());
		setUniform(uniformName + ".direction", directionalLight.getDirection());
	}
	
	public void setUniform(String uniformName, PointLight pointLight){
		setUniform(uniformName + ".base", pointLight.getBaseLight());
		setUniformf(uniformName + ".attenuation.constant", pointLight.getAttenuation().getConstant());
		setUniformf(uniformName + ".attenuation.linear", pointLight.getAttenuation().getLinear());
		setUniformf(uniformName + ".attenuation.exponent", pointLight.getAttenuation().getExponent());
		setUniform(uniformName + ".position", pointLight.getPosition());
		setUniformf(uniformName + ".range", pointLight.getRange());
	}
	
	public void setUniform(String uniformName, SpotLight spotLight){
		setUniform(uniformName + ".pointLight", spotLight.getPointLight());
		setUniform(uniformName + ".direction", spotLight.getDirection());
		setUniformf(uniformName + ".cutoff", spotLight.getCutoff());
	}
	
	public static Vector3f getAmbientLight() {
		return ambientLight;
	}

	public static void setAmbientLight(Vector3f ambientLight) {
		PhongShader.ambientLight = ambientLight;
	}

	public static void setDirectionalLight(DirectionalLight directionalLight)
	{
		PhongShader.directionalLight = directionalLight;
	}
	
	public static void setPointLight(PointLight[] pointLights){
		if(pointLights.length > MAX_POINT_LIGHTS){
			System.err.println("Error: You passed in too many pointlights, max is " + MAX_POINT_LIGHTS + ", you added" + pointLights.length);
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		PhongShader.pointLights = pointLights;
	}
	
	public static void setSpotLight(SpotLight[] spotLights){
		if(spotLights.length > MAX_SPOT_LIGHTS){
			System.err.println("Error: You passed in too many spotlights, max is " + MAX_SPOT_LIGHTS + ", you added" + spotLights.length);
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		PhongShader.spotLights = spotLights;
	}
}

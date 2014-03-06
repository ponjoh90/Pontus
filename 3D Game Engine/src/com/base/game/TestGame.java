package com.base.game;

import com.base.engine.core.Game;
import com.base.engine.core.GameObject;
import com.base.engine.core.ResourceLoader;
import com.base.engine.core.Time;
import com.base.engine.core.Transform;
import com.base.engine.core.Vector2f;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Attenuation;
import com.base.engine.rendering.BaseLight;
import com.base.engine.rendering.Camera;
import com.base.engine.rendering.DirectionalLight;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.PhongShader;
import com.base.engine.rendering.PointLight;
import com.base.engine.rendering.Shader;
import com.base.engine.rendering.SpotLight;
import com.base.engine.rendering.Vertex;
import com.base.engine.rendering.Window;

public class TestGame extends Game{

	public void init (){
		float fieldWidth = 10.0f;
		float fieldDepth = 10.0f;
		
		Vertex[] vertices = new Vertex[] { 	new Vertex( new Vector3f(-fieldWidth, -1.0f, -fieldDepth), new Vector2f(0.0f, 0.0f)),
											new Vertex( new Vector3f(-fieldWidth, -1.0f, fieldDepth * 3), new Vector2f(0.0f, 1.0f)),
											new Vertex( new Vector3f(fieldWidth * 3, -1.0f, -fieldDepth), new Vector2f(1.0f, 0.0f)),
											new Vertex( new Vector3f(fieldWidth * 3, -1.0f, fieldDepth * 3), new Vector2f(1.0f, 1.0f))};
	
		int indices[] = {	0, 1, 2,
							2, 1, 3};
		
		Mesh mesh = new Mesh();//;ResourceLoader.loadMesh("kub.obj");
		mesh.addVertices(vertices, indices, true);
		Material material = new Material(ResourceLoader.loadTexture("amulettTexture.png"), new Vector3f(1, 1, 1), 1, 8);
		
		MeshRenderer meshRenderer = new MeshRenderer(mesh, material);
		
		GameObject planeObject = new GameObject();
		planeObject.addComponent(meshRenderer);
		
		getRootObject().addChild(planeObject);
			}
}

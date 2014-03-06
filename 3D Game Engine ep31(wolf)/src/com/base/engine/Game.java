package com.base.engine;

public class Game {
	private Mesh mesh1;
	private Mesh mesh2;
	private Shader shader;
	private Transform transform;
	private Material material;
	private Camera camera;
	
	PointLight pLight1 = new PointLight(new BaseLight(new Vector3f(1,0,0), 0.8f), new Attenuation(0,0,1), new Vector3f(-2, 0, 3), 10);
	PointLight pLight2 = new PointLight(new BaseLight(new Vector3f(0,1,0), 0.8f), new Attenuation(0,0,1), new Vector3f(2, 0, 7), 10);
	
	SpotLight sLight1 = new SpotLight(new PointLight(new BaseLight(new Vector3f(1,0,0), 0.8f), new Attenuation(0,0,1), new Vector3f(-2, 0, 3), 10),
						new Vector3f(1,1,1), 0.7f);
	
	public Game(){
		mesh1 = ResourceLoader.loadMesh("kub.obj");
		material = new Material(ResourceLoader.loadTexture("amulettTexture.png"), new Vector3f(1, 1, 1), 1, 8);
		shader = PhongShader.getInstance();
		camera = new Camera();
		transform = new Transform();
		
		float fieldWidth = 10.0f;
		float fieldDepth = 10.0f;
		
		Vertex[] vertices = new Vertex[] { 	new Vertex( new Vector3f(-fieldWidth, -1.0f, -fieldDepth), new Vector2f(0.0f, 0.0f)),
											new Vertex( new Vector3f(-fieldWidth, -1.0f, fieldDepth * 3), new Vector2f(0.0f, 1.0f)),
											new Vertex( new Vector3f(fieldWidth * 3, -1.0f, -fieldDepth), new Vector2f(1.0f, 0.0f)),
											new Vertex( new Vector3f(fieldWidth * 3, -1.0f, fieldDepth * 3), new Vector2f(1.0f, 1.0f))};
	
		int indices[] = {	0, 1, 2,
							2, 1, 3};
		
		mesh2 = new Mesh();
		mesh2.addVertices(vertices, indices, true);
		
		Transform.setProjection(70f, Window.getWidth(), Window.getHeight(), 0.1f, 1000);
		Transform.setCamera(camera);
		
		//PhongShader.setAmbientLight(new Vector3f(0.1f, 0.1f, 0.1f));
		//PhongShader.setDirectionalLight(new DirectionalLight(new BaseLight(new Vector3f(0.5f, 0.5f, 0.5f), 0.8f), new Vector3f(1, 1, 1)));
		//PhongShader.setPointLight(new PointLight[]{pLight1, pLight2});
		PhongShader.setSpotLight(new SpotLight[]{sLight1});
	}
	
	public void input(){
		camera.input();
	}
	
	float temp = 0.0f;
	
	public void update(){
		temp += Time.getDelta();
		transform.setTranslation(0, 0, 5);
		//transform.setRotation(0, (float)Math.sin(temp) * 180, 0);//(float)Math.sin(temp) * 180);
		transform.setScale(1f, 1f ,1f);
		pLight1.setPosition(new Vector3f(3,0.0f,8.0f * (float)(Math.sin(temp)+ 1.0/2.0) + 10));
		pLight2.setPosition(new Vector3f(7,0,8.0f * (float)(Math.cos(temp)+ 1.0/2.0) + 10));
	
		sLight1.getPointLight().setPosition(camera.getPos());
		sLight1.setDirection(camera.getForward());
	}
	
	public void render(){
		RenderUtil.setClearColor(Transform.getCamera().getPos().div(2048f).abs());
		
		shader.bind();
		shader.updateUniform(transform.getTransformation(), transform.getProjectedTransformation(), material);
		
		mesh1.draw();
		mesh2.draw();
	}
}

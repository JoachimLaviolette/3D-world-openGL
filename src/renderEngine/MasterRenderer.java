package renderEngine;

import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;

public class MasterRenderer {
	private static final float RED = .1f;
	private static final float BLUE = .1f;
	private static final float GREEN = .1f;
	private static final float ALPHA = 1f;	
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = .1f;
	private static final float FAR_PLANE = 1000f;	

	private Matrix4f projectionMatrix;
	
	private EntityRenderer entityRenderer;	
	private TerrainRenderer terrainRenderer;
	private SkyboxRenderer skyboxRenderer;

	private StaticShader staticShader;
	private TerrainShader terrainShader;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();

	public MasterRenderer(Loader loader, boolean useAmbiantLights) {
		enableCulling();
		this.staticShader = new StaticShader(useAmbiantLights);
		this.terrainShader = new TerrainShader(useAmbiantLights);
		createProjectionMatrix();
		this.entityRenderer = new EntityRenderer(staticShader, projectionMatrix);
		this.terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		this.skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
	}
	
	public void prepare() {
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(RED, BLUE, GREEN, ALPHA);
	}
	
	public void render(List<Light> lights, Camera camera) {
		prepare();
		
		staticShader.start();
		staticShader.loadSkyColour(RED, GREEN, BLUE);
		staticShader.loadLights(lights);
		staticShader.loadViewMatrix(camera);
		entityRenderer.render(entities);
		staticShader.stop();
		
		terrainShader.start();
		terrainShader.loadSkyColour(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		skyboxRenderer.render(camera);
		
		terrains.clear();
		entities.clear();
	}
	
	public void processEntity(Entity entity) {
		TexturedModel entityTextureModel = entity.getTexturedModel();
		List<Entity> batch = entities.get(entityTextureModel);
		
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityTextureModel, newBatch);
		}
	}
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	private void createProjectionMatrix() {
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        
        int screenWidth = GLFWvidmode.width(vidmode);
        int screenHeight = GLFWvidmode.height(vidmode);
        
		float aspectRatio = (float) screenWidth / (float) screenHeight;
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustrum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustrum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustrum_length);
		projectionMatrix.m33 = 0;
	}
	
	private void enableCulling() {
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
	}
	
	public void cleanUp() {
		staticShader.cleanUp();
		terrainShader.cleanUp();
	}
}

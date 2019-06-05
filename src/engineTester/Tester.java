package engineTester;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;

public class Tester {
	public static void main(String[] args) {
		long windowID = DisplayManager.createDisplay();
		Loader loader = new Loader();
		Camera camera = new Camera();		
		boolean useAmbiantLights = false;
		MasterRenderer masterRenderer = new MasterRenderer(useAmbiantLights);
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		
		// Tree entities
		ModelTexture treeModelTexture = new ModelTexture(loader.loadTexture("tree"), 1f, 0f);
		RawModel treeModel = OBJLoader.loadOBJModel("tree", loader);
		TexturedModel treeTexturedModel = new TexturedModel(treeModel, treeModelTexture);
		
		for(int i = 0; i < 100; i++) {
			entities.add(
					new Entity(
							treeTexturedModel,
							new Vector3f(
									Math.abs(random.nextFloat() * 800 - 400),
									0f,
									random.nextFloat() * 800),
							0,
							0,
							0,
							3)
					);
		}
		
		// Weed entities
		ModelTexture weedModelTexture = new ModelTexture(loader.loadTexture("high_grass"), 1f, .3f);
		RawModel weedModel = OBJLoader.loadOBJModel("high_grass", loader);
		TexturedModel weedTexturedModel = new TexturedModel(weedModel, weedModelTexture);
		
		for(int i = 0; i < 4000; i++) {
			entities.add(
					new Entity(
							weedTexturedModel,
							new Vector3f(
									Math.abs(random.nextFloat() * 800 - 400),
									0f,
									random.nextFloat() * 800),
							0,
							0,
							0,
							30f)
					);
		}
		
		// Panel entities
		ModelTexture panelModelTexture = new ModelTexture(loader.loadTexture("image"), 1f, .3f);
		RawModel panelModel = OBJLoader.loadOBJModel("exampleObj", loader);
		TexturedModel panelTexturedModel = new TexturedModel(panelModel, panelModelTexture);
		
		for(int i = 0; i < 15; i++) {
			entities.add(
					new Entity(
							panelTexturedModel,
							new Vector3f(
									Math.abs(random.nextFloat() * 800 - 400),
									//0,
									0f,
									random.nextFloat() * 800),
							0,
							Math.abs(random.nextFloat() * 360 - 0),
							0,
							3)
					);
		}
		
		// Lights		
		Light light_0 = new Light(
				new Vector4f(200f, 200f, 100f, 1f),
				new Vector3f(1f, 1f, 1f),
				new Vector3f(0f, 0f, 0f),
				new Vector3f(0.9f, 0.0f, 0.0f),
				new Vector3f(1.0f, 1.0f, 1.0f));
		
		Light light_1 = new Light(
				new Vector4f(100f, 100f, 4f, 1f),
				new Vector3f(1f, .5f, 0f),
				new Vector3f(0f, 0f, 0f),
				new Vector3f(0.0f, 0.0f, 0.9f),
				new Vector3f(1.0f, 1.0f, 1.0f));
		
		List<Light> lights = new ArrayList<Light>();
		lights.add(light_0);
		lights.add(light_1);
		
		// Terrains
		Terrain terrain = new Terrain(
				0,
				0,
				loader,
				new ModelTexture(loader.loadTexture("grassTexture")));
		Terrain terrain2 = new Terrain(
				1,
				0,
				loader,
				new ModelTexture(loader.loadTexture("grassTexture")));
				
		while (glfwWindowShouldClose(windowID) != GL_TRUE) {
			camera.move(windowID);
			camera.moveBack();
			//camera.moveRight();
			//camera.moveUp();
			
			masterRenderer.processTerrain(terrain);
			masterRenderer.processTerrain(terrain2);
			
			for(Entity e: entities){
				//e.increaseRotation(0, 0.09f, 0f);
				masterRenderer.processEntity(e);
			}
			
			masterRenderer.render(lights, camera);
			
			// Swap display buffers
			glfwSwapBuffers(windowID);			

			// Poll for window events.
			glfwPollEvents();
		}
		
		masterRenderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();		
	}
}

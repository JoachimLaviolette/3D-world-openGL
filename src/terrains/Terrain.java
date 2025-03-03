package terrains;

import models.RawModel;
import renderEngine.Loader;
import textures.ModelTexture;

public class Terrain {
	private static final int SIZE = 800;
	private static final int VERTEX_COUNT = 128;
	
	private float x;
	private float y;
	private RawModel rawModel;
	private ModelTexture modelTexture;
	
	public Terrain(int _X, int _Y, Loader loader, ModelTexture modelTexture) {
		this.x = _X * SIZE;
		this.y = _Y * SIZE;
		this.modelTexture = modelTexture;	
		this.rawModel = generateTerrain(loader);
	}
	
	private RawModel generateTerrain(Loader loader) {
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		
		for(int i = 0; i < VERTEX_COUNT; i++) {
			for(int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = 0;
				vertices[vertexPointer * 3 + 2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				normals[vertexPointer * 3] = 0;
				normals[vertexPointer * 3 + 1] = 1;
				normals[vertexPointer * 3 + 2] = 0;
				textureCoords[vertexPointer * 2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		
		int pointer = 0;
		
		for(int y = 0; y < VERTEX_COUNT - 1; y++) {
			for(int x = 0; x < VERTEX_COUNT - 1; x++) {
				int topLeft = (y * VERTEX_COUNT) + x;
				int topRight = topLeft + 1;
				int bottomLeft = ((y + 1) * VERTEX_COUNT) + x;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getModelTexture() {
		return modelTexture;
	}
}

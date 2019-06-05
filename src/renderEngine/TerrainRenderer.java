package renderEngine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.List;

import models.RawModel;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import shaders.TerrainShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexturePackage;
import toolbox.Maths;

public class TerrainRenderer {

	private TerrainShader terrainShader;

	public TerrainRenderer(TerrainShader terrainShader, Matrix4f projectionMatrix) {
		this.terrainShader = terrainShader;
		terrainShader.start();
		terrainShader.loadProjectionMatrix(projectionMatrix);
		terrainShader.connectTextureUnits();
		terrainShader.stop();
	}

	public void render(List<Terrain> terrains) {
		for (Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			glDrawElements(GL_TRIANGLES, terrain.getRawModel().getVertexCount(),
					GL_UNSIGNED_INT, 0);
			unbindTexturedModel();
		}
	}

	private void prepareTerrain(Terrain terrain) {
		RawModel rawModel = terrain.getRawModel();
		glBindVertexArray(rawModel.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		bindTextures(terrain);
		terrainShader.loadShineVariables(1, 0);	
	}
	
	private void bindTextures(Terrain terrain) {
		TerrainTexturePackage terrainTexturePackage = terrain.getTerrainTexturePackage();
		
		// Bind terrain texture
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, terrainTexturePackage.getTerraintexture().getTextureID());

		// Bind r texture
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, terrainTexturePackage.getRTexture().getTextureID());
		
		// Bind g texture
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, terrainTexturePackage.getGTexture().getTextureID());
		
		// Bind b texture
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, terrainTexturePackage.getBTexture().getTextureID());
	
		// Bind blend map texture
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
	
	}

	private void unbindTexturedModel() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
	}

	private void loadModelMatrix(Terrain terrain) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
				new Vector3f(terrain.getX(), 0, terrain.getY()), 0, 0, 0, 1);
		terrainShader.loadTransformationMatrix(transformationMatrix);
	}

}

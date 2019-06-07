package renderEngine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import de.matthiasmann.twl.utils.PNGDecoder;

import models.RawModel;
import textures.TextureData;

public class Loader {
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	/**
	 * Load the given data into a VAO
	 * @param data
	 * @return A new raw model from the given data
	 */
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		
		return new RawModel(vaoID, indices.length);
	}
	
	/**
	 * Load data to VAO
	 * Used for cube mapping
	 * @param positions
	 * @param dimensions
	 * @return
	 */
	public RawModel loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		
		return new RawModel(vaoID, positions.length / dimensions);
	}
	
	public int loadTexture(String textureFile) {
		Texture texture = null;
		
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/texture/" + textureFile + ".png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int textureID = texture.getTextureID();
		textures.add(textureID);
		
		// Texture filtering method
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		
		return textureID;
	}
	
	public int loadCubeMap(String[] textureFilenames) {
		int textureID = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, textureID);
		
		for (int x = 0; x < textureFilenames.length; x++) {
			TextureData textureData = decodeTextureFile("skybox/" + textureFilenames[x]);
			glTexImage2D(
					GL_TEXTURE_CUBE_MAP_POSITIVE_X + x,
					0,
					GL_RGBA,
					textureData.getWidth(),
					textureData.getHeight(),
					0,
					GL_RGBA, 
					GL_UNSIGNED_BYTE,
					textureData.getBb()
					);
		}
		
		textures.add(textureID);
		
		// Texture filtering method
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER,	GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER,	GL_LINEAR);
		
		
		return textureID;
	}
	
	/**
	 * Create a new VAO
	 * @return The VAO id
	 */
	private int createVAO() {
		int vaoID = glGenVertexArrays();
		vaos.add(vaoID);
		glBindVertexArray(vaoID);
		
		return vaoID;
	}
	
	/**
	 * Create a new VBO
	 * @return The VBO id
	 */
	private int createVBO(int bufferType) {
		int vboID = glGenBuffers();
		vbos.add(vboID);
		glBindBuffer(bufferType, vboID);
		
		return vboID;
	}
	
	/**
	 * Unbind the current VAO
	 */
	private void unbindVAO() {
		glBindVertexArray(0);
	}
	
	/**
	 * Unbind the current VBO
	 */
	private void unbindVBO(int bufferType) {
		glBindBuffer(bufferType, 0);		
	}
	
	private void bindIndicesBuffer(int[] indices) {
		createVBO(GL_ELEMENT_ARRAY_BUFFER);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
	}
	
	/**
	 * Store the given data in a VBO
	 * @param attributeNumber
	 * @param data
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		createVBO(GL_ARRAY_BUFFER);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(attributeNumber, coordinateSize, GL_FLOAT, false, 0, 0);
		unbindVBO(GL_ARRAY_BUFFER);
	}
	
	/**
	 * Store the given data in a int buffer
	 * @param data
	 * @return The float buffer containing the given data
	 */
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	/**
	 * Store the given data in a float buffer
	 * @param data
	 * @return The float buffer containing the given data
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	/**
	 * Decode a texture file
	 * @param filename Name of the texture file to decode
	 * @return
	 */
	private TextureData decodeTextureFile(String filename) {
		int width = 0;
		int height = 0;
		ByteBuffer bb = null;
		try {
		    FileInputStream in = new FileInputStream("res/" + filename + ".png");
		    PNGDecoder decoder = new PNGDecoder(in);
		    width = decoder.getWidth();
		    height = decoder.getHeight();
		    bb = ByteBuffer.allocateDirect(4 * width * height);
		    decoder.decode(bb, width * 4, PNGDecoder.Format.RGBA);
		    bb.flip();
		    in.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			System.err.println("Texture " + filename + " loading failed!");
			System.exit(-1);
		}
		
		return new TextureData(width, height, bb);
	}
	
	/**
	 * Delete all vaos and vbos that have been created
	 */
	public void cleanUp() {
		for (int vao: vaos) {
			glDeleteVertexArrays(vao);
		}
		
		for (int vbo: vbos) {
			glDeleteBuffers(vbo);
		}
		
		for (int texture: textures) {
			glDeleteTextures(texture);
		}
	}	
}

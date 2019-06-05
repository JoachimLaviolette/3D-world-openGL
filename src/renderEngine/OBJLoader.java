package renderEngine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;

public class OBJLoader {
	public static RawModel loadOBJModel(String OBJFile, Loader loader) {
		String line = null;
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] texturesArray = null;
		int[] indicesArray = null;

		try (BufferedReader reader = new BufferedReader(new FileReader("res/" + OBJFile + ".obj"))) {
			while(true) {
				line = reader.readLine();
				if (!line.isEmpty() && !line.startsWith("#")) {
					// To normalize data each line
					line = line.replaceAll("\\s{2,}", " ").trim();
					// Tokenize the line
					String[] currentLine = line.split(" ");
					
					if (line.startsWith("v ")) {
						// vertex
						Vector3f vertex = new Vector3f(
								Float.parseFloat(currentLine[1]),
								Float.parseFloat(currentLine[2]), 
								Float.parseFloat(currentLine[3]));
						vertices.add(vertex);
					} else if (line.startsWith("vt ")) {
						// texture
						Vector2f texture = new Vector2f(
								Float.parseFloat(currentLine[1]),
								Float.parseFloat(currentLine[2]));
						textures.add(texture);
					} else if (line.startsWith("vn ")) {
						// normal
						Vector3f normal = new Vector3f(
								Float.parseFloat(currentLine[1]),
								Float.parseFloat(currentLine[2]), 
								Float.parseFloat(currentLine[3]));
						normals.add(normal);
					} else if (line.startsWith("f ")) {
						// face
						texturesArray = new float[vertices.size() * 2];
						normalsArray = new float[vertices.size() * 3];
						break;
					}
				}
			}
				
			while (line != null) {
				if (!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				
				String[] currentLine = line.split(" ");
				//System.out.println(line);
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
										
				processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray);
				processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray);
				processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray);
				line = reader.readLine();
			}	
			
			reader.close();
		} catch (Exception e) {
			throw new RuntimeException("Unable to load obj data from file: " + OBJFile + ".", e);
		}
		
		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];
		
		int vertexPointer = 0;
		
		for (Vector3f vertex: vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;			
		}
		
		for (int i = 0; i < indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}
		
		return loader.loadToVAO(verticesArray, texturesArray, normalsArray, indicesArray);
	}
	
	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals,
			float[] texturesArray, float[] normalsArray) {
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);
		
		if (!textures.isEmpty()) {
			Vector2f currentTexture = textures.get(Integer.parseInt(vertexData[1]) - 1);
			texturesArray[currentVertexPointer * 2] = currentTexture.x;
			texturesArray[currentVertexPointer * 2 + 1] = currentTexture.y;
		}
		
		if (!normals.isEmpty()) {
			Vector3f currentNormal = normals.get(Integer.parseInt(vertexData[2]) - 1);
			normalsArray[currentVertexPointer * 3] = currentNormal.x;
			normalsArray[currentVertexPointer * 3 + 1] = currentNormal.y;
			normalsArray[currentVertexPointer * 3 + 2] = currentNormal.z;
		}
	}
}

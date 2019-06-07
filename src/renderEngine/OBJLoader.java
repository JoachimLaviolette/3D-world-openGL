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

		try (BufferedReader reader = new BufferedReader(new FileReader("res/obj/" + OBJFile + ".obj"))) {
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
						texturesArray = new float[vertices.size() * 2]; // 1 texture by vertex, 2 components by texture 
						normalsArray = new float[vertices.size() * 3]; // 1 normal by vertex, 3 components by normal
						
						// read each face line
						// NB: face lines are the last one of the file
						while (line != null) {
							if (line.startsWith("f ")) {
								// here different types of face lines can be found
								// without "/", with "/", with "//"
								// examples: 
								// f 1 2 1
								// f 1/1/1 2/2/2 3/3/3f 1/1 2/2 3/3 4/4 5/5 
								// f 1//4 2//5 4//6 (cow.obj)
								// f 21//21 24//24 25//25 19//19 (player.obj)
								// f 26660/1/26660 26661/2/26661 26579/3/26579 (bunny.obj)
								// ...
									
								String haystack = line.contains("//") ? "//" : "/"; // here we avoid the following 1-uplet structures: f 542 1677 726
								currentLine = line.split(" ");
								
								List<String[]> faceData = new ArrayList<String[]>();
								
								// totally modular !
								// can work with all the examples we described above
								// except the following structure: f 1562 51 727
								// but we assume not to have it
								for(int x = 1; x < currentLine.length; x++) {
									faceData.add(currentLine[x].split(haystack));
								}
								
								processFaceData(faceData, indices, textures, normals, texturesArray, normalsArray);
							}
							
							line = reader.readLine();
						}
						
						// we break the loop !
						// from there, if the file is correctly made
						// we should only have face lines remaining
						// otherwise, the file should be reviewed
						// examples of not working .obj files: wood.obj, wood1.obj
						break;  
					}
				}
			}		
			
			reader.close();
		} catch (Exception e) {
			throw new RuntimeException("Unable to load obj data from file: " + OBJFile + ".", e);
		}
		
		// fill and arrange properly vertices and indices arrays
		verticesArray = new float[vertices.size() * 3]; // times 3 because 3 components per vertex
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
		
		// create a new VAO for the obj model loaded and return its ID
		return loader.loadToVAO(verticesArray, texturesArray, normalsArray, indicesArray);
	}
	
	private static void processFaceData(List<String[]> faceData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals,
			float[] texturesArray, float[] normalsArray) {
		for(String[] fd: faceData) {
			// face numbering starts at 1 in .obj files, there is no index 0 !!!
			// we store vertices, textures and normals AT FLY
			// when the first component of the first face's vertex (here vertex 1)
			// indicates 1 for example, it means go find the very first vertex defined in the obj file
			// but the very first vertex has been stored at the very beginning of our list, so index 0
			// so we need to subtract one to access the proper one!
			int currentVertexPointer = Integer.parseInt(fd[0]) - 1;
			
			// we add the current pointer/index to the indices list
			indices.add(currentVertexPointer);
			
			// we check the textures
			if (!textures.isEmpty()) {
				// same as above about the numbering starting at 1 and not 0
				int textureIndex = Integer.parseInt(fd[1]) - 1;
				// we retrieve the vector pointed by the texture index in the list
				Vector2f currentTexture = textures.get(textureIndex);
				// we store the texture vector's x value
				// (current pointer times 2) as index in textures array
				// because texture vector has 2 components
				texturesArray[currentVertexPointer * 2] = currentTexture.x;
				// we increment by one to store the y value as the direct next neighbor in the array
				texturesArray[currentVertexPointer * 2 + 1] = currentTexture.y;
			}
			
			// we check the normals
			// NB: same process than above
			if (!normals.isEmpty()) {
				int normalIndex = Integer.parseInt(fd[fd.length - 1]) - 1;
				Vector3f currentNormal = normals.get(normalIndex);
				// (current pointer times 3) as index in normals array
				// because texture vector has 3 components
				normalsArray[currentVertexPointer * 3] = currentNormal.x;
				normalsArray[currentVertexPointer * 3 + 1] = currentNormal.y;
				normalsArray[currentVertexPointer * 3 + 2] = currentNormal.z;
			}
		}		
	}
}

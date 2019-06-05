package shaders;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import toolbox.Constants;

public abstract class ShaderProgram {
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;	
	private boolean initialized = false;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	/**
	 * Constructors
	 */
	public ShaderProgram() {}
	
	public ShaderProgram(String vertexShaderFile, String fragmentShaderFile) {
		setupShaderFiles(vertexShaderFile, fragmentShaderFile);
	}
	
	public void setupShaderFiles(String vertexShaderFile, String fragmentShaderFile) {
		// Create the shader program
		programID = glCreateProgram();
		
		// Initialize this program to be the active program.
		glUseProgram(programID);
		
		// Initialize vertex and fragment shader from the provided files
		initVertexAndFragmentShaders(vertexShaderFile, fragmentShaderFile);
	}
	
	/**
	 * Load and compile vertex and fragment shaders from file, then link them to this shader program
	 * and validate its status.
	 * If the loading, compilation, linkage or validation processes fail then a RuntimeException is thrown.
	 * 
	 * @param  vertexShaderFilename
	 * @param  fragmentShaderFilename
	 */
	private void initVertexAndFragmentShaders(String vertexShaderFile, String fragmentShaderFile) {
		// Load and compile the two shaders
		vertexShaderID = compileShaderFromFile(vertexShaderFile, GL_VERTEX_SHADER);
		fragmentShaderID = compileShaderFromFile(fragmentShaderFile, GL_FRAGMENT_SHADER);

		// Link the shaders to the shader program and validate it
		linkAndValidate();
		
		// Set the flag to indicate our shader program creation was successful
		initialized = true;
	}
	
	/**
	 * Get the shader program id
	 * 
	 * @return The shader program's id
	 */
	public int getProgramId() {
		return programID;
	}

	/**
	 * Set this ShaderProgram to be the active one
	 *
	 * @throws RuntimeException If the shader program is not initialized before attempting to be used then a RuntimeException is thrown
	 */
	public void start() {
		if (initialized) {
			glUseProgram(programID);
			
			return;
		}
		
		throw new RuntimeException("Shader with program id " + programID + " is not initialised -  initialise with initFromFiles() or initFromStrings() first.");
	}

	/** 
	 * Disable this shader program
	 */
	public void stop() { 
		glUseProgram(0); 
	}
		
	/**
	 * Load shader code from file
	 * If loading the file fails at any point a RuntimeException is thrown
	 * 
	 * @param  filename The name of the shader file to load
	 */
	private String loadShaderFromFile(String shaderFile) {	
		StringBuilder shaderSourceSB = new StringBuilder();
		String line = null;

		// Try to open the file specified
		try (BufferedReader reader = new BufferedReader(new FileReader(shaderFile))) {

			// Read lines from the file and append them to the StringBuilder while there are lines to read
			while((line = reader.readLine()) != null) {
				shaderSourceSB.append(line + Constants.NEW_LINE);
			}

			// No need to close the reader - it's specified as Auto-Closable in try block.
		} catch (Exception e) {
			throw new RuntimeException("Unable to load shader from file: " + shaderFile + ".", e);
		}

		// Return the shader source as a String
		return shaderSourceSB.toString();
	}
	
	/**
	 *  Load and compile a shader from file
	 *  If an absolute path to the shader file is not used, then the relative path to the file will
	 *  be dependent on the configuration of your Java project. Typically, placing the shader file
	 *  in the top level of the project (above any packages) will allow it to be loaded by specifying
	 *  only its filename
	 *  If the shader could not be created, or if there was a error compiling the GLSL source code
	 *  for the shader then a RuntimeException is thrown
	 *  
	 *  @param  filename    The name of the file to load.
	 *  @param  shaderType  The type of shader to create, typically GL_VERTEX_SHADER or GL_FRAGMENT_SHADER
	 *  @return             The shader id
	 *  @see  <a href="https://www.opengl.org/sdk/docs/man/docbook4/xhtml/glCreateShader.xml">glCreateShader</a>
	 *  @see  <a href="https://www.opengl.org/sdk/docs/man/docbook4/xhtml/glShaderSource.xml">glShaderSource</a>
	 *  @see  <a href="https://www.opengl.org/sdk/docs/man/docbook4/xhtml/glCompileShader.xml">glCompileShader</a>
	 */ 
	private int compileShaderFromFile(String shaderFile, int shaderType) {
		// Load the shader GLSL code from file into a String
		String shaderGLSLCode = loadShaderFromFile(shaderFile);

		// Compile it and return the shader id
		return compileShaderFromString(shaderGLSLCode, shaderType);
	}

	/**
	 *  Load and compile a shader from GLSL source code provided as a String.
	 *  If the shader could not be created, or if there was a error compiling the GLSL source code
	 *  for the shader then a RuntimeException is thrown.
	 *  
	 *  @param  shaderSource  The GLSL source code for the shader.
	 *  @param  shaderType    The type of shader to create, typically GL_VERTEX_SHADER or GL_FRAGMENT_SHADER.
	 *  @return               The shader id
	 *  @see  <a href="https://www.opengl.org/sdk/docs/man/docbook4/xhtml/glCreateShader.xml">glCreateShader</a>
	 *  @see  <a href="https://www.opengl.org/sdk/docs/man/docbook4/xhtml/glShaderSource.xml">glShaderSource</a>
	 *  @see  <a href="https://www.opengl.org/sdk/docs/man/docbook4/xhtml/glCompileShader.xml">glCompileShader</a>
	 */ 
	private int compileShaderFromString(String shaderGLSLCode, int shaderType) {
		// The shaderId will be non-zero if successfully created
		int shaderId = glCreateShader(shaderType);

		// Throw a RuntimeException is there was a problem creating the shader
		if (shaderId == 0) {	
			throw new RuntimeException("Shader creation failed: " + glGetProgramInfoLog(programID, 1000));
		}
		
		// Attach the GLSL source code to the shader
		glShaderSource(shaderId, shaderGLSLCode);

		// Compile the shader
		glCompileShader(shaderId);

		// We no longer need to keep the shader source around, so mark it as null so the GC can free the memory
		shaderGLSLCode = null;

		// Get the shader compilation status
		int shaderStatus = glGetShaderi(shaderId, GL_COMPILE_STATUS);

		// If the shader failed to compile then throw a RuntimeException with debug info 
		if (shaderStatus == GL_FALSE) {
			String prefixString = "Fragment shader compilation failed: ";

			if (shaderType == GL_VERTEX_SHADER) {
				prefixString = "Vertex shader compilation failed: ";
			}
		
			throw new RuntimeException(prefixString + glGetShaderInfoLog(shaderId, 1000) );
		}

		return shaderId;
	}
	
	/**
	 * Link and validate the shader program
	 */
	private void linkAndValidate() {
		// Attach the compiled shaders to the program by their id values
		glAttachShader(programID, vertexShaderID);
		glAttachShader(programID, fragmentShaderID);

		// Bind shaders attributes
		bindAttributes();
		
		// Link the shader program
		glLinkProgram(programID);
		
		// Validate shader linking - bail on failure
		if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
			throw new RuntimeException( "Could not link shader program: " + glGetProgramInfoLog(programID, 1000));
		}

		// Detach the compiled shaders from the program
		glDetachShader(programID, vertexShaderID);
		glDetachShader(programID, fragmentShaderID);		

		// Perform general validation that the shader program is usable
		glValidateProgram(programID);
		
		// Get all uniform locations
		getAllUniformLocations();
		
		if (glGetProgrami(programID, GL_VALIDATE_STATUS) == GL_FALSE) {
			throw new RuntimeException("Could not validate shader program: " + glGetProgramInfoLog(programID, 1000));
		}
	}

	protected abstract void bindAttributes();
	
	protected void bindAttribute(int attribute, String variableName) {
		glBindAttribLocation(programID, attribute, variableName);
	}
	
	protected void loadFloat(int location, float value) {
		glUniform1f(location, value);
	}
	
	protected void loadVector(int location, Vector3f vector) {
		glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	protected void loadVector(int location, Vector4f vector) {
		glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
	}
	
	protected void loadBoolean(int location, boolean value) {		
		glUniform1f(location, value ? 1 : 0);
	}	
	
	protected void loadMatrix(int location, Matrix4f matrix) {
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		glUniformMatrix4(location, false, matrixBuffer);
	}
	
	protected abstract void getAllUniformLocations();
	
	protected int getUniformLocation(String uniformName) {
		return glGetUniformLocation(programID, uniformName);
	}
	
	public void cleanUp() {
		stop();
		glDetachShader(programID, vertexShaderID);
		glDetachShader(programID, fragmentShaderID);
		glDeleteShader(vertexShaderID);
		glDeleteShader(fragmentShaderID);
		glDeleteProgram(programID);
	}
}

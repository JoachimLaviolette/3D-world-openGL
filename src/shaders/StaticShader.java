package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Light;
import toolbox.Maths;

public class StaticShader extends ShaderProgram {
	private static final int MAX_LIGHTS = 2;
	private static String VERTEX_FILE = "src/shaders/toonShader_vertex_noAmbient.txt";
	private static String FRAGMENT_FILE = "src/shaders/toonShader_fragment_noAmbient.txt";

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColour[];
	private int location_lightLa[];
	private int location_lightLd[];
	private int location_lightLs[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColour;

	public StaticShader(boolean useAmbientLights) {
		super();
		setupShaderFiles(useAmbientLights);		
		super.setupShaderFiles(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {	
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		setupLightAttributesLocations();
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_skyColour = super.getUniformLocation("skyColour");
	}
	
	private void setupShaderFiles(boolean useAmbiantLights) {
		if (useAmbiantLights) {
			VERTEX_FILE = "src/shaders/staticShader_vertex_ambient.txt";
			FRAGMENT_FILE = "src/shaders/staticShader_fragment_ambient.txt";
		}
	}
	
	private void setupLightAttributesLocations() {
		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColour = new int[MAX_LIGHTS];
		location_lightLa = new int[MAX_LIGHTS];
		location_lightLd = new int[MAX_LIGHTS];
		location_lightLs = new int[MAX_LIGHTS];
		
		for (int i = 0; i < MAX_LIGHTS; i++) {
			location_lightPosition[i] = super.getUniformLocation("lights[" + i + "].position");
			location_lightColour[i] = super.getUniformLocation("lights[" + i + "].colour");
			location_lightLa[i] = super.getUniformLocation(Light.getLaStr(i));
			location_lightLd[i] = super.getUniformLocation(Light.getLdStr(i));
			location_lightLs[i] = super.getUniformLocation(Light.getLsStr(i));
		}
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadLights(List<Light> lights) {
		for(int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColour[i], lights.get(i).getColour());
				super.loadVector(location_lightLa[i], lights.get(i).getLa());
				super.loadVector(location_lightLd[i], lights.get(i).getLd());
				super.loadVector(location_lightLs[i], lights.get(i).getLs());
			} else {
				super.loadVector(location_lightPosition[i],new Vector4f(0f, 0f, 0f, 0f));
				super.loadVector(location_lightColour[i], new Vector3f(0f, 0f, 0f));
				super.loadVector(location_lightLa[i], new Vector3f(0f, 0f, 0f));
				super.loadVector(location_lightLd[i], new Vector3f(0f, 0f, 0f));
				super.loadVector(location_lightLs[i], new Vector3f(0f, 0f, 0f));
			}
		}
	}
	
	public void loadShineVariables(float shineDamper, float reflectivity) {
		super.loadFloat(location_shineDamper, shineDamper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadSkyColour(float r, float g, float b) {
		super.loadVector(location_skyColour, new Vector3f(r, g, b));
	}
	
	public void useAmbiantLights(boolean use) {
		VERTEX_FILE = "";
	}
}

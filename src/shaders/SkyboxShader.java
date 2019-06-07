package shaders;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import toolbox.Maths;

public class SkyboxShader extends ShaderProgram {
    private static final String VERTEX_FILE = "src/shaders/skyboxShader_vertex.txt";
    private static final String FRAGMENT_FILE = "src/shaders/skyboxShader_fragment.txt";
     
    private int location_projectionMatrix;
    private int location_viewMatrix;
     
    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }
 
    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = Maths.createViewMatrix(camera);
        // we want the camera to always stays at the center of the skybox
        // instead of creating a transformation matrix for the skybox and
        // setting it to the camera trasnformation
        // we modify the view matrix translation components (defined in the last column)
        // to apply the rotation of the skybox on the view matrix
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        super.loadMatrix(location_viewMatrix, matrix);
    }
     
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
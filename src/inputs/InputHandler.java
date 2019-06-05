package inputs;

import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.glfw.GLFWKeyCallback;

import entities.Camera;
import entities.Player;

public class InputHandler {
	private static long windowID;
	private Player player;
	private Camera camera;
	
	private GLFWKeyCallback keyCallback;
		
	public InputHandler(Player player, Camera camera, long wID) {
		this.player = player;
		this.camera = camera;
		windowID = wID;
		setupInputs();
	}
	
	public void handleInputs() {
		camera.move();
		player.move();
	}
	
	private void setupInputs() {
		keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {				
				if (key == GLFW_KEY_ESCAPE) {
					glfwSetWindowShouldClose(window, GL_TRUE);
				}
				
				/* CAMERA INPUTS */
				// zoom in
				else if (key == 266) {
					camera.setDistanceFromPlayer(camera.getDistanceFromPlayer() + 2f); 
				} 
				// zoom out
				else if (key == 267) {
					camera.setDistanceFromPlayer(camera.getDistanceFromPlayer() - 2f); 
				}
				// pitch in
				else if (key == GLFW_KEY_W) {
					camera.setPitch(camera.getPitch() + 5f);
				}
				// pitch out
				else if (key == GLFW_KEY_S) {
					camera.setPitch(camera.getPitch() - 5f);
				}
				// rotation left
				else if (key == GLFW_KEY_A) {
					camera.setAngleAroundPlayer(camera.getAngleAroundPlayer() + 5f);
				}
				// rotation right
				else if (key == GLFW_KEY_D) {
					camera.setAngleAroundPlayer(camera.getAngleAroundPlayer() - 5f);
				}
				
				/* PLAYER INPUTS */
				// Walk speed
				else if (key == GLFW_KEY_UP) {
					player.forward();
				} else if (key == GLFW_KEY_DOWN) {
					player.back();
				}
				
				// Turn speed				
				else if (key == GLFW_KEY_RIGHT) {
					player.rotateRight();
				} else if (key == GLFW_KEY_LEFT) {
					player.rotateLeft();
				}
				
				// Jump 
				else if (key == GLFW_KEY_SPACE) {
					player.jump();
				}
			}
		};
		
		glfwSetKeyCallback(windowID, keyCallback);
	}
	
}

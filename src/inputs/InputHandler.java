package inputs;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

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
				/* CAMERA INPUTS */
				// zoom in
				if (key == 266) {
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

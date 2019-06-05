package entities;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private Vector3f position = new Vector3f(100f, 25f, 500f);
	private float pitch = 5f;
	private float yaw;
	private float roll;
	
	private GLFWKeyCallback keyCallback;
	
	public Camera() {}
	
	public void move(long windowID) {
		keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW_KEY_ESCAPE) {
					glfwSetWindowShouldClose(window, GL_TRUE);
				}
				
				// Qwerty configuration downhere
				if (key == GLFW_KEY_W) {
					pitch -= 0.9f;
				}
				
				if (key == GLFW_KEY_S) {
					pitch += 0.9f;
				} 
				
				if (key == GLFW_KEY_D) {
					yaw += 0.9f;
				} 
				
				if (key == GLFW_KEY_A) {
					yaw -= 0.9f;
				}
				
				if (key == GLFW_KEY_LEFT_SHIFT) {
					position.y += 10f;
				} 
				
				if (key == GLFW_KEY_LEFT_CONTROL) {
					position.y -= 10f;
				}
				
				if (key == GLFW_KEY_UP) {
					position.z -= 10f;
				}				
				
				if (key == GLFW_KEY_DOWN) {
					position.z += 10f;
				}
				
				if (key == GLFW_KEY_LEFT) {
					position.x -= 10f;
				}				
				
				if (key == GLFW_KEY_RIGHT) {
					position.x += 10f;
				}
			}
		};
		
		glfwSetKeyCallback(windowID, keyCallback); 
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public float getRoll() {
		return roll;
	}
	
	public void moveRight() {
		position.x += 0.2f;
	}
	
	public void moveLeft() {
		position.x -= 0.2f;
	}
	
	public void moveFront() {
		position.z -= 0.2f;
	}
	
	public void moveBack() {
		position.z += 0.2f;
	}
	
	public void moveUp() {
		position.y += 0.2f;
	}
	
	public void moveDown() {
		position.y -= 0.2f;
	}
}

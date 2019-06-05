package entities;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_TRUE;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;

public class Player extends Entity {
	private static final float WALK_SPEED = 20f;
	private static final float TURN_SPEED = 160f;
	private static final float GRAVITY = -5f;
	private static final float JUMP_POWER = 30f;
	
	private static final float TERRAIN_HEIGHT = 0;
	
	private float mWalkSpeed = 0f;
	private float mTurnSpeed = 0f;
	private float mUpwardsSpeed = 0f; 
	private boolean hasJumpEnded = true;
	
	private GLFWKeyCallback keyCallback;

	public Player(TexturedModel texturedModel, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(texturedModel, position, rotX, rotY, rotZ, scale);
	}
	
	public void move(long windowID) {
		this.checkInputs(windowID);
		float distance = mWalkSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		mUpwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, mUpwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		
		if (super.getPosition().y < TERRAIN_HEIGHT) {
			mUpwardsSpeed = 0f;
			super.getPosition().y = TERRAIN_HEIGHT;
			hasJumpEnded = true;
		}
		
		mWalkSpeed = 0f;
		mTurnSpeed = 0f;
		mUpwardsSpeed = 0f;
	}
	
	private void jump() {
		if (hasJumpEnded) {
			mUpwardsSpeed = JUMP_POWER;
			hasJumpEnded = false;
		}
	}
	
	private void checkInputs(long windowID) {
		keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW_KEY_ESCAPE) {
					glfwSetWindowShouldClose(window, GL_TRUE);
				}
				// QWERTY configuration down there
				
				// Walk speed
				if (key == GLFW_KEY_W && action == GLFW_RELEASE) {
					mWalkSpeed = -WALK_SPEED;
				} else if (key == GLFW_KEY_S && action == GLFW_RELEASE) {
					mWalkSpeed = WALK_SPEED;
				}
				
				// Turn speed				
				if (key == GLFW_KEY_D && action == GLFW_RELEASE) {
					mTurnSpeed = -TURN_SPEED;
				} else if (key == GLFW_KEY_A && action == GLFW_RELEASE) {
					mTurnSpeed = TURN_SPEED;
				}
				
				// Jump 
				if (key == GLFW_KEY_SPACE && action == GLFW_RELEASE) {
					jump();
				}
			}
		};
		
		glfwSetKeyCallback(windowID, keyCallback); 
	}
	
}

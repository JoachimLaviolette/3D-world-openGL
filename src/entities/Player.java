package entities;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
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
	private static final float WALK_SPEED = 30f;
	private static final float TURN_SPEED = 30f;
	private static final float GRAVITY = -50f;
	private static final float JUMP_POWER = 30f;
	
	private static final float TERRAIN_HEIGHT = 0;
	
	private float mWalkSpeed = 0f;
	private float mTurnSpeed = 0f;
	private float mUpwardsSpeed = 0f; 
	private boolean hasJumpEnded = true;
	
	private static long windowID;
	
	private GLFWKeyCallback keyCallback;

	public Player(TexturedModel texturedModel, Vector3f position, float rotX, float rotY, float rotZ, float scale, long wID) {
		super(texturedModel, position, rotX, rotY, rotZ, scale);
		windowID = wID;
	}
	
	public void move() {
		float frameTime = DisplayManager.getFrameTimeSeconds();
		super.increaseRotation(0f, mTurnSpeed * frameTime, 0f);
		
		float distance = mWalkSpeed * frameTime;
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		
		mUpwardsSpeed += GRAVITY * frameTime;
		super.increasePosition(0, mUpwardsSpeed * frameTime, 0);
		
		if (super.getPosition().y < TERRAIN_HEIGHT) {
			mUpwardsSpeed = 0f;
			super.getPosition().y = TERRAIN_HEIGHT;
			hasJumpEnded = true;
		}
		
		mWalkSpeed = 0f;
		mTurnSpeed = 0f;
	}
	
	public void jump() {
		if (hasJumpEnded) {
			mUpwardsSpeed = JUMP_POWER;
			hasJumpEnded = false;
		}
	}
	
	public void forward() {
		mWalkSpeed = WALK_SPEED;
	}
	
	public void back() {
		mWalkSpeed = -WALK_SPEED;
	} 
	
	public void rotateLeft() {
		mTurnSpeed = TURN_SPEED;
	}
	
	public void rotateRight() {
		mTurnSpeed = -TURN_SPEED;
	}
	
}

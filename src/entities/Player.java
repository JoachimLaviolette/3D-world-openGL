package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;

public class Player extends Entity {
	private static final float WALK_SPEED = 30f;
	private static final float TURN_SPEED = 30f;
	private static final float GRAVITY = -50f;
	private static final float JUMP_POWER = 30f;
	
	private static final float TERRAIN_HEIGHT = 0f;
	
	private float mMoveSpeed = 0f;
	private float mRotateSpeed = 0f;
	private float mUpwardsSpeed = 0f; 
	private boolean isJumping = true;
	
	private static long windowID;

	/**
	 * Constructor
	 * @param texturedModel The textured model applied to the player
	 * @param position The spawn position of the player
	 * @param rotX x rotation amount
	 * @param rotY y rotation amount
	 * @param rotZ z rotation amount
	 * @param scale scale amount
	 * @param wID The scene's window's id 
	 */
	public Player(TexturedModel texturedModel, Vector3f position, float rotX, float rotY, float rotZ, float scale, long wID) {
		super(texturedModel, position, rotX, rotY, rotZ, scale);
		windowID = wID;
	}
	
	/**
	 * Handle player's moves
	 */
	public void move() {
		// retrieve the elapsed time since last frame
		float frameTime = DisplayManager.getFrameTimeSeconds();
		
		// handle player behavior
		handleRotation(frameTime);
		handlePosition(frameTime);
		handleJump(frameTime);
		
		// reset speed attributes
		mMoveSpeed = 0f;
		mRotateSpeed = 0f;
	}
	
	/**
	 * Apply the calculated rotation to the player
	 * @param frameTime Time elapsed since last frame
	 */
	private void handleRotation(float frameTime) {
		super.increaseRotation(0f, mRotateSpeed * frameTime, 0f);
	}
	
	/**
	 * Calculate player's new position using trigonometry
	 * https://www.mathsisfun.com/algebra/trigonometry.html
	 * @param frameTime Time elapsed since last frame
	 */
	private void handlePosition(float frameTime) {
		float distance = mMoveSpeed * frameTime;
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
	}
	
	/**
	 * Apply gravity force's effect to the player
	 * @param frameTime Time elapsed since last frame
	 */
	private void handleJump(float frameTime) {
		mUpwardsSpeed += GRAVITY * frameTime;
		super.increasePosition(0, mUpwardsSpeed * frameTime, 0);
		
		if (super.getPosition().y < TERRAIN_HEIGHT) {
			mUpwardsSpeed = 0f;
			super.getPosition().y = TERRAIN_HEIGHT;
			isJumping = true;
		}
	}
	
	/**
	 * Make the player jump
	 */
	public void jump() {
		if (isJumping) {
			mUpwardsSpeed = JUMP_POWER;
			isJumping = false;
		}
	}
	
	/**
	 * Make the player go forward
	 */
	public void forward() {
		mMoveSpeed = WALK_SPEED;
	}
	
	/**
	 * Make the player go back
	 */
	public void back() {
		mMoveSpeed = -WALK_SPEED;
	} 
	
	/**
	 * Make the player rotate to the left
	 */
	public void rotateLeft() {
		mRotateSpeed = TURN_SPEED;
	}
	
	/**
	 * Make the player rotate to the right
	 */
	public void rotateRight() {
		mRotateSpeed = -TURN_SPEED;
	}
	
}

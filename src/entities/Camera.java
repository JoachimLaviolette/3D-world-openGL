package entities;

import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private Vector3f position = new Vector3f(0f, 0f, 0f);
	private float pitch = 20f;
	private float yaw = 0f;
	private float roll = 0f;
	
	private Player player;
	private float distanceFromPlayer = 100f;
	private float angleAroundPlayer = 0f;
	
	private static long windowID;
	
	/**
	 * Constructor
	 * @param player The player instance
	 * @param wID The scene's window's id
	 */
	public Camera(Player player, long wID) {
		this.player = player;
		windowID = wID;
	}
	
	/**
	 * Handle camera move's
	 */
	public void move() {
		this.computeCameraPosition();
	}
	
	/**
	 * Calculate the horizontal distance from the player
	 * @return The computed horizontal distance
	 */
	private float computeHorizontalDistanceFromPlayer() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	/**
	 * Calculate the vertical distance from the player
	 * @return The computed vertical distance
	 */
	private float computeVerticalDistanceFromPlayer() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	/**
	 * Calculate the camera's position according to the player's
	 * Use of the trigonometry
	 * https://www.mathsisfun.com/algebra/trigonometry.html
	 */
	private void computeCameraPosition() {
		float h_Dist = computeHorizontalDistanceFromPlayer();
		float v_Dist = computeVerticalDistanceFromPlayer();
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (h_Dist * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (h_Dist * Math.cos(Math.toRadians(theta)));
		
		// apply the offsets to the camera's position
		position.x = player.getPosition().x - offsetX;
		position.y = player.getPosition().y + v_Dist;
		position.z = player.getPosition().z - offsetZ;
		
		// center the rotation around the player
		yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}
	
	/***
	 * Get the camera'a position
	 * @return Camera's position
	 */
	public Vector3f getPosition() {
		return position;
	}
	
	/**
	 * Get the camera's pitch
	 * @return Camera's pitch
	 */
	public float getPitch() {
		return pitch;
	}
	
	/**
	 * Get the camera's yaw
	 * @return Camera's yaw
	 */
	public float getYaw() {
		return yaw;
	}
	
	/**
	 * Get the camera's roll
	 * @return Camera's roll
	 */
	public float getRoll() {
		return roll;
	}

	/**
	 * Set the camera's position
	 * @param position
	 */
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	/**
	 * Set the camera's pitch
	 * @param pitch
	 */
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getDistanceFromPlayer() {
		return distanceFromPlayer;
	}
	
	/***
	 * Set the distance from the player
	 * Used for the zoom feature (see InputHandler class) 
	 * @param distanceFromPlayer
	 */
	public void setDistanceFromPlayer(float distanceFromPlayer) {
		this.distanceFromPlayer = distanceFromPlayer;
	}

	/**
	 * Get the angle amount around the player
	 */
	public float getAngleAroundPlayer() {
		return angleAroundPlayer;
	}
	
	/**
	 * Set the angle amount around the player
	 * Used for the rotate feature (see InputHandler class)
	 * @param angleAroundPlayer
	 */
	public void setAngleAroundPlayer(float angleAroundPlayer) {
		this.angleAroundPlayer = angleAroundPlayer;
	}	
}

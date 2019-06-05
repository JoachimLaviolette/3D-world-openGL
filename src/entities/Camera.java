package entities;


import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private Vector3f position = new Vector3f(0f, 0f, 0f);
	private float pitch = 20f;
	private float yaw = 0f;
	private float roll = 0f;
	
	private Player player;
	private float distanceFromPlayer = 50f;
	private float angleAroundPlayer = 0f;
	
	private static long windowID;
	
	private GLFWKeyCallback keyCallback;
	
	public Camera(Player player, long wID) {
		this.player = player;
		windowID = wID;
	}
	
	public void move() {
		this.computeCameraPosition();
	}
	
	private float computeHorizontalDistanceFromPlayer() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float computeVerticalDistanceFromPlayer() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void computeCameraPosition() {
		float h_Dist = computeHorizontalDistanceFromPlayer();
		float v_Dist = computeVerticalDistanceFromPlayer();
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (h_Dist * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (h_Dist * Math.cos(Math.toRadians(theta)));
		
		position.x = player.getPosition().x - offsetX;
		position.y = player.getPosition().y + v_Dist;
		position.z = player.getPosition().z - offsetZ;
			
		yaw = 180 - (player.getRotY() + angleAroundPlayer);
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

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getDistanceFromPlayer() {
		return distanceFromPlayer;
	}
	
	public void setDistanceFromPlayer(float distanceFromPlayer) {
		this.distanceFromPlayer = distanceFromPlayer;
	}

	public float getAngleAroundPlayer() {
		return angleAroundPlayer;
	}
	
	public void setAngleAroundPlayer(float angleAroundPlayer) {
		this.angleAroundPlayer = angleAroundPlayer;
	}
	
	
}

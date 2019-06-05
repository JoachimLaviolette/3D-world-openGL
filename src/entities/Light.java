package entities;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Light {
	private Vector4f position;
	private Vector3f colour;
	private Vector3f La;
	private Vector3f Ld;
	private Vector3f Ls;
		
	public Light(Vector4f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
	}
	
	public Light(Vector4f position, Vector3f La, Vector3f Ld, Vector3f Ls) {
		this.position = position;
		this.La = La;
		this.Ld = Ld;
		this.Ls = Ls;
	}
	
	public Light(Vector4f position, Vector3f colour, Vector3f La, Vector3f Ld, Vector3f Ls) {
		this.position = position;
		this.colour = colour;
		this.La = La;
		this.Ld = Ld;
		this.Ls = Ls;
	}

	public Vector4f getPosition() {
		return position;
	}

	public Vector3f getColour() {
		return colour;
	}

	public Vector3f getLa() {
		return La;
	}

	public Vector3f getLd() {
		return Ld;
	}

	public Vector3f getLs() {
		return Ls;
	}

	public static String getPositionStr(int index) {
		return "lights[" + index + "].position";
	}
	
	public static String getColourStr(int index) {
		return "lights[" + index + "].colour";
	}
	
	public static String getLaStr(int index) {
		return "lights[" + index + "].La";
	}
	
	public static String getLdStr(int index) {
		return "lights[" + index + "].Ld";
	}
	
	public static String getLsStr(int index) {
		return "lights[" + index + "].Ls";
	}
}

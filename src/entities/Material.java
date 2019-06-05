package entities;

import org.lwjgl.util.vector.Vector3f;

public class Material {
	
	private Vector3f Ka;
	private Vector3f Kd;
	private Vector3f Ks;
	private float f;
	
	public Material(Vector3f ka, Vector3f kd, Vector3f ks, float f) {
		Ka = ka;
		Kd = kd;
		Ks = ks;
		this.f = f;
	}

	public Vector3f getKa() {
		return Ka;
	}

	public Vector3f getKd() {
		return Kd;
	}

	public Vector3f getKs() {
		return Ks;
	}

	public float getF() {
		return f;
	}
}

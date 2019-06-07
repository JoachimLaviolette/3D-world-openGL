package textures;

import java.nio.ByteBuffer;

public class TextureData {
	private int width;
	private int height;
	private ByteBuffer bb;
	
	public TextureData(int width, int height, ByteBuffer bb) {
		this.width = width;
		this.height = height;
		this.bb = bb;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public ByteBuffer getBb() {
		return bb;
	}
	
	
}

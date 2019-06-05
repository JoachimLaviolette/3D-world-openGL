package textures;

public class TerrainTexturePackage {
	private TerrainTexture terraintexture;
	private TerrainTexture rTexture;
	private TerrainTexture gTexture;
	private TerrainTexture bTexture;
	
	public TerrainTexturePackage(TerrainTexture terraintexture, TerrainTexture rTexture, TerrainTexture gTexture,
			TerrainTexture bTexture) {
		this.terraintexture = terraintexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}

	public TerrainTexture getTerraintexture() {
		return terraintexture;
	}

	public TerrainTexture getRTexture() {
		return rTexture;
	}

	public TerrainTexture getGTexture() {
		return gTexture;
	}

	public TerrainTexture getBTexture() {
		return bTexture;
	}
}

package models;

/*
 * This class represents a 3D model stored in the memory
 */

public class RawModel {
	
	private int vaoID; // The id
	private int vertexCount; // How many vertixes it is in this model.
	
	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	// Getters
	
	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	
}

package renderEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import models.RawModel;

/**
 * This class loads 3D models into memory
 */
public class Loader {
	
	// Keep track of the vaos and vbos we create
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		// Creates a VAO and gets the ID
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		
		// Stores in list 0, this is for no special reason. 3 is because it is a 3D vector (xyz)
		storeDataInAttributeList(0,3,positions);
		// Stores in list 1, because 0 holds another list. it is 2 because texture is 2D
		storeDataInAttributeList(1,2,textureCoords);
		// Stores the normals in list 2, normals are 3D vector
		storeDataInAttributeList(2,3,normals);
		
		// Now the VAO is not used anymore, so we now have to unbind it.
		unbindVAO();
		
		// This is needed and not the one below to use Index buffer
		// Returns the data we have created the information about the VAO as a raw model
		return new RawModel(vaoID, indices.length);
	}
	
	/**
	 * Takes in the filname of the texture we want
	 * returns the ID of the texture so we can access it and use it whenever we want
	 */
	public int loadTexture(String fileName) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + fileName + ".png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int textureID = texture.getTextureID();
		textures.add(textureID);
		
		return textureID;
	}
	
	/**
	 * Creates a new empty VAOO
	 */
	private int createVAO() {
		// Creates a empty VAO
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		
		// To use the VAO we need to activate it by binding it
		GL30.glBindVertexArray(vaoID); // Will stay bound until we unbind it.
		
		return vaoID;
	}
	
	/**
	 * Called when we close down the game
	 * Deletes all the VAO, VBO and textures
	 */
	public void cleanUp() {
		for(int vao:vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		
		for (int vbo:vaos) {
			GL15.glDeleteBuffers(vbo);
		}
		
		for (int texture:textures) {
			GL11.glDeleteTextures(texture);
		}
	}

	/**
	 * Stores one of the data into the attribute list of the VAO
	 * 
	 * To store data into a attribute list we need to create a VBO
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		// Do the same here as with the VAO
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		
		// Binds the VBO, now that its bound, we can store data to it
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		
		// Converts the data into a float buffer
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		
		// Now we can store the data into the VBO
		// Param: 1: Is the type of VBO, 2: the data we want to put in, 3: Specifiy the data is going to be used for. Here it is static, which means we are not going to edit the data after its stored in VBO
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		
		// Puts the VBO into the VAO, into one of the attributes list of VAO
		// Param: 1: The number of the attribute list you want to store the data in, 2: Length of the vertex (3 because its x,y and z), 3: The type data, 4: Is the data normalized? No = false, 5: The distance between each of the vertices we have 0, 6: The offset where the data should, so we put 0 because we want it to start at the start
		// Param: 2 is now coordinateSize because if we are taking in a texture it is 2D, so we cant hard code 3.
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		
		// Finished used the VBO, we now unbind it, the same way as VAO but here we put in 0, which unbinds the current VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * Unbinds the VAO
	 * When you are done using a VAO you have to unbind it
	 */
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Loads up the indices buffer and binds it to a VBO we want to render
	 */
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		
		// Stores it into the VBO, NOTICE its ELEMENT array buffer. We also have STATIC DRAW so we tell OpenGL we wont configure it
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * Store our indices to the VBO
	 */
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	/**
	 * Converts a float array to a float buffer
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length); // Needs the lenght of the data
		
		// Put the data into the buffer
		buffer.put(data);
		
		// Finished writing to the buffer and is ready to be read from
		buffer.flip();
		
		return buffer;
	}
}

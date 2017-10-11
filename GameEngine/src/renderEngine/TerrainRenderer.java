package renderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.TerrainShader;
import terrains.Terrain;
import textures.ModelTexture;
import toolbox.Maths;

/*
 * To render terrain
 */
public class TerrainRenderer {

	private TerrainShader shader;
	
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	/**
	 * Renders the terrains
	 */
	public void render(List<Terrain> terrains) {
		for(Terrain terrain:terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTexturedModel();
		}
	}
	
	private void prepareTerrain(Terrain terrain) {
		RawModel rawModel = terrain.getModel();
		
		// Binds the VAO we want to use
		GL30.glBindVertexArray(rawModel.getVaoID());
		
		// Activate the attribute list where the data is stored, we put the list in zero, check Loader class, therefore we have 0 here
		GL20.glEnableVertexAttribArray(0);
		
		// Activate the attribute list where the data is store, list 1, and ths is the texture coordinates
		GL20.glEnableVertexAttribArray(1);
		
		// Activate the attribute list where the data is stored, list 2 is normals
		GL20.glEnableVertexAttribArray(2);
		
		// Load up the texutre and apply the shinedamper and reflectivity on it
		ModelTexture texture = terrain.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
				
		// Tells OpgenGL which texture we want to render, and put it to one of the texture banks (GL_TEXTURE0 is one of the banks) ! 
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
	}
	
	private void unbindTexturedModel() {
		// Disable the attribute list when everything is finished!
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
				
		// Then also have to unbind the VAO, instead of putting a ID in, we just put in 0
		GL30.glBindVertexArray(0);
	}
	
	private void loadModelMatrix(Terrain terrain) {
		// Creates a transformationMatrix from the entities position
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0,terrain.getZ()), 0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
} 

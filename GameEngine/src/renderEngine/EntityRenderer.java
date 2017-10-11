package renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

/**
 * Handles the rendering of a entity
 */
public class EntityRenderer {
	
	private StaticShader shader;
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
	
		// Loads the shader, only has to be done once.
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	// Renders the entity
	public void render(Map<TexturedModel, List<Entity>> entities) {
		for(TexturedModel model:entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity:batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel(); // Unbind all textures after its done
		}
	}
	
	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		
		// Binds the VAO we want to use
		GL30.glBindVertexArray(rawModel.getVaoID());
		
		// Activate the attribute list where the data is stored, we put the list in zero, check Loader class, therefore we have 0 here
		GL20.glEnableVertexAttribArray(0);
		
		// Activate the attribute list where the data is store, list 1, and ths is the texture coordinates
		GL20.glEnableVertexAttribArray(1);
		
		// Activate the attribute list where the data is stored, list 2 is normals
		GL20.glEnableVertexAttribArray(2);
		
		// Load up the texture and apply the shinedamper and reflectivity on it
		ModelTexture texture = model.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
				
		// Tells OpgenGL which texture we want to render, and put it to one of the texture banks (GL_TEXTURE0 is one of the banks) ! 
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}
	
	private void unbindTexturedModel() {
		// Disable the attribute list when everything is finished!
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
				
		// Then also have to unbind the VAO, instead of putting a ID in, we just put in 0
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Entity entity) {
		// Creates a transformationMatrix from the entities position
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}
}

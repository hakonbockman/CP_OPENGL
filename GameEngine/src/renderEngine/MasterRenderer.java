package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;

/**
 * Handles all the rendering code in our game
 * This class is needed so rendering same model multiple times wont happend! (Only need to be rendered once... not 200 times example)
 *
 */
public class MasterRenderer {
	
		// The final variables is for the projection Matrix! See createProjectionMatrix method further down
		private static final float FOV = 70;
		private static final float NEAR_PLANE = 0.1f;
		private static final float FAR_PLANE = 1000;
		
		private Matrix4f projectionMatrix;

		private StaticShader shader = new StaticShader();
		private EntityRenderer renderer;
		
		private TerrainRenderer terrainRenderer;
		private TerrainShader terrainShader = new TerrainShader();
		
		private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
		private List<Terrain> terrains = new ArrayList<Terrain>();
		
		public MasterRenderer(){
			// Makes so the inside of the model is not rendered. Makes so it is more efficient!
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glCullFace(GL11.GL_BACK);
			
			createProjectionMatrix();
			renderer = new EntityRenderer(shader, projectionMatrix);
			terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
			
			
		}
		
		// Called once every frame, and will render all entities on the scene
		public void render(Light sun, Camera camera) {
			prepare();
			shader.start();
			shader.loadLight(sun);
			shader.loadViewMatrix(camera);
			renderer.render(entities);
			shader.stop();
			terrainShader.start();
			terrainShader.loadLight(sun);
			terrainShader.loadViewMatrix(camera);
			terrainRenderer.render(terrains);
			terrainShader.stop();
			
			terrains.clear(); // Remember to clear same as entities
			entities.clear(); // Important to clear! Or it will build up and eat your machine
		}
		
		public void processTerrain(Terrain terrain) {
			terrains.add(terrain);
		}
		
		/*
		 * Entities get sorted to the correct list
		 */
		public void processEntity(Entity entity) {
			TexturedModel entityModel = entity.getModel();
			List<Entity> batch = entities.get(entityModel);
			
			// If already exists
			if(batch != null) {
				batch.add(entity);
			}
			// If it doesnt make a new batch
			else {
				List<Entity> newBatch = new ArrayList<Entity>();
				newBatch.add(entity);
				entities.put(entityModel, newBatch);
			}
		}
		
		// Is called once every frame, this prepares openGL to prepare the frame
		public void prepare() {
			GL11.glEnable(GL11.GL_DEPTH_TEST); // Makes so the triangles genereate in the correct order!
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
			// Have to clear the color from the last frame
			GL11.glClearColor(0, 0, 0, 0); // Black background	
		}
		
		// Creates the projectionMatrix, code is not needed to be understanded
		private void createProjectionMatrix() {
			float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
			float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
			float x_scale = y_scale / aspectRatio;
			float frustum_length = FAR_PLANE - NEAR_PLANE;
			
			projectionMatrix = new Matrix4f();
			projectionMatrix.m00 = x_scale;
			projectionMatrix.m11 = y_scale;
			projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
			projectionMatrix.m23 = -1;
			projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
			projectionMatrix.m33 = 0;
		}
		
		
		// Need to clean up after we close the game
		public void cleanUp() {
			shader.cleanUp();
			terrainShader.cleanUp();
		}
}

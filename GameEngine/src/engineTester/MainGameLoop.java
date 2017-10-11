package engineTester;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import renderEngine.EntityRenderer;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {	
		
		// When true the gate moves upwards
		boolean openGate = false;
		// when value 1, spawn boss, when value 2 boss is done spawning
		int spawnBoss = 0;
		
		// Opens up the display
		DisplayManager.createDisplay();
		
		// Initiate a new loader
		Loader loader = new Loader();
		
		// Load the vertices to a model
		RawModel model = OBJLoader.loadObjModel("castle", loader);
		RawModel castleGateObject = OBJLoader.loadObjModel("gate", loader);
		RawModel bossObject = OBJLoader.loadObjModel("castleBoss", loader);
		RawModel bossFaceObject = OBJLoader.loadObjModel("bossFace", loader);
		RawModel bossHeadObject = OBJLoader.loadObjModel("bossHead", loader);
		RawModel fireOrbObject = OBJLoader.loadObjModel("fireOrb", loader);
		RawModel torchesObject = OBJLoader.loadObjModel("torches", loader);
		RawModel pictureFrameObject = OBJLoader.loadObjModel("pictureFrame", loader);
		RawModel planetObject = OBJLoader.loadObjModel("planet", loader);
		
		// Loads the texture you want
		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("stoneWall")));
		TexturedModel castleGateModel = new TexturedModel(castleGateObject, new ModelTexture(loader.loadTexture("woodGate")));
		TexturedModel bossModel = new TexturedModel(bossObject, new ModelTexture(loader.loadTexture("lava")));
		TexturedModel bossFaceModel = new TexturedModel(bossFaceObject, new ModelTexture(loader.loadTexture("face3")));
		TexturedModel bossHeadModel = new TexturedModel(bossHeadObject, new ModelTexture(loader.loadTexture("lava2")));
		TexturedModel fireOrbModel = new TexturedModel(fireOrbObject, new ModelTexture(loader.loadTexture("ice")));
		TexturedModel fireOrb2Model = new TexturedModel(fireOrbObject, new ModelTexture(loader.loadTexture("earth")));
		TexturedModel fireModel = new TexturedModel(fireOrbObject, new ModelTexture(loader.loadTexture("fire")));
		TexturedModel torchesModel = new TexturedModel(torchesObject, new ModelTexture(loader.loadTexture("woodTorch")));
		TexturedModel pictureFrameModel = new TexturedModel(pictureFrameObject, new ModelTexture(loader.loadTexture("he")));
		TexturedModel pictureFrameModel2 = new TexturedModel(pictureFrameObject, new ModelTexture(loader.loadTexture("hehe")));
		TexturedModel pictureFrameModel3 = new TexturedModel(pictureFrameObject, new ModelTexture(loader.loadTexture("hehehe")));
		TexturedModel pictureFrameModel4 = new TexturedModel(pictureFrameObject, new ModelTexture(loader.loadTexture("hehehehe")));
		TexturedModel planetModel = new TexturedModel(planetObject, new ModelTexture(loader.loadTexture("planet")));
		TexturedModel planet2Model = new TexturedModel(planetObject, new ModelTexture(loader.loadTexture("planet2")));
		
		
		// Loads ShineDamper and Reflectivity
		ModelTexture texture = staticModel.getTexture();
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		
		ModelTexture textureCastleGate = castleGateModel.getTexture();
		textureCastleGate.setShineDamper(10);
		textureCastleGate.setReflectivity(1);
		
		ModelTexture textureFire = fireModel.getTexture();
		textureFire.setShineDamper(15);
		textureFire.setReflectivity(4);
		
		ModelTexture texturePictureFrame2 = pictureFrameModel2.getTexture();
		texturePictureFrame2.setShineDamper(15);
		texturePictureFrame2.setReflectivity(1);
		
		ModelTexture texturePictureFrame3 = pictureFrameModel3.getTexture();
		texturePictureFrame3.setShineDamper(15);
		texturePictureFrame3.setReflectivity(1);

		// Create entity and place it in the world, decide position, rotation and scale
		Entity castle = new Entity(staticModel, new Vector3f(0, 0, -700), 0, 270, 0, 50);
		Entity castleGate = new Entity(castleGateModel, new Vector3f(0,0, -700), 0, 270, 0, 50);
		Entity torches = new Entity(torchesModel, new Vector3f(0, 0, -700), 0, 270, 0, 50);
		Entity torchFire1 = new Entity(fireModel, new Vector3f(-30.3f, 43, -361), 0, 270, 0, 2);
		Entity torchFire2 = new Entity(fireModel, new Vector3f(29.8f, 43, -361), 0, 270, 0, 2);
		
		Entity boss = new Entity(bossModel, new Vector3f(-3, -100, -700), 0, 0, 0, 50);
		Entity bossFace = new Entity(bossFaceModel, new Vector3f(-3, -100, -700), 0, 0, 0, 50);
		Entity bossHead = new Entity(bossHeadModel, new Vector3f(-3, -100, -700), 0, 0, 0, 50);
		Entity fireOrb1 = new Entity(fireOrbModel, new Vector3f(-20, -52, -700), 0, 0, 0, 4);
		Entity fireOrb2 = new Entity(fireOrb2Model, new Vector3f(20, -52, -700), 0, 0, 0, 4);
		
		Entity pictureFrame = new Entity(pictureFrameModel, new Vector3f(-11, 35, -835), 0, 0, 270, 30);
		Entity pictureFrame2 = new Entity(pictureFrameModel2, new Vector3f(89, 35, -800), 0, 0, 270, 30);
		Entity pictureFrame3 = new Entity(pictureFrameModel3, new Vector3f(-100, 35, -800), 0, 0, 270, 30);
		Entity pictureFrame4 = new Entity(pictureFrameModel4, new Vector3f(-11, 20, -837), 0, 0, 270, 30);
		Entity planet = new Entity(planetModel, new Vector3f(140, 300, -1200), 0, 0, 0, 50);
		Entity planet2 = new Entity(planet2Model, new Vector3f(-120, 300, -1000), 0, 0, 0, 20);
		
		// Light source to light up our world
		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1,1,1));
		
		// The terrain of the world
		Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grasspng")));
		Terrain terrain2 = new Terrain(1, 0, loader, new ModelTexture(loader.loadTexture("grasspng")));
		Terrain terrain3 = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grasspng")));
		Terrain terrain4 = new Terrain(1, -1, loader, new ModelTexture(loader.loadTexture("grasspng")));
		
		// Initiate the camera
		Camera camera = new Camera();
		
		// Initiate the renderer
		MasterRenderer renderer = new MasterRenderer();
		
		// This is where all the objects are updated every frame, and all the rendering happend
		// While loop stops when the user tries to close the display
		while(!Display.isCloseRequested()) {	
			
			// Rotates the chosen entities
			torchFire1.increaseRotation(0, 0, 4);
			torchFire2.increaseRotation(0, 0, 4);
			boss.increaseRotation(0, 0, 0);
			fireOrb1.increaseRotation(0, 1, 0);
			fireOrb2.increaseRotation(0, 1, 0);
			planet.increaseRotation(0.1f, 0.3f, 0.2f);
			planet2.increaseRotation(0.02f, 0.1f, 0.01f);

			
			// Open gate if camera is this close to the gate
			if(camera.getPosition().z <= -310) {
				if(Keyboard.isKeyDown(Keyboard.KEY_T) && castleGate.getPosition().y <= 45) {
					openGate = true;
				}
			}
		
			// Opens the gate
			if(openGate == true) {
				castleGate.increasePosition(0, 0.1f, 0);
				//System.out.println(castleGate.getPosition());
				if(castleGate.getPosition().y > 45) {
					openGate = false;
				}
			}
			
			// If camera is in this position, and boss is not spawned, start spawing it
			if(camera.getPosition().z <= -595 && spawnBoss == 0) {
				spawnBoss = 1;
			}
			
			// Moves the boss upwards exposing it to the camera
			if(spawnBoss == 1) {
				fireOrb1.increasePosition(0, 0.2f, 0);
				fireOrb2.increasePosition(0, 0.2f, 0);
				boss.increasePosition(0, 0.2f, 0);
				bossFace.increasePosition(0, 0.2f, 0);
				bossHead.increasePosition(0, 0.2f, 0);
				if(fireOrb1.getPosition().y > 38 && bossHead.getPosition().y > -12 && spawnBoss == 1) {
					spawnBoss = 2; // Boss is finished spawning
				}
			}
			
			// When boss is finished spawning, move pictureFrame4 towards camera
			if(spawnBoss == 2) {
				pictureFrame.increaseRotation(0, 0, 0);
				pictureFrame2.increaseRotation(0, 1, 1);
				pictureFrame3.increaseRotation(0, -1, -1);
				pictureFrame4.increasePosition(0, 0, 1);
				
			}
			
			// Disable camera movement when the boss is spawning
			if(spawnBoss == 1) {}
			else {
				camera.move(); // Enables so we can move the world with the keyboard!
			}
			
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			renderer.processTerrain(terrain3);
			renderer.processTerrain(terrain4);
			
			renderer.processEntity(castle);
			renderer.processEntity(castleGate);
			renderer.processEntity(torches);
			renderer.processEntity(torchFire1);
			renderer.processEntity(torchFire2);
			
			renderer.processEntity(boss);
			renderer.processEntity(bossFace);
			renderer.processEntity(bossHead);
			renderer.processEntity(fireOrb1);
			renderer.processEntity(fireOrb2);
			
			renderer.processEntity(pictureFrame);
			renderer.processEntity(pictureFrame2);
			renderer.processEntity(pictureFrame3);
			renderer.processEntity(pictureFrame4);
			renderer.processEntity(planet);
			renderer.processEntity(planet2);
			
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
			
		}
		
		
		// Have to clean up after game shuts down
		renderer.cleanUp();
		
		// Cleans of all the VBOs and VAOs when game shuts down
		loader.cleanUp();
		
		// When while loop is done, we want to close the display
		DisplayManager.closeDisplay();

	}

}

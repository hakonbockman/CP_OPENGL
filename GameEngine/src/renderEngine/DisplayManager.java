package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

/**
 * Manages the display
 */
public class DisplayManager {
	
	private static final int WIDTH = 1980;
	private static final int HEIGHT = 1080;
	private static final int FPS_CAP = 120;

	// Opens the display at the beginning of launch
	public static void createDisplay() {
		
		// The contextAttribs takes inn the version of openGL we want to use, the others are just settings so its forwardcompatible
		ContextAttribs attribs = new ContextAttribs(4,4).withForwardCompatible(true).withProfileCore(true);
		
		try {
			// Sets the size of the display window
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			
			// Creates the display
			Display.create(new PixelFormat(), attribs);
			
			// Sets the title of the window
			Display.setTitle("LIGHTWEIGHT BABY");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		// Here we tell where in the display it can render the game, and we want it on the whole display:
		GL11.glViewport(0, 0, WIDTH, HEIGHT); // This tells openGL we want to use the whole display.
	}
	
	// Updates the display every single frame
	public static void updateDisplay() {
		
		// This is where you set the fps you want the game to run at.
		Display.sync(FPS_CAP);
		Display.update();
	}
	
	// Closes the display
	public static void closeDisplay() {
		// Exits the display
		Display.destroy();
	}
}

package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f(0,20,0); // Here you change position of the camera! Had to change to see terrain!
	private float pitch;	// How high and low the camera is aimed
	private float yaw;		// How much left or right the camera is aiming
	private float roll;		// How much its tilted
	
	private float movement_speed = 1;
	
	// Constructor
	public Camera() {
		
	}
	
	// Moves the camera in the game, with the help of W,A,S,D,R and F buttons
	public void move() {
		// Moves camera forward
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.z -= movement_speed;
		}
		// Moves camera to the right
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x += movement_speed;
		}
		// Moves camera to the left
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.x -= movement_speed;
		}
		// Moves camera backwards
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.z += movement_speed;
		}
		// Moves camera upwards
		if(Keyboard.isKeyDown(Keyboard.KEY_R)) {
			position.y -= 3;
		}
		// Moves camera downwards
		if(Keyboard.isKeyDown(Keyboard.KEY_F)) {
			position.y += 3;
		}

	}

	// Getters and setters
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	
	
	
}

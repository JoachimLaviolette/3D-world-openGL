package renderEngine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.glfw.*;

public class DisplayManager {
	private static final String W_TITLE = "3D Scene [OpenGL]";
	private static final int W_WIDTH = 1920;
	private static final int W_HEIGHT = 1080;
	
	private static final float clearColorRed = 1f;
	private static final float clearColorBlue = 0f;
	private static final float clearColorGreen = 0f;
	private static final float clearColorAlpha = 1f;
	
	private static long windowID;
	private static GLFWErrorCallback errorCallback;
	
	public static long createDisplay() {
		// Register method for error messages
		errorCallback = errorCallbackPrint(System.err);
		glfwSetErrorCallback(errorCallback);
		
		// Initialize GLFW
		if (glfwInit() != GL_TRUE) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
		
		// Set up window's hints
		glfwDefaultWindowHints(); 					     // Window will be floating
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);	 // Ask for OpenGL 4.2 - lower the major/minor versions if context creation fails.
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        
		// Get the size of the screen then calculate the centre.
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		int windowHorizOffset = (GLFWvidmode.width(vidmode) - W_WIDTH)/2;
        int windowVertOffset  = (GLFWvidmode.height(vidmode) - W_HEIGHT)/2;
        
        // Code for windowed window
        windowID = glfwCreateWindow(W_WIDTH, W_HEIGHT, W_TITLE, NULL, NULL);
        
        // Check the window was successfully created before we try to use it.
        if (windowID == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        
        // Set the window's position on the screen (center)
        glfwSetWindowPos(windowID, windowHorizOffset, windowVertOffset);
        
        // Set GLFW context as current
		glfwMakeContextCurrent(windowID);			// Make the glfw context current
		GLContext.createFromCurrent();				// Generate an OpenGL context from the current glfw context
		
		// Set the clear colour
		glClearColor(clearColorRed, clearColorBlue, clearColorGreen, clearColorAlpha);
	
		// Where to display
		glViewport(0, 0, W_WIDTH, W_HEIGHT);
		
		return windowID;
	}
	
	public static void updateDisplay() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glfwSwapBuffers(windowID);
	}
	
	public static void closeDisplay() {
		glfwDestroyWindow(windowID);
		glfwTerminate();
	}
}

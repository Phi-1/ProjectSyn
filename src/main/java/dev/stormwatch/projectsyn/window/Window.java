package dev.stormwatch.projectsyn.window;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {

    private static Window instance = null;

    public static Window get() {
        if (Window.instance == null) {
            Window.instance = new Window();
        }
        return Window.instance;
    }

    private int width, height;
    private String title;
    private long glfwWindow;
    private double tickrate;

    private Window() {
        this.width = 960;
        this.height = 540;
        this.title = "Project Syn";
        this.tickrate = 0.05;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Set up error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) { throw new IllegalStateException("Unable to initialize GLFW"); }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // Create window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) { throw new IllegalStateException("Failed to create the GLFW window."); }

        // Set mouse and keyboard callbacks
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        // Enable v-sync
        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);

        GL.createCapabilities();
    }

    private void loop() {

        glClearColor(0f, 0f, 0f, 1f);

        double currentTime = 0;
        double lastTime = 0;
        double deltaTime = 0;
        double lastTick = 0;
        int frames = 0;
        double timer = 0;

        while(!glfwWindowShouldClose(glfwWindow)) {
            currentTime = glfwGetTime();
            deltaTime = currentTime - lastTime;
            lastTime = currentTime;
            lastTick += deltaTime;
            timer += deltaTime;
            frames++;
            if (timer >= 1) {
                timer--;
                System.out.println(frames);
                frames = 0;
            }

            if (lastTick >= tickrate) {
                lastTick -= tickrate;
                System.out.println(MouseListener.getX() + ", " + MouseListener.getY());
                if (KeyListener.isKeyPressed(GLFW_KEY_Y)) { System.out.println("Y"); }
                MouseListener.endFrame();
            }

            glClear(GL_COLOR_BUFFER_BIT);
            glfwSwapBuffers(glfwWindow);
            glfwPollEvents();
        }
    }

}

package dev.stormwatch.projectsyn.window;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {

    private static KeyListener instance = null;

    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    private final boolean[] keyPressed = new boolean[350];

    private KeyListener() {}

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key >= get().keyPressed.length) { return; }
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int key) {
        return get().keyPressed[key];
    }

}

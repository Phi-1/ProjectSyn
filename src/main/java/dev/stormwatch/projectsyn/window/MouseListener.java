package dev.stormwatch.projectsyn.window;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {

    private static MouseListener instance = null;

    public static MouseListener get() {
        if (MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    private double xPos, yPos, lastX, lastY;
    private double scrollX, scrollY;
    private final boolean[] mouseButtonPressed = new boolean[3];
    private boolean dragging;

    private MouseListener() {
        this.xPos = 0;
        this.yPos = 0;
        this.lastX = 0;
        this.lastY = 0;
        this.scrollX = 0;
        this.scrollY = 0;
        this.dragging = false;
    }

    public static void mousePosCallback(long window, double x, double y) {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = x;
        get().yPos = y;
        get().dragging = get().mouseButtonPressed[GLFW_MOUSE_BUTTON_LEFT] || get().mouseButtonPressed[GLFW_MOUSE_BUTTON_RIGHT];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (button >= get().mouseButtonPressed.length) { return; }
        if (action == GLFW_PRESS) {
            get().mouseButtonPressed[button] = true;
        } else if (action == GLFW_RELEASE) {
            get().mouseButtonPressed[button] = false;
            get().dragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX() {
        return (float) get().xPos;
    }

    public static float getY() {
        return (float) get().yPos;
    }

    public static float getDX() {
        return (float) (get().xPos - get().lastX);
    }

    public static float getDY() {
        return (float) (get().yPos - get().lastY);
    }

    public static float getScrollX() {
        return (float) get().scrollX;
    }

    public static float getScrollY() {
        return (float) get().scrollY;
    }

    public static boolean isDragging() {
        return get().dragging;
    }

    public static boolean isMouseButtonPressed(int button) {
        return get().mouseButtonPressed[button];
    }

}

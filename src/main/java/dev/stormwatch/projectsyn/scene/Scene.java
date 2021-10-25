package dev.stormwatch.projectsyn.scene;

import dev.stormwatch.projectsyn.window.Window;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public abstract class Scene {

    protected String vertexShaderSource = "#version 330 core\n" +
            "\n" +
            "layout (location = 0) in vec3 aPos;\n" +
            "layout (location = 1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main() {\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";

    protected String fragmentShaderSource = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 FragColor;\n" +
            "\n" +
            "void main() {\n" +
            "    FragColor = vec4(fColor.xyz, 2);\n" +
            "}";

    protected float[] vertexArray = {
        // Position             // Color
        0.5f, -0.5f, 0.0f,      0.9f, 0.2f, 0.2f, 1.0f, // Top left
        -0.5f, 0.5f, 0.0f,      0.5f, 0.0f, 0.1f, 1.0f, // Top right
        0.5f, 0.5f, 0.0f,       0.3f, 0.2f, 0.6f, 1.0f, // Bottom right
        -0.5f, -0.5f, 0.0f,     0.3f, 0.2f, 0.6f, 1.0f, // Bottom left
    };

    protected int[] elementArray = {
        2, 1, 0,
        0, 1, 3
    };

    protected int vertexShader, fragmentShader, shaderProgram;
    protected int VBO, VAO, EBO;

    public Scene() {

    }

    public abstract void reset();
    public void update(float dt) {
        glUseProgram(shaderProgram);
        glBindVertexArray(VAO);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        glUseProgram(0);
    }

//    public abstract void getRenderData(); // Returns all objects and shit so rendering and shaders stuff can happen in the window class

    public void init() {
        // Compile shaders
        vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);

        int success = glGetShaderi(vertexShader, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexShader, GL_INFO_LOG_LENGTH);
            System.err.println("Failed to compile vertex shader");
            System.err.println(glGetShaderInfoLog(vertexShader, len));
            Window.get().closeWindow();
        }

        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);

        success = glGetShaderi(fragmentShader, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentShader, GL_INFO_LOG_LENGTH);
            System.err.println("Failed to compile fragment shader");
            System.err.println(glGetShaderInfoLog(fragmentShader, len));
            Window.get().closeWindow();
        }

        // Link shaders
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.err.println("Failed to link shaders");
            System.err.println(glGetProgramInfoLog(shaderProgram, len));
            Window.get().closeWindow();
        }

        // Create VAO, BVO and EBO
        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        EBO = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int positionSize = 3;
        int colorSize = 4;
        int vertexSizeBytes = (positionSize + colorSize) * Float.BYTES;

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glUseProgram(shaderProgram);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

}

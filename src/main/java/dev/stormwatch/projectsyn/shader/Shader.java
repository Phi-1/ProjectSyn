package dev.stormwatch.projectsyn.shader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Shader {

    private String file;

    public Shader(String file) {
        this.file = file;
        init();

    }

    private void init() {

    }

    private String[] loadShaderFromFile() throws IOException {
        Path path = Paths.get("/shaders/" + file + ".glsl");
        List<String> shaderSource = Files.readAllLines(path);

        return new String[] { "hej", "flask" };
    }

}

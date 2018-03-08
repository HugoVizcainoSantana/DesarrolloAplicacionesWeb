package daw.spring.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utilities {

    private Utilities() {
    }

    public static boolean checkIfPathNotExists(Path path) {
        return !path.toFile().exists();
    }

    public static void createFolder(Path path) throws IOException {
        Files.createDirectories(path);
    }
}

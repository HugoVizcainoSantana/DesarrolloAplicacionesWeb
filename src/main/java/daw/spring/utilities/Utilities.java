package daw.spring.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utilities {
    public static boolean checkIfPathExists(Path path) {
        return Files.exists(path);
    }

    public static void createFolder(Path path) throws IOException {
        Files.createDirectories(path);
    }
}

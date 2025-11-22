package edu.icet.util;

import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class FileStorageService {

    // Path to upload directory
    private final String UPLOAD_DIR = "D:\\Projects\\MedSync\\medsync_uploads";

    private final Path rootLocation;

    public FileStorageService() {
        this.rootLocation = Paths.get(UPLOAD_DIR);
        try {
            // Create the directory if it doesn't exist
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    /**
     * Saves a file to the local file system.
     *
     * @param file The file to be saved.
     * @param newFileName The unique name for the saved file.
     * @return The full, absolute path to the saved file.
     * @throws IOException
     */
    public String saveFile(File file, String newFileName) throws IOException {
        Path destinationFile = this.rootLocation.resolve(Paths.get(newFileName))
                .normalize().toAbsolutePath();

        // Copy the file to the target location
        Files.copy(file.toPath(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

        // Return the path as a string
        return destinationFile.toString();
    }
}
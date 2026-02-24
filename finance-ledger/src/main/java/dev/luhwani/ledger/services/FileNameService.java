package dev.luhwani.ledger.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileNameService {

    private FileNameService() {}

    /*
     * What this method does is that if the path inside the method signature already
     * exists,
     * it will create a substring of the path and i edit the file name to be
     * something like filepath (1) .ext
     * so as to prevent conflicting filenames
     */
    public static Path uniquePath(Path path) throws IOException {

        // this ensures that the parent directory exists. In our case, since this is to
        // be used
        // with the export summary function, the parent directory should be exports/
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        // if the file we want to add doesn't exist, we return it without having to edit
        // the filename
        if (!Files.exists(path)) {
            return path;
        }

        // if it exists, we separate the base file name from its extension (.csv, .txt
        // etc)
        String fileName = path.getFileName().toString();
        String base;
        String ext;
        int dot = fileName.lastIndexOf(".");
        if (dot > 0) {
            base = fileName.substring(0, dot);
            ext = fileName.substring(dot);
        } else {
            base = fileName;
            ext = "";
        }
        int i = 1;
        // then we check thru the parent directory if there is any one that doesnt match
        // and we append the desired number to it
        while (true) {
            Path candidate = (parent == null) ? Paths.get(base + " (" + i + ") " + ext)
                    : parent.resolve(base + " (" + i + ") " + ext);
            if (!Files.exists(candidate)) {
                return candidate;
            }
            i++;
        }

    }
}

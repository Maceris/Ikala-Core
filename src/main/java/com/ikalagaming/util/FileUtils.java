package com.ikalagaming.util;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Contains helper methods for handling files.
 *
 * @author Ches Burks
 */
@Slf4j
public class FileUtils {

    /**
     * Creates a blank file at the supplied path with the supplied name. If the file already exists,
     * or the file location is not writable by the program, no file is created and false is
     * returned.
     *
     * @param path the path of the parent folder
     * @param filename the name of the file
     * @return true if the file was created, otherwise false
     */
    public static boolean createFile(@NonNull String path, @NonNull String filename) {

        try {
            File f = new File(path + filename);
            if (f.exists()) {
                return false;
            }
            return f.createNewFile();
        } catch (IOException | SecurityException e) {
            return false;
        }
    }

    /**
     * Creates an empty folder at the supplied path with the supplied name. If the folder already
     * exists, or the folder location is not writable by the program, no folder is created and false
     * is returned. Any necessary but nonexistent parent directories will be created.
     *
     * @param path the path of the folder to create
     * @param folderName The name of the folder to create.
     * @return true if the folder was created, otherwise false
     */
    public static boolean createFolder(@NonNull String path, @NonNull String folderName) {

        try {
            File f = new File(path + folderName);
            if (f.exists()) {
                return false;
            }
            return f.mkdirs();
        } catch (SecurityException e) {
            return false;
        }
    }

    /**
     * If the supplied path exists, and it can be read and written to, then the file is deleted. If
     * this pathname denotes a directory, then the directory must be empty in order to be deleted.
     *
     * @param path The path of the file or directory.
     * @return <code>true</code> if and only if the file or directory is successfully deleted;
     *     <code>false</code> otherwise
     */
    public static boolean deleteFile(String path) {
        if (path == null) {
            return false;
        }
        File f = new File(path);
        try {
            if (!f.exists() || !f.canRead() || !f.canWrite()) {
                return false;
            }
            /*
             * This is less informative than Files.delete, but we throw away
             * information anyways so this will not help us. Ignoring
             * java:S4042.
             */
            return f.delete(); // NOSONAR
        } catch (SecurityException e) {
            return false; // if it can't be accessed, then it can't be deleted.
        }
    }

    /**
     * Returns true if a file or directory exists at the given path.
     *
     * @param path the path to the file/directory
     * @return true if the supplied file/directory exists
     */
    public static boolean fileExists(@NonNull String path) {
        /*
         * Will not throw an exception because path can't be null
         */
        File f = new File(path);
        try {
            return f.exists();
        } catch (SecurityException e) {
            /*
             * Cannot read that location due to security manager, so just assume
             * it does not exist.
             */
            return false;
        }
    }

    /**
     * Returns a reference to the requested file. The file may be null, and no permissions are
     * guaranteed.
     *
     * @param path the path of the file
     * @return the file requested
     */
    public static Optional<File> getFile(@NonNull String path) {
        return Optional.of(new File(path));
    }

    /**
     * Reads in all the text in a file, concatenates it into a string, and returns that. This is
     * very likely to take a relatively long time as we are reading from disk, be careful calling
     * it.
     *
     * @param file The file to read from.
     * @return The contents of the file as a UTF8 string, or empty string if it's not a valid file.
     */
    public static String readAsString(@NonNull File file) {
        if (!file.exists() || !file.canRead() || !file.isFile()) {
            return "";
        }
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
            stream.forEach(line -> contentBuilder.append(line).append("\n"));
        } catch (IOException e) {
            log.warn("Exception occurred reading file", e);
        }

        return contentBuilder.toString();
    }

    /** Private constructor so this class is not instantiated. */
    private FileUtils() {
        throw new UnsupportedOperationException("This utility class should not be instantiated");
    }
}

package pl.mimuw.fs;


import org.junit.jupiter.api.Assertions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FSTestsUtils {

    public static Path convertStringToPath(String... entries) {
        return Paths.get(String.join("/", entries));
    }

    public static void assertEntryExists(final Path pathToEntry) {
        Assertions.assertTrue(Files.exists(pathToEntry));
    }

    public static void assertIsRegularFile(final Path pathToFile) {
        Assertions.assertTrue(Files.isRegularFile(pathToFile));
    }

    public static void assertIsDirectory(final Path pathToDirectory) {
        Assertions.assertTrue(Files.isDirectory(pathToDirectory));
    }

    public static void assertFileContent(final Path pathToFile, final String content) {
        try {
            Assertions.assertEquals(content, Files.readString(pathToFile));
        } catch (Exception e) {
            Assertions.fail("Error while reading file: " + pathToFile.toString());
        }
    }

    public static void assertEntriesInDirectory(final Path pathToDirectory, final int expectedNumberOfEntries) {
        try {
            Assertions.assertEquals(expectedNumberOfEntries, Files.list(pathToDirectory).count());
        } catch (Exception e) {
            Assertions.fail("Error while reading directory: " + pathToDirectory.toString());
        }
    }
}

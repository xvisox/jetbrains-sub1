package pl.mimuw.fs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pl.mimuw.fs.exceptions.FSEntryNotCreatedException;
import pl.mimuw.fs.exceptions.FSEntrySizeException;
import pl.mimuw.fs.exceptions.FSNoSuchDirectoryException;

import java.nio.file.Path;
import java.util.List;

public class FSTests {

    private static final String TEST_FOLDER_NAME = "test";
    private static final String TEST_FILE_NAME = "test.txt";
    private static final String TEST_FILE_CONTENT = "Hello world!";

    @Test
    void createFileTest(@TempDir Path tempDir) {
        // given
        final FSFile file = new FSFile(TEST_FILE_NAME, TEST_FILE_CONTENT);

        // when
        final String path = tempDir.toString();
        FSCreator.create(file, path);

        // then
        final Path pathToFile = FSTestsUtils.convertStringToPath(path, TEST_FILE_NAME);
        FSTestsUtils.assertEntryExists(pathToFile);
        FSTestsUtils.assertIsRegularFile(pathToFile);
        FSTestsUtils.assertFileContent(pathToFile, TEST_FILE_CONTENT);
    }

    @Test
    void createFolderTest(@TempDir Path tempDir) {
        // given
        final FSFolder folder = new FSFolder(TEST_FOLDER_NAME, List.of());

        // when
        final String path = tempDir.toString();
        FSCreator.create(folder, path);

        // then
        final Path pathToDirectory = FSTestsUtils.convertStringToPath(path, TEST_FOLDER_NAME);
        FSTestsUtils.assertEntryExists(pathToDirectory);
        FSTestsUtils.assertIsDirectory(pathToDirectory);
        FSTestsUtils.assertEntriesInDirectory(pathToDirectory, 0);
    }

    @Test
    void createFolderWithFileTest(@TempDir Path tempDir) {
        // given
        final FSFile file = new FSFile(TEST_FILE_NAME, TEST_FILE_CONTENT);
        final FSFolder folder = new FSFolder(TEST_FOLDER_NAME, List.of(file));

        // when
        final String path = tempDir.toString();
        FSCreator.create(folder, path);

        // then
        final Path pathToDirectory = FSTestsUtils.convertStringToPath(path, TEST_FOLDER_NAME);
        FSTestsUtils.assertEntryExists(pathToDirectory);
        FSTestsUtils.assertIsDirectory(pathToDirectory);
        FSTestsUtils.assertEntriesInDirectory(pathToDirectory, 1);

        final Path pathToFile = FSTestsUtils.convertStringToPath(path, TEST_FOLDER_NAME, TEST_FILE_NAME);
        FSTestsUtils.assertEntryExists(pathToFile);
        FSTestsUtils.assertIsRegularFile(pathToFile);
        FSTestsUtils.assertFileContent(pathToFile, TEST_FILE_CONTENT);
    }


    @Test
    void createComplexStructureTest(@TempDir Path tempDir) {
        // given
        final FSFile file1 = new FSFile(TEST_FILE_NAME, TEST_FILE_CONTENT);
        final FSFile file2 = new FSFile(TEST_FILE_NAME, TEST_FILE_CONTENT);

        final String folderName1 = TEST_FOLDER_NAME + "1";
        final String folderName2 = TEST_FOLDER_NAME + "2";
        final String folderName3 = TEST_FOLDER_NAME + "3";
        final FSFolder folder1 = new FSFolder(folderName1, List.of(file1));
        final FSFolder folder2 = new FSFolder(folderName2, List.of(file2));
        final FSFolder folder3 = new FSFolder(folderName3, List.of(folder1, folder2));

        // when
        final String path = tempDir.toString();
        FSCreator.create(folder3, path);

        // then
        final Path pathToDirectory = FSTestsUtils.convertStringToPath(path, folderName3);
        FSTestsUtils.assertEntryExists(pathToDirectory);
        FSTestsUtils.assertIsDirectory(pathToDirectory);
        FSTestsUtils.assertEntriesInDirectory(pathToDirectory, 2);

        final Path pathToDirectory1 = FSTestsUtils.convertStringToPath(path, folderName3, folderName1);
        FSTestsUtils.assertEntryExists(pathToDirectory1);
        FSTestsUtils.assertIsDirectory(pathToDirectory1);
        FSTestsUtils.assertEntriesInDirectory(pathToDirectory1, 1);

        final Path pathToFile1 = FSTestsUtils.convertStringToPath(path, folderName3, folderName1, TEST_FILE_NAME);
        FSTestsUtils.assertEntryExists(pathToFile1);
        FSTestsUtils.assertIsRegularFile(pathToFile1);
        FSTestsUtils.assertFileContent(pathToFile1, TEST_FILE_CONTENT);

        final Path pathToDirectory2 = FSTestsUtils.convertStringToPath(path, folderName3, folderName2);
        FSTestsUtils.assertEntryExists(pathToDirectory2);
        FSTestsUtils.assertIsDirectory(pathToDirectory2);
        FSTestsUtils.assertEntriesInDirectory(pathToDirectory2, 1);

        final Path pathToFile2 = FSTestsUtils.convertStringToPath(path, folderName3, folderName2, TEST_FILE_NAME);
        FSTestsUtils.assertEntryExists(pathToFile2);
        FSTestsUtils.assertIsRegularFile(pathToFile2);
        FSTestsUtils.assertFileContent(pathToFile2, TEST_FILE_CONTENT);
    }

    @Test
    void createSameFileTwiceExceptionTest(@TempDir Path tempDir) {
        // given
        final FSFile file = new FSFile(TEST_FILE_NAME, TEST_FILE_CONTENT);

        // when & then
        final String path = tempDir.toString();
        FSCreator.create(file, path);
        Assertions.assertThrows(FSEntryNotCreatedException.class, () -> FSCreator.create(file, path));
    }

    @Test
    void createSameFolderTwiceExceptionTest(@TempDir Path tempDir) {
        // given
        final FSFolder folder = new FSFolder(TEST_FOLDER_NAME, List.of());

        // when & then
        final String path = tempDir.toString();
        FSCreator.create(folder, path);
        Assertions.assertThrows(FSEntryNotCreatedException.class, () -> FSCreator.create(folder, path));
    }

    @Test
    void createFileWithSameNameAsFolderExceptionTest(@TempDir Path tempDir) {
        // given
        final FSFile file = new FSFile(TEST_FOLDER_NAME, TEST_FILE_CONTENT);
        final FSFolder folder = new FSFolder(TEST_FOLDER_NAME, List.of());

        // when & then
        final String path = tempDir.toString();
        FSCreator.create(folder, path);
        Assertions.assertThrows(FSEntryNotCreatedException.class, () -> FSCreator.create(file, path));
    }

    @Test
    void createFileWithWrongPathExceptionTest(@TempDir Path tempDir) {
        // given
        final FSFile file = new FSFile(TEST_FILE_NAME, TEST_FILE_CONTENT);

        // when & then
        final String path = tempDir.toString() + "/nonexistent";
        Assertions.assertThrows(FSNoSuchDirectoryException.class, () -> FSCreator.create(file, path));
    }

    @Test
    void createFolderWithFileWithWrongPathExceptionTest(@TempDir Path tempDir) {
        // given
        final FSFolder folder = new FSFolder(TEST_FOLDER_NAME, List.of());

        // when & then
        final String path = tempDir.toString() + "/nonexistent";
        Assertions.assertThrows(FSNoSuchDirectoryException.class, () -> FSCreator.create(folder, path));
    }

    @Test
    void createTheBiggestPossibleFolderPerformanceTest(@TempDir Path tempDir) {
        // given
        final List<FSEntry> fileEntries = FSTestsUtils.createListOfUniqueFiles(FSConstants.FOLDER_CONTENT_MAX_SIZE, FSConstants.FILE_CONTENT_MAX_SIZE);
        final FSFolder folder = new FSFolder(TEST_FOLDER_NAME, fileEntries);

        // when
        final String path = tempDir.toString();
        final long timeElapsed = FSTestsUtils.measureTime(() -> FSCreator.create(folder, path));

        // then
        Assertions.assertTrue(timeElapsed < 1500L);
    }

    @Test
    void createFolderWithTooMuchEntriesExceptionTest() {
        // given
        final List<FSEntry> fileEntries = FSTestsUtils.createListOfUniqueFiles(FSConstants.FOLDER_CONTENT_MAX_SIZE + 1, 1);
        final FSFolder folder = new FSFolder(TEST_FOLDER_NAME, fileEntries);

        // when & then
        final String path = "/some/path";
        Assertions.assertThrows(FSEntrySizeException.class, () -> FSCreator.create(folder, path));
    }

    @Test
    void createFileWithTooMuchContentExceptionTest() {
        // given
        final String fileContent = FSTestsUtils.createStringOfLength(FSConstants.FILE_CONTENT_MAX_SIZE + 1);
        final FSFile file = new FSFile(TEST_FILE_NAME, fileContent);

        // when & then
        final String path = "/some/path";
        Assertions.assertThrows(FSEntrySizeException.class, () -> FSCreator.create(file, path));
    }

    @Test
    void createFileWithTooLongNameExceptionTest() {
        // given
        final String fileName = FSTestsUtils.createStringOfLength(FSConstants.FILE_NAME_MAX_LENGTH + 1);
        final FSFile file = new FSFile(fileName, TEST_FILE_CONTENT);

        // when & then
        final String path = "/some/path";
        Assertions.assertThrows(FSEntrySizeException.class, () -> FSCreator.create(file, path));
    }

    @Test
    void createFolderWithTooLongNameExceptionTest() {
        // given
        final String folderName = FSTestsUtils.createStringOfLength(FSConstants.FOLDER_NAME_MAX_LENGTH + 1);
        final FSFolder folder = new FSFolder(folderName, List.of());

        // when & then
        final String path = "/some/path";
        Assertions.assertThrows(FSEntrySizeException.class, () -> FSCreator.create(folder, path));
    }

    @Test
    void createFileInFolderWithoutPermissionsExceptionTest() {
        // given
        final FSFile file = new FSFile(TEST_FILE_NAME, TEST_FILE_CONTENT);

        // when & then
        final String path = "/root";
        Assertions.assertThrows(FSEntryNotCreatedException.class, () -> FSCreator.create(file, path));
    }
}

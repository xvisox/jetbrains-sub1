package pl.mimuw.fs;

import lombok.extern.slf4j.Slf4j;
import pl.mimuw.fs.exceptions.FSEntrySizeException;
import pl.mimuw.fs.exceptions.FSEntryNotCreatedException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
public class FSFile extends FSEntry {
    private final String content;

    public FSFile(String name, String content) {
        super(name);
        this.content = content;
    }

    private void createFile(final String pathToFile, final String content) throws FSEntryNotCreatedException {
        try {
            Files.write(Paths.get(pathToFile), content.getBytes(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
            log.info("Created file: " + pathToFile);
        } catch (Exception e) {
            log.error("Error occurred while creating file: " + pathToFile, e);
            throw new FSEntryNotCreatedException(e.getMessage());
        }
    }

    @Override
    public void createEntryInFS(final String destination) throws FSEntryNotCreatedException {
        throwIfDirectoryDoesNotExist(destination);
        createFile(destination + "/" + name, content);
    }

    @Override
    public void validateEntrySize() throws FSEntrySizeException {
        if (name.length() > FSConstants.FILE_NAME_MAX_LENGTH) {
            throw new FSEntrySizeException("File name exceeds max size: " + FSConstants.FILE_NAME_MAX_LENGTH);
        }
        if (content.length() > FSConstants.FILE_CONTENT_MAX_SIZE) {
            throw new FSEntrySizeException("File content exceeds max size: " + FSConstants.FILE_CONTENT_MAX_SIZE);
        }
    }
}

package pl.mimuw.fs;

import lombok.extern.slf4j.Slf4j;
import pl.mimuw.fs.exceptions.FSEntrySizeException;
import pl.mimuw.fs.exceptions.FSEntryNotCreatedException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class FSFolder extends FSEntry {
    private final List<FSEntry> content;

    public FSFolder(String name, List<FSEntry> content) {
        super(name);
        this.content = content;
    }

    private void createDirectory(final String pathToDirectory) throws FSEntryNotCreatedException {
        try {
            Files.createDirectory(Paths.get(pathToDirectory));
            log.info("Created directory: " + pathToDirectory);
        } catch (Exception e) {
            log.error("Error occurred while creating directory: " + pathToDirectory, e);
            throw new FSEntryNotCreatedException(e.getMessage());
        }
    }

    @Override
    public void createEntryInFS(final String destination) throws FSEntryNotCreatedException {
        throwIfDirectoryDoesNotExist(destination);
        final String pathToDirectory = destination + "/" + name;
        createDirectory(pathToDirectory);
        for (FSEntry entry : content) {
            entry.createEntryInFS(pathToDirectory);
        }
    }

    @Override
    public void validateEntrySize() throws FSEntrySizeException {
        if (name.length() > FSConstants.FOLDER_NAME_MAX_LENGTH) {
            throw new FSEntrySizeException("Directory name exceeds max size: " + FSConstants.FOLDER_NAME_MAX_LENGTH);
        }
        if (content.size() > FSConstants.FOLDER_CONTENT_MAX_SIZE) {
            throw new FSEntrySizeException("Directory content exceeds max size: " + FSConstants.FOLDER_CONTENT_MAX_SIZE);
        }
        for (FSEntry entry : content) {
            entry.validateEntrySize();
        }
    }
}

package pl.mimuw.fs;

import lombok.extern.slf4j.Slf4j;
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
            log.info("Created file: " + pathToFile + " with content: " + content);
        } catch (Exception e) {
            log.error("Error occurred while creating file: " + pathToFile + " with content: " + content, e);
            throw new FSEntryNotCreatedException(e.getMessage());
        }
    }

    @Override
    public void createEntryInFS(final String destination) throws FSEntryNotCreatedException {
        throwIfDirectoryDoesNotExist(destination);
        createFile(destination + "/" + name, content);
    }
}

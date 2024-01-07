package pl.mimuw.fs;

import lombok.extern.slf4j.Slf4j;
import pl.mimuw.fs.exceptions.FSCyclicStructureException;
import pl.mimuw.fs.exceptions.FSEntryNotCreatedException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

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
    public void checkForCycle(final Set<FSEntry> visited) throws FSCyclicStructureException {
        if (visited.contains(this)) {
            log.error("Cyclic structure detected");
            throw new FSCyclicStructureException("Cyclic structure detected! Cannot create entry in FS");
        }
        visited.add(this);
        for (FSEntry entry : content) {
            entry.checkForCycle(visited);
        }
    }
}

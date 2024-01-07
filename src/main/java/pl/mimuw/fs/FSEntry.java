package pl.mimuw.fs;

import lombok.extern.slf4j.Slf4j;
import pl.mimuw.fs.exceptions.FSCyclicStructureException;
import pl.mimuw.fs.exceptions.FSEntryNotCreatedException;
import pl.mimuw.fs.exceptions.FSNoSuchDirectoryException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

@Slf4j
public abstract class FSEntry {
    protected final String name;

    protected FSEntry(String name) {
        this.name = name;
    }

    public abstract void createEntryInFS(final String destination) throws FSEntryNotCreatedException;

    public abstract void checkForCycle(final Set<FSEntry> visited) throws FSCyclicStructureException;

    protected void throwIfDirectoryDoesNotExist(final String pathToDirectory) {
        final Path path = Paths.get(pathToDirectory);
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            log.error("Directory: " + pathToDirectory + " does not exist");
            throw new FSNoSuchDirectoryException(pathToDirectory);
        }
    }
}

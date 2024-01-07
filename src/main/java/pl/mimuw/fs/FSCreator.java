package pl.mimuw.fs;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;

@Slf4j
public class FSCreator {

    @SneakyThrows
    public static void create(final FSEntry entryToCreate, final String destination) {
        try {
            entryToCreate.checkForCycle(new HashSet<>());
            entryToCreate.createEntryInFS(destination);
        } catch (Exception e) {
            // It wasn't specified what to do in case of failure,
            // I would probably expect rollback here i.e. deleting all created files
            log.error("Failed to create entry in FS, performing a rollback...");
            throw e;
        }
    }
}

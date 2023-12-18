package pl.mimuw.fs.exceptions;

public class FSNoSuchDirectoryException extends RuntimeException {
    public FSNoSuchDirectoryException(String message) {
        super(message);
    }
}

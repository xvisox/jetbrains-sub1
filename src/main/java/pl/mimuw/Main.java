package pl.mimuw;

import pl.mimuw.fs.FSCreator;
import pl.mimuw.fs.FSFile;
import pl.mimuw.fs.FSFolder;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        FSFile file = new FSFile("file.txt", "Hello World!");
        FSFolder folder = new FSFolder("temp", List.of(file));
        FSCreator.create(folder, ".");
    }
}
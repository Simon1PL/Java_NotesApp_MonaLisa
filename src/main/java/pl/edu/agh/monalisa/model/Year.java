package pl.edu.agh.monalisa.model;

import java.nio.file.Path;
import java.util.List;

public class Year extends Package<Subject> {

    public Year(String name, Path path) {
        super(name, path);
    }

    public Year(String name, Path parentDirectoryPath, List<Subject> subjects) {
        super(name, parentDirectoryPath, subjects);
    }
}

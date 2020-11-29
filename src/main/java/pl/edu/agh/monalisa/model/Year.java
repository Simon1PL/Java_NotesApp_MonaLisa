package pl.edu.agh.monalisa.model;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;

public class Year extends Package {
    private final Collection<Subject> subjects;

    public Year(String name, Path path) {
        super(name, path);
        this.subjects = new HashSet<>();
    }

    public Year(String name, Path parentDirectoryPath, Collection<Subject> subjects) {
        super(name, parentDirectoryPath);
        this.subjects = subjects;
    }

    public void addSubject(Subject subject) {
        this.subjects.add(subject);
    }

    public Collection<Subject> getSubjects() {
        return subjects;
    }
}

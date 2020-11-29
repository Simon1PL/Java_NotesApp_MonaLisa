package pl.edu.agh.monalisa.model;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;

public class Student extends Package {
    private final Collection<AssignmentFile> assignments;

    public Student(String name, Path parentDirectoryPath, Collection<AssignmentFile> assignments) {
        super(name, parentDirectoryPath);
        this.assignments = assignments;
    }

    public Student(String fileName, Path parent) {
        super(fileName, parent);
        this.assignments = new HashSet<>();
    }

    public void addAssigment(AssignmentFile assigment) {
        this.assignments.add(assigment);
    }

    public Collection<AssignmentFile> getLabs() {
        return assignments;
    }
}

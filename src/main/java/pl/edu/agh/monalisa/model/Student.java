package pl.edu.agh.monalisa.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public class Student extends Package {
    private final ObservableList<AssignmentFile> assignments;

    public Student(String name, Path parentDirectoryPath, List<AssignmentFile> assignments) {
        super(name, parentDirectoryPath);
        this.assignments = FXCollections.observableList(assignments);
    }

    public Student(String fileName, Path parent) {
        super(fileName, parent);
        this.assignments = FXCollections.observableArrayList();
    }

    public void addAssigment(AssignmentFile assigment) {
        this.assignments.add(assigment);
    }

    public ObservableList<AssignmentFile> getAssignments() {
        return assignments;
    }
}

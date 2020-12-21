package pl.edu.agh.monalisa.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Student extends Package {
    private final ObservableList<AssignmentFile> assignments;
    private final Lab parent;

    public Student(String name, Lab parent, List<AssignmentFile> assignments) {
        super(name, parent.getPath());
        this.parent = parent;
        this.assignments = FXCollections.observableList(assignments);
    }

    public Student(String fileName, Lab parent) {
        super(fileName, parent.getPath());
        this.parent = parent;
        this.assignments = FXCollections.observableArrayList();
    }

    @Override
    public ObservableList<? extends Package> getChildrenPackages() {
        return FXCollections.observableArrayList();
    }

    @Override
    public ObservableList<? extends GenericFile> getChildren() {
        return getAssignments();
    }

    public void addAssigment(AssignmentFile assigment) {
        this.assignments.add(assigment);
    }

    public ObservableList<AssignmentFile> getAssignments() {
        return assignments;
    }

    public Lab getParent() {
        return parent;
    }
}

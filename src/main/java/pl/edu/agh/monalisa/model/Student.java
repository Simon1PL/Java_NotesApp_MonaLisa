package pl.edu.agh.monalisa.model;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Student extends Package {
    private final Lab parent;

    public Student(String fileName, Lab parent) {
        this(fileName, parent, null);
    }

    public Student(String name, Lab parent, List<AssignmentFile> assignments) {
        super(name, parent.getPath(), assignments);
        this.parent = parent;
    }

    public void addAssigment(AssignmentFile assigment) {
        this.addChild(assigment);
    }

    public Lab getParent() {
        return parent;
    }

    public Collection<AssignmentFile> getAssignments() {
        return this.getChildren().stream()
                .filter(child -> child instanceof AssignmentFile)
                .map(child -> (AssignmentFile)child).collect(Collectors.toList());
    }
}

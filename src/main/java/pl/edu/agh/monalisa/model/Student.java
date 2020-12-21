package pl.edu.agh.monalisa.model;

import java.util.List;

public class Student extends Package<AssignmentFile> {
    private final Lab parent;

    public Student(String name, Lab parent, List<AssignmentFile> assignments) {
        super(name, parent.getPath(), assignments);
        this.parent = parent;
    }

    public Student(String fileName, Lab parent) {
        super(fileName, parent.getPath());
        this.parent = parent;
    }

    public Lab getParent() {
        return parent;
    }
}

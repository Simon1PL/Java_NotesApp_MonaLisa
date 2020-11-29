package pl.edu.agh.monalisa.model;

import java.util.Collection;
import java.util.HashSet;

public class Year extends Package {
    private Collection<Subject> subjects = new HashSet<>();

    public Year(String name, String path) {
        super(name, path);
    }

    public void addSubject(Subject subject) {
        this.subjects.add(subject);
        subject.setYear(this);
    }

    public Collection<Subject> getSubjects() {
        return subjects;
    }
}

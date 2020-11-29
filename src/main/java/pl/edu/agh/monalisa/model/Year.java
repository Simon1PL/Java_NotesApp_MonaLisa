package pl.edu.agh.monalisa.model;

import java.util.Collection;
import java.util.HashSet;

public class Year {
    private String name;
    private Collection<Subject> subjects = new HashSet<>();

    public Year(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addSubject(Subject subject) {
        this.subjects.add(subject);
        subject.setYear(this);
    }

    public Collection<Subject> getSubjects() {
        return subjects;
    }
}

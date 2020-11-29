package pl.edu.agh.monalisa.model;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;

public class Subject extends Package {
    private Collection<Lab> labs;

    public Subject(String name, Path parent) {
        super(name, parent);
        this.labs = new HashSet<>();
    }

    public Subject(String name, Path parent, Collection<Lab> labs) {
        super(name, parent);
        this.labs = labs;
    }

    public void addLab(Lab subject) {
        this.labs.add(subject);
    }

    public Collection<Lab> getLabs() {
        return labs;
    }
}

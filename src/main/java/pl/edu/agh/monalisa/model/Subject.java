package pl.edu.agh.monalisa.model;

import java.nio.file.Path;
import java.util.List;

public class Subject extends Package {

    public Subject(String name, Path parent) {
        this(name, parent, null);
    }

    public Subject(String name, Path parent, List<Lab> labs) {
        super(name, parent, labs);
    }

    public void addLab(Lab subject) {
        this.addChild(subject);
    }
}

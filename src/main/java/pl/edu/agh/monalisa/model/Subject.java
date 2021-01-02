package pl.edu.agh.monalisa.model;

import java.nio.file.Path;
import java.util.List;

public class Subject extends Package<Lab> {

    public Subject(String name, Path parent) {
        super(name, parent);
    }

    public Subject(String name, Path parent, List<Lab> labs) {
        super(name, parent, labs);
    }

}

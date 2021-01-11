package pl.edu.agh.monalisa.model;

import java.nio.file.Path;
import java.util.List;

public class Lab extends Package<Student> {

    public Lab(String name, Path parent) {
        super(name, parent);
    }

    public Lab(String name, Path parent, List<Student> students) {
        super(name, parent, students);
    }
}

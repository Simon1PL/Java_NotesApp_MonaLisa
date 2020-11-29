package pl.edu.agh.monalisa.model;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;

public class Lab extends Package {
    private Collection<Student> students;

    public Lab(String name, Subject subject) {
        super(name, subject);
        this.students = new HashSet<>();
    }

    public Lab(String name, Path parent, Collection<Student> students) {
        super(name, parent);
        this.students = students;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public Collection<Student> getStudents() {
        return students;
    }
}

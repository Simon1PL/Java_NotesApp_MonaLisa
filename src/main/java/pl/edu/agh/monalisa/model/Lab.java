package pl.edu.agh.monalisa.model;

import java.util.Collection;
import java.util.HashSet;

public class Lab {
    private String name;
    private Collection<Student> students = new HashSet<>();

    public Lab(String name) {
        this.name = name;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public Collection<Student> getStudents() {
        return students;
    }
}

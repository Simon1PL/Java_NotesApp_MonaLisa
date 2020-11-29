package pl.edu.agh.monalisa.model;

import java.util.Collection;
import java.util.HashSet;

public class Lab extends Package {
    private String name;
    private Collection<Student> students = new HashSet<>();

    public Lab(String name, Subject subject) {
        super(name, subject);
        subject.addLab(this);
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public Collection<Student> getStudents() {
        return students;
    }
}

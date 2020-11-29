package pl.edu.agh.monalisa.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class Lab extends Package {
    private ObservableList<Student> students;

    public Lab(String name, Subject subject) {
        super(name, subject);
        this.students = FXCollections.observableArrayList();
    }

    public Lab(String name, Path parent, List<Student> students) {
        super(name, parent);
        this.students = FXCollections.observableList(students);
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public ObservableList<Student> getStudents() {
        return students;
    }
}

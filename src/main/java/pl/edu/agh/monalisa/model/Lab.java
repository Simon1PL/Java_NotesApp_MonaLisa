package pl.edu.agh.monalisa.model;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Lab extends Package {
    private ObservableList<Student> students;

    public Lab(String name, Path parent) {
        this(name, parent, null);
    }

    public Lab(String name, Path parent, List<Student> students) {
        super(name, parent, students);
        initializeStudents();
    }

    public void addStudent(Student student) {
        this.addChild(student);
    }

    public ObservableList<Student> getStudents() {
        return students;
    }

    public void initializeStudents() {
        this.students = FXCollections.observableList(this.getChildren().stream()
                .filter(child -> child instanceof Student)
                .map(child -> (Student)child).collect(Collectors.toList()));

        this.getChildren().addListener((ListChangeListener<FileOrPackage>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(child -> {
                        if (child instanceof Student) {
                            students.add((Student) child);
                        }
                    });
                } else if (change.wasRemoved()) {
                    change.getRemoved().forEach(child -> {
                        students.removeIf(student -> student.equals(child));
                    });
                }
            }
        });
    }
}

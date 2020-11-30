package pl.edu.agh.monalisa.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class Loader {
    private Root root;

    public Loader() {
        FileSystemChangeListener.startListening();
    }

    public Root loadModel(Path rootPath) {
        var files = rootPath.toFile().listFiles();
        if (files == null) throw new IllegalArgumentException("Root path must be a directory");

        var years = Arrays.stream(files)
                .map(this::loadYear)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        root = new Root(years);
        FileSystemChangeListener.registerPath(rootPath);
        return root;
    }

    private Year loadYear(File yearFile) {
        var subjectFiles = yearFile.listFiles();
        if (subjectFiles == null) return null;

        var subjects = Arrays.stream(subjectFiles)
                .map(this::loadSubject)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        FileSystemChangeListener.registerPath(yearFile.toPath());
        return new Year(yearFile.getName(), yearFile.getParentFile().toPath(), subjects);
    }

    private Subject loadSubject(File subjectFile) {
        var labFiles = subjectFile.listFiles();
        if (labFiles == null) return null;

        var labs = Arrays.stream(labFiles)
                .map(this::loadLab)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        FileSystemChangeListener.registerPath(subjectFile.toPath());
        return new Subject(subjectFile.getName(), subjectFile.getParentFile().toPath(), labs);
    }

    private Lab loadLab(File labFile) {
        var studentFiles = labFile.listFiles();
        if (studentFiles == null) return null;

        var students = Arrays.stream(studentFiles)
                .map(this::loadStudent)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        FileSystemChangeListener.registerPath(labFile.toPath());
        return new Lab(labFile.getName(), labFile.getParentFile().toPath(), students);
    }

    private Student loadStudent(File studentFile) {
        var assignmentFiles = studentFile.listFiles();
        if (assignmentFiles == null) return null;

        var students = Arrays.stream(assignmentFiles)
                .filter(File::isFile)
                .map(file -> new AssignmentFile(file.getName(), studentFile.toPath()))
                .collect(Collectors.toList());

        FileSystemChangeListener.registerPath(studentFile.toPath());
        return new Student(studentFile.getName(), studentFile.getParentFile().toPath(), students);
    }
}

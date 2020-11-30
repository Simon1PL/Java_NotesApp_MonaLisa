package pl.edu.agh.monalisa.loader;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import pl.edu.agh.monalisa.model.*;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Singleton
public class Loader {
    private Root root;
    private final FilesystemWatcher filesystemListener;

    @Inject
    public Loader(FilesystemWatcher filesystemListener) {
        this.filesystemListener = filesystemListener;
    }

    public Root loadModel(Path rootPath) {
        var files = rootPath.toFile().listFiles();
        if (files == null) throw new IllegalArgumentException("Root path must be a directory");

        var years = Arrays.stream(files)
                .map(this::loadYear)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        root = new Root(rootPath.getFileName().toString(), rootPath.getParent(), years);
        filesystemListener.register(root, (yearFile) -> root.addYear(loadYear(yearFile)),
                (yearFile) -> root.getYears().removeIf(y -> y.getPath().equals(yearFile.toPath())),
                FileType.DIRECTORY);
        return root;
    }

    private Year loadYear(File yearFile) {
        var subjectFiles = yearFile.listFiles();
        if (subjectFiles == null) return null;

        var subjects = Arrays.stream(subjectFiles)
                .map(this::loadSubject)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        var year = new Year(yearFile.getName(), yearFile.getParentFile().toPath(), subjects);
        filesystemListener.register(year, (subjectFile) -> year.addSubject(loadSubject(subjectFile)),
                (subjectFile) -> year.getSubjects().removeIf(s -> s.getPath().equals(subjectFile.toPath())),
                FileType.DIRECTORY);
        return year;
    }

    private Subject loadSubject(File subjectFile) {
        var labFiles = subjectFile.listFiles();
        if (labFiles == null) return null;

        var labs = Arrays.stream(labFiles)
                .map(this::loadLab)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        var subject = new Subject(subjectFile.getName(), subjectFile.getParentFile().toPath(), labs);

        filesystemListener.register(subject, (labFile) -> subject.addLab(loadLab(labFile)),
                (labFile) -> subject.getLabs().removeIf(l -> l.getPath().equals(labFile.toPath())),
                FileType.DIRECTORY);
        return subject;
    }

    private Lab loadLab(File labFile) {
        var studentFiles = labFile.listFiles();
        if (studentFiles == null) return null;

        var students = Arrays.stream(studentFiles)
                .map(this::loadStudent)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        var lab = new Lab(labFile.getName(), labFile.getParentFile().toPath(), students);
        filesystemListener.register(lab, (studentFile) -> lab.addStudent(loadStudent(studentFile)),
                (studentFile) -> lab.getStudents().removeIf(s -> s.getPath().equals(studentFile.toPath())),
                FileType.DIRECTORY);
        return lab;
    }

    private Student loadStudent(File studentFile) {
        var assignmentFiles = studentFile.listFiles();
        if (assignmentFiles == null) return null;

        var students = Arrays.stream(assignmentFiles)
                .filter(File::isFile)
                .map(file -> new AssignmentFile(file.getName(), studentFile.toPath()))
                .collect(Collectors.toList());

        var student = new Student(studentFile.getName(), studentFile.getParentFile().toPath(), students);
        filesystemListener.register(student, (assignmentFile) -> student.addAssigment(new AssignmentFile(assignmentFile.getName(), assignmentFile.getParentFile().toPath())),
                (assignmentFile) -> student.getAssignments().removeIf(a -> a.getPath().equals(assignmentFile.toPath())),
                FileType.FILE);
        return student;
    }
}

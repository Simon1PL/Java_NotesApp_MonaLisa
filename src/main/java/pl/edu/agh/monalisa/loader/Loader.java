package pl.edu.agh.monalisa.loader;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
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

    @Inject
    public Root loadModel(@Named("RootPath") Path rootPath) {
        var files = rootPath.toFile().listFiles();
        if (files == null) throw new IllegalArgumentException("Root path must be a directory");

        var years = Arrays.stream(files)
                .map(this::loadYear)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        root = new Root(rootPath.getFileName().toString(), rootPath.getParent(), years);

        filesystemListener.register(root, FileType.DIRECTORY)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(event -> {
                    if (event.getKind() == FileSystemEvent.EventKind.CREATED)
                        root.addYear(loadYear(event.getTarget().toFile()));
                    else
                        root.getYears().removeIf(y -> y.getPath().equals(event.getTarget()));
                });
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

        filesystemListener.register(year, FileType.DIRECTORY)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(event -> {
                    if (event.getKind() == FileSystemEvent.EventKind.CREATED)
                        year.addSubject(loadSubject(event.getTarget().toFile()));
                    else year.getSubjects().removeIf(s -> s.getPath().equals(event.getTarget()));
                });
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


        filesystemListener.register(subject, FileType.DIRECTORY)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(event -> {
                    if (event.getKind() == FileSystemEvent.EventKind.CREATED)
                        subject.addLab(loadLab(event.getTarget().toFile()));
                    else subject.getLabs().removeIf(l -> l.getPath().equals(event.getTarget()));
                });
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
        filesystemListener.register(lab, FileType.DIRECTORY)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(event -> {
                    if (event.getKind() == FileSystemEvent.EventKind.CREATED)
                        lab.addStudent(loadStudent(event.getTarget().toFile()));
                    else lab.getStudents().removeIf(s -> s.getPath().equals(event.getTarget()));
                });
        return lab;
    }

    private Student loadStudent(File studentFile) {
        var assignmentFiles = studentFile.listFiles();
        if (assignmentFiles == null) return null;

        var students = Arrays.stream(assignmentFiles)
                .filter(File::isFile)
                .filter(file -> !file.toString().endsWith(".json"))
                .map(file -> new AssignmentFile(file.getName(), studentFile.toPath()))
                .collect(Collectors.toList());

        var student = new Student(studentFile.getName(), studentFile.getParentFile().toPath(), students);

        filesystemListener.register(student, FileType.FILE)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(event -> {
                    if (event.getKind() == FileSystemEvent.EventKind.CREATED) {
                        var assignmentFile = event.getTarget().toFile();
                        student.addAssigment(new AssignmentFile(assignmentFile.getName(), assignmentFile.getParentFile().toPath()));
                    } else
                        student.getAssignments().removeIf(a -> a.getPath().equals(event.getTarget()));
                });

        return student;
    }
}

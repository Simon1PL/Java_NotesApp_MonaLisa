package pl.edu.agh.monalisa.loader;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import pl.edu.agh.monalisa.model.*;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Singleton
public class Loader {
    private Root root;
    private final FilesystemWatcher filesystemListener;
    private final NoteLoader noteLoader;

    @Inject
    public Loader(FilesystemWatcher filesystemListener, NoteLoader noteLoader) {
        this.filesystemListener = filesystemListener;
        this.noteLoader = noteLoader;
    }

    public Root loadModel(Path rootPath) {
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
                    if (event.getKind() == FileSystemEvent.EventKind.CREATED) {
                        root.addYear(loadYear(event.getTarget().toFile()));
                    } else if (event.getKind() == FileSystemEvent.EventKind.DELETED) {
                        root.removeChild(event.getTarget());
                    }
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
                    if (event.getKind() == FileSystemEvent.EventKind.CREATED) {
                        year.addSubject(loadSubject(event.getTarget().toFile()));
                    } else if (event.getKind() == FileSystemEvent.EventKind.DELETED) {
                        year.getChildren().removeIf(s -> s.getPath().equals(event.getTarget()));
                    }
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
                    if (event.getKind() == FileSystemEvent.EventKind.CREATED) {
                        subject.addLab(loadLab(event.getTarget().toFile()));
                    } else if (event.getKind() == FileSystemEvent.EventKind.DELETED) {
                        subject.getChildren().removeIf(l -> l.getPath().equals(event.getTarget()));
                    }
                });
        return subject;
    }

    private Lab loadLab(File labFile) {
        var studentFiles = labFile.listFiles();
        if (studentFiles == null) return null;

        var lab = new Lab(labFile.getName(), labFile.getParentFile().toPath(), new ArrayList<>());

        Arrays.stream(studentFiles)
                .map(studentFile -> loadStudent(studentFile, lab))
                .filter(Objects::nonNull)
                .forEach(lab::addStudent);

        filesystemListener.register(lab, FileType.DIRECTORY)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(event -> {
                    if (event.getKind() == FileSystemEvent.EventKind.CREATED) {
                        lab.addStudent(loadStudent(event.getTarget().toFile(), lab));
                    } else if (event.getKind() == FileSystemEvent.EventKind.DELETED) {
                        lab.getChildren().removeIf(s -> s.getPath().equals(event.getTarget()));
                    }
                });
        return lab;
    }

    private Student loadStudent(File studentFile, Lab lab) {
        var assignmentFiles = studentFile.listFiles();
        if (assignmentFiles == null) return null;

        var student = new Student(studentFile.getName(), lab, new ArrayList<>());

        Arrays.stream(assignmentFiles)
                .filter(File::isFile)
                .filter(file -> !file.toString().endsWith(".note"))
                .map(file -> new AssignmentFile(file.getName(), student))
                .forEach(student::addAssigment);

        filesystemListener.register(student, FileType.FILE)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(event -> {
                    if (event.getKind() == FileSystemEvent.EventKind.CREATED) {
                        File assignmentFile = event.getTarget().toFile();
                        student.addAssigment(new AssignmentFile(assignmentFile.getName(), student));
                    } else if (event.getKind() == FileSystemEvent.EventKind.DELETED) {
                        student.removeChild(event.getTarget());
                    } else {
                        student.getAssignments().stream().filter(a -> a.getPath().equals(event.getTarget())).findFirst().ifPresent(file -> file.loadTextFromFile());
                    }
                });

        student.getAssignments().forEach(noteLoader::setupAssignmentFile);

        return student;
    }
}

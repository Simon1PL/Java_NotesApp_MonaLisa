package pl.edu.agh.monalisa.loader;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import pl.edu.agh.monalisa.model.Package;
import pl.edu.agh.monalisa.model.*;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
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

    private <T extends GenericFile> void registerPackageListener(Package<T> pkg, FileType childrenFileType, Function<File, T> loaderFunction) {
        filesystemListener.register(pkg, childrenFileType)
                .observeOn(JavaFxScheduler.platform())
                .subscribe(event -> {
                    if (event.getKind() == FileSystemEvent.EventKind.CREATED) {
                        pkg.addChild(loaderFunction.apply(event.getTarget().toFile()));
                    } else {
                        pkg.getChildren().removeIf(c -> c.getPath().equals(event.getTarget()));
                    }
                });
    }

    //TODO check
    @Inject
    public Root loadModel(@Named("RootPath") Path rootPath) {
        var files = rootPath.toFile().listFiles();
        if (files == null) throw new IllegalArgumentException("Root path must be a directory");

        var years = Arrays.stream(files)
                .map(this::loadYear)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        root = new Root(rootPath.getFileName().toString(), rootPath.getParent(), years);

        registerPackageListener(root, FileType.DIRECTORY, this::loadYear);
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

        registerPackageListener(year, FileType.DIRECTORY, this::loadSubject);

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

        registerPackageListener(subject, FileType.DIRECTORY, this::loadLab);
        return subject;
    }

    private Lab loadLab(File labFile) {
        var studentFiles = labFile.listFiles();
        if (studentFiles == null) return null;

        var lab = new Lab(labFile.getName(), labFile.getParentFile().toPath(), new ArrayList<>());

        Arrays.stream(studentFiles)
                .map(studentFile -> loadStudent(studentFile, lab))
                .filter(Objects::nonNull)
                .forEach(lab::addChild);

        registerPackageListener(lab, FileType.DIRECTORY, (file -> loadStudent(file, lab)));

        return lab;
    }

    private Student loadStudent(File studentFile, Lab lab) {
        var assignmentFiles = studentFile.listFiles();
        if (assignmentFiles == null) return null;

        var student = new Student(studentFile.getName(), lab, new ArrayList<>());

        Arrays.stream(assignmentFiles)
                .filter(File::isFile)
                .filter(file -> !file.toString().endsWith(Note.NOTE_EXTENSION))
                .map(file -> new AssignmentFile(file.getName(), student))
                .forEach(student::addChild);

        filesystemListener.register(student, FileType.FILE)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(event -> {
                    if (event.getKind() == FileSystemEvent.EventKind.CREATED) {
                        File assignmentFile = event.getTarget().toFile();
                        student.addChild(new AssignmentFile(assignmentFile.getName(), student));
                    } else if (event.getKind() == FileSystemEvent.EventKind.DELETED) {
                        student.getChildren().removeIf(a -> a.getPath().equals(event.getTarget()));
                    }
                });

        student.getChildren().forEach(noteLoader::setupAssignmentFile);

        return student;
    }
}

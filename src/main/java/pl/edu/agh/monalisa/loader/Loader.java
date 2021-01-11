package pl.edu.agh.monalisa.loader;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import pl.edu.agh.monalisa.model.Package;
import pl.edu.agh.monalisa.model.*;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Function;

@Singleton
public class Loader {
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

    public Root loadModel(Path rootPath) {
        if (!rootPath.toFile().isDirectory()) throw new IllegalArgumentException("Root path must be a directory");

        var root = new Root(rootPath.getFileName().toString(), rootPath.getParent());

        registerPackageListener(root, FileType.DIRECTORY, this::loadYear);
        return root;
    }

    private Year loadYear(File yearFile) {
        if (!yearFile.isDirectory()) return null;

        var year = new Year(yearFile.getName(), yearFile.getParentFile().toPath());

        registerPackageListener(year, FileType.DIRECTORY, this::loadSubject);

        return year;
    }

    private Subject loadSubject(File subjectFile) {
        if (!subjectFile.isDirectory()) return null;

        var subject = new Subject(subjectFile.getName(), subjectFile.getParentFile().toPath());

        registerPackageListener(subject, FileType.DIRECTORY, this::loadLab);
        return subject;
    }

    private Lab loadLab(File labFile) {
        if (!labFile.isDirectory()) return null;

        var lab = new Lab(labFile.getName(), labFile.getParentFile().toPath());

        registerPackageListener(lab, FileType.DIRECTORY, (file -> loadStudent(file, lab)));

        return lab;
    }

    private Student loadStudent(File studentFile, Lab lab) {
        if (!studentFile.isDirectory()) return null;

        var student = new Student(studentFile.getName(), lab);

        registerPackageListener(student, FileType.FILE, (file -> loadAssignmentFile(file, student)));

        return student;
    }

    private AssignmentFile loadAssignmentFile(File file, Student parentStudent) {
        var assignmentFile = new AssignmentFile(file.getName(), parentStudent);
        noteLoader.setupAssignmentFile(assignmentFile);
        return assignmentFile;
    }
}

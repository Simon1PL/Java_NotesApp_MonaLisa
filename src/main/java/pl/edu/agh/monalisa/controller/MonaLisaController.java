package pl.edu.agh.monalisa.controller;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import pl.edu.agh.monalisa.loader.FilesystemWatcher;
import pl.edu.agh.monalisa.model.*;
import pl.edu.agh.monalisa.loader.Loader;
import pl.edu.agh.monalisa.model.Package;

import java.nio.file.Path;

public class MonaLisaController {
    @Inject
    @Named("RootPath")
    private Path rootPath;

    private final Loader loader;
    private final FilesystemWatcher watcher;
    private Root model;
    private AssignmentFile selectedFile;
    private int notesAmount = 1; //Do wywalenia potem

    @FXML
    private TreeView<Package> fileTree;

    @FXML
    private TextArea fileView;

    @FXML
    private Button addNoteButton;

    @Inject
    public MonaLisaController(Loader loader, FilesystemWatcher watcher) {
        this.loader = loader;
        this.watcher = watcher;
    }

    @FXML
    public void initialize() {
        model = loader.loadModel(this.rootPath);
        fileTree.setRoot(new TreeItem<>(model));
        fileTree.setShowRoot(false);

        for (Year year : model.getYears()) {
            TreeItem<Package> yearItem = addTreeItem(fileTree.getRoot(), year);
            addListener(yearItem, year);
            for (Subject subject : year.getSubjects()) {
                var subjectItem = addTreeItem(yearItem, subject);
                addListener(subjectItem, subject);
                for (Lab lab : subject.getLabs()) {
                    var labItem = addTreeItem(subjectItem, lab);
                    addListener(labItem, lab);
                    for (Student student : lab.getStudents()) {
                        var studentItem = addTreeItem(labItem, student);
                        addListener(studentItem, student);

                        for (AssignmentFile assignmentFile : student.getAssignments())
                            addTreeItem(studentItem, assignmentFile);
                    }
                }
            }
        }

        fileTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getValue() instanceof AssignmentFile) {
                if (this.selectedFile != null)
                    watcher.closeAssignmentFile(this.selectedFile);
                this.selectedFile = (AssignmentFile) newValue.getValue();
                watcher.openAssignmentFile(this.selectedFile);
                this.fileView.textProperty().bind(this.selectedFile.contentProperty());
            }
        });

        addNoteButton.setOnAction((actionEvent) -> {
            if (this.selectedFile != null) this.selectedFile.addNote(new Note(this.notesAmount++, "title", "NOTE"));
        });
    }

    private void updateTree(TreeItem<Package> parent, ListChangeListener.Change<? extends Package> change) {
        while (change.next())
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(pkg -> {
                    var newTreeItem = addTreeItem(parent, pkg);
                    if (pkg.getChildren() != null)
                        addListener(newTreeItem, pkg);
                });
            } else if (change.wasRemoved()) {
                change.getRemoved().forEach(pkg -> {
                    parent.getChildren().removeIf(treeItem -> treeItem.getValue().equals(pkg));
                });
            }
    }

    private void addListener(TreeItem<Package> parent, Package pkg) {
        pkg.getChildren().addListener((ListChangeListener<Package>) c -> updateTree(parent, c));
    }

    private TreeItem<Package> addTreeItem(TreeItem<Package> parent, Package item) {
        var newItem = new TreeItem<>(item);
        parent.getChildren().add(newItem);
        return newItem;
    }
}

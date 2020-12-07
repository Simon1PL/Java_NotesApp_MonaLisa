package pl.edu.agh.monalisa.controller;

import com.google.inject.Inject;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import pl.edu.agh.monalisa.model.*;
import pl.edu.agh.monalisa.loader.Loader;
import pl.edu.agh.monalisa.model.Package;

import java.nio.file.Path;

public class MonaLisaController {
    private final Loader loader;
    private Root model;

    @FXML
    private TreeView<String> fileTree;

    private TreeItem<String> fileTreeRoot;

    @Inject
    public MonaLisaController(Loader loader) {
        this.loader = loader;
    }


    @FXML
    public void initialize() {
        model = loader.loadModel(Path.of("MonaLisa"));
        fileTreeRoot = new TreeItem<>();
        fileTree.setRoot(fileTreeRoot);
        fileTree.setShowRoot(false);

        for (Year year : model.getYears()) {
            var yearItem = addTreeItem(fileTreeRoot, year);
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
        model.getYears().addListener((ListChangeListener<Year>) c -> updateTree(null, c));


    }

    private void updateTree(TreeItem<String> parent, ListChangeListener.Change<? extends Package> change) {
        if (parent == null) parent = fileTreeRoot;
        while (change.next())
            if (change.wasAdded()) {
                TreeItem<String> finalParent = parent;
                change.getAddedSubList().forEach(pkg -> {
                    var newTreeItem = addTreeItem(finalParent, pkg);
                    if (pkg.getChildren() != null)
                        addListener(newTreeItem, pkg);
                });
            } else if (change.wasRemoved()) {
                TreeItem<String> finalParent1 = parent;
                change.getRemoved().forEach(pkg -> {
                    finalParent1.getChildren().removeIf(treeItem -> treeItem.getValue().equals(pkg.getName()));
                });
            }
    }

    private void addListener(TreeItem<String> parent, Package pkg) {
        pkg.getChildren().addListener((ListChangeListener<Package>) c -> updateTree(parent, c));
    }

    private TreeItem<String> addTreeItem(TreeItem<String> parent, Package item) {
        var newItem = new TreeItem<>(item.getName());
        parent.getChildren().add(newItem);
        return newItem;
    }

}

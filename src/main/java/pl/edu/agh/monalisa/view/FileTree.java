package pl.edu.agh.monalisa.view;

import javafx.collections.ListChangeListener;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import pl.edu.agh.monalisa.model.*;
import pl.edu.agh.monalisa.model.Package;

public class FileTree extends TreeView<GenericFile> {

    public void setModel(Root model) {
        setRoot(new TreeItem<>(model));
        setShowRoot(false);

        loadModel(model);
    }

    private void loadModel(Root root) {
        for (Year year : root.getChildren()) {
            loadYear(year, getRoot());
        }
    }

    private void loadYear(Year year, TreeItem<GenericFile> parent) {
        TreeItem<GenericFile> yearItem = loadPackage(year, parent);
        for (Subject subject : year.getChildren()) {
            loadSubject(subject, yearItem);
        }
    }

    private void loadSubject(Subject subject, TreeItem<GenericFile> parent) {
        var subjectItem = loadPackage(subject, parent);
        for (Lab lab : subject.getChildren()) {
            loadLab(lab, subjectItem);
        }
    }

    private void loadLab(Lab lab, TreeItem<GenericFile> parent) {
        var labItem = loadPackage(lab, parent);
        for (Student student : lab.getChildren()) {
            loadStudent(student, labItem);
        }
    }

    private void loadStudent(Student student, TreeItem<GenericFile> parent) {
        var studentItem = loadPackage(student, parent);
        for (AssignmentFile assignmentFile : student.getChildren())
            addTreeItem(studentItem, assignmentFile);
    }

    private TreeItem<GenericFile> loadPackage(Package<?> pkg, TreeItem<GenericFile> parent) {
        var treeItem = addTreeItem(parent, pkg);
        addListener(treeItem, pkg);
        return treeItem;
    }

    private <T extends GenericFile> void addListener(TreeItem<GenericFile> parent, Package<T> pkg) {
        pkg.getChildren().addListener((ListChangeListener<GenericFile>) c -> updateTree(parent, c));
    }

    private TreeItem<GenericFile> addTreeItem(TreeItem<GenericFile> parent, GenericFile item) {
        var newItem = new TreeItem<>(item);
        parent.getChildren().add(newItem);
        return newItem;
    }

    private void updateTree(TreeItem<GenericFile> parent, ListChangeListener.Change<? extends GenericFile> change) {
        while (change.next())
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(file -> {
                    var newTreeItem = addTreeItem(parent, file);
                    if (file instanceof Package) addListener(newTreeItem, (Package<?>) file);
                });
            } else if (change.wasRemoved()) {
                change.getRemoved().forEach(pkg -> {
                    parent.getChildren().removeIf(treeItem -> treeItem.getValue().equals(pkg));
                });
            }
    }
}

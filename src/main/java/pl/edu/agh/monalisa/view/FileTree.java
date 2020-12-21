package pl.edu.agh.monalisa.view;

import javafx.collections.ListChangeListener;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import pl.edu.agh.monalisa.model.*;
import pl.edu.agh.monalisa.model.Package;

public class FileTree extends TreeView<GenericFile> {

    public void setModel(Root model){
        setRoot(new TreeItem<>(model));
        setShowRoot(false);

        for (Year year : model.getChildren()) {
            TreeItem<GenericFile> yearItem = addTreeItem(getRoot(), year);
            addListener(yearItem, year);
            for (Subject subject : year.getChildren()) {
                var subjectItem = addTreeItem(yearItem, subject);
                addListener(subjectItem, subject);
                for (Lab lab : subject.getChildren()) {
                    var labItem = addTreeItem(subjectItem, lab);
                    addListener(labItem, lab);
                    for (Student student : lab.getChildren()) {
                        var studentItem = addTreeItem(labItem, student);
                        addListener(studentItem, student);

                        for (AssignmentFile assignmentFile : student.getChildren())
                            addTreeItem(studentItem, assignmentFile);
                    }
                }
            }

        }
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

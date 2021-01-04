package pl.edu.agh.monalisa.view;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.input.TransferMode;
import pl.edu.agh.monalisa.model.GenericFile;
import pl.edu.agh.monalisa.model.Package;
import pl.edu.agh.monalisa.model.Root;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class FileCellFactory {

    private final Root root;

    public FileCellFactory(Root root) {
        this.root = root;
    }

    TreeCell<GenericFile> createCell(TreeView<GenericFile> view) {
        var newCell = new TreeCell<GenericFile>() {
            @Override
            protected void updateItem(GenericFile item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setText(item.getName());
                } else {
                    setText("");
                }
            }
        };

        newCell.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles())
                event.acceptTransferModes(TransferMode.COPY);
        });
        newCell.setOnDragDropped(event -> {
            var treeItem = newCell.getTreeItem();
            var parent = treeItem == null ? null : treeItem.getValue();
            if (parent == null || parent instanceof Package) {
                handleFilesDropped(parent, event.getDragboard().getFiles());
            }
        });
        return newCell;
    }

    private void handleFilesDropped(GenericFile parent, List<File> files) {
        var parentPath = parent != null ? parent.getPath() : root.getPath();
        for (File file : files) {
            if (file.exists() && file.isFile()) {
                copyFile(file.toPath(), parentPath.resolve(file.getName()));
            } else if (file.exists() && file.isDirectory()) {
                copyDirectory(file.toPath(), parentPath);
            }
        }
    }

    private void copyFile(Path source, Path target) {
        if (source.toAbsolutePath().startsWith(target.toAbsolutePath())) return;
        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyDirectory(Path source, Path targetParent) {
        try {
            Files.walk(source).forEach(path -> {
                var targetPath = targetParent.resolve(source.getParent().relativize(path));
                copyFile(path, targetPath);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

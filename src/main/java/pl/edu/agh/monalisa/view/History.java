package pl.edu.agh.monalisa.view;

import javafx.scene.control.TreeItem;
import pl.edu.agh.monalisa.model.GenericFile;

import java.util.LinkedList;

public class History {
    private final LinkedList<TreeItem<GenericFile>> openFilesHistory = new LinkedList<>();
    private int currentIndex = -1;

    public TreeItem<GenericFile> undo() {
        return openFilesHistory.get(--currentIndex);
    }

    public TreeItem<GenericFile> redo() {
        return openFilesHistory.get(++currentIndex);
    }

    public boolean canRedo() {
        return currentIndex < openFilesHistory.size() - 1;
    }

    public boolean canUndo() {
        return currentIndex > 0;
    }

    public void addToHistory(TreeItem<GenericFile> chosenFile) {
        // don't add to history after undo/redo:
        if (currentIndex >= 0 && openFilesHistory.get(currentIndex) == chosenFile) return;
        if (openFilesHistory.contains(chosenFile) && openFilesHistory.indexOf(chosenFile) < currentIndex) {
            currentIndex--;
        }
        openFilesHistory.remove(chosenFile);
        openFilesHistory.add(++currentIndex, chosenFile);
    }
}

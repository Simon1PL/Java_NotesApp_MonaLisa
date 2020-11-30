package pl.edu.agh.monalisa.loader;

import java.nio.file.Path;
import java.nio.file.WatchKey;

public class FilesystemChangeAction {
    private final ItemChangeListener onCreate;
    private final ItemChangeListener onDelete;
    private final Path parent;
    private final FileType acceptedFileType;
    private final WatchKey key;

    public FilesystemChangeAction(
            ItemChangeListener onCreate,
            ItemChangeListener onDelete,
            Path parent,
            FileType acceptedFileType,
            WatchKey key) {
        this.onCreate = onCreate;
        this.onDelete = onDelete;
        this.parent = parent;
        this.acceptedFileType = acceptedFileType;
        this.key = key;
    }

    public ItemChangeListener getOnCreate() {
        return onCreate;
    }

    public ItemChangeListener getOnDelete() {
        return onDelete;
    }

    public Path getParent() {
        return parent;
    }

    public FileType getAcceptedFileType() {
        return acceptedFileType;
    }

    public WatchKey getKey() {
        return key;
    }
}

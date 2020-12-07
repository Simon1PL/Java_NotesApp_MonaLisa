package pl.edu.agh.monalisa.loader;

import java.nio.file.Path;

public class FileSystemEvent {
    private final Path target;
    private final EventKind kind;

    public FileSystemEvent(Path target, EventKind kind) {
        this.target = target;
        this.kind = kind;
    }

    public Path getTarget() {
        return target;
    }

    public EventKind getKind() {
        return kind;
    }

    public enum EventKind{
        CREATED,
        DELETED,
        MODIFIED
    }
}

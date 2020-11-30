package pl.edu.agh.monalisa.loader;

import javafx.application.Platform;
import pl.edu.agh.monalisa.model.Package;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;


public class FilesystemWatcher {
    private final Map<Path, FilesystemChangeAction> actions = new ConcurrentHashMap<>();
    private WatchService watcher;

    public FilesystemWatcher() {
        try {
            watcher = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            e.printStackTrace();
        }
        startListening();
    }

    public synchronized void register(Package pkg, ItemChangeListener onChildCreated, ItemChangeListener onChildDeleted, FileType fileType) {
        removeListener(pkg.getPath());

        try {
            WatchKey key = pkg.getPath().register(watcher, ENTRY_CREATE, ENTRY_DELETE);
            actions.put(
                    pkg.getPath(),
                    new FilesystemChangeAction(onChildCreated, onChildDeleted, pkg.getPath(), fileType, key)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startListening() {
        new Thread(() -> {
            while (true) {
                synchronized (this) {
                    actions.values().forEach(action -> {
                        for (WatchEvent<?> event : action.getKey().pollEvents()) {
                            if (event.kind() == ENTRY_CREATE)
                                handleItemCreated(action.getParent(), (Path) event.context());
                            else if (event.kind() == ENTRY_DELETE)
                                handleItemDeleted(action.getParent(), (Path) event.context());
                        }
                        action.getKey().reset();
                    });
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void handleItemCreated(Path parent, Path item) {
        var action = actions.get(parent);
        var childFile = parent.resolve(item).toFile();
        if (action != null && FileType.fromFile(childFile) == action.getAcceptedFileType()) {
            Platform.runLater(() -> action.getOnCreate().run(childFile));
        }
    }

    private void handleItemDeleted(Path parent, Path item) {

        var action = actions.get(parent);
        var childPath = parent.resolve(item);
        removeListener(childPath);
        if (action != null) {
            Platform.runLater(() -> action.getOnDelete().run(childPath.toFile()));
        }
    }

    private void removeListener(Path path) {
        if (actions.containsKey(path)) {
            actions.get(path).getKey().cancel();
            actions.remove(path);
        }
    }
}

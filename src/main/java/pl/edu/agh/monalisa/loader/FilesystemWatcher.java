package pl.edu.agh.monalisa.loader;

import io.reactivex.rxjava3.core.Observable;
import pl.edu.agh.monalisa.model.Package;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;


public class FilesystemWatcher {

    public Observable<FileSystemEvent> register(Package pkg, FileType fileType) {
        return Observable.create(subscriber -> {
            try (WatchService watcher = pkg.getPath().getFileSystem().newWatchService()) {
                pkg.getPath().register(watcher, ENTRY_CREATE, ENTRY_DELETE);

                while (!subscriber.isDisposed()) {
                    WatchKey key = watcher.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        var targetPath = pkg.getPath().resolve((Path) event.context());
                        if (event.kind() == ENTRY_CREATE && fileType == FileType.fromFile(targetPath.toFile()))
                            subscriber.onNext(new FileSystemEvent(targetPath, FileSystemEvent.EventKind.CREATED));
                        else if(event.kind() == ENTRY_DELETE)
                            subscriber.onNext(new FileSystemEvent(targetPath, FileSystemEvent.EventKind.DELETED));
                    }
                    if (!key.reset()) break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}

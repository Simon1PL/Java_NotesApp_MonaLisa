package pl.edu.agh.monalisa.loader;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import pl.edu.agh.monalisa.model.AssignmentFile;
import pl.edu.agh.monalisa.model.Package;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;


public class FilesystemWatcher {

    public Observable<FileSystemEvent> register(Package pkg, FileType fileType) {
        return Observable.create(subscriber -> {
            try (WatchService watcher = pkg.getPath().getFileSystem().newWatchService()) {
                pkg.getPath().register(watcher, ENTRY_CREATE, ENTRY_DELETE);

                while (!subscriber.isDisposed()) {
                    WatchKey key = watcher.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        var targetPath = pkg.getPath().resolve((Path) event.context());
                        if (targetPath.toString().endsWith(".note")) continue;
                        if (event.kind() == ENTRY_CREATE && fileType == FileType.fromFile(targetPath.toFile()))
                            subscriber.onNext(new FileSystemEvent(targetPath, FileSystemEvent.EventKind.CREATED));
                        else if (event.kind() == ENTRY_DELETE)
                            subscriber.onNext(new FileSystemEvent(targetPath, FileSystemEvent.EventKind.DELETED));
                    }
                    if (!key.reset()) break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public Observable<String> openAssignmentFile(AssignmentFile file) {
        Path parent = file.getPath().getParent();
        Observable<String> observable = Observable.create(subscriber -> {
            try (WatchService watcher = parent.getFileSystem().newWatchService()) {
                parent.register(watcher, ENTRY_MODIFY);
                subscriber.onNext(Files.readString(file.getPath()));

                while (!subscriber.isDisposed()) {
                    WatchKey key = watcher.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        var targetPath = parent.resolve((Path) event.context());
                        if (event.kind() == ENTRY_MODIFY && targetPath.equals(file.getPath()))
                            subscriber.onNext(Files.readString(file.getPath()));
                    }
                    if (!key.reset()) break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException ignored) {
            }
        });
        return observable.subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform());
    }

    public void closeAssignmentFile(AssignmentFile file) {
        if (file.getFileContentListener() != null)
            file.getFileContentListener().dispose();
        //TODO delete watcher
    }

}

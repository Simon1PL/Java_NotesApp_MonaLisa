package pl.edu.agh.monalisa.model;

import java.io.IOException;
import java.nio.file.*;
import java.util.LinkedList;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.*;

public class FileSystemChangeListener {
    private volatile static List<WatchKey> keys = new LinkedList<>();
    private static WatchService watcher;

    static {
        try {
            watcher = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void registerPath(Path path) {
        try {
            keys.add(path.register(watcher,
                    ENTRY_MODIFY));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startListening() {
        new Thread(() -> {
            while (true) {
                if (keys.size() > 0) {
                    keys.forEach(key -> {
                        for (WatchEvent<?> event : key.pollEvents()) {
                            System.out.println(event.context());
                        }
                        key.reset();
                    });
                }
            }
        }).start();
    }
}

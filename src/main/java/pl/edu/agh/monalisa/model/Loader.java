package pl.edu.agh.monalisa.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Loader {
    public static void loadData(String rootPath, Package parentPackage) {
        try {
            Files.walk(Paths.get(rootPath), 1)
                    .forEach(path -> {
                        if (parentPackage == null) {
                            Year loadedYear = new Year(path.getFileName().toString(), path.getParent().toString());
                            loadData(path.toString(), loadedYear);
                        }
                        else if (parentPackage  instanceof Year) {
                            Subject loadedSubject = new Subject(path.getFileName().toString(), (Year)parentPackage);
                            loadData(path.toString(), loadedSubject);
                        }
                        // itd... przy Student tworzymy nowego tylko jesli taki z taką "nazwa" nie istnieje
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

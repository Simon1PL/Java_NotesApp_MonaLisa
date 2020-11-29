package pl.edu.agh.monalisa;

import pl.edu.agh.monalisa.model.*;

import java.io.File;
import java.io.IOException;
import java.lang.Package;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MonaLisaApplication {
    public static void walk(String rootPath) throws IOException {
        Files.walk(Paths.get(rootPath))
                .forEach(path -> visit(path.toString()));
    }
    public static void visit(String filePath) {
        System.out.println(filePath);
    }

    public static void walkToDo(String rootPath) throws IOException {
        Files.walk(Paths.get(rootPath))
                .forEach(path -> {
                    // if (path.depth ==) jesli głębokość == 1 to tworzymy rok, jesli 2 to przedmiot itd...
                        new Year(path.getFileName().toString(), path.getParent().toString());
                });
    }

    public static void createExampleData(File mainAppFile) {
        Year year1 = new Year("2018", mainAppFile.getPath());
        year1.create();
        Subject subject1 = new Subject("WDI", year1);
        subject1.create();
        Lab lab1 = new Lab("Lab1", subject1);
        lab1.create();
        Student student1 = new Student("Student1", lab1);
        student1.create();
        Assigment assigment1 = new Assigment("zad1", student1);
        assigment1.create();
        AssigmentFile assigmentFile1 = new AssigmentFile("main.py", assigment1);
        assigmentFile1.create();
    }

    public static void main(String[] args) {
        File mainAppFile = new File(System.getProperty("user.dir") + "\\MonaLisa");
        mainAppFile.mkdir();
        createExampleData(mainAppFile);

        try {
            walk(mainAppFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

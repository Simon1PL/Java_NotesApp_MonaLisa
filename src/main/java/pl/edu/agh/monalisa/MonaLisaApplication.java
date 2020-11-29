package pl.edu.agh.monalisa;

import pl.edu.agh.monalisa.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MonaLisaApplication {
    public static void printFilesStructure(String rootPath) {
        try {
            Files.walk(Paths.get(rootPath))
                    .forEach(path -> System.out.println(path.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createExampleData(String mainAppFilePath) {
        File mainAppFile = new File(mainAppFilePath);
        mainAppFile.mkdir();
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
        String mainAppFilePath = System.getProperty("user.dir") + "\\MonaLisa";
        createExampleData(mainAppFilePath);
        Loader.loadData(mainAppFilePath, null);
        printFilesStructure(mainAppFilePath);
    }
}

package pl.edu.agh.monalisa.model;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class PackageTest {
    private static final String packageName = "MonaLisaTest";
    private static final Package newPackage = new Year(packageName, Path.of(System.getProperty("user.dir"))) { };

    @Test
    public void getName() {
        assertEquals(newPackage.getName(), packageName);
    }

    @Test
    public void getPath() {
        assertEquals(newPackage.getPath(), Path.of(System.getProperty("user.dir")).resolve(packageName));
    }

    @Test
    public void create() {
        newPackage.create();
        File file = newPackage.getPath().toFile();
        assertTrue(file.exists() && file.isDirectory());
    }

//    @Test
//    public void delete() {
//        newPackage.delete();
//        assertFalse(newPackage.getPath().toFile().exists());
//    }

    @AfterAll
    public static void afterAll() {
        newPackage.getPath().toFile().delete();
    }
}
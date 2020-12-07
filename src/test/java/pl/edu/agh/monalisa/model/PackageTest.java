package pl.edu.agh.monalisa.model;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class PackageTest {
    private static final String packageName = "MonaLisaTest";
    private static final Package newPackage = new Package(packageName, Path.of(System.getProperty("user.dir"))) { };

    @BeforeClass
    public static void beforeAll() {
    }

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

    @Test
    public void delete() {
        newPackage.delete();
        assertFalse(newPackage.getPath().toFile().exists());
    }

    @AfterClass
    public static void afterAll() {
        newPackage.getPath().toFile().delete();
    }
}
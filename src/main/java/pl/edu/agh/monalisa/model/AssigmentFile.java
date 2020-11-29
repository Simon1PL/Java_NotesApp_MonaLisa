package pl.edu.agh.monalisa.model;

import pl.edu.agh.monalisa.constants.AvailableExtensionsEnum;
import pl.edu.agh.monalisa.constants.AvailableExtensionsEnum.AvailableExtensions;

import java.util.Collection;
import java.util.HashSet;

public class AssigmentFile {
    private String name;
    private AvailableExtensions extension;
    private Collection<Note> notes = new HashSet<>();

    public AssigmentFile(String name) {
        this.name = name;
        this.extension = AvailableExtensionsEnum.getExtension(name);
    }
}

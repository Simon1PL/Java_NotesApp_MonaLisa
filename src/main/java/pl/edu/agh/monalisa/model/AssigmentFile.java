package pl.edu.agh.monalisa.model;

import pl.edu.agh.monalisa.constants.AvailableExtensionsEnum;
import pl.edu.agh.monalisa.constants.AvailableExtensionsEnum.AvailableExtensions;

import java.util.Collection;
import java.util.HashSet;

public class AssigmentFile extends File {
    private String name;
    private AvailableExtensions extension;
    private Collection<Note> notes = new HashSet<>();

    public AssigmentFile(String name, Assigment assigment) {
        super(name, assigment);
        this.extension = AvailableExtensionsEnum.getExtension(name);
        assigment.addFile(this);
    }
}

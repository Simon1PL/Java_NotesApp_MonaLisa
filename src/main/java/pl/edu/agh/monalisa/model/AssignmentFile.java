package pl.edu.agh.monalisa.model;

import javafx.collections.ObservableList;
import pl.edu.agh.monalisa.constants.AvailableExtensionsEnum;
import pl.edu.agh.monalisa.constants.AvailableExtensionsEnum.AvailableExtensions;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;

public class AssignmentFile extends GenericFile {
    private AvailableExtensions extension;
    private Collection<Note> notes = new HashSet<>();

    public AssignmentFile(String name, Path parent) {
        super(name, parent);
        this.extension = AvailableExtensionsEnum.getExtension(name);
    }

    @Override
    public ObservableList<? extends Package> getChildren() {
        return null;
    }
}

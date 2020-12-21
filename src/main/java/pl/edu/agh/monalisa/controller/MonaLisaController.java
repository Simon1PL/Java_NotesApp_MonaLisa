package pl.edu.agh.monalisa.controller;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import pl.edu.agh.monalisa.loader.FilesystemWatcher;
import pl.edu.agh.monalisa.loader.Loader;
import pl.edu.agh.monalisa.model.Package;
import pl.edu.agh.monalisa.model.*;
import pl.edu.agh.monalisa.view.GenericSyntaxHighlighter;
import pl.edu.agh.monalisa.view.StudentCell;
import pl.edu.agh.monalisa.view.VisibleParagraphStyler;

import java.nio.file.Path;

public class MonaLisaController {
    @Inject
    @Named("RootPath")
    private Path rootPath;

    private final Loader loader;
    private final FilesystemWatcher watcher;
    private Root model;
    private AssignmentFile selectedFile;
    private int notesAmount = 1; //Do wywalenia potem

    @FXML
    private TreeView<GenericFile> fileTree;

    @FXML
    private CodeArea fileView;

    @FXML
    private Button addNoteButton;

    @FXML
    private HBox container;

    @FXML
    private TextArea noteView;

    @FXML
    private ListView<Student> studentListView;

    @Inject
    public MonaLisaController(Loader loader, FilesystemWatcher watcher) {
        this.loader = loader;
        this.watcher = watcher;
    }

    @FXML
    public void initialize() {
        model = loader.loadModel(this.rootPath);
        fileTree.setRoot(new TreeItem<>(model));
        fileTree.setShowRoot(false);

        for (Year year : model.getChildren()) {
            TreeItem<GenericFile> yearItem = addTreeItem(fileTree.getRoot(), year);
            addListener(yearItem, year);
            for (Subject subject : year.getChildren()) {
                var subjectItem = addTreeItem(yearItem, subject);
                addListener(subjectItem, subject);
                for (Lab lab : subject.getChildren()) {
                    var labItem = addTreeItem(subjectItem, lab);
                    addListener(labItem, lab);
                    for (Student student : lab.getChildren()) {
                        var studentItem = addTreeItem(labItem, student);
                        addListener(studentItem, student);

                        for (AssignmentFile assignmentFile : student.getChildren())
                            addTreeItem(studentItem, assignmentFile);
                    }
                }
            }

        }
        fileView.setParagraphGraphicFactory(LineNumberFactory.get(fileView));
        fileView.getVisibleParagraphs().addModificationObserver(
                new VisibleParagraphStyler<>(fileView, new GenericSyntaxHighlighter()));
        fileView.getStylesheets().add(MonaLisaController.class.getResource("syntaxHighlighting.css").toExternalForm());


        fileTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getValue() instanceof AssignmentFile) {
                if (this.selectedFile != null) {
                    watcher.closeAssignmentFile(this.selectedFile);
                    this.selectedFile.noteProperty().unbindBidirectional(noteView.textProperty());
                    noteView.clear();
                }
                this.selectedFile = (AssignmentFile) newValue.getValue();
                var disposable = watcher.openAssignmentFile(this.selectedFile)
                        .observeOn(JavaFxScheduler.platform())
                        .subscribe(this.fileView::replaceText);
                this.selectedFile.setFileContentListener(disposable);

                noteView.setText(this.selectedFile.noteProperty().getValue());
                this.selectedFile.noteProperty().bindBidirectional(noteView.textProperty());

                studentListView.setItems(this.selectedFile.getParent().getParent().getChildren());
            }
        });

//        addNoteButton.setOnAction((actionEvent) -> {
//            if (this.selectedFile != null) this.selectedFile.addNote(new Note(this.notesAmount++, "title", "NOTE"));
//        });

        studentListView.setCellFactory(param -> new StudentCell());
    }

    private void updateTree(TreeItem<GenericFile> parent, ListChangeListener.Change<? extends GenericFile> change) {
        while (change.next())
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(file -> {
                    var newTreeItem = addTreeItem(parent, file);
                    if (file instanceof Package) addListener(newTreeItem, (Package) file);
                });
            } else if (change.wasRemoved()) {
                change.getRemoved().forEach(pkg -> {
                    parent.getChildren().removeIf(treeItem -> treeItem.getValue().equals(pkg));
                });
            }
    }

    private void addListener(TreeItem<GenericFile> parent, Package pkg) {
        pkg.getChildren().addListener((ListChangeListener<GenericFile>) c -> updateTree(parent, c));
    }

    private TreeItem<GenericFile> addTreeItem(TreeItem<GenericFile> parent, GenericFile item) {
        var newItem = new TreeItem<>(item);
        parent.getChildren().add(newItem);
        return newItem;
    }


}

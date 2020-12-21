package pl.edu.agh.monalisa.controller;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import pl.edu.agh.monalisa.loader.FilesystemWatcher;
import pl.edu.agh.monalisa.loader.Loader;
import pl.edu.agh.monalisa.model.AssignmentFile;
import pl.edu.agh.monalisa.model.Root;
import pl.edu.agh.monalisa.model.Student;
import pl.edu.agh.monalisa.view.FileTree;
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

    @FXML
    private FileTree fileTree;

    @FXML
    private CodeArea fileView;

    @FXML
    private Button addNoteButton;

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
        initializeFileTree();


        initializeFileTreeSelectionListener();
        initializeCodeView();
        initializeStudentView();

//        addNoteButton.setOnAction((actionEvent) -> {
//            if (this.selectedFile != null) this.selectedFile.addNote(new Note(this.notesAmount++, "title", "NOTE"));
//        });

    }

    private void initializeFileTree() {
        model = loader.loadModel(this.rootPath);
        fileTree.setModel(model);
    }

    private void initializeFileTreeSelectionListener() {
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
    }

    private void initializeCodeView() {
        fileView.setParagraphGraphicFactory(LineNumberFactory.get(fileView));
        fileView.getVisibleParagraphs().addModificationObserver(
                new VisibleParagraphStyler<>(fileView, new GenericSyntaxHighlighter()));
        fileView.getStylesheets().add(MonaLisaController.class.getResource("syntaxHighlighting.css").toExternalForm());

    }

    private void initializeStudentView(){
        studentListView.setCellFactory(param -> new StudentCell());
    }

}

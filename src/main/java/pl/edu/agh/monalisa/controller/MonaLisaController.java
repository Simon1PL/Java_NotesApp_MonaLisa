package pl.edu.agh.monalisa.controller;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import pl.edu.agh.monalisa.loader.FilesystemWatcher;
import pl.edu.agh.monalisa.loader.Loader;
import pl.edu.agh.monalisa.model.AssignmentFile;
import pl.edu.agh.monalisa.model.Root;
import pl.edu.agh.monalisa.model.Student;
import pl.edu.agh.monalisa.view.*;

import java.nio.file.Path;

public class MonaLisaController {
    @Inject
    @Named("RootPath")
    private Path rootPath;

    private final Loader loader;
    private final FilesystemWatcher watcher;
    private Root model;
    private AssignmentFile selectedFile;
    private final History history;

    @FXML
    private FileTree fileTree;

    @FXML
    private CodeArea fileView;

    @FXML
    private Button undoButton;

    @FXML
    private Button redoButton;

    @FXML
    private TextArea noteView;

    @FXML
    private ListView<Student> studentListView;

    @FXML
    private Label studentListLabel;

    @FXML
    private NoteList noteListView;

    @FXML
    private Label noteListLabel;


    @Inject
    public MonaLisaController(Loader loader, FilesystemWatcher watcher) {
        this.loader = loader;
        this.watcher = watcher;
        this.history = new History();
    }

    @FXML
    public void initialize() {
        initializeFileTree();

        initializeFileTreeSelectionListener();
        initializeCodeView();
        initializeStudentView();
        initializeNoteList();
        initializeControls();
    }

    private void initializeFileTree() {
        model = loader.loadModel(this.rootPath);
        fileTree.setModel(model);
    }

    private void initializeFileTreeSelectionListener() {
        fileTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getValue() instanceof AssignmentFile) {
                history.add(newValue.getValue());
                var assignmentFile = (AssignmentFile) newValue.getValue();
                changeSelectedFile(assignmentFile);
            }
        });
    }

    private void initializeCodeView() {
        fileView.setParagraphGraphicFactory(LineNumberFactory.get(fileView));
        fileView.getVisibleParagraphs().addModificationObserver(
                new VisibleParagraphStyler<>(fileView, new GenericSyntaxHighlighter()));
        fileView.getStylesheets().add(MonaLisaController.class.getResource("syntaxHighlighting.css").toExternalForm());

    }

    private void initializeStudentView() {
        studentListView.setCellFactory(param -> new StudentCell());
    }

    private void changeSelectedFile(AssignmentFile newSelectedFile) {
        if (this.selectedFile != null) {
            watcher.closeAssignmentFile(this.selectedFile);
            this.selectedFile.noteProperty().unbindBidirectional(noteView.textProperty());
            noteView.clear();
        }
        this.selectedFile = newSelectedFile;
        var disposable = watcher.openAssignmentFile(this.selectedFile)
                .observeOn(JavaFxScheduler.platform())
                .subscribe(this.fileView::replaceText);
        this.selectedFile.setFileContentListener(disposable);

        noteView.setText(this.selectedFile.noteProperty().getValue());
        this.selectedFile.noteProperty().bindBidirectional(noteView.textProperty());

        studentListView.setItems(this.selectedFile.getParent().getParent().getChildren());
        updateNoteList();
        updatePathLabels(newSelectedFile);
    }

    private void initializeControls() {
        undoButton.setOnAction((actionEvent) -> handleUndo());
        undoButton.disableProperty().bind(history.isUndoDisabled());

        redoButton.setOnAction((actionEvent) -> handleRedo());
        redoButton.disableProperty().bind(history.isRedoDisabled());
    }

    @FXML
    private void handleOnKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT && event.isAltDown()) {
            handleUndo();
        }
        if (event.getCode() == KeyCode.RIGHT && event.isAltDown()) {
            handleRedo();
        }
    }

    private void handleUndo() {
        if (!history.isUndoDisabled().getValue()) {
            fileTree.getSelectionModel().select(FileTree.getTreeViewItem(fileTree.getRoot(), this.history.undo()));
        }
    }

    private void handleRedo() {
        if (!history.isRedoDisabled().getValue()) {
            fileTree.getSelectionModel().select(FileTree.getTreeViewItem(fileTree.getRoot(), this.history.redo()));
        }
    }
    private void updatePathLabels(AssignmentFile file) {
        StringBuilder stringBuilder = new StringBuilder();
        var path = model.getPath()
                .relativize(file.getPath())
                .getParent()//student
                .getParent();//lab

        var iterator = path.iterator();
        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next().toString());
            if (iterator.hasNext()) stringBuilder.append(" > ");
        }
        studentListLabel.setText(stringBuilder.toString());
        noteListLabel.setText(stringBuilder.toString());
    }

    private void updateNoteList() {
        noteListView.setSelectedFile(this.selectedFile);
    }

    private void initializeNoteList() {
        noteListView.setOnShowClicked(newSelectedFile -> {
            changeSelectedFile(newSelectedFile);
            fileTree.getSelectionModel().select(FileTree.getTreeViewItem(fileTree.getRoot(), newSelectedFile));
        });
    }

}

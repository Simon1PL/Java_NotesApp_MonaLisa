package pl.edu.agh.monalisa.controller;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import pl.edu.agh.monalisa.model.Loader;

public class MonaLisaController {
    private Loader loader;

    @Inject
    public MonaLisaController(Loader loader) {
        this.loader = loader;
    }


    @FXML
    public void initialize(){
        System.out.println("qwe");
    }

}

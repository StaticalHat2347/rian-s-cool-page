package com.comp301.a08dungeon.view;

import com.comp301.a08dungeon.controller.Controller;
import com.comp301.a08dungeon.controller.ControllerImpl;
import com.comp301.a08dungeon.model.Model;
import com.comp301.a08dungeon.model.ModelImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AppLauncher extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Celestial Denizen");
        Model model = new ModelImpl(26, 10);
        Controller controller = new ControllerImpl(model);

        Scene scene = new Scene(new StackPane(), 854, 480);
        View view = new View(controller, model, scene);

        scene.setRoot(view.render());
        scene.setOnKeyPressed(view::keyPressed);
        scene.getStylesheets().add("dungeon.css");
        model.addObserver(view);

        stage.setScene(scene);
        stage.show();
        scene.getRoot().requestFocus();
    }
}

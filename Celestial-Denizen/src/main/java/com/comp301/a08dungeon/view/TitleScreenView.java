package com.comp301.a08dungeon.view;

import com.comp301.a08dungeon.controller.Controller;
import com.comp301.a08dungeon.model.Model;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class TitleScreenView implements FXComponent{
    private final Controller controller;
    private final Model model;

    public TitleScreenView(Controller controller, Model model) {
        this.controller = controller;
        this.model = model;
    }

    @Override
    public Parent render() {
        Label titleLabel     = new Label("CELESTIAL DENIZEN");
        titleLabel.getStyleClass().add("title-label");

        Label highScoreLabel = new Label("HIGH SCORE: " + model.getHighScore());
        highScoreLabel.getStyleClass().add("score-label");

        Label lastScoreLabel = new Label("LAST SCORE: " + model.getCurScore());
        lastScoreLabel.getStyleClass().add("score-label");

        Button startButton   = new Button("START GAME");
        startButton.getStyleClass().add("start-button");
        startButton.setOnAction(e -> controller.startGame());

        Label byLine = new Label("CREATED BY: Rian");
        byLine.getStyleClass().add("byline-label");

        VBox vbox = new VBox(20,
                titleLabel,
                highScoreLabel,
                lastScoreLabel,
                startButton,
                byLine
        );
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(40));

        BorderPane root = new BorderPane(vbox);
        root.setPrefSize(854, 480);
        root.getStyleClass().add("title-screen");
        return root;
    }
}

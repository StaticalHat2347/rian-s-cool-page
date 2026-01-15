package com.comp301.a08dungeon.view;

import com.comp301.a08dungeon.controller.Controller;
import com.comp301.a08dungeon.model.Model;
import com.comp301.a08dungeon.model.board.Posn;
import com.comp301.a08dungeon.model.pieces.Piece;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


import java.util.HashMap;
import java.util.Map;

public class GameView implements FXComponent {
    private static final int TILE_SIZE = 32;
    private static final Map<String, Image> IMAGE_CACHE = new HashMap<>();
    private final Controller controller;
    private final Model model;

    public GameView(Controller controller, Model model) {
        this.controller = controller;
        this.model = model;
    }

    private static Image getImage(String path) {
        return IMAGE_CACHE.computeIfAbsent(path, p ->
                new Image(GameView.class.getResourceAsStream(p),
                        TILE_SIZE, TILE_SIZE, true, false));
    }

    @Override
    public Parent render() {
        int rows = model.getHeight(), cols = model.getWidth();

        GridPane boardGrid = new GridPane();
        boardGrid.setHgap(0);
        boardGrid.setVgap(0);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Piece piece = model.get(new Posn(r, c));
                String path = piece == null ? "/images/floor.png" : piece.getResourcePath();
                ImageView iv = new ImageView(getImage(path));
                boardGrid.add(iv, c, r);
            }
        }

        StackPane playfield = new StackPane(boardGrid);
        playfield.setPrefSize(cols * TILE_SIZE, rows * TILE_SIZE);
        playfield.setMaxSize(cols * TILE_SIZE, rows * TILE_SIZE);
        BorderPane.setAlignment(playfield, Pos.CENTER);

        Label scoreLabel     = new Label("Score: " + model.getCurScore());
        scoreLabel.setTextFill(Color.WHITE);
        Label highScoreLabel = new Label("High Score: " + model.getHighScore());
        highScoreLabel.setTextFill(Color.WHITE);
        VBox scoreBox = new VBox(5, scoreLabel, highScoreLabel);
        scoreBox.setAlignment(Pos.CENTER);

        Button up    = new Button("↑"); up.setOnAction(e -> controller.moveUp());
        Button down  = new Button("↓"); down.setOnAction(e -> controller.moveDown());
        Button left  = new Button("←"); left.setOnAction(e -> controller.moveLeft());
        Button right = new Button("→"); right.setOnAction(e -> controller.moveRight());


        GridPane controlGrid = new GridPane();
        controlGrid.setHgap(5);
        controlGrid.setVgap(5);
        controlGrid.add(up,    1, 0);
        controlGrid.add(left,  0, 1);
        controlGrid.add(down,  1, 2);
        controlGrid.add(right, 2, 1);
        controlGrid.setAlignment(Pos.CENTER);

        VBox bottomBox = new VBox(10, scoreBox, controlGrid);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: black;");    // black everywhere
        root.setPrefSize(854, 480);                       // lock to your window size
        root.setCenter(playfield);
        root.setBottom(bottomBox);
        root.setFocusTraversable(true);
        return root;
    }
    // USING KEYS IS BETTERRRRRR
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case UP -> controller.moveUp();
            case DOWN -> controller.moveDown();
            case LEFT -> controller.moveLeft();
            case RIGHT -> controller.moveRight();
            default -> {}

        }
    }
}

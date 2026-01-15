package com.comp301.a08dungeon.view;

import com.comp301.a08dungeon.controller.Controller;
import com.comp301.a08dungeon.model.Model;
import com.comp301.a08dungeon.model.Observer;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

public class View implements FXComponent, Observer {
  private final Controller controller;
  private final Model model;
  private final Scene scene;

  public View(Controller controller, Model model, Scene scene) {
    this.controller = controller;
    this.model      = model;
    this.scene      = scene;
  }

  @Override
  public Parent render() {
    return model.getStatus() == Model.STATUS.IN_PROGRESS
            ? new GameView(controller, model).render()
            : new TitleScreenView(controller, model).render();
  }

  @Override
  public void update() {
    Parent newRoot = render();
    scene.setRoot(newRoot);
    newRoot.requestFocus();
  }

  public void keyPressed(KeyEvent e) {
    switch (e.getCode()) {
      case UP    -> controller.moveUp();
      case DOWN  -> controller.moveDown();
      case LEFT  -> controller.moveLeft();
      case RIGHT -> controller.moveRight();
      default    -> {}
    }
  }

}

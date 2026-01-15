package com.comp301.a08dungeon.model;

import com.comp301.a08dungeon.model.board.Board;
import com.comp301.a08dungeon.model.board.BoardImpl;
import com.comp301.a08dungeon.model.board.Posn;
import com.comp301.a08dungeon.model.pieces.CollisionResult;
import com.comp301.a08dungeon.model.pieces.Piece;
import java.util.ArrayList;
import java.util.List;

public class ModelImpl implements Model {
  private final Board board;
  private int curScore;
  private int highScore;
  private int level;
  private STATUS status;
  private final List<Observer> observers = new ArrayList<>();

  public ModelImpl(int width, int height) {
    this.board = new BoardImpl(width, height);
    this.curScore = 0;
    this.highScore = 0;
    this.level = 0;
    this.status = STATUS.END_GAME;
  }

  public ModelImpl(Board board) {
    this.board = board;
    this.curScore = 0;
    this.highScore = 0;
    this.level = 0;
    this.status = STATUS.END_GAME;
  }

  @Override
  public int getWidth() {
    return board.getWidth();
  }

  @Override
  public int getHeight() {
    return board.getHeight();
  }

  @Override
  public Piece get(Posn p) {
    return board.get(p);
  }

  @Override
  public int getCurScore() {
    return curScore;
  }

  @Override
  public int getHighScore() {
    return highScore;
  }

  @Override
  public int getLevel() {
    return level;
  }

  @Override
  public STATUS getStatus() {
    return status;
  }

  @Override
  public void addObserver(Observer o) {
    observers.add(o);
  }

  private void notifyObservers() {
    for (Observer o : observers) {
      o.update();
    }
  }

  @Override
  public void startGame() {
    status = STATUS.IN_PROGRESS;
    curScore = 0;
    level = 1;
    board.init(level + 1, 5, 12);
    notifyObservers();
  }

  @Override
  public void endGame() {
    status = STATUS.END_GAME;
    if (curScore > highScore) {
      highScore = curScore;
    }
    notifyObservers();
  }

  private void move(CollisionResult result) {
    curScore += result.getPoints();
    switch (result.getResults()) {
      case NEXT_LEVEL:
        level++;
        board.init(level + 1, 5, 12);
        break;
      case GAME_OVER:
        endGame();
        return;
      default:
    }
    notifyObservers();
  }

  @Override
  public void moveUp() {
    move(board.moveHero(-1, 0));
  }

  @Override
  public void moveDown() {
    move(board.moveHero(1, 0));
  }

  @Override
  public void moveLeft() {
    move(board.moveHero(0, -1));
  }

  @Override
  public void moveRight() {
    move(board.moveHero(0, 1));
  }
}

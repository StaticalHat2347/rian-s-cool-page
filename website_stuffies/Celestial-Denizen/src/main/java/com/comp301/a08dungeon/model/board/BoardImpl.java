package com.comp301.a08dungeon.model.board;

import com.comp301.a08dungeon.model.pieces.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BoardImpl implements Board {
  private final Piece[][] board;
  private Posn heroPosn;
  private List<Enemy> enemies;
  private final Random random = new Random();

  public BoardImpl(int width, int height) {
    this.board = new Piece[height][width];
    this.enemies = new ArrayList<>();
  }

  public BoardImpl(Piece[][] input) {
    this.board = input;
    this.enemies = new ArrayList<>();
    for (int r = 0; r < getHeight(); r++) {
      for (int c = 0; c < getWidth(); c++) {
        Piece p = board[r][c];
        if (p instanceof Hero) {
          heroPosn = new Posn(r, c);
        } else if (p instanceof Enemy) {
          enemies.add((Enemy) p);
        }
      }
    }
  }

  @Override
  public void init(int enemiesCount, int treasures, int walls) {
    for (int r = 0; r < getHeight(); r++) {
      for (int c = 0; c < getWidth(); c++) {
        board[r][c] = null;
      }
    }
    enemies.clear();
    int slots = getHeight() * getWidth();
    if (enemiesCount + treasures + walls + 2 > slots) {
      throw new IllegalArgumentException();
    }
    while (true) {
      int r = random.nextInt(getHeight()), c = random.nextInt(getWidth());
      if (board[r][c] == null) {
        Hero h = new Hero();
        Posn pos = new Posn(r, c);
        set(h, pos);
        heroPosn = pos;
        break;
      }
    }
    while (true) {
      int r = random.nextInt(getHeight()), c = random.nextInt(getWidth());
      if (board[r][c] == null) {
        Exit exit = new Exit();
        set(exit, new Posn(r, c));
        break;
      }
    }
    for (int i = 0; i < enemiesCount; i++) {
      while (true) {
        int r = random.nextInt(getHeight()), c = random.nextInt(getWidth());
        if (board[r][c] == null) {
          Enemy e = new Enemy();
          Posn pos = new Posn(r, c);
          set(e, pos);
          enemies.add(e);
          break;
        }
      }
    }
    for (int i = 0; i < treasures; i++) {
      while (true) {
        int r = random.nextInt(getHeight()), c = random.nextInt(getWidth());
        if (board[r][c] == null) {
          Treasure t = new Treasure();
          set(t, new Posn(r, c));
          break;
        }
      }
    }
    for (int i = 0; i < walls; i++) {
      while (true) {
        int r = random.nextInt(getHeight()), c = random.nextInt(getWidth());
        if (board[r][c] == null) {
          Wall w = new Wall();
          set(w, new Posn(r, c));
          break;
        }
      }
    }
  }

  @Override
  public int getWidth() {
    return board[0].length;
  }

  @Override
  public int getHeight() {
    return board.length;
  }

  @Override
  public Piece get(Posn posn) {
    return board[posn.getRow()][posn.getCol()];
  }

  @Override
  public void set(Piece p, Posn newPos) {
    Posn old = p.getPosn();
    if (old != null) {
      board[old.getRow()][old.getCol()] = null;
    }
    board[newPos.getRow()][newPos.getCol()] = p;
    p.setPosn(newPos);
    if (p instanceof Hero) {
      heroPosn = newPos;
    }
  }

  @Override
  public CollisionResult moveHero(int drow, int dcol) {
    int oldR = heroPosn.getRow(), oldC = heroPosn.getCol();
    int newR = oldR + drow, newC = oldC + dcol;
    if (newR < 0 || newR >= getHeight() || newC < 0 || newC >= getWidth()) {
      return new CollisionResult(0, CollisionResult.Result.CONTINUE);
    }
    Posn newPos = new Posn(newR, newC);
    Piece dest = get(newPos);
    if (dest instanceof Wall) {
      return new CollisionResult(0, CollisionResult.Result.CONTINUE);
    }
    MovablePiece hero = (MovablePiece) get(heroPosn);
    CollisionResult hResult = hero.collide(dest);
    set(hero, newPos);
    if (hResult.getResults() != CollisionResult.Result.CONTINUE) {
      return hResult;
    }
    for (Enemy e : enemies) {
      CollisionResult eResult = moveEnemy(e);
      if (eResult.getResults() == CollisionResult.Result.GAME_OVER) {
        int pts = hResult.getPoints() + eResult.getPoints();
        return new CollisionResult(pts, CollisionResult.Result.GAME_OVER);
      }
    }
    return hResult;
  }

  private CollisionResult moveEnemy(Enemy e) {
    List<int[]> dirs = new ArrayList<>();
    Collections.addAll(
        dirs, new int[] {-1, 0}, new int[] {1, 0}, new int[] {0, -1}, new int[] {0, 1});
    Collections.shuffle(dirs, random);
    for (int[] d : dirs) {
      int er = e.getPosn().getRow() + d[0], ec = e.getPosn().getCol() + d[1];
      if (er < 0 || er >= getHeight() || ec < 0 || ec >= getWidth()) continue;
      Posn p = new Posn(er, ec);
      Piece dest = get(p);
      if (dest instanceof Wall || dest instanceof Exit || dest instanceof Enemy) continue;
      CollisionResult res = ((MovablePiece) e).collide(dest);
      set(e, p);
      return res;
    }
    return new CollisionResult(0, CollisionResult.Result.CONTINUE);
  }
}

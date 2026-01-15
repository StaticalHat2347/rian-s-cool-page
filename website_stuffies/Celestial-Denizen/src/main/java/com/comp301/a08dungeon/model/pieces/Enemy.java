package com.comp301.a08dungeon.model.pieces;

import com.comp301.a08dungeon.model.board.Posn;

public class Enemy extends APiece implements MovablePiece {

  public Enemy() {
    super("Pirate", "/images/enemy.png");
  }

  public CollisionResult collide(Piece other) {
    if (other == null) {
      return new CollisionResult(0, CollisionResult.Result.CONTINUE);
    }
    if (other instanceof Treasure) {
      return new CollisionResult(0, CollisionResult.Result.CONTINUE);
    }
    if (other instanceof Hero) {
      return new CollisionResult(0, CollisionResult.Result.GAME_OVER);
    }
    if (other instanceof Wall) {
      throw new IllegalArgumentException("Enemy collided with wall!");
    }
    if (other instanceof Piece) {
      throw new IllegalArgumentException("Enemy collided with unknown object!");
    }

    return new CollisionResult(0, CollisionResult.Result.CONTINUE);
  }
}

package com.comp301.a08dungeon.model.pieces;

import com.comp301.a08dungeon.model.board.Posn;

public class Hero extends APiece implements MovablePiece {

  public Hero() {
    super("Pilot", "/images/hero.png");
  }

  public CollisionResult collide(Piece other) {

    if (other == null) {
      return new CollisionResult(0, CollisionResult.Result.CONTINUE);
    }
    if (other instanceof Treasure) {
      int t = ((Treasure) other).getValue();
      return new CollisionResult(t, CollisionResult.Result.CONTINUE);
    }
    if (other instanceof Enemy) {
      return new CollisionResult(0, CollisionResult.Result.GAME_OVER);
    }
    if (other instanceof Exit) {
      return new CollisionResult(0, CollisionResult.Result.NEXT_LEVEL);
    }
    if (other instanceof Wall) {
      throw new IllegalArgumentException("Hero collided with wall!");
    }
    if (other instanceof Piece) {
      throw new IllegalArgumentException("Hero collided with unknown");
    }

    return new CollisionResult(0, CollisionResult.Result.CONTINUE);
  }
}

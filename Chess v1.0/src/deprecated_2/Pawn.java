package src;

public abstract class Pawn extends ChessPiece {

    private boolean enpassantable = false;

    Pawn(String side, Position position) {
        super("pawn", side, position);
    }

    public boolean isEnpassantable() {
        return enpassantable;
    }

    @Override public void move() {
        if (!hasMoved()) {
            enpassantable = true;
            super.move();
        } else {
            enpassantable = false;
        }
    }

    public void cancelEnpassantable() {
        enpassantable = false;
    }

    public abstract Position[] getMoves(ChessPiece[] pieces);
}

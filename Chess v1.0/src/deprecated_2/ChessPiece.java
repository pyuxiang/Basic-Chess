package src;

import java.util.ArrayList;

public abstract class ChessPiece {

    // Individual instance fields
    private String type;
    private String side;
    private Position position;
    private boolean alive = true;
    private boolean moved = false;

    ChessPiece(String type, String side, Position position) {
        this.type = type;
        this.side = side;
        this.position = position;
    }

    public String getSide() {
        return side;
    }

    public String getType() {
        return type;
    }

    public Position getPos() {
        return position;
    }

    public void setPos(Position pos) {
        position = pos;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        alive = false;
    }

    public boolean hasMoved() {
        return moved;
    }

    public void move() {
        if (!moved) {
            moved = true;
        }
    }

    // Sparse implementation!
    // Does not check for checking conditions
    public abstract Position[] getMoves(ChessPiece[] pieces);
    public Position[] getMoves(ArrayList<ChessPiece> pieces) {
        return getMoves(pieces.toArray(new ChessPiece[pieces.size()]));
    }

    @Override public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        }

        ChessPiece other = (ChessPiece) obj;
        return other.getType().equals(this.getType())
            && other.getSide().equals(this.getSide())
            && other.getPos().equals(this.getPos());
    }

}

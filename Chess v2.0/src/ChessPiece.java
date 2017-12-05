package src;

import java.util.ArrayList;

public abstract class ChessPiece {

    // Individual instance fields
    private String type;
    private String side;
    private Position position;
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
        return position.clone();
    }

    public void setPos(Position pos) {
        position = pos;
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
    public abstract ArrayList<Position> getMovement(ArrayList<ChessPiece> pieces);
    public abstract ArrayList<Position> getInfluence(ArrayList<ChessPiece> pieces);

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

    @Override public String toString() {
        return "[" + side + " " + type + " " + position.toString() + "]";
    }

}

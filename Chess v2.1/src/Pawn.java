package src;

import java.util.ArrayList;

public abstract class Pawn extends ChessPiece {

    Pawn(String side, Position position) {
        super("pawn", side, position);
    }

    public abstract ArrayList<Position> getMovement(ArrayList<ChessPiece> pieces);
    public abstract ArrayList<Position> getInfluence(ArrayList<ChessPiece> pieces);
}

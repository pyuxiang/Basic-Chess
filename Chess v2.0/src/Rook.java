package src;

import java.util.ArrayList;

// Note: Check for castling should be done by King, NOT Rook
public class Rook extends ChessPiece {

    Rook(String side, Position position) {
        super("rook", side, position);
    }

    @Override public ArrayList<Position> getMovement(ArrayList<ChessPiece> pieces) {

        ArrayList<Position> moves = new ArrayList<>();
        Position current = getPos();
        Position search;

        // Up
        search = current.getUp();
        if (search.isValid()) {
            while (Board.isPosEmpty(search)) {
                moves.add(search);
                search = search.getUp();
                if (!search.isValid()) {
                    search = search.getDown();
                    break;
                }
            }
            if (Board.isPosEnemy(search, getSide())) {
                moves.add(search);
            }
        }

        // Down
        search = current.getDown();
        if (search.isValid()) {
            while (Board.isPosEmpty(search)) {
                moves.add(search);
                search = search.getDown();
                if (!search.isValid()) {
                    search = search.getUp();
                    break;
                }
            }
            if (Board.isPosEnemy(search, getSide())) {
                moves.add(search);
            }
        }

        // Left
        search = current.getLeft();
        if (search.isValid()) {
            while (Board.isPosEmpty(search)) {
                moves.add(search);
                search = search.getLeft();
                if (!search.isValid()) {
                    search = search.getRight();
                    break;
                }
            }
            if (Board.isPosEnemy(search, getSide())) {
                moves.add(search);
            }
        }

        // Right
        search = current.getRight();
        if (search.isValid()) {
            while (Board.isPosEmpty(search)) {
                moves.add(search);
                search = search.getRight();
                if (!search.isValid()) {
                    search = search.getLeft();
                    break;
                }
            }
            if (Board.isPosEnemy(search, getSide())) {
                moves.add(search);
            }
        }
        return moves;
    }

    @Override public ArrayList<Position> getInfluence(ArrayList<ChessPiece> pieces) {
        ArrayList<Position> influence = new ArrayList<>();
        Position current = getPos();
        Position search;

        // Up
        search = current.getUp();
        while (search.isValid()) {
            influence.add(search);
            if (!Board.isPosEmpty(search)) {
                break;
            }
            search = search.getUp();
        }

        // Down
        search = current.getDown();
        while (search.isValid()) {
            influence.add(search);
            if (!Board.isPosEmpty(search)) {
                break;
            }
            search = search.getDown();
        }

        // Left
        search = current.getLeft();
        while (search.isValid()) {
            influence.add(search);
            if (!Board.isPosEmpty(search)) {
                break;
            }
            search = search.getLeft();
        }

        // Right
        search = current.getRight();
        while (search.isValid()) {
            influence.add(search);
            if (!Board.isPosEmpty(search)) {
                break;
            }
            search = search.getRight();
        }

        return influence;
    }
}

package src;

import java.util.ArrayList;

public class Bishop extends ChessPiece {

    Bishop(String side, Position position) {
        super("bishop", side, position);
    }

    @Override public ArrayList<Position> getMovement(ArrayList<ChessPiece> pieces) {

        ArrayList<Position> moves = new ArrayList<>();
        Position current = getPos();
        Position search;

        // Diagonal up-left
        search = current.getUp().getLeft();
        if (search.isValid()) {
            while (Board.isPosEmpty(search)) {
                moves.add(search);
                search = search.getUp().getLeft();
                if (!search.isValid()) {
                    search = search.getDown().getRight();
                    break;
                }
            }
            if (Board.isPosEnemy(search, getSide())) {
                moves.add(search);
            }
        }

        // Diagonal down-right
        search = current.getDown().getRight();
        if (search.isValid()) {
            while (Board.isPosEmpty(search)) {
                moves.add(search);
                search = search.getDown().getRight();
                if (!search.isValid()) {
                    search = search.getUp().getLeft();
                    break;
                }
            }
            if (Board.isPosEnemy(search, getSide())) {
                moves.add(search);
            }
        }

        // Diagonal down-left
        search = current.getDown().getLeft();
        if (search.isValid()) {
            while (Board.isPosEmpty(search)) {
                moves.add(search);
                search = search.getDown().getLeft();
                if (!search.isValid()) {
                    search = search.getUp().getRight();
                    break;
                }
            }
            if (Board.isPosEnemy(search, getSide())) {
                moves.add(search);
            }
        }

        // Diagonal up-right
        search = current.getUp().getRight();
        if (search.isValid()) {
            while (Board.isPosEmpty(search)) {
                moves.add(search);
                search = search.getUp().getRight();
                if (!search.isValid()) {
                    search = search.getDown().getLeft();
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

        // Diagonal up-left
        search = current.getUp().getLeft();
        while (search.isValid()) {
            influence.add(search);
            if (!Board.isPosEmpty(search)) {
                break;
            }
            search = search.getUp().getLeft();
        }

        // Diagonal down-right
        search = current.getDown().getRight();
        while (search.isValid()) {
            influence.add(search);
            if (!Board.isPosEmpty(search)) {
                break;
            }
            search = search.getDown().getRight();
        }

        // Diagonal down-left
        search = current.getDown().getLeft();
        while (search.isValid()) {
            influence.add(search);
            if (!Board.isPosEmpty(search)) {
                break;
            }
            search = search.getDown().getLeft();
        }

        // Diagonal up-right
        search = current.getUp().getRight();
        while (search.isValid()) {
            influence.add(search);
            if (!Board.isPosEmpty(search)) {
                break;
            }
            search = search.getUp().getRight();
        }

        return influence;
    }
}

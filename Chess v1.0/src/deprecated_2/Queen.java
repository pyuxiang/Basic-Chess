package src;

import java.util.ArrayList;

public class Queen extends ChessPiece {

    Queen(String side, Position position) {
        super("queen", side, position);
    }

    @Override public Position[] getMoves(ChessPiece[] pieces) {

        ArrayList<Position> moves = new ArrayList<>();
        Position current = getPos();
        Position search;
        ChessHelper.setPieces(pieces);

        // Up
        search = current.getUp();
        if (search.isValid()) {
            while (ChessHelper.posEmpty(search)) {
                moves.add(search);
                search = search.getUp();
                if (!search.isValid()) {
                    search = search.getDown();
                    break;
                }
            }
            if (ChessHelper.posEnemy(search, getSide())) {
                moves.add(search);
            }
        }

        // Down
        search = current.getDown();
        if (search.isValid()) {
            while (ChessHelper.posEmpty(search)) {
                moves.add(search);
                search = search.getDown();
                if (!search.isValid()) {
                    search = search.getUp();
                    break;
                }
            }
            if (ChessHelper.posEnemy(search, getSide())) {
                moves.add(search);
            }
        }

        // Left
        search = current.getLeft();
        if (search.isValid()) {
            while (ChessHelper.posEmpty(search)) {
                moves.add(search);
                search = search.getLeft();
                if (!search.isValid()) {
                    search = search.getRight();
                    break;
                }
            }
            if (ChessHelper.posEnemy(search, getSide())) {
                moves.add(search);
            }
        }

        // Right
        search = current.getRight();
        if (search.isValid()) {
            while (ChessHelper.posEmpty(search)) {
                moves.add(search);
                search = search.getRight();
                if (!search.isValid()) {
                    search = search.getLeft();
                    break;
                }
            }
            if (ChessHelper.posEnemy(search, getSide())) {
                moves.add(search);
            }
        }

        // Diagonal up-left
        search = current.getUp().getLeft();
        if (search.isValid()) {
            while (ChessHelper.posEmpty(search)) {
                moves.add(search);
                search = search.getUp().getLeft();
                if (!search.isValid()) {
                    search = search.getDown().getRight();
                    break;
                }
            }
            if (ChessHelper.posEnemy(search, getSide())) {
                moves.add(search);
            }
        }

        // Diagonal down-right
        search = current.getDown().getRight();
        if (search.isValid()) {
            while (ChessHelper.posEmpty(search)) {
                moves.add(search);
                search = search.getDown().getRight();
                if (!search.isValid()) {
                    search = search.getUp().getLeft();
                    break;
                }
            }
            if (ChessHelper.posEnemy(search, getSide())) {
                moves.add(search);
            }
        }

        // Diagonal down-left
        search = current.getDown().getLeft();
        if (search.isValid()) {
            while (ChessHelper.posEmpty(search)) {
                moves.add(search);
                search = search.getDown().getLeft();
                if (!search.isValid()) {
                    search = search.getUp().getRight();
                    break;
                }
            }
            if (ChessHelper.posEnemy(search, getSide())) {
                moves.add(search);
            }
        }

        // Diagonal up-right
        search = current.getUp().getRight();
        if (search.isValid()) {
            while (ChessHelper.posEmpty(search)) {
                moves.add(search);
                search = search.getUp().getRight();
                if (!search.isValid()) {
                    search = search.getDown().getLeft();
                    break;
                }
            }
            if (ChessHelper.posEnemy(search, getSide())) {
                moves.add(search);
            }
        }
        return moves.toArray(new Position[moves.size()]);
    }
}

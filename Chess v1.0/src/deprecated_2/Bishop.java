package src;

import java.util.ArrayList;

public class Bishop extends ChessPiece {

    Bishop(String side, Position position) {
        super("bishop", side, position);
    }

    @Override public Position[] getMoves(ChessPiece[] pieces) {

        ArrayList<Position> moves = new ArrayList<>();
        Position current = getPos();
        Position search;
        ChessHelper.setPieces(pieces);

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

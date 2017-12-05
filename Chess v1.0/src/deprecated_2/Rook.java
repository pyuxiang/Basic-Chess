package src;

import java.util.ArrayList;

// Note: Check for castling should be done by King, NOT Rook
public class Rook extends ChessPiece {

    Rook(String side, Position position) {
        super("rook", side, position);
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
        return moves.toArray(new Position[moves.size()]);
    }
}

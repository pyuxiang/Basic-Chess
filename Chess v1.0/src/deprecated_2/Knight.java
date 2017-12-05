package src;

import java.util.ArrayList;

/*
X X X X X X X
X X 1 X 2 X X
X 8 X X X 3 X
X X X O X X X
X 7 X X X 4 X
X X 6 X 5 X X
X X X X X X X
*/

public class Knight extends ChessPiece {

    Knight(String side, Position position) {
        super("knight", side, position);
    }

    @Override public Position[] getMoves(ChessPiece[] pieces) {

        ArrayList<Position> moves = new ArrayList<>();
        Position current = getPos();
        Position search;
        ChessHelper.setPieces(pieces);

        // 1
        search = current.getUp().getUp().getLeft();
        if (search.isValid()) {
            if (!ChessHelper.posFriend(search, getSide())) {
                moves.add(search);
            }
        }

        // 2
        search = current.getUp().getUp().getRight();
        if (search.isValid()) {
            if (!ChessHelper.posFriend(search, getSide())) {
                moves.add(search);
            }
        }

        // 3
        search = current.getUp().getRight().getRight();
        if (search.isValid()) {
            if (!ChessHelper.posFriend(search, getSide())) {
                moves.add(search);
            }
        }

        // 4
        search = current.getDown().getRight().getRight();
        if (search.isValid()) {
            if (!ChessHelper.posFriend(search, getSide())) {
                moves.add(search);
            }
        }

        // 5
        search = current.getDown().getDown().getRight();
        if (search.isValid()) {
            if (!ChessHelper.posFriend(search, getSide())) {
                moves.add(search);
            }
        }

        // 6
        search = current.getDown().getDown().getLeft();
        if (search.isValid()) {
            if (!ChessHelper.posFriend(search, getSide())) {
                moves.add(search);
            }
        }

        // 7
        search = current.getDown().getLeft().getLeft();
        if (search.isValid()) {
            if (!ChessHelper.posFriend(search, getSide())) {
                moves.add(search);
            }
        }

        // 8
        search = current.getUp().getLeft().getLeft();
        if (search.isValid()) {
            if (!ChessHelper.posFriend(search, getSide())) {
                moves.add(search);
            }
        }

        return moves.toArray(new Position[moves.size()]);
    }
}

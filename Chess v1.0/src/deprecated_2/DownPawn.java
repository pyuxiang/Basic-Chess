package src;

import java.util.ArrayList;

public class DownPawn extends Pawn {

    DownPawn(String side, Position position) {
        super(side, position);
    }

    @Override public Position[] getMoves(ChessPiece[] pieces) {

        ArrayList<Position> moves = new ArrayList<>();
        Position current = getPos();
        Position search, target;
        ChessHelper.setPieces(pieces);

        // One step down
        // DEBUGGING: System.out.println(current.getX());
        search = current.getDown();
        // DEBUGGING: System.out.println(current.getX());
        if (ChessHelper.posEmpty(search)) {
            moves.add(search);

            // Two steps down
            search = search.getDown();
            // DEBUGGING: System.out.println(current.getX());
            if (current.getX() == Position.getTopBound() + 1
                    && ChessHelper.posEmpty(search)) {
                moves.add(search);
            }
        }

        // Diagonal left kill / enpassant
        search = current.getDown().getLeft();
        if (search.isValid()) {
            if (ChessHelper.posEnemy(search, getSide())) {
                moves.add(search);
            } else if (ChessHelper.posEmpty(search)) {
                target = current.getLeft();
                if (ChessHelper.posEnemyPawn(target, getSide())) {
                    moves.add(search);
                }
            }
        }

        // Diagonal right kill / enpassant
        search = current.getDown().getRight();
        if (search.isValid()) {
            if (ChessHelper.posEnemy(search, getSide())) {
                moves.add(search);
            } else if (ChessHelper.posEmpty(search)) {
                target = current.getRight();
                if (ChessHelper.posEnemyPawn(target, getSide())) {
                    moves.add(search);
                }
            }
        }
        return moves.toArray(new Position[moves.size()]);
    }
}

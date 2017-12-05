package src;

import java.util.ArrayList;

public class UpPawn extends Pawn {

    UpPawn(String side, Position position) {
        super(side, position);
    }

    @Override public Position[] getMoves(ChessPiece[] pieces) {

        ArrayList<Position> moves = new ArrayList<>();
        Position current = getPos();
        Position search, target;
        ChessHelper.setPieces(pieces);

        // One step up
        search = current.getUp();
        if (ChessHelper.posEmpty(search)) {
            moves.add(search);

            // Two steps up
            search = search.getUp();
            if (current.getX() == Position.getBottomBound() - 1
                    && ChessHelper.posEmpty(search)) {
                moves.add(search);
            }
        }

        // Diagonal left kill / enpassant
        search = current.getUp().getLeft();
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
        search = current.getUp().getRight();
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

package src;

import java.util.ArrayList;

public class UpPawn extends Pawn {

    UpPawn(String side, Position position) {
        super(side, position);
    }

    @Override public ArrayList<Position> getMovement(ArrayList<ChessPiece> pieces) {

        ArrayList<Position> moves = new ArrayList<>();
        Position current = getPos();
        Position search, target;

        // One step up
        search = current.getUp();
        if (Board.isPosEmpty(search)) {
            moves.add(search);

            // Two steps up
            search = search.getUp();
            if (current.getX() == Position.getBottomBound() - 1
                    && Board.isPosEmpty(search)) {
                moves.add(search);
            }
        }

        // Diagonal left kill / enpassant
        search = current.getUp().getLeft();
        if (search.isValid()) {
            if (Board.isPosEnemy(search, getSide())) {
                moves.add(search);
            } else {
                // Check en passant
                target = current.getLeft();
                if (Board.isPosEmpty(search)
                        && Board.isPosEnemyPawn(target, getSide())) {
                    // Check if pawn is last moved
                    if (Board.isEnPassantable(target)) {
                        moves.add(search);
                    }
                }
            }
        }

        // Diagonal right kill / enpassant
        search = current.getUp().getRight();
        if (search.isValid()) {
            if (Board.isPosEnemy(search, getSide())) {
                moves.add(search);
            } else {
                // Check en passant
                target = current.getRight();
                if (Board.isPosEmpty(search)
                        && Board.isPosEnemyPawn(target, getSide())) {
                    // Check if pawn is last moved
                    if (Board.isEnPassantable(target)) {
                        moves.add(search);
                    }
                }
            }
        }
        return moves;
    }

    @Override public ArrayList<Position> getInfluence(ArrayList<ChessPiece> pieces) {
        ArrayList<Position> influence = new ArrayList<>();
        Position current = getPos();
        Position search;

        // Diagonal left
        search = current.getUp().getLeft();
        if (search.isValid()) { influence.add(search); }

        // Diagonal right
        search = current.getUp().getRight();
        if (search.isValid()) { influence.add(search); }

        return influence;
    }
}

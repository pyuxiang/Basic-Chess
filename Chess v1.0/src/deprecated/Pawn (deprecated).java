package src;

import java.util.ArrayList;

// Note: Implemented only for white and black
public class Pawn extends ChessPiece {

    private static final String WHITE = "white";
    private static final String BLACK = "black";
    private boolean enpassantable = false;

    Pawn(String side, Position position) {
        super("pawn", side, position);
    }

    public boolean isEnpassantable() {
        return enpassantable;
    }

    @Override public void move() {
        if (!hasMoved()) {
            enpassantable = true;
            move();
        } else {
            enpassantable = false;
        }
    }

    // NOTE! Only implemented for WHITE and BLACK
    @Override public Position[] getMoves(ChessPiece[] pieces) {

        ArrayList<Position> moves = new ArrayList<>();
        Position current = getPos();
        Position search, target;
        ChessHelper.setPieces(pieces);

        if (getSide().equals(WHITE)) {

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

        } else if (getSide().equals(BLACK)) {

            // One step down
            search = current.getDown();
            if (ChessHelper.posEmpty(search)) {
                moves.add(search);

                // Two steps down
                search = search.getDown();
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

        } else {
            System.out.println("Not implemented");
        }
        return moves.toArray(new Position[moves.size()]);
    }
}

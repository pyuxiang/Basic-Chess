package src;

import java.util.ArrayList;

// NOT implementing the checking function here
// True that checking function is unique to king, but the possibility of a pin
// (-> other pieces defending the king) means that this checking should be done
// for all pieces rather than just the king.

// i.e. the game of chess is uniquely about the game of kings

public class King extends ChessPiece {

    King(String side, Position position) {
        super("king", side, position);
    }

    @Override public ArrayList<Position> getMovement(ArrayList<ChessPiece> pieces) {

        ArrayList<Position> moves = new ArrayList<>();
        Position current = getPos();
        Position search;

        // Up
        search = current.getUp();
        if (search.isValid()) {
            if (!Board.isPosFriendly(search, getSide())) {
                moves.add(search);
            }
        }

        // Up-right
        search = current.getUp().getRight();
        if (search.isValid()) {
            if (!Board.isPosFriendly(search, getSide())) {
                moves.add(search);
            }
        }

        // Right
        search = current.getRight();
        if (search.isValid()) {
            if (!Board.isPosFriendly(search, getSide())) {
                moves.add(search);
            }
        }

        // Down-right
        search = current.getDown().getRight();
        if (search.isValid()) {
            if (!Board.isPosFriendly(search, getSide())) {
                moves.add(search);
            }
        }

        // Down
        search = current.getDown();
        if (search.isValid()) {
            if (!Board.isPosFriendly(search, getSide())) {
                moves.add(search);
            }
        }

        // Down-left
        search = current.getDown().getLeft();
        if (search.isValid()) {
            if (!Board.isPosFriendly(search, getSide())) {
                moves.add(search);
            }
        }

        // Left
        search = current.getLeft();
        if (search.isValid()) {
            if (!Board.isPosFriendly(search, getSide())) {
                moves.add(search);
            }
        }

        // Up-left
        search = current.getUp().getLeft();
        if (search.isValid()) {
            if (!Board.isPosFriendly(search, getSide())) {
                moves.add(search);
            }
        }




        ///////////////////////////////////
        // CHECK FOR CASTLING CONDITIONS //
        ///////////////////////////////////

        // Uniquely in cardinal directions at distances of 3 squares or more
        // By conventional chess rules, king and rook must be on the same rank
        if (!hasMoved()) {
            Position rookPosition;
            ArrayList<Position> enemyInfluence = Board.getEnemyInfluence(getSide());

            // SEARCHES FOR IMMEDIATE LEFT ROOK
            rookPosition = null;
            search = current.getLeft();
            for (int i = 0; i < 2 && search.isValid(); i++) {
                // For two squares, check if empty
                if (!Board.isPosEmpty(search)) {
                    break;
                }
                // Check if is checkable position
                if (Board.isPosIn(search, enemyInfluence)) {
                    break;
                }
                search = search.getLeft();
            }

            // Check if checkable empty, or a ChessPiece, then break
            while (search.isValid()) {
                if (!Board.isPosEmpty(search)) {
                    if (Board.isPosFriendlyRook(search, getSide())) {
                        ChessPiece rook = Board.getPiece(search);
                        if (!rook.hasMoved()) {
                            rookPosition = search;
                        }
                    } else {
                        break;
                    }
                }
                if (Board.isPosIn(search, enemyInfluence)) {
                    break;
                }
                search = search.getLeft();
            }

            if (rookPosition != null) {
                moves.add(current.getLeft().getLeft());
            }



            // SEARCHES FOR IMMEDIATE RIGHT ROOK
            rookPosition = null;
            search = current.getRight();
            for (int i = 0; i < 2 && search.isValid(); i++) {
                // For two squares, check if empty
                if (!Board.isPosEmpty(search)) {
                    break;
                }
                // Check if is checkable position
                if (Board.isPosIn(search, enemyInfluence)) {
                    break;
                }
                search = search.getRight();
            }

            // Check if checkable empty, or a ChessPiece, then break
            while (search.isValid()) {
                if (!Board.isPosEmpty(search)) {
                    if (Board.isPosFriendlyRook(search, getSide())) {
                        ChessPiece rook = Board.getPiece(search);
                        if (!rook.hasMoved()) {
                            rookPosition = search;
                        }
                    } else {
                        break;
                    }
                }
                if (Board.isPosIn(search, enemyInfluence)) {
                    break;
                }
                search = search.getRight();
            }

            if (rookPosition != null) {
                moves.add(current.getRight().getRight());
            }

        ///////////////////////////
        // END OF CASTLING CHECK //
        ///////////////////////////

        }
        return moves;
    }

    @Override public ArrayList<Position> getInfluence(ArrayList<ChessPiece> pieces) {
        ArrayList<Position> influence = new ArrayList<>();
        Position current = getPos();
        Position search;

        // Up
        search = current.getUp();
        if (search.isValid()) { influence.add(search); }

        // Up-right
        search = current.getUp().getRight();
        if (search.isValid()) { influence.add(search); }

        // Right
        search = current.getRight();
        if (search.isValid()) { influence.add(search); }

        // Down-right
        search = current.getDown().getRight();
        if (search.isValid()) { influence.add(search); }

        // Down
        search = current.getDown();
        if (search.isValid()) { influence.add(search); }

        // Down-left
        search = current.getDown().getLeft();
        if (search.isValid()) { influence.add(search); }

        // Left
        search = current.getLeft();
        if (search.isValid()) { influence.add(search); }

        // Up-left
        search = current.getUp().getLeft();
        if (search.isValid()) { influence.add(search); }

        return influence;
    }
}

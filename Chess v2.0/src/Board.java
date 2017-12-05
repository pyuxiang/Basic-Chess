package src;

import java.util.ArrayList;

// 8x8 board with "white" and "black"
public class Board {

    static boolean debugger = false; // DEBUGGING

    public static ArrayList<ChessPiece> pieces = new ArrayList<>();
    private String turn = "white";

    private static ChessPiece lastMovedPiece = null;
    private static Position srcLastMove = null;
    private King whiteKing;
    private King blackKing;

    public Board() {
        for (int i = 0; i < 8; i++) {
            pieces.add(new DownPawn("black", new Position(1,i)));
        }
        pieces.add(new Rook("black", new Position(0,0)));
        pieces.add(new Knight("black", new Position(0,1)));
        pieces.add(new Bishop("black", new Position(0,2)));
        pieces.add(new Queen("black", new Position(0,3)));
        blackKing = new King("black", new Position(0,4));
        pieces.add(blackKing);
        pieces.add(new Bishop("black", new Position(0,5)));
        pieces.add(new Knight("black", new Position(0,6)));
        pieces.add(new Rook("black", new Position(0,7)));

        for (int i = 0; i < 8; i++) {
            pieces.add(new UpPawn("white", new Position(6,i)));
        }
        pieces.add(new Rook("white", new Position(7,0)));
        pieces.add(new Knight("white", new Position(7,1)));
        pieces.add(new Bishop("white", new Position(7,2)));
        pieces.add(new Queen("white", new Position(7,3)));
        whiteKing = new King("white", new Position(7,4));
        pieces.add(whiteKing);
        pieces.add(new Bishop("white", new Position(7,5)));
        pieces.add(new Knight("white", new Position(7,6)));
        pieces.add(new Rook("white", new Position(7,7)));

    }

    //////////////////////////
    // BOARD STATE CHECKERS //
    //////////////////////////

    // Check states at positions : Empty
    public static boolean isPosEmpty(Position pos) {
        ChessPiece cp = getPiece(pos);
        return cp == null;
    }

    // Check states at positions : Enemy
    public static boolean isPosEnemy(Position pos, String side) {
        ChessPiece cp = getPiece(pos);
        if (cp == null) {
            return false;
        } else if (!(cp.getSide().equals(side))) {
            return true;
        }
        return false;
    }

    // Check states at positions : Friendly
    public static boolean isPosFriendly(Position pos, String side) {
        ChessPiece cp = getPiece(pos);
        if (cp == null) {
            return false;
        } else if (cp.getSide().equals(side)) {
            return true;
        }
        return false;
    }

    // Check states at position : Enemy Pawn (for en passant)
    public static boolean isPosEnemyPawn(Position pos, String side) {
        ChessPiece cp = getPiece(pos);
        if (cp == null) {
            return false;
        } else if (!(cp.getSide().equals(side)) && cp.getClass() == Pawn.class) {
            return true;
        }
        return false;
    }

    // Check states at position : Friendly Rook (for castling)
    public static boolean isPosFriendlyRook(Position pos, String side) {
        ChessPiece cp = getPiece(pos);
        if (cp == null) {
            return false;
        } else if (cp.getSide().equals(side) && cp.getClass() == Rook.class) {
            return true;
        }
        return false;
    }

    // Check states at position : Enemy influence (for castling)
    // Generic element-in-array checker
    public static boolean isPosIn(Position pos, ArrayList<Position> posAry) {
        for (Position position : posAry) {
            if (pos.equals(position)) {
                return true;
            }
        }
        return false;
    }

    // Check if pawn is enpassantable
    // Method relegated here because Board has info on pieces
    public static boolean isEnPassantable(Position pos) {
        ChessPiece target = getPiece(pos);
        if (target.getClass() != Pawn.class) {
            return false;
        }
        if (target.equals(lastMovedPiece)) {
            // Check for white pawn at pos (moving up)
            if (pos.getDown().getDown().equals(srcLastMove)) {
                return true;
            }
            // Check for black pawn at pos (moving down)
            else if (pos.getUp().getUp().equals(srcLastMove)) {
                return true;
            }
        }
        return false;
    }






    //////////////////////////
    // CHESS STATE CHECKERS //
    //////////////////////////

    // Check whether selected piece is current player's
    public boolean isValidPiece(ChessPiece cp) {
        if (cp == null) {
            return false;
        }
        return cp.getSide().equals(turn);
    }

    public boolean isValidPiece(Position pos) {
        ChessPiece cp = getPiece(pos);
        return isValidPiece(cp);
    }

    public static ChessPiece getPiece(Position pos) {
        for (ChessPiece cp : pieces) {
            if (cp.getPos().equals(pos)) {
                return cp;
            }
        }
        return null;
    }

    public ChessPiece getKing() { // current turn
        if (whiteKing.getSide().equals(turn)) {
            return whiteKing;
        } else if (blackKing.getSide().equals(turn)) {
            return blackKing;
        } else {
            // Not supposed to happen!
            System.exit(1);
            return null;
        }
    }

    // Checked == King in enemy influence zone
    public boolean isChecked() { // current turn
        if (debugger) { System.out.println(">>> ICK: Checking if checked"); }
        Position kingPos = getKing().getPos();
        boolean check = isPosIn(kingPos, getEnemyInfluence(turn));
        if (debugger) { System.out.printf(">>> ICK: Checked: %s\n", (check ? "true" : "false")); }
        return check;
    }

    // Checkmate == No valid moves
    // Note: Modifying array while iterating through it!
    public boolean isCheckmated() { // current turn
        if (debugger) { System.out.println(">>> ICM: Checking if checkmated"); }
        // Creating temporary array to loop through elements
        ArrayList<ChessPiece> piecesIter = new ArrayList<>();
        for (ChessPiece cp : pieces) {
            piecesIter.add(cp);
        }
        for (ChessPiece cp : piecesIter) {
            if (cp.getSide().equals(turn)) {
                for (Position pos : cp.getMovement(pieces)) {
                    if (isValidMove(cp, pos)) {
                        if (debugger) { System.out.println(">>> ICM: Checked: false"); }
                        return false;
                    }
                }
            }
        }
        if (debugger) { System.out.println(">>> ICM: Checked: true"); }
        return true;
    }

    // Get list of enemy line-of-fire, regardless of whether pinned
    public static ArrayList<Position> getEnemyInfluence(String side) {
        if (debugger) { System.out.printf(">>> GEI: getEnemyInfluence(%s)\n", side); }
        ArrayList<Position> influenceZone = new ArrayList<>();
        for (ChessPiece piece : pieces) {
            if (!piece.getSide().equals(side)) { // Check if is enemy
                if (debugger) { System.out.printf(">>> GEI: %s\n", piece); }
                ArrayList<Position> influence = piece.getInfluence(pieces);
                for (Position tile : influence) {
                    influenceZone.add(tile);
                }
            }
        }
        return influenceZone;
    }







    /////////////////////////////
    // STATE MODIFYING METHODS //
    /////////////////////////////

    // Assumes piece is valid
    private void removePiece(ChessPiece piece) {
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).equals(piece)) {
                if (debugger) { System.out.printf(">>> RVP: %s removed\n", pieces.get(i)); }
                pieces.remove(i);
            }
        }
    }

    // Returns ChessPiece to be removed after move
    // Returns null if no ChessPiece removed
    private ChessPiece getTarget(ChessPiece piece, Position dsc) {
        ChessPiece target = getPiece(dsc);
        if (target != null) {
            return target;
        } else { // Is either en passant capture, or no capture
            // Check for en passant (pawn moved inside its influence)
            if (piece.getClass() == Pawn.class) {
                // Check if pawn did a capture (en passant)
                if (isPosIn(dsc, piece.getInfluence(pieces))) {
                    return getPiece(dsc.getDown());
                }
            }
        }
        return null;
    }

    // NOTE: This is a (and the only) public state-modifying method!
    // Modifies state to reflect results of piece movement
    // Method assumes move to be valid -> pass from isValidMove()
    public void movePiece(ChessPiece piece, Position dsc) {

        ChessPiece target = getTarget(piece, dsc);
        if (target != null) {
            if (debugger) { System.out.printf(">>> MVP: Removing %s\n", target); }
            removePiece(target);
        }

        // Check if king castled, if yes, move Rook
        if (piece.getClass() == King.class) {
            Position pos = piece.getPos();
            // Search left for rook
            if (pos.getLeft().getLeft().equals(dsc)) {
                while (!Board.isPosFriendlyRook(pos, piece.getSide())) {
                    pos = pos.getLeft();
                }
                ChessPiece rook = getPiece(pos);
                rook.setPos(piece.getPos().getLeft());
                rook.move();
            }
            // Search right for rook
            else if (pos.getRight().getRight().equals(dsc)) {
                while (!Board.isPosFriendlyRook(pos, piece.getSide())) {
                    pos = pos.getRight();
                }
                ChessPiece rook = getPiece(pos);
                rook.setPos(piece.getPos().getRight());
                rook.move();
            }
        }

        lastMovedPiece = piece;
        srcLastMove = piece.getPos();
        piece.setPos(dsc);
        piece.move();
        turn = turn.equals("white") ? "black" : "white";
        if (debugger) { System.out.printf(">>> MVP: Current turn: %s\n", turn); }
    }

    // Programmer note: MUST only derive from getMovement() from ChessPiece,
    // and isChecked(), lastMovedPiece, srcLastMove from Board (this file)
    // Returns true if move can be made in ACTUAL chess gameplay
    // i.e. own king must not fall in check
    public boolean isValidMove(ChessPiece piece, Position dsc) {
        if (debugger) { System.out.printf(">>> IVM: Checking: %s\n", piece); }

        // Quick check if piece belongs to current turn's player
        if (!piece.getSide().equals(turn)) {
            if (debugger) { System.out.println(">>> IVM: Checked: false"); }
            return false;
        }

        // Check if move can be made by piece under chess rules
        if (debugger) { System.out.println(">>> IVM: Checking movement"); }
        if (!isPosIn(dsc, piece.getMovement(pieces))) {
            if (debugger) { System.out.println(">>> IVM: Checked: false"); }
            return false;
        }

        // Check if move will cause own king to be in check
        int i = 0;
        ChessPiece target = getTarget(piece, dsc);
        if (target != null) {
            // Find the index within pieces so that it can be slot back later
            for (i = 0; i < pieces.size(); i++) {
                if (pieces.get(i).equals(target)) {
                    if (debugger) { System.out.println(">>> IVM: Removing target"); }
                    pieces.remove(i);
                    break;
                }
            }
        }

        // Save source position, then move
        Position src = piece.getPos();
        piece.setPos(dsc);

        // isChecked() only after movement since king might be moving
        boolean valid = true;
        if (isChecked()) {
            valid = false;
        }

        // Reset kill and move
        if (target != null) {
            if (debugger) { System.out.println(">>> IVM: Restoring removed target"); }
            pieces.add(i, target);
        }
        piece.setPos(src);
        if (debugger) { System.out.printf(">>> IVM: Checked: %s\n", (valid ? "true" : "false")); }
        return valid;
    }
}

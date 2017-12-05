package src;

import java.util.ArrayList;

// 8x8 board with "white" and "black"
public class Board {

    public ArrayList<ChessPiece> pieces = new ArrayList<>();
    private String turn = "white";

    public Board() {
        for (int i = 0; i < 8; i++) {
            pieces.add(new DownPawn("black", new Position(1,i)));
        }
        pieces.add(new Rook("black", new Position(0,0)));
        pieces.add(new Knight("black", new Position(0,1)));
        pieces.add(new Bishop("black", new Position(0,2)));
        pieces.add(new Queen("black", new Position(0,3)));
        pieces.add(new King("black", new Position(0,4)));
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
        pieces.add(new King("white", new Position(7,4)));
        pieces.add(new Bishop("white", new Position(7,5)));
        pieces.add(new Knight("white", new Position(7,6)));
        pieces.add(new Rook("white", new Position(7,7)));

    }

    public boolean checkMove(ChessPiece piece, Position dsc) {

        // Check if piece should be moving in the first place
        if (!piece.getSide().equals(turn)) {
            return false;
        }

        // Check if piece can move
        boolean possibleMove = false;
        for (Position move : piece.getMoves(pieces)) {
            if (dsc.equals(move)) {
                possibleMove = true;
                break;
            }
        }
        if (!possibleMove) {
            return false;
        }

        // Attempt move, then
        // Check if own side will be in check
        // Get piece at dsc if any
        ChessPiece dsc_cp = null;
        int idx = 0;
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).getPos().equals(dsc) && pieces.get(i).isAlive()) {
                dsc_cp = pieces.get(i);
                idx = i;
                pieces.remove(i);
                break;
            }
        }

        // Get same side king
        ChessPiece king = null;
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).getSide().equals(piece.getSide())
                    && pieces.get(i) instanceof King) {
                king = pieces.get(i);
                break;
            }
        }

        Position src = piece.getPos();
        piece.setPos(dsc);
        ChessHelper.setPieces(pieces);
        Position[] illegal = ChessHelper.calculateEnemyTerritory(piece.getSide());

        boolean validMove = true;
        for (Position move : illegal) {
            if (move.equals(king.getPos())) {
                return false;
            }
        }

        // Preserve states!
        if (dsc_cp != null) {
            pieces.add(idx, dsc_cp);
        }
        piece.setPos(src);

        return validMove;
    }

    // Returns false if checkmated
    public boolean action(ChessPiece piece, Position dsc) {

        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).getPos().equals(dsc) && pieces.get(i).isAlive()) {
                pieces.get(i).kill();
                pieces.remove(i);
                break;
            }
        }

        piece.setPos(dsc);
        piece.move();
        turn = turn.equals("white") ? "black" : "white";
        if (isCheckmated(turn)) {
            return false;
        }
        return true;
    }

    public String getTurn() {
        return turn;
    }

    // Checkmated when testing move, no valid moves
    public boolean isCheckmated(String side) {
        ChessHelper.setPieces(pieces);

        boolean checkmated = true;
        outer:
        for (ChessPiece own_cp : pieces) {
            if (own_cp.getSide().equals(side)) {
                for (Position step : own_cp.getMoves(pieces)) {
                    if (checkMove(own_cp, step)) {
                        checkmated = false;
                        break outer;
                    }
                }
            }
        }
        return checkmated;
    }

    public ChessPiece getChecked() {
        // Get same side king
        ChessPiece king = null;
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).getSide().equals(turn)
                    && pieces.get(i) instanceof King) {
                king = pieces.get(i);
                break;
            }
        }

        ChessHelper.setPieces(pieces);
        for (Position p : ChessHelper.calculateEnemyTerritory(turn)) {
            if (king.getPos().equals(p)) {
                return king;
            }
        }
        return null;
    }

    public ChessPiece getPiece(Position pos) {
        for (ChessPiece cp : pieces) {
            if (cp.getPos().equals(pos) && cp.isAlive()) {
                return cp;
            }
        }
        return null;
    }


    // Check whether selected piece is current player's
    public boolean isValidPiece(ChessPiece cp) {
        return cp.getSide().equals(turn);
    }
    public boolean isValidPiece(Position pos) {
        ChessPiece cp = getPiece(pos);
        return isValidPiece(cp);
    }


}

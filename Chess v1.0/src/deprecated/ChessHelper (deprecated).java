package src;

import java.util.ArrayList;

// Functional class
public class ChessHelper {

    private static ChessPiece[] pieces;
    private static Position[] enemyTerritory;

    // Always initialize this first! (optimisation for subsequent method calls)
    public static void setPieces(ChessPiece[] currentPieces) {
        pieces = currentPieces;
    }
    public static void setPieces(ArrayList<ChessPiece> currentPieces) {
        setPieces(currentPieces.toArray(new ChessPiece[currentPieces.size()]));
    }

    // Get list of enemy pieces, get valid moves for all of them
    // regardless of whether they are pinned or not.
    public static Position[] calculateEnemyTerritory(String side) {
        ArrayList<Position> positions = new ArrayList<>();
        for (ChessPiece piece : pieces) {
            if (!piece.getSide().equals(side)) {
                if (piece.getType().equals("king") && !piece.hasMoved()) {
                    continue;
                }
                Position[] territories = piece.getMoves(pieces);
                for (Position territory : territories) {
                    positions.add(territory);
                }
            }
        }
        enemyTerritory = positions.toArray(new Position[positions.size()]);
        return enemyTerritory;
    }

    // For use in getting moves for pieces
    public static boolean posEmpty(Position pos) {
        for (ChessPiece piece : pieces) {
            if (pos.equals(piece.getPos())) {
                if (piece.isAlive()) {
                    return false;
                }
                break;
            }
        }
        return true;
    }

    public static boolean posEnemy(Position pos, String side) {
        for (ChessPiece piece : pieces) {
            if (pos.equals(piece.getPos())) {
                if (!(piece.getSide().equals(side)) && piece.isAlive()) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    public static boolean posFriend(Position pos, String side) {
        for (ChessPiece piece : pieces) {
            if (pos.equals(piece.getPos())) {
                if (piece.getSide().equals(side) && piece.isAlive()) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    // Only applicable for pawn!
    public static boolean posEnemyPawn(Position pos, String side) {
        for (ChessPiece piece : pieces) {
            if (pos.equals(piece.getPos())) {
                if (!(piece.getSide().equals(side))
                        && piece.isAlive()
                        && piece.getClass() == Pawn.class) {
                    Pawn pawn = (Pawn) piece;
                    if (pawn.isEnpassantable()) {
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }

    // Castling, only applicable for king!
    public static boolean posFriendRook(Position pos, String side) {
        for (ChessPiece piece : pieces) {
            if (pos.equals(piece.getPos())) {
                if (piece.getSide().equals(side)
                        && piece.isAlive()
                        && piece.getClass() == Rook.class) {
                    Rook rook = (Rook) piece;
                    if (!rook.hasMoved()) {
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }

    // Only applicable for king!
    // Ensure that enemyTerritory is calculated first
    public static boolean entersCheck(Position pos) {
        for (Position position : enemyTerritory) {
            if (pos.equals(position)) {
                return true;
            }
        }
        return false;
    }


}

/**
 * Chess Java application
 * Experimenting use of GridLayout, and implementing a chess GUI.
 * Not very successful, because the way the data is stored isn't very well-thought
 * out, resulting in excessive code just to refer to the piece/position.
 *
 * Another room for improvement is how the methods are implemented:
 *     - Do not mix up the purpose of each method!
 *     - Functions that do a simple check to return a true-false state should
 *       NOT be modifying state.
 *     - Methods running via the EDT should NOT have code that computes the logic
 *       for the chess: offload them to other methods!
 *
 * @author Peh Yu Xiang
 * @version 1.0
 * @since 8 August 2017
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

/* ActionCommand:
 * 1. Check selected button:
 *      - Check if is piece
 *      - Check if valid piece (var whiteturn)
 * 2. Check var selection:
 *      if false: save button via reflection, modify border, save as src
 *      if true: change back button border, save as dsc, pass to checkMove()
 * 3. checkMove():
 *      - Implement check next time...
 *      - Check dsc, must be empty or opposing piece
 *      - Generate possible moves, check if valid move
 *          - If valid, replace board, return true
 *          - If not valid, return false
 */

public class FirstChess extends JFrame implements ActionListener {

    static String[][] board = new String[8][8];
    static JButton[][] buttons = new JButton[8][8];
    static ImageIcon wr, wn, wb, wq, wk, wp, br, bn, bb, bq, bk, bp, empty;
    static String[] gridLetters = {"A", "B", "C", "D", "E", "F", "G", "H"};


    // Logic variables
    static JButton position = null;
    static String selection = "none";
    static String turn = "w"; // "w"/"b"

    // Checking mechanism
    static boolean checked = false;
    static JButton checkingPiecePos = null;
    static JButton checkedPiecePos = null;


    private void initImages() {
        wr = new ImageIcon("img/wr.jpg");
        wn = new ImageIcon("img/wn.jpg");
        wb = new ImageIcon("img/wb.jpg");
        wq = new ImageIcon("img/wq.jpg");
        wk = new ImageIcon("img/wk.jpg");
        wp = new ImageIcon("img/wp.jpg");

        br = new ImageIcon("img/br.jpg");
        bn = new ImageIcon("img/bn.jpg");
        bb = new ImageIcon("img/bb.jpg");
        bq = new ImageIcon("img/bq.jpg");
        bk = new ImageIcon("img/bk.jpg");
        bp = new ImageIcon("img/bp.jpg");

        empty = new ImageIcon("img/null.jpg");
    }

    private void initBoard() {
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = "none";
            }
        }
        board[0][0] = "br"; board[0][7] = "br";
        board[0][1] = "bn"; board[0][6] = "bn";
        board[0][2] = "bb"; board[0][5] = "bb";
        board[0][3] = "bq"; board[0][4] = "bk";

        board[7][0] = "wr"; board[7][7] = "wr";
        board[7][1] = "wn"; board[7][6] = "wn";
        board[7][2] = "wb"; board[7][5] = "wb";
        board[7][3] = "wq"; board[7][4] = "wk";

        for (int j = 0; j < 8; j++) {
            board[1][j] = "bp";
            board[6][j] = "wp";
        }
    }

    FirstChess(String name) {
        super(name);
        setResizable(true);
        initBoard();
        initImages();
        // System.out.println(Arrays.deepToString(board));
    }

    private void addComponents(Container pane) {
        JPanel game = new JPanel();
        game.setLayout(new GridLayout(8,8));

        // Add buttons to grid
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton cell = new JButton();
                cell.addActionListener(this);
                cell.setActionCommand(i + "" + j);
                cell.setBorder(BorderFactory.createLineBorder(Color.black, 1));

                ImageIcon ref = null;
                switch (board[i][j]) {
                    case "none": ref = empty; break;
                    case "wr": ref = wr; break;
                    case "wn": ref = wn; break;
                    case "wb": ref = wb; break;
                    case "wq": ref = wq; break;
                    case "wk": ref = wk; break;
                    case "wp": ref = wp; break;
                    case "br": ref = br; break;
                    case "bn": ref = bn; break;
                    case "bb": ref = bb; break;
                    case "bq": ref = bq; break;
                    case "bk": ref = bk; break;
                    case "bp": ref = bp; break;
                }
                cell.setIcon(ref);
                buttons[i][j] = cell;
                game.add(cell);
            }
        }
        pane.add(game, BorderLayout.NORTH);

        // JPanel log = new JPanel();
        // pane.add(log, BorderLayout.SOUTH);
    }

    // Called by the event dispatch thread
    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();
        int row = Integer.parseInt(command.substring(0,1));
        int col = Integer.parseInt(command.substring(1,2));

        if (selection.equals("none")) {
            String piece = board[row][col];
            if (piece.substring(0,1).equals(turn)) {
                selection = piece;
                position = buttons[row][col];
                position.setBorder(BorderFactory.createLineBorder(Color.red, 1));
            }

        } else {
            String str_position = position.getActionCommand();
            position.setBorder(BorderFactory.createLineBorder(Color.black, 1));
            if (checkMove(selection, str_position, command)) {
                if (checked) {
                    int src_row = Integer.parseInt(str_position.substring(0,1));
                    int src_col = Integer.parseInt(str_position.substring(1,2));
                    int dsc_row = Integer.parseInt(command.substring(0,1));
                    int dsc_col = Integer.parseInt(command.substring(1,2));
                    String temp = board[dsc_row][dsc_col];
                    board[src_row][src_col] = "none";
                    board[dsc_row][dsc_col] = selection;

                    String pos = checkingPiecePos.getActionCommand();
                    int check_row = Integer.parseInt(pos.substring(0,1));
                    int check_col = Integer.parseInt(pos.substring(1,2));
                    if (checkCheck(board[check_row][check_col], pos).equals("none")) {
                        checkedPiecePos.setBorder(BorderFactory.createLineBorder(Color.black, 1));
                        checked = false;
                        checkingPiecePos = null;
                        checkedPiecePos = null;
                        refresh();
                        resetTurn(true);
                    } else {
                        board[src_row][src_col] = selection;
                        board[dsc_row][dsc_col] = temp;
                        resetTurn(false);
                    }

                } else {
                    // Check stuff
                    // TODO: This one is wrong! This only checks for explicit check
                    // but uncovered check is not implemented (need to review ALL pieces)
                    String checkedPiece = checkCheck(selection, command);
                    if (!checkedPiece.equals("none")) {
                        checked = true;
                        int checking_row = Integer.parseInt(command.substring(0,1));
                        int checking_col = Integer.parseInt(command.substring(1,2));
                        checkingPiecePos = buttons[checking_row][checking_col];

                        int checked_row = Integer.parseInt(checkedPiece.substring(0,1));
                        int checked_col = Integer.parseInt(checkedPiece.substring(1,2));
                        checkedPiecePos = buttons[checked_row][checked_col];
                        checkedPiecePos.setBorder(BorderFactory.createLineBorder(Color.green, 1));
                    }

                    // Board stuff
                    int src_row = Integer.parseInt(str_position.substring(0,1));
                    int src_col = Integer.parseInt(str_position.substring(1,2));
                    int dsc_row = Integer.parseInt(command.substring(0,1));
                    int dsc_col = Integer.parseInt(command.substring(1,2));
                    board[src_row][src_col] = "none";
                    board[dsc_row][dsc_col] = selection;
                    refresh();
                    resetTurn(true);
                }
            } else {
                resetTurn(false);
            }
        }
    }

    private void refresh() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton cell = buttons[i][j];
                ImageIcon ref = null;
                switch (board[i][j]) {
                    case "none": ref = empty; break;
                    case "wr": ref = wr; break;
                    case "wn": ref = wn; break;
                    case "wb": ref = wb; break;
                    case "wq": ref = wq; break;
                    case "wk": ref = wk; break;
                    case "wp": ref = wp; break;
                    case "br": ref = br; break;
                    case "bn": ref = bn; break;
                    case "bb": ref = bb; break;
                    case "bq": ref = bq; break;
                    case "bk": ref = bk; break;
                    case "bp": ref = bp; break;
                }
                cell.setIcon(ref);
            }
        }
    }

    private void resetTurn(boolean next) {
        position = null;
        selection = "none";
        if (next) {
            turn = turn.equals("w") ? "b" : "w";
        }
    }

    // Returns position of piece in check if any, else "notInCheck"
    private String checkCheck(String selection, String pos) {
        String[] moves = generateMoves(selection, pos);
        for (String possibleMove : moves) {
            int row = Integer.parseInt(possibleMove.substring(0,1));
            int col = Integer.parseInt(possibleMove.substring(1,2));
            if (board[row][col].equals((selection.substring(0,1).equals("w")?"b":"w") + "k")) {
                return possibleMove;
            }
        }
        return "none";
    }

    private boolean checkMove(String selection, String src, String dsc) {

        String[] moves = generateMoves(selection, src);
        for (String possibleMove : moves) {
            if (dsc.equals(possibleMove)) {
                return true;
            }
        }
        return false;
    }





    // PLEASE BRING THIS METHOD TO CHESSLOGIC.JAVA PLEASE
    private String[] generateMoves(String piece, String position) {
        int row = Integer.parseInt(position.substring(0,1));
        int col = Integer.parseInt(position.substring(1,2));
        ArrayList<String> moves = new ArrayList<>();

        int newRow = row;
        int newCol = col;

        switch (piece) {

            // White pawn logic
            case "wp":
                if (board[row-1][col].equals("none")) {
                    moves.add((row-1)+""+col);
                    if (row == 6 && board[4][col].equals("none")) {
                        moves.add(4+""+col);
                    }
                }
                if (col != 0) {
                    if (board[row-1][col-1].substring(0,1).equals("b")) {
                        moves.add((row-1)+""+(col-1));
                    }
                }
                if (col != 7) {
                    if (board[row-1][col+1].substring(0,1).equals("b")) {
                        moves.add((row-1)+""+(col+1));
                    }
                }
                break;

            // Black pawn logic
            case "bp":
                if (board[row+1][col].equals("none")) {
                    moves.add((row+1)+""+col);
                    if (row == 1 && board[3][col].equals("none")) {
                        moves.add(3+""+col);
                    }
                }
                if (col != 0) {
                    if (board[row+1][col-1].substring(0,1).equals("w")) {
                        moves.add((row+1)+""+(col-1));
                    }
                }
                if (col != 7) {
                    if (board[row+1][col+1].substring(0,1).equals("w")) {
                        moves.add((row+1)+""+(col+1));
                    }
                }
                break;

            // Rook logic
            case "wr":
            case "br":
                // Check down
                if (newRow != 7) {
                    newRow++;
                    while (board[newRow][newCol].equals("none")) {
                        moves.add(newRow+""+newCol);
                        if (newRow == 7) { break; }
                        newRow++;
                    }
                    if (board[newRow][newCol].substring(0,1).equals(piece.substring(0,1).equals("w")?"b":"w")) {
                        moves.add(newRow+""+newCol);
                    }
                    newRow = row;
                }

                // Check up
                if (newRow != 0) {
                    newRow--;
                    while (board[newRow][newCol].equals("none")) {
                        moves.add(newRow+""+newCol);
                        if (newRow == 0) { break; }
                        newRow--;
                    }
                    if (board[newRow][newCol].substring(0,1).equals(piece.substring(0,1).equals("w")?"b":"w")) {
                        moves.add(newRow+""+newCol);
                    }
                    newRow = row;
                }

                // Check right
                if (newCol != 7) {
                    newCol++;
                    while (board[newRow][newCol].equals("none")) {
                        moves.add(newRow+""+newCol);
                        if (newCol == 7) { break; }
                        newCol++;
                    }
                    if (board[newRow][newCol].substring(0,1).equals(piece.substring(0,1).equals("w")?"b":"w")) {
                        moves.add(newRow+""+newCol);
                    }
                    newCol = col;
                }

                // Check left
                if (newCol != 0) {
                    newCol--;
                    while (board[newRow][newCol].equals("none")) {
                        moves.add(newRow+""+newCol);
                        if (newCol == 0) { break; }
                        newCol--;
                    }
                    if (board[newRow][newCol].substring(0,1).equals(piece.substring(0,1).equals("w")?"b":"w")) {
                        moves.add(newRow+""+newCol);
                    }
                    newCol = col;
                }
                break;

            // Knight logic
            case "wn":
            case "bn":
                newRow = row + 2;
                newCol = col + 1;
                if (validPosition(newRow, newCol)) {
                    if (!board[newRow][newCol].substring(0,1).equals(piece.substring(0,1))) {
                        moves.add(newRow+""+newCol);
                    }
                }

                newRow = row + 2;
                newCol = col - 1;
                if (validPosition(newRow, newCol)) {
                    if (!board[newRow][newCol].substring(0,1).equals(piece.substring(0,1))) {
                        moves.add(newRow+""+newCol);
                    }
                }

                newRow = row + 1;
                newCol = col + 2;
                if (validPosition(newRow, newCol)) {
                    if (!board[newRow][newCol].substring(0,1).equals(piece.substring(0,1))) {
                        moves.add(newRow+""+newCol);
                    }
                }

                newRow = row + 1;
                newCol = col - 2;
                if (validPosition(newRow, newCol)) {
                    if (!board[newRow][newCol].substring(0,1).equals(piece.substring(0,1))) {
                        moves.add(newRow+""+newCol);
                    }
                }

                newRow = row - 1;
                newCol = col + 2;
                if (validPosition(newRow, newCol)) {
                    if (!board[newRow][newCol].substring(0,1).equals(piece.substring(0,1))) {
                        moves.add(newRow+""+newCol);
                    }
                }

                newRow = row - 1;
                newCol = col - 2;
                if (validPosition(newRow, newCol)) {
                    if (!board[newRow][newCol].substring(0,1).equals(piece.substring(0,1))) {
                        moves.add(newRow+""+newCol);
                    }
                }

                newRow = row - 2;
                newCol = col + 1;
                if (validPosition(newRow, newCol)) {
                    if (!board[newRow][newCol].substring(0,1).equals(piece.substring(0,1))) {
                        moves.add(newRow+""+newCol);
                    }
                }

                newRow = row - 2;
                newCol = col - 1;
                if (validPosition(newRow, newCol)) {
                    if (!board[newRow][newCol].substring(0,1).equals(piece.substring(0,1))) {
                        moves.add(newRow+""+newCol);
                    }
                }
                break;

            // Bishop logic
            case "wb":
            case "bb":
                // Check down-right
                if (newRow != 7 && newCol != 7) {
                    newRow++; newCol++;
                    while (board[newRow][newCol].equals("none")) {
                        moves.add(newRow+""+newCol);
                        if (newRow == 7 || newCol == 7) { break; }
                        newRow++; newCol++;
                    }
                    if (board[newRow][newCol].substring(0,1).equals(piece.substring(0,1).equals("w")?"b":"w")) {
                        moves.add(newRow+""+newCol);
                    }
                    newRow = row; newCol = col;
                }

                // Check up-right
                if (newRow != 0 && newCol != 7) {
                    newRow--; newCol++;
                    while (board[newRow][newCol].equals("none")) {
                        moves.add(newRow+""+newCol);
                        if (newRow == 0 || newCol == 7) { break; }
                        newRow--; newRow--; newCol++;
                    }
                    if (board[newRow][newCol].substring(0,1).equals(piece.substring(0,1).equals("w")?"b":"w")) {
                        moves.add(newRow+""+newCol);
                    }
                    newRow = row; newCol = col;
                }

                // Check down-left
                if (newRow != 7 && newCol != 0) {
                    newRow++; newCol--;
                    while (board[newRow][newCol].equals("none")) {
                        moves.add(newRow+""+newCol);
                        if (newRow == 7 || newCol == 0) { break; }
                        newRow++; newCol--;
                    }
                    if (board[newRow][newCol].substring(0,1).equals(piece.substring(0,1).equals("w")?"b":"w")) {
                        moves.add(newRow+""+newCol);
                    }
                    newRow = row; newCol = col;
                }

                // Check up-left
                if (newRow != 0 && newCol != 0) {
                    newRow--; newCol--;
                    while (board[newRow][newCol].equals("none")) {
                        moves.add(newRow+""+newCol);
                        if (newRow == 0 || newCol == 0) { break; }
                        newRow--; newCol--;
                    }
                    if (board[newRow][newCol].substring(0,1).equals(piece.substring(0,1).equals("w")?"b":"w")) {
                        moves.add(newRow+""+newCol);
                    }
                    newRow = row; newCol = col;
                }
                break;

            case "wq":
            case "bq":
                // ROOK LOGIC + BISHOP LOGIC
                // Check down-right
                if (newRow != 7 && newCol != 7) {
                    newRow++; newCol++;
                    while (board[newRow][newCol].equals("none")) {
                        moves.add(newRow+""+newCol);
                        if (newRow == 7 || newCol == 7) { break; }
                        newRow++; newCol++;
                    }
                    if (board[newRow][newCol].substring(0,1).equals(piece.substring(0,1).equals("w")?"b":"w")) {
                        moves.add(newRow+""+newCol);
                    }
                    newRow = row; newCol = col;
                }

                // Check up-right
                if (newRow != 0 && newCol != 7) {
                    newRow--; newCol++;
                    while (board[newRow][newCol].equals("none")) {
                        moves.add(newRow+""+newCol);
                        if (newRow == 0 || newCol == 7) { break; }
                        newRow--; newCol++;
                    }
                    if (board[newRow][newCol].substring(0,1).equals(piece.substring(0,1).equals("w")?"b":"w")) {
                        moves.add(newRow+""+newCol);
                    }
                    newRow = row; newCol = col;
                }

                // Check down-left
                if (newRow != 7 && newCol != 0) {
                    newRow++; newCol--;
                    while (board[newRow][newCol].equals("none")) {
                        moves.add(newRow+""+newCol);
                        if (newRow == 7 || newCol == 0) { break; }
                        newRow++; newCol--;
                    }
                    if (board[newRow][newCol].substring(0,1).equals(piece.substring(0,1).equals("w")?"b":"w")) {
                        moves.add(newRow+""+newCol);
                    }
                    newRow = row; newCol = col;
                }

                // Check up-left
                if (newRow != 0 && newCol != 0) {
                    newRow--; newCol--;
                    while (board[newRow][newCol].equals("none")) {
                        moves.add(newRow+""+newCol);
                        if (newRow == 0 || newCol == 0) { break; }
                        newRow--; newCol--;
                    }
                    if (board[newRow][newCol].substring(0,1).equals(piece.substring(0,1).equals("w")?"b":"w")) {
                        moves.add(newRow+""+newCol);
                    }
                    newRow = row; newCol = col;
                }

                // Check down
                if (newRow != 7) {
                    newRow++;
                    while (board[newRow][newCol].equals("none")) {
                        moves.add(newRow+""+newCol);
                        if (newRow == 7) { break; }
                        newRow++;
                    }
                    if (board[newRow][newCol].substring(0,1).equals(piece.substring(0,1).equals("w")?"b":"w")) {
                        moves.add(newRow+""+newCol);
                    }
                    newRow = row;
                }

                // Check up
                if (newRow != 0) {
                    newRow--;
                    while (board[newRow][newCol].equals("none")) {
                        moves.add(newRow+""+newCol);
                        if (newRow == 0) { break; }
                        newRow--;
                    }
                    if (board[newRow][newCol].substring(0,1).equals(piece.substring(0,1).equals("w")?"b":"w")) {
                        moves.add(newRow+""+newCol);
                    }
                    newRow = row;
                }

                // Check right
                if (newCol != 7) {
                    newCol++;
                    while (board[newRow][newCol].equals("none")) {
                        moves.add(newRow+""+newCol);
                        if (newCol == 7) { break; }
                        newCol++;
                    }
                    if (board[newRow][newCol].substring(0,1).equals(piece.substring(0,1).equals("w")?"b":"w")) {
                        moves.add(newRow+""+newCol);
                    }
                    newCol = col;
                }

                // Check left
                if (newCol != 0) {
                    newCol--;
                    while (board[newRow][newCol].equals("none")) {
                        moves.add(newRow+""+newCol);
                        if (newCol == 0) { break; }
                        newCol--;
                    }
                    if (board[newRow][newCol].substring(0,1).equals(piece.substring(0,1).equals("w")?"b":"w")) {
                        moves.add(newRow+""+newCol);
                    }
                    newCol = col;
                }
                break;

            // King logic
            case "wk":
            case "bk":
                newRow = row + 1;
                newCol = col - 1;
                if (validPosition(newRow, newCol)) {
                    if (!board[newRow][newCol].substring(0,1).equals(piece.substring(0,1))) {
                        moves.add(newRow+""+newCol);
                    }
                }

                newRow = row + 1;
                newCol = col;
                if (validPosition(newRow, newCol)) {
                    if (!board[newRow][newCol].substring(0,1).equals(piece.substring(0,1))) {
                        moves.add(newRow+""+newCol);
                    }
                }

                newRow = row + 1;
                newCol = col + 1;
                if (validPosition(newRow, newCol)) {
                    if (!board[newRow][newCol].substring(0,1).equals(piece.substring(0,1))) {
                        moves.add(newRow+""+newCol);
                    }
                }

                newRow = row;
                newCol = col + 1;
                if (validPosition(newRow, newCol)) {
                    if (!board[newRow][newCol].substring(0,1).equals(piece.substring(0,1))) {
                        moves.add(newRow+""+newCol);
                    }
                }

                newRow = row - 1;
                newCol = col + 1;
                if (validPosition(newRow, newCol)) {
                    if (!board[newRow][newCol].substring(0,1).equals(piece.substring(0,1))) {
                        moves.add(newRow+""+newCol);
                    }
                }

                newRow = row - 1;
                newCol = col;
                if (validPosition(newRow, newCol)) {
                    if (!board[newRow][newCol].substring(0,1).equals(piece.substring(0,1))) {
                        moves.add(newRow+""+newCol);
                    }
                }

                newRow = row - 1;
                newCol = col - 1;
                if (validPosition(newRow, newCol)) {
                    if (!board[newRow][newCol].substring(0,1).equals(piece.substring(0,1))) {
                        moves.add(newRow+""+newCol);
                    }
                }

                newRow = row;
                newCol = col - 1;
                if (validPosition(newRow, newCol)) {
                    if (!board[newRow][newCol].substring(0,1).equals(piece.substring(0,1))) {
                        moves.add(newRow+""+newCol);
                    }
                }
                break;

        }
        return moves.toArray(new String[moves.size()]);
    }


    private boolean validPosition(int row, int col) {
        if (row < 0 || row > 7) {
            return false;
        }
        return (col >= 0 && col <= 7);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                FirstChess chess = new FirstChess("Chess version 1.0");
                chess.setSize(1000, 1000);
                chess.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                chess.addComponents(chess.getContentPane());
                chess.pack();
                chess.setVisible(true);
            }
        });
    }
}

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

public class ZerothChess extends JFrame implements ActionListener {

    static String[][] board = new String[8][8];
    static JButton[][] buttons = new JButton[8][8];
    static ImageIcon wr, wn, wb, wq, wk, wp, br, bn, bb, bq, bk, bp, empty;
    static String[] gridLetters = {"A", "B", "C", "D", "E", "F", "G", "H"};


    // Logic variables
    static JButton position = null;
    static String selection = "none";
    static String turn = "w"; // "w"/"b"
    // static boolean checked = false;
    // static String checkingPiece = null;


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

    ZerothChess(String name) {
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
                int src_row = Integer.parseInt(str_position.substring(0,1));
                int src_col = Integer.parseInt(str_position.substring(1,2));
                int dsc_row = Integer.parseInt(command.substring(0,1));
                int dsc_col = Integer.parseInt(command.substring(1,2));
                board[src_row][src_col] = "none";
                board[dsc_row][dsc_col] = selection;
                refresh();
                resetTurn(true);
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

    private boolean checkMove(String selection, String src, String dsc) {

        String[] moves = generateMoves(selection, src);
        for (String possibleMove : moves) {
            if (dsc.equals(possibleMove)) {
                // check for blocks
                return true;
            }
        }
        return false;
    }

    private String[] generateMoves(String piece, String position) {
        // Position = 00 ... 77
        int row = Integer.parseInt(position.substring(0,1));
        int col = Integer.parseInt(position.substring(1,2));
        ArrayList<String> moves = new ArrayList<>();

        switch (piece) {
            case "wp":
                moves.add(Integer.toString(10*(row-1)+col));
                if (row == 6) {
                    moves.add(Integer.toString(10*(row-2)+col));
                }
                break;
            case "bp":
                moves.add(Integer.toString(10*(row+1)+col));
                if (row == 1) {
                    moves.add(Integer.toString(10*(row+2)+col));
                }
                break;
            case "wr":
            case "br":
                for (int i = 0; i < 8; i++) {
                    int cell = 10*i+col;
                    if (cell != 10*row+col) {
                        moves.add(Integer.toString(cell));
                    }
                    cell = 10*row+i;
                    if (cell != 10*row+col) {
                        moves.add(Integer.toString(cell));
                    }
                }
                break;
            case "wn":
            case "bn":
                break;
            default:
                break;
        }
        return moves.toArray(new String[moves.size()]);
    }

    /*
    private boolean validPosition(String position) {
        int row = Integer.parseInt(position.substring(0,1));
        int col = Integer.parseInt(position.substring(1,2));
        return validPosition(10*row+col);
    }
    private boolean validPosition(int position) {
        int row = position/10;
        int col = position%10;
        if (row < 0 || row > 7) {
            return false;
        }
        return (col >= 0 || col <= 7);
    }
    */


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ZerothChess chess = new ZerothChess("Chess version 0.01");
                chess.setSize(1000, 1000);
                chess.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                chess.addComponents(chess.getContentPane());
                chess.pack();
                chess.setVisible(true);
            }
        });
    }
}

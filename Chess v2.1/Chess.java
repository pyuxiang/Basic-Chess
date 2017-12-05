/**
 * Chess Java application
 *
 * A super big lesson... getClass() method doesn't work with comparing classes!
 * Pawn pawn = new Pawn("white", new Position(1,2));
 * ChessPiece cp = (ChessPiece) pawn;
 * (cp.getClass() == Pawn.class) returns false!
 * (cp.getClass() == ChessPiece.class) returns false!
 * (((Pawn)cp).getClass().equals(Pawn.class)) returns false!
 * (cp instanceof Pawn) returns true!
 * (cp instanceof ChessPiece) returns true!
 *
 * Improvement over the first iteration:
 *     - Stalemate implemented!
 *     - Promotion half-implemented (choice limited to only Queen for now)
 *     - Added a dirt cheap JLabel textbox in GUI
 * Areas of improvement:
 *     - Promotion (for other pieces, once the side panel comes in)
 *     - Log panel
 *     - New Game button
 *
 * @author Peh Yu Xiang
 * @version 2.1
 * @since 10 August 2017
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

import src.*;

public class Chess extends JFrame implements ActionListener {

    static boolean debugger = true; // DEBUGGING

    JButton[][] positions = new JButton[8][8];
    Board chess = new Board();
    ChessPiece selected = null;
    ChessPiece lastMoved = null;
    Position checkedPos = null;

    static ImageIcon wr, wn, wb, wq, wk, wp, br, bn, bb, bq, bk, bp, empty, ded, smug;
    static String[] gridLetters = {"A", "B", "C", "D", "E", "F", "G", "H"};
    JPanel options;
    JLabel optionsText;

    static {
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
        ded = new ImageIcon("img/ded.jpg");
        smug = new ImageIcon("img/smug.jpg");
    }

    Chess(String name) {
        super(name);
    }

    private void addComponents(Container pane) {
        JPanel game = new JPanel();
        game.setLayout(new GridLayout(8,8));

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton cell = new JButton();
                cell.addActionListener(this);
                cell.setActionCommand(i + "" + j);
                setNormal(cell);
                positions[i][j] = cell;
                game.add(cell);
            }
        }
        pane.add(game, BorderLayout.NORTH);

        options = new JPanel();
        options.setPreferredSize(new Dimension(800,30));
        options.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        optionsText = new JLabel("Welcome to Chess version 2.1");
        options.add(optionsText);
        pane.add(options, BorderLayout.SOUTH);

        refresh();
    }

    private void refresh() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton cell = positions[i][j];
                ImageIcon ref = null;
                String hash = "none";
                for (ChessPiece cp : chess.pieces) {
                    if (cp.getPos().equals(parsePos(cell))) {
                        hash = cp.getSide() + cp.getType();
                        break;
                    }
                }
                switch (hash) {
                    case "none": ref = empty; break;
                    case "whiterook": ref = wr; break;
                    case "whiteknight": ref = wn; break;
                    case "whitebishop": ref = wb; break;
                    case "whitequeen": ref = wq; break;
                    case "whiteking": ref = wk; break;
                    case "whitepawn": ref = wp; break;
                    case "blackrook": ref = br; break;
                    case "blackknight": ref = bn; break;
                    case "blackbishop": ref = bb; break;
                    case "blackqueen": ref = bq; break;
                    case "blackking": ref = bk; break;
                    case "blackpawn": ref = bp; break;
                }
                cell.setIcon(ref);
            }
        }
    }

    private void stopGame() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton cell = positions[i][j];
                cell.removeActionListener(this);
            }
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (debugger) { System.out.printf(">>> ActionEvent %s initiated...\n", ae.getActionCommand()); }
        Position position = parsePos(ae.getActionCommand());
        ChessPiece cp = chess.getPiece(position);
        if (selected == null) {
            if (cp != null) {
                if (chess.isValidPiece(cp)) {
                    selected = cp;
                    setSelection(cp);
                }
            }
        } else {
            // Aesthetics
            setNormal(selected);
            if (checkedPos != null) {
                if (checkedPos.equals(selected.getPos())) {
                    setChecked(checkedPos);
                }
            }
            if (cp != null) {
                if (cp.equals(selected)) {
                    selected = null;
                    reviewText(); // DIRT CHEAP
                    return;
                } else if (chess.isValidPiece(cp)) {
                    selected = cp;
                    setSelection(cp);
                    reviewText(); // DIRT CHEAP
                    return;
                }
            }

            if (debugger) { System.out.printf("\n>>> CHESS: isValidMove()\n"); }
            if (chess.isValidMove(selected, position)) {
                if (debugger) { System.out.printf("\n>>> CHESS: movePiece()\n"); }
                selected = chess.movePiece(selected, position); // account for piece change, i.e. promotion

                if (lastMoved != null) {
                    setNormal(lastMoved);
                }
                setMoved(selected);
                lastMoved = selected;
                selected = null;

                if (checkedPos != null) {
                    setNormal(checkedPos);
                }
                if (debugger) { System.out.printf("\n>>> CHESS: isChecked()\n"); }
                boolean checkmated = false;
                boolean stalemated = false;
                if (chess.isChecked()) {
                    // Checked ...
                    checkedPos = chess.getKing().getPos();
                    setChecked(checkedPos);
                    if (debugger) { System.out.printf("\n>>> CHESS: hasValidMoves()\n"); }
                    if (!chess.hasValidMoves()) {
                        checkmated = true; // Checked ... and Checkmate
                    }                      // Checked ... but otherwise as per normal
                } else {
                    // Not checked ...
                    checkedPos = null;
                    if (debugger) { System.out.printf("\n>>> CHESS: hasValidMoves()\n"); }
                    if (!chess.hasValidMoves()) {
                        stalemated = true; // Not checked ... and Stalemate
                    }                      // Not checked ... and as per normal
                }

                // Modify image elements
                refresh();
                if (checkmated) {
                    parseJButton(checkedPos).setIcon(ded);
                    stopGame();
                } else if (stalemated) {
                    JButton kingButton = parseJButton(chess.getKing());
                    kingButton.setIcon(smug);
                    setChecked(kingButton);
                    stopGame();
                }

            } else {
                selected = null;
                if (checkedPos != null) {
                    setChecked(checkedPos);
                }
            }
        }

        reviewText(); // DIRT CHEAP

    }


    // DIRT CHEAP SELECTION TEXT MODIFIER
    private void reviewText() {
        // Modify selection text
        if (selected == null) {
            optionsText.setText("Nothing selected...");
        } else {
            optionsText.setText(selected + " selected!");
        }
    }


    // Reference to Position
    private Position parsePos(String position) {
        int row = Integer.parseInt(position.substring(0,1));
        int col = Integer.parseInt(position.substring(1,2));
        return new Position(row, col);
    }
    private Position parsePos(JButton cell) {
        return parsePos(cell.getActionCommand());
    }


    // Reference to JButton
    private JButton parseJButton(ChessPiece select) {
        Position p = select.getPos();
        return parseJButton(p);
    }
    private JButton parseJButton(Position p) {
        return positions[p.getX()][p.getY()];
    }


    // Set borders
    private void setNormal(Position pos) {
        setNormal(parseJButton(pos));
    }
    private void setNormal(JButton cell) {
        cell.setBorder(BorderFactory.createLineBorder(Color.black, 2));
    }
    private void setNormal(ChessPiece select) {
        JButton cell = parseJButton(select);
        setNormal(cell);
    }

    private void setChecked(Position pos) {
        setChecked(parseJButton(pos));
    }
    private void setChecked(JButton cell) {
        cell.setBorder(BorderFactory.createLineBorder(Color.red, 2));
    }
    private void setChecked(ChessPiece select) {
        JButton cell = parseJButton(select);
        setChecked(cell);
    }

    private void setSelection(ChessPiece select) {
        JButton cell = parseJButton(select);
        cell.setBorder(BorderFactory.createLineBorder(Color.blue, 2));
    }

    private void setMoved(ChessPiece select) {
        JButton cell = parseJButton(select);
        cell.setBorder(BorderFactory.createLineBorder(Color.orange, 2));
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Chess chessFrm = new Chess("Chess version 2.1");
                chessFrm.setResizable(false);
                chessFrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                chessFrm.addComponents(chessFrm.getContentPane());
                chessFrm.pack();
                chessFrm.setVisible(true);
            }
        });
    }

}

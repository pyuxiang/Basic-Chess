/**
 * Chess Java application
 * The first bug-free (hopefully) chess program!
 * Of course, there is room for improvement, i.e.
 *     - Promotion
 *     - Stalemate
 *     - Log panel
 *     - New Game button
 *
 * @author Peh Yu Xiang
 * @version 2.0
 * @since 10 August 2017
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

import src.*;

public class SecondChess extends JFrame implements ActionListener {

    static boolean debugger = false; // DEBUGGING

    JButton[][] positions = new JButton[8][8];
    Board chess = new Board();

    ChessPiece selected = null;
    ChessPiece lastMoved = null;
    Position checkedPos = null;

    static ImageIcon wr, wn, wb, wq, wk, wp, br, bn, bb, bq, bk, bp, empty, ded;
    static String[] gridLetters = {"A", "B", "C", "D", "E", "F", "G", "H"};

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
    }

    SecondChess(String name) {
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
        pane.add(game);
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
                    setChecked(parseJButton(checkedPos));
                }
            }
            if (cp != null) {
                if (cp.equals(selected)) {
                    selected = null;
                    return;
                } else if (chess.isValidPiece(cp)) {
                    selected = cp;
                    setSelection(cp);
                    return;
                }
            }

            if (debugger) { System.out.printf("\n>>> CHESS: isValidMove()\n"); }
            if (chess.isValidMove(selected, position)) {
                if (debugger) { System.out.printf("\n>>> CHESS: movePiece()\n"); }
                chess.movePiece(selected, position);

                if (lastMoved != null) {
                    setNormal(lastMoved);
                }
                setMoved(selected);
                lastMoved = selected;
                selected = null;

                if (checkedPos != null) {
                    setNormal(parseJButton(checkedPos));
                }
                if (debugger) { System.out.printf("\n>>> CHESS: isChecked()\n"); }
                if (chess.isChecked()) {
                    checkedPos = chess.getKing().getPos();
                    setChecked(parseJButton(checkedPos));
                } else {
                    checkedPos = null;
                }

                // TODO: Implement stalemate

                refresh();

                // Win-loss
                if (debugger) { System.out.printf("\n>>> CHESS: isCheckmated()\n"); }
                if (chess.isCheckmated()) {
                    parseJButton(checkedPos).setIcon(ded);
                    stopGame();
                }
            } else {
                selected = null;
                if (checkedPos != null) {
                    setChecked(parseJButton(checkedPos));
                }
            }
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
    private void setNormal(JButton cell) {
        cell.setBorder(BorderFactory.createLineBorder(Color.black, 2));
    }
    private void setNormal(ChessPiece select) {
        JButton cell = parseJButton(select);
        setNormal(cell);
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
                SecondChess chessFrm = new SecondChess("Chess version 2.0");
                chessFrm.setResizable(false);
                chessFrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                chessFrm.addComponents(chessFrm.getContentPane());
                chessFrm.pack();
                chessFrm.setVisible(true);
            }
        });
    }

}

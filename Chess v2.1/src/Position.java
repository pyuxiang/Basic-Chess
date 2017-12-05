package src;

public class Position implements Cloneable {

    // Provides generality for board size
    private static int leftBound = 0;
    private static int rightBound = 7;
    private static int topBound = 0;
    private static int bottomBound = 7;

    public static void setBounds(int left, int right, int up, int down) {
        leftBound = left;
        rightBound = right;
        topBound = up;
        bottomBound = down;
    }

    public static int getLeftBound() { return leftBound; }
    public static int getRightBound() { return rightBound; }
    public static int getTopBound() { return topBound; }
    public static int getBottomBound() { return bottomBound; }

    private int xPos; // row number
    private int yPos; // col number

    public Position(int x, int y) {
        xPos = x;
        yPos = y;
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public void setX(int x) {
        xPos = x;
    }

    public void setY(int y) {
        yPos = y;
    }

    public boolean isValid() {
        if (xPos < topBound || xPos > bottomBound) {
            return false;
        } else if (yPos < leftBound || yPos > rightBound) {
            return false;
        }
        return true;
    }

    // Cloneable: For searching (note that new object created)
    public Position clone() { return new Position(xPos, yPos); }
    public Position getUp() { return new Position(xPos-1, yPos); }
    public Position getDown() { return new Position(xPos+1, yPos); }
    public Position getLeft() { return new Position(xPos, yPos-1); }
    public Position getRight() { return new Position(xPos, yPos+1); }

    @Override public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        }

        Position other = (Position) obj;
        return other.getX() == this.getX()
            && other.getY() == this.getY();
    }

    @Override public String toString() {
        return "P(" + xPos + "," + yPos + ")";
    }
}

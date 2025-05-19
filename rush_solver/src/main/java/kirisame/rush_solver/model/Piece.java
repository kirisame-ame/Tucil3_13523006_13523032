package kirisame.rush_solver.model;

public class Piece {
    protected char id;
    protected int length;
    protected int axis;
    protected int row;
    protected int col;

    /**
     * @param length length of the piece
     * @param axis   0 for horizontal, 1 for vertical
     * @param row    row position of the piece, Top-Left as reference
     * @param col    column position of the piece, Top-Left as reference
     */
    public Piece(char id, int length, int axis, int row, int col) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be a positive integer.");
        }
        if (row < 0 || col < 0) {
            throw new IllegalArgumentException("X and Y coordinates must be non-negative integers.");
        }
        this.id = id;
        this.length = length;
        this.axis = axis;
        this.row = row;
        this.col = col;
    }

    public char getId() {
        return id;
    }

    public int getLength() {
        return length;
    }

    public int getAxis() {
        return axis;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void printInfo() {
        System.out.println("Id: " + id);
        System.out.println("    Length: " + length);
        System.out.println("    Axis: " + axis);
        System.out.println("    Location: (" + row + "," + col + ")");
    }

}

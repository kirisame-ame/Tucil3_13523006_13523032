package kirisame.rush_solver.model;

public class Piece {
    protected int id;
    protected int height;
    protected int width;
    protected int x;
    protected int y;
    public Piece(int id, int height, int width, int x, int y) {
        if (height <= 0 || width <= 0) {
            throw new IllegalArgumentException("Height and width must be positive integers.");
        }
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("X and Y coordinates must be non-negative integers.");
        }
        this.id = id;
        this.height = height;
        this.width = width;
    }
    public int getId() {
        return id;
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}

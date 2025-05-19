package kirisame.rush_solver.model;

public class PrimaryPiece extends Piece{
    
    /**
     * Constructs a PrimaryPiece object with the specified height, width, and position.
     * The piece is initialized with an ID of 0.
     *
     * @param height the height of the primary piece
     * @param width the width of the primary piece
     * @param x the x-coordinate of the primary piece's position
     * @param y the y-coordinate of the primary piece's position
     */
    public PrimaryPiece(int height, int width, int x, int y) {
        super('P', height, width, x, y);
    }
}

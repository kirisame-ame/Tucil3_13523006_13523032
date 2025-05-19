package kirisame.rush_solver.model;

public class PrimaryPiece extends Piece{
    
    /**
     * Constructs a PrimaryPiece object with the specified length and position.
     * The piece is initialized with an ID of 0.
    */
    public PrimaryPiece(int length, int axis, int row, int col) {
        super('P', length, axis, row, col);
    }
}

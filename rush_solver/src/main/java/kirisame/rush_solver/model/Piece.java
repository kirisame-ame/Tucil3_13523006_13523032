package kirisame.rush_solver.model;

public class Piece {
    protected char id;
    protected int length;
    protected int axis;
    protected int row;
    protected int col;

    /**
     * @param length length of the piece
     * @param axis 0 for horizontal, 1 for vertical
     * @param row row position of the piece, Top-Left as reference
     * @param col column position of the piece, Top-Left as reference
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
    public int getId() {
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
    /**
     * Moves the piece in the specified direction.
     * @param distance positive goes Right and Up, negative goes Left and Down
     */
    public void move(int distance){
        boolean positive = true;
        char[][] board = Board.getInstance().getBoard();
        if(distance <0){
            positive = false;
            distance = -distance;
        }
        if(this.axis==0){
            for (int i = 0; i < distance; i++) {
                if(positive){
                    if(board[this.row][this.col+i]=='K' && this instanceof PrimaryPiece){
                        // TODO: Implement win
                    }else if(!(board[this.row][this.col+i] == '.')){
                        throw new IllegalArgumentException("Piece hits something at (" + this.row + "," + (this.col+i) + ")");
                    }else{
                        board[this.row][this.col+i-1] = '.';
                        board[this.row][this.col+i] = this.id;
                    }
                }
            }
            // Move the piece if no collision
            if(positive){
                this.col += distance;
            }else{
                this.col -= distance;
            }
            Board.getInstance().setBoard(board);
        }else if (this.axis==1){
            for (int i = 0; i < distance; i++) {
                if(positive){
                    if(board[this.row+i][this.col]=='K' && this instanceof PrimaryPiece){
                        // TODO: Implement win
                    }else if(!(board[this.row+i][this.col] == '.')){
                        throw new IllegalArgumentException("Piece hits something at (" + (this.row+i) + "," + this.col + ")");
                    }else{
                        board[this.row+i-1][this.col] = '.';
                        board[this.row+i][this.col] = this.id;
                    }
                }
            }
            // Move the piece if no collision
            if(positive){
                this.row += distance;
            }else{
                this.row -= distance;
            }
            Board.getInstance().setBoard(board);
        }
    }
}

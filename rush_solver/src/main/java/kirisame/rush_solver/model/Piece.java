package kirisame.rush_solver.model;

public class Piece {
    protected char id;
    protected int height;
    protected int width;
    protected int row;
    protected int col;

    /**
     *  Note: Piece row and col are set on the top-left corner of the piece.
     *  The row and col are the coordinates of the piece on the board.
     */
    public Piece(char id, int height, int width, int row, int col) {
        if (height <= 0 || width <= 0) {
            throw new IllegalArgumentException("Height and width must be positive integers.");
        }
        if (row < 0 || col < 0) {
            throw new IllegalArgumentException("X and Y coordinates must be non-negative integers.");
        }
        this.id = id;
        this.height = height;
        this.width = width;
        this.row = row;
        this.col = col;
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
        return row;
    }
    public int getY() {
        return col;
    }
    /**
     * Moves the piece in the specified direction.
     * @param axis 0 for row, 1 for column
     * @param distance positive goes Right and Up, negative goes Left and Down
     */
    public void move(int axis, int distance){
        boolean positive = true;
        char[][] board = Board.getInstance().getBoard();
        if(distance <0){
            positive = false;
            distance = -distance;
        }
        if(axis==0){
            for (int i = 0; i < distance; i++) {
                if(positive){
                    if(board[this.row][this.col+i]=='K'){
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
        }else if (axis==1){
            for (int i = 0; i < distance; i++) {
                if(positive){
                    if(board[this.row+i][this.col]=='K'){
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

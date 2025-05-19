package kirisame.rush_solver.model;

import java.util.ArrayList;
import java.util.HashSet;

public class Board {
    // Singleton Pattern
    private int height;
    private int width;
    private char[][] board;
    private int normalPieceCount;
    private int[] endGoal = new int[2];
    private ArrayList<Piece> pieces = new ArrayList<>();
    private static final Board instance = new Board();
    private Board() {
        this.height = 0;
        this.width = 0;
        this.board = new char[0][0];
        this.normalPieceCount = 0;
        this.endGoal[0] = 0;
        this.endGoal[1] = 0;
        this.pieces = new ArrayList<>();
    }
    /**
     * Returns the singleton instance of the {@code Board} class.
     *
     * @return the single {@code Board} instance
     */
    public static Board getInstance() {
        return instance;
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
    /**
     * 
     * @return a clone of the board
     */
    public char[][] getBoard() {
        return board.clone();
    }
    public void setBoard(char[][] board){
        this.board = board.clone();
    }
    /**
     * Care to account for the border.
     */
    public void setBoardAt(int row, int col, char value) {
        if (row < 0 || col < 0 || row >= height || col >= width) {
            throw new IndexOutOfBoundsException("Coordinates are out of bounds.");
        }
        this.board[row][col] = value;
    }
    public int getPieceCount() {
        return normalPieceCount;
    }
    public int[] getEndGoal() {
        return endGoal;
    }
    public ArrayList<Piece> getPieces() {
        return pieces;
    }
    public HashSet<Character> getPieceIds(){
        HashSet<Character> pieceIds = new HashSet<>();
        for (Piece p : pieces){
            pieceIds.add(p.id);
        }
        return pieceIds;
    }
    /**
     * Sets the size of the board.
     * Size is padded by 1 to account for the border. 
    */
    public void setSize(int height, int width) {
        if (height <= 0 || width <= 0) {
            throw new IllegalArgumentException("Height and width must be positive integers.");
        }
        this.width = width+2;
        this.height = height+2;
        this.board = new char[height+2][width+2];
        // Initialize the board with walls
        for (int i = 0; i < height+2; i++) {
            for (int j = 0; j < width+2; j++) {
                this.board[i][j] = '壁';
            }
        }
    }
    public void setPieceCount(int normalPieceCount) {
        if (normalPieceCount <= 0) {
            throw new IllegalArgumentException("Piece count must be a positive integer.");
        }
        this.normalPieceCount = normalPieceCount;
    }
    public void setEndGoal(int row, int col) {
        if (row < 0 || col < 0) {
            throw new IllegalArgumentException("X and Y coordinates must be non-negative integers.");
        }
        this.endGoal[0] = row;
        this.endGoal[1] = col;
    }
    public String boardToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sb.append(board[i][j]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    public void parsePieces(){
        HashSet<Character> pieceIds = new HashSet<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                char value = board[i][j];
                if (value != '壁' && value != ' ' && value != '.') {
                    if (!pieceIds.contains(value)) {
                        pieceIds.add(value);
                        int length = 0;
                        int axis;
                        // Check horizontal
                        for (int k = j; k < width && board[i][k] == value; k++) {
                            length++;
                        }
                        if (length > 1) {
                            axis = 0;
                            if(value=='P'){
                                pieces.add(new PrimaryPiece(length, axis, i, j));
                            }else{
                                pieces.add(new Piece(value, length, axis, i, j));
                            }
                        }
                        // Check vertical
                        length = 0;
                        for (int k = i; k < height && board[k][j] == value; k++) {
                            length++;
                        }
                        if (length > 1) {
                            axis = 1;
                            if(value=='P'){
                                pieces.add(new PrimaryPiece(length, axis, i, j));
                            }else{
                                pieces.add(new Piece(value, length, axis, i, j));
                            }
                        }
                    }
                }
            }
        }
    }
}

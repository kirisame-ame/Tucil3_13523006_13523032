package kirisame.rush_solver.model;

import java.util.HashMap;
import java.util.HashSet;

public class Board {
    private int height;
    private int width;
    private char[][] board;
    private int normalPieceCount;
    private int[] endGoal = new int[2];
    private HashMap<Character, Piece> pieces = new HashMap<>();

    public Board() {
        this.height = 0;
        this.width = 0;
        this.board = new char[0][0];
        this.normalPieceCount = 0;
        this.endGoal[0] = 0;
        this.endGoal[1] = 0;
        this.pieces = new HashMap<>();
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
        char[][] copy = new char[this.board.length][];
        for (int i = 0; i < this.board.length; i++) {
            copy[i] = this.board[i].clone();
        }
        return copy;
    }

    public void setBoard(char[][] board) {
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

    public HashMap<Character, Piece> getPieces() {
        return pieces;
    }

    public HashSet<Character> getPieceIds() {
        HashSet<Character> pieceIds = new HashSet<>();
        for (Piece p : pieces.values()) {
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
        this.width = width + 2;
        this.height = height + 2;
        this.board = new char[height + 2][width + 2];
        // Initialize the board with walls
        for (int i = 0; i < height + 2; i++) {
            for (int j = 0; j < width + 2; j++) {
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

    public void printPieces() {
        for (Piece p : pieces.values()) {
            p.printInfo();
        }
    }

    public void parsePieces() {
        pieces.clear();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                char value = board[i][j];
                if (value != '壁' && value != ' ' && value != '.') {
                    if(value == 'K') {
                        this.endGoal[0] = i;
                        this.endGoal[1] = j;
                        continue;
                    }
                    if (!pieces.containsKey(value)) {
                        int length = 0;
                        int axis;
                        // Check horizontal
                        for (int k = j; k < width && board[i][k] == value; k++) {
                            length++;
                        }
                        if (length > 1) {
                            axis = 0;
                            if (value == 'P') {
                                pieces.put(value, new PrimaryPiece(length, axis, i, j));
                            } else {
                                pieces.put(value, new Piece(value, length, axis, i, j));
                            }
                        }
                        // Check vertical
                        length = 0;
                        for (int k = i; k < height && board[k][j] == value; k++) {
                            length++;
                        }
                        if (length > 1) {
                            axis = 1;
                            if (value == 'P') {
                                pieces.put(value, new PrimaryPiece(length, axis, i, j));
                            } else {
                                pieces.put(value, new Piece(value, length, axis, i, j));
                            }
                        }
                    }
                }
            }
        }
    }

    public Board deepCopy() {
        Board copy = new Board();

        // Copy dimensions
        copy.setSize(this.getHeight() - 2, this.getWidth() - 2); // Account for padding

        // Copy board (deep)
        char[][] boardCopy = this.getBoard(); // Now does deep copy after update
        copy.setBoard(boardCopy);

        // Copy normal piece count
        copy.setPieceCount(this.getPieceCount());

        // Copy end goal
        int[] goal = this.getEndGoal();
        copy.setEndGoal(goal[0], goal[1]);

        // Copy pieces (deep)
        for (Piece p : this.getPieces().values()) {
            Piece copyPiece;
            if (p instanceof PrimaryPiece) {
                copyPiece = new PrimaryPiece(p.getLength(), p.getAxis(), p.getRow(), p.getCol());
            } else {
                copyPiece = new Piece(p.getId(), p.getLength(), p.getAxis(), p.getRow(), p.getCol());
            }
            copy.getPieces().put(copyPiece.id, copyPiece);
        }

        return copy;
    }

    public boolean equals(Board other) {

        if (other.getHeight() == 0 || other.getWidth() == 0) {
            return false;
        }

        // Compare board dimensions
        if (this.height != other.getHeight() || this.width != other.getWidth()) {
            return false;
        }

        // Compare board contents

        char[][] otherBoard = other.getBoard();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (this.board[i][j] != otherBoard[i][j]) {
                    return false;
                }
            }
        }

        // compare normal piece count
        if (this.normalPieceCount != other.getPieceCount()) {
            return false;
        }

        // Compare end goal
        int[] otherEndGoal = other.getEndGoal();
        return (!(this.endGoal[0] != otherEndGoal[0] || this.endGoal[1] != otherEndGoal[1]));
    }

    /**
     * Moves the piece in the specified direction.
     * 
     * @param p        the piece moved
     * @param distance positive goes Right and Up, negative goes Left and Down
     */
    public void move(Piece p, int distance) {
        boolean positive = true;
        char[][] tempBoard = this.getBoard();
        if (distance < 0) {
            positive = false;
            distance = -distance;
        }
        if (p.axis == 0) {
            for (int i = 1; i < distance; i++) {
                if (positive) {
                    if (tempBoard[p.row][p.col+p.length - 1 + i] == 'K' && p instanceof PrimaryPiece) {
                        // TODO: Implement win
                    } else if (!(tempBoard[p.row][p.col+p.length-1 + i] == '.')) {
                        throw new IllegalArgumentException(
                                "Piece hits something at (" + p.row + "," + (p.col+p.length-1 + i) + ")");
                    } else {
                        tempBoard[p.row][p.col + i - 1] = '.';
                        tempBoard[p.row][p.col+p.length-1 + i] = p.id;
                    }
                }else{
                    if (tempBoard[p.row][p.col - i] == 'K' && p instanceof PrimaryPiece) {
                        //TODO: Implement win
                    } else if (!(tempBoard[p.row][p.col - i] == '.')) {
                        throw new IllegalArgumentException(
                                "Piece hits something at (" + p.row + "," + (p.col - i) + ")");
                    } else {
                        tempBoard[p.row][p.col - i+ p.length] = '.';
                        tempBoard[p.row][p.col - i] = p.id;
                    }
                }
            }
            // Move the piece if no collision
            p.col += distance;
            if (p.col < 0 || p.col >= this.width) {
                throw new IndexOutOfBoundsException("Piece moved out of bounds.");
            }
            this.setBoard(tempBoard);
        } else if (p.axis == 1) {
            for (int i = 1; i < distance; i++) {
                if (positive) {
                    if (tempBoard[p.row- i][p.col] == 'K' && p instanceof PrimaryPiece) {
                        // TODO: Implement win
                    } else if (!(tempBoard[p.row - i][p.col] == '.')) {
                        throw new IllegalArgumentException(
                                "Piece hits something at (" + (p.row - i) + "," + p.col + ")");
                    } else {
                        tempBoard[p.row - i + p.length][p.col] = '.';
                        tempBoard[p.row - i][p.col] = p.id;
                    }
                }else{
                    if (tempBoard[p.row + p.length-1+ i][p.col] == 'K' && p instanceof PrimaryPiece) {
                        //TODO: Implement win
                    }
                    else if (!(tempBoard[p.row + p.length-1+ i][p.col] == '.')) {
                        throw new IllegalArgumentException(
                                "Piece hits something at (" + (p.row + p.length-1+ i) + "," + p.col + ")");
                    } else {
                        tempBoard[p.row + i - 1][p.col] = '.';
                        tempBoard[p.row + p.length-1+ i][p.col] = p.id;
                    }
                }
            }
            // Move the piece if no collision
            p.row += distance;
            if (p.row < 0 || p.row >= this.height) {
                throw new IndexOutOfBoundsException("Piece moved out of bounds.");
            }
            this.setBoard(tempBoard);
        }
    }
}

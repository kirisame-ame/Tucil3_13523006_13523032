package kirisame.rush_solver.model;

import java.util.ArrayList;
import java.util.HashSet;

public class Board {
    private int height;
    private int width;
    private char[][] board;
    private int normalPieceCount;
    private int[] endGoal = new int[2];
    private ArrayList<Piece> pieces = new ArrayList<>();

    public Board() {
        this.height = 0;
        this.width = 0;
        this.board = new char[0][0];
        this.normalPieceCount = 0;
        this.endGoal[0] = 0;
        this.endGoal[1] = 0;
        this.pieces = new ArrayList<>();
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

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public HashSet<Character> getPieceIds() {
        HashSet<Character> pieceIds = new HashSet<>();
        for (Piece p : pieces) {
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
        for (Piece p : pieces) {
            p.printInfo();
        }
    }

    public void parsePieces() {
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
                            if (value == 'P') {
                                pieces.add(new PrimaryPiece(length, axis, i, j));
                            } else {
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
                            if (value == 'P') {
                                pieces.add(new PrimaryPiece(length, axis, i, j));
                            } else {
                                pieces.add(new Piece(value, length, axis, i, j));
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
        for (Piece p : this.getPieces()) {
            Piece copyPiece;
            if (p instanceof PrimaryPiece) {
                copyPiece = new PrimaryPiece(p.getLength(), p.getAxis(), p.getRow(), p.getCol());
            } else {
                copyPiece = new Piece(p.getId(), p.getLength(), p.getAxis(), p.getRow(), p.getCol());
            }
            copy.getPieces().add(copyPiece);
        }

        return copy;
    }

    /**
     * Moves the piece in the specified direction.
     * 
     * @param p        the piece moved
     * @param distance positive goes Right and Up, negative goes Left and Down
     */
    public void move(Piece p, int distance) {
        boolean positive = true;
        char[][] board = this.getBoard();
        if (distance < 0) {
            positive = false;
            distance = -distance;
        }
        if (p.axis == 0) {
            for (int i = 0; i < distance; i++) {
                if (positive) {
                    if (board[p.row][p.col + i] == 'K' && p instanceof PrimaryPiece) {
                        // TODO: Implement win
                    } else if (!(board[p.row][p.col + i] == '.')) {
                        throw new IllegalArgumentException(
                                "Piece hits something at (" + p.row + "," + (p.col + i) + ")");
                    } else {
                        board[p.row][p.col + i - 1] = '.';
                        board[p.row][p.col + i] = p.id;
                    }
                }
            }
            // Move the piece if no collision
            if (positive) {
                p.col += distance;
            } else {
                p.col -= distance;
            }
            this.setBoard(board);
        } else if (p.axis == 1) {
            for (int i = 0; i < distance; i++) {
                if (positive) {
                    if (board[p.row + i][p.col] == 'K' && p instanceof PrimaryPiece) {
                        // TODO: Implement win
                    } else if (!(board[p.row + i][p.col] == '.')) {
                        throw new IllegalArgumentException(
                                "Piece hits something at (" + (p.row + i) + "," + p.col + ")");
                    } else {
                        board[p.row + i - 1][p.col] = '.';
                        board[p.row + i][p.col] = p.id;
                    }
                }
            }
            // Move the piece if no collision
            if (positive) {
                p.row += distance;
            } else {
                p.row -= distance;
            }
            this.setBoard(board);
        }
    }
}

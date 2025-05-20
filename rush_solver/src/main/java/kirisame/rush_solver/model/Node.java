package kirisame.rush_solver.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Node {

    private Board board;
    private Node parent;
    private int depth;
    String heuristic;
    private int heuristicValue;
    private char movedPiece;
    private int moveDistance;

    public Node(Board board, Node parent, int depth, String heuristic, char movedPiece, int moveDistance) {
        this.board = board.deepCopy();
        this.parent = parent;
        this.depth = depth;
        this.heuristic = heuristic;
        calculateCost();
        this.movedPiece = movedPiece;
        this.moveDistance = moveDistance;
    }

    public Node(Board board, Node parent, int depth, String heuristic) {
        this.board = board.deepCopy();
        this.parent = parent;
        this.depth = depth;
        this.heuristic = heuristic;
        calculateCost();
        this.movedPiece = '-';
        this.moveDistance = 0;
    }

    public Board getBoard() {
        return board;
    }

    public Node getParent() {
        return parent;
    }

    public int getDepth() {
        return depth;
    }

    public int getHeuristicValue() {
        return heuristicValue;
    }

    public String getHeuristic() {
        return heuristic;
    }
    public void setHeuristic(String heuristic) {
        this.heuristic = heuristic;
    }

    public char getMovedPiece() {
        return movedPiece;
    }

    public int getMoveDistance() {
        return moveDistance;
    }

    /**
     * Expands the current node by checking all possible moves for each piece
     * 
     * @return a list of nodes that are the result of moving the pieces
     */
    public Node[] expand() {
        ArrayList<Node> nodes = new ArrayList<>();
        HashMap<Character, Piece> pieces = this.board.deepCopy().getPieces();

        for (Piece piece : pieces.values()) {

            // Try moving in positive direction
            for (int dist = 1;; dist++) {
                try {
                    Board newBoard = this.board.deepCopy();
                    Piece copiedPiece = newBoard.getPieces().get(piece.getId());
                    newBoard.move(copiedPiece, dist);
                    nodes.add(new Node(newBoard, this, this.depth + 1, this.heuristic, piece.getId(), dist));
                } catch (Exception e) {
                    break; // Stop trying further in this direction
                }
            }

            // Try moving in negative direction
            for (int dist = -1;; dist--) {
                try {
                    Board newBoard = this.board.deepCopy();
                    Piece copiedPiece = newBoard.getPieces().get(piece.getId());
                    newBoard.move(copiedPiece, dist);
                    nodes.add(new Node(newBoard, this, this.depth + 1, this.heuristic, piece.getId(), dist));
                } catch (Exception e) {
                    break;
                }
            }
        }

        return nodes.toArray(Node[]::new);
    }

    // Heuristics
    private void calculateCost() {
        switch (this.heuristic.toLowerCase()) {
            case "blocking" -> {
                this.heuristicValue = this.blockingPieces();
            }
            default -> throw new IllegalArgumentException("Invalid heuristic: " + heuristic);
            case "none" -> {
                this.heuristicValue = 0;
            }
            case "ucs" -> {
                this.heuristicValue = 0;
            }
            case "distance" -> {
                this.heuristicValue = this.distanceToGoal();
            }
            case "blocking_distance" -> {
                this.heuristicValue = this.distanceToGoal() + this.blockingPieces();
            }
        }
    }

    /**
     * Counts the number of pieces that are blocking the primary piece
     * 
     * @param node the node to check
     * @return the number of blocking pieces
     */
    public int blockingPieces() {
        int count = 0;
        int axis = this.board.getPieces().get('P').getAxis();
        int row = this.board.getPieces().get('P').getRow();
        int col = this.board.getPieces().get('P').getCol();
        int pieceLength = this.board.getPieces().get('P').getLength();
        HashSet<Character> visitedPieces = new HashSet<>();

        // For horizontal primary piece
        if (axis == 0) {
            // Get rightmost position of the piece
            int startCol = col + pieceLength - 1;

            if (this.board.getEndGoal()[1] < startCol) {
                // Need to move left
                for (int i = startCol; i >= this.board.getEndGoal()[1]; i--) {
                    char value = this.board.getBoard()[row][i];
                    if (value == 'K') {
                        break;
                    }
                    if (value >= 'A' && value <= 'Z' && value != 'P') {
                        if (!visitedPieces.contains(value)) {
                            count++;
                            visitedPieces.add(value);
                        }
                    }
                }
            } else {
                // Need to move right
                for (int i = startCol; i <= this.board.getEndGoal()[1]; i++) {
                    char value = this.board.getBoard()[row][i];
                    if (value == 'K') {
                        break;
                    }
                    if (value >= 'A' && value <= 'Z' && value != 'P') {
                        if (!visitedPieces.contains(value)) {
                            count++;
                            visitedPieces.add(value);
                        }
                    }
                }
            }
        } else {
            // For vertical primary piece
            int startRow = row + pieceLength - 1;

            if (this.board.getEndGoal()[0] < startRow) {
                // Need to move up
                for (int i = startRow; i >= this.board.getEndGoal()[0]; i--) {
                    char value = this.board.getBoard()[i][col];
                    if (value == 'K') {
                        break;
                    }
                    if (value >= 'A' && value <= 'Z' && value != 'P') {
                        if (!visitedPieces.contains(value)) {
                            count++;
                            visitedPieces.add(value);
                        }
                    }
                }
            } else {
                // Need to move down
                for (int i = startRow; i <= this.board.getEndGoal()[0]; i++) {
                    char value = this.board.getBoard()[i][col];
                    if (value == 'K') {
                        break;
                    }
                    if (value >= 'A' && value <= 'Z' && value != 'P') {
                        if (!visitedPieces.contains(value)) {
                            count++;
                            visitedPieces.add(value);
                        }
                    }
                }
            }
        }
        return count;
    }

    private int distanceToGoal() {
        Piece primary = this.getBoard().getPieces().get('P');
        int[] goal = this.getBoard().getEndGoal();

        if (primary == null) {
            System.out.println("Primary piece not found in the board.");
            return 0;
        }
        if (primary.getAxis() == 0) {
            return Math.abs(goal[1] - (primary.getCol() + primary.getLength() - 1));
        } else {
            return Math.abs(goal[0] - (primary.getRow() + primary.getLength() - 1));
        }
    }

    public String getDirection() {
        Piece moved = this.board.getPieces().get(this.movedPiece);
        if (moved == null) {
            System.out.println("Moved piece not found in the board.");
            return "";
        }
        int axis = moved.getAxis();
        if (axis == 0) {
            return (this.moveDistance < 0 ? "LEFT" : "RIGHT");
        } else {
            return (this.moveDistance < 0 ? "UP" : "DOWN");
        }
    }

    public void pieceMovementInfo() {
        if (this.movedPiece == '-') {
            System.out.println("No piece moved, this is the root node.");
            return;
        }
        System.out.print("Piece: " + this.movedPiece);
        System.out.print(", MoveDistance: " + Math.abs(this.moveDistance));
        System.out.println(", Direction: " + this.getDirection());
    }
}

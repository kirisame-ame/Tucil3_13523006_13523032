package kirisame.rush_solver.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Node {

    private Board board;
    private Node parent;
    private int depth;

    public Node(Board board, Node parent, int depth) {
        this.board = board.deepCopy();
        this.parent = parent;
        this.depth = depth;
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

    /**
     * Expands the current node by checking all possible moves for each piece
     * 
     * @return a list of nodes that are the result of moving the pieces
     */
    public Node[] expand() {
        ArrayList<Node> nodes = new ArrayList<>();
        HashMap<Character, Piece> pieces = this.board.getPieces();

        for (Piece piece : pieces.values()) {

            // Try moving in positive direction
            for (int dist = 1;; dist++) {
                try {
                    Board newBoard = this.board.deepCopy();
                    System.out.println("Trying to move piece " + piece.getId() + " by " + dist);
                    newBoard.move(piece, dist);
                    nodes.add(new Node(newBoard, this, this.depth + 1));
                } catch (Exception e) {
                    break; // Stop trying further in this direction
                }
            }

            // Try moving in negative direction
            for (int dist = -1;; dist--) {
                try {
                    Board newBoard = this.board.deepCopy();
                    System.out.println("Trying to move piece " + piece.getId() + " by " + dist);
                    newBoard.move(piece, dist);
                    nodes.add(new Node(newBoard, this, this.depth + 1));
                } catch (Exception e) {
                    break;
                }
            }
        }

        return nodes.toArray(Node[]::new);
    }

    // Heuristics
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
        HashSet<Character> visitedPieces = new HashSet<>();
        if(axis == 0) {
            if(this.board.getEndGoal()[0]<row){
                for(int i=row;i>=this.board.getEndGoal()[0];i--){
                    char value = this.board.getBoard()[i][col];
                    if(value=='K'){
                        break;
                    }
                    if(value>='A' && value<='Z'){
                        if(!visitedPieces.contains(value)){
                            count++;
                            visitedPieces.add(value);
                        }
                    }
                }
            }else{
                for(int i=row;i<=this.board.getEndGoal()[0];i++){
                    char value = this.board.getBoard()[i][col];
                    if(value=='K'){
                        break;
                    }
                    if(value>='A' && value<='Z'){
                        if(!visitedPieces.contains(value)){
                            count++;
                            visitedPieces.add(value);
                        }
                    }
                }
            }
            
        } else {
            if(this.board.getEndGoal()[1]<col){
                for(int i=col;i>=this.board.getEndGoal()[1];i--){
                    char value = this.board.getBoard()[row][i];
                    if(value=='K'){
                        break;
                    }
                    if(value>='A' && value<='Z'){
                        if(!visitedPieces.contains(value)){
                            count++;
                            visitedPieces.add(value);
                        }
                    }
                }
            }else{
                for(int i=col;i<=this.board.getEndGoal()[1];i++){
                    char value = this.board.getBoard()[row][i];
                    if(value=='K'){
                        break;
                    }
                    if(value>='A' && value<='Z'){
                        if(!visitedPieces.contains(value)){
                            count++;
                            visitedPieces.add(value);
                        }
                    }
                }
            }
            
        }
        
        return count;
    }
}

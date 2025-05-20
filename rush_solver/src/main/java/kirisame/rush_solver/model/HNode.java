package kirisame.rush_solver.model;

public class HNode extends Node {

    private int f;

    public HNode(Node node) {

        // f(n) = g(n) + h(n)
        // g(n) = depth
        super(node.getBoard(), node.getParent(), node.getDepth(), node.heuristic, node.getMovedPiece(),
                node.getMoveDistance());
        this.f = node.getDepth() + this.getHeuristicValue();
    }

    public int getF() {
        return f;
    }

    /**
     * Compares this node with another node based on the f value.
     * This is used for sorting the priority queue.
     * 
     * @param other
     * @return
     */
    public int compareTo(HNode other) {
        if (this.f == other.getF()) {
            return 0;
        }
        if (this.f > other.getF()) {
            return 1;
        }
        return -1;
    }

    /**
     * Compares this Hnode with another Hnode based on the f value and Board object.
     * 
     * @param other
     * @return
     */
    public boolean equals(HNode other) {
        // compare f value
        if (this.f != other.getF()) {
            return false;
        }

        // compare board
        return (this.getBoard().equals(other.getBoard()));
    }
}

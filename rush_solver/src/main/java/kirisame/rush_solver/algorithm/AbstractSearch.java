package kirisame.rush_solver.algorithm;

import java.util.ArrayList;
import java.util.Collection;

import kirisame.rush_solver.model.Node;
public abstract class AbstractSearch {
    // Abstract class for search algorithms
    protected ArrayList<Node> path ;
    public abstract ArrayList<Node> solve();

    
    protected void buildPath(Node goalNode) {
        path = new ArrayList<>();
        while (goalNode != null) {
            path.add(0, goalNode);
            goalNode = goalNode.getParent();
        }
    }

    protected static boolean containsBoard(Collection<? extends Node> collection, Node compNode) {
        for (Node n : collection) {
            if (n.getBoard().equals(compNode.getBoard())) {
                return true;
            }
        }
        return false;
    }
}

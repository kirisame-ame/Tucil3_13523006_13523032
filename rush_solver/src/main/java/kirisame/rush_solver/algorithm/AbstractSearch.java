package kirisame.rush_solver.algorithm;

import java.util.Collection;

import kirisame.rush_solver.model.Node;
public class AbstractSearch {
    // Abstract class for search algorithms
    // This class should be extended by specific search algorithms like A*, GBFS, etc.
    // It should define the common interface and properties for all search algorithms.

    // Common properties and methods can be defined here
    // For example, a method to check if a node is in the open or closed list

    public void search() {
        // Implement the search logic here
        // This method should be overridden by subclasses
    }

    protected static boolean containsBoard(Collection<? extends Node> collection, Node node) {
        for (Node Node : collection) {
            if (Node.getBoard().equals(node.getBoard())) {
                return true;
            }
        }
        return false;
    }

    protected void keepBetterNodeInOpen(Node newNode) {
        // Logic to keep the better node in the open list
    }
}

package kirisame.rush_solver.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import kirisame.rush_solver.model.Node;

public class GBFS extends AbstractSearch {
    private PriorityQueue<Node> open;
    private ArrayList<Node> closed;
    private long executionTime;

    public GBFS(Node startNode) {
        Comparator<Node> comparator = Comparator.comparingInt(Node::getHeuristicValue);
        open = new PriorityQueue<>(comparator);
        closed = new ArrayList<>();
        open.add(startNode);
    }

    @Override
    public ArrayList<Node> solve() {

        long startTime = System.nanoTime();

        while (!open.isEmpty()) {
            Node currentNode = open.poll();
            closed.add(currentNode);
            if (currentNode.getBoard().isGoal()) {
                System.out.println("Found solution");
                buildPath(currentNode);
                executionTime = System.nanoTime() - startTime; // Stop timing
                return this.path;
            }
            Node[] children = currentNode.expand();
            for (Node child : children) {
                if (!containsBoard(open, child) && !containsBoard(closed, child)) {
                    open.add(child);
                }
            }
        }
        System.out.println("No solution found");
        this.path = null; // No solution found
        executionTime = System.nanoTime() - startTime; // Stop timing
        return this.path;
    }

    /**
     * get visited nodes count
     * 
     * @return the number of visited nodes
     */
    public int getNodeCount() {
        return open.size() + closed.size();
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public long getExecutionTimeInMillis() {
        return executionTime / 1_000_000; // Convert to milliseconds
    }

}

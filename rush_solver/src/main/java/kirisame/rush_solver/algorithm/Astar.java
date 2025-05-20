package kirisame.rush_solver.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import kirisame.rush_solver.model.HNode;
import kirisame.rush_solver.model.Node;

public class Astar extends AbstractSearch {

    // Own Implementation of Prioqueue
    private PriorityQueue<HNode> open;
    private ArrayList<HNode> closed = new ArrayList<>();
    private long executionTime;

    /**
     * Constructs and Solves the problem with class A*
     * 
     * @param startBoard
     * @param heuristic
     * @return
     */
    public Astar(Node startNode) {

        Comparator<HNode> comparator = Comparator.comparingInt(HNode::getF);
        open = new PriorityQueue<>(comparator);

        // Node startNode = new Node(initialBoard.deepCopy(), null, 0, heuristic);
        HNode root = new HNode(startNode);
        open.add(root);

    }

    @Override
    public ArrayList<Node> solve() {

        long startTime = System.nanoTime();

        while (!open.isEmpty()) {
            HNode current = open.poll();

            if (current.getBoard().isGoal()) {
                buildPath(current);
                executionTime = System.nanoTime() - startTime; // Stop timing
                return path;
            }

            closed.add(current);

            for (Node successor : current.expand()) {

                HNode hSuccessor = new HNode(successor);

                if (containsBoard(open, hSuccessor)) {
                    keepBetterNodeInOpen(hSuccessor);
                } else if (!containsBoard(closed, hSuccessor)) {
                    open.add(hSuccessor);
                }
            }
        }

        System.out.println("No solution found");
        path = null; // No solution found
        executionTime = System.nanoTime() - startTime; // Stop timing
        return path;
    }

    private void keepBetterNodeInOpen(HNode newNode) {
        // Inefficient PriorityQueue workaround: rebuild without the worse node
        List<HNode> tempList = new ArrayList<>();
        boolean replaced = false;

        while (!open.isEmpty()) {
            HNode node = open.poll();
            if (!replaced && node.getBoard().equals(newNode.getBoard())) {
                if (node.getF() > newNode.getF()) {
                    tempList.add(newNode); // replace with better node
                } else {
                    tempList.add(node); // keep old one
                }
                replaced = true;
            } else {
                tempList.add(node);
            }
        }

        open.addAll(tempList); // rebuild the queue
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

    // public ArrayList<Node> getPath() {
    // return path;
    // }

}

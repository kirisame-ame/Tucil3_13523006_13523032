package kirisame.rush_solver.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import kirisame.rush_solver.model.Board;
import kirisame.rush_solver.model.HNode;
import kirisame.rush_solver.model.Node;

public class Astar extends AbstractSearch {

    // Board Solution Path
    public ArrayList<Board> path;

    // Own Implementation of Prioqueue
    private PriorityQueue<HNode> open;
    private ArrayList<HNode> closed = new ArrayList<>();

    /**
     * Constructs and Solves the problem with class A*
     * 
     * @param startBoard
     * @param heuristic
     * @return
     */
    // TODO: REPLACE 'int heuristic' WITH HEURISTIC CLASS
    public Astar(Board initialBoard, String heuristic) {

        Comparator<HNode> comparator = Comparator.comparingInt(HNode::getF);
        open = new PriorityQueue<>(comparator);

        Node startNode = new Node(initialBoard.deepCopy(), null, 0, heuristic);
        HNode root = new HNode(startNode);
        open.add(root);

        while (!open.isEmpty()) {
            HNode current = open.poll();

            if (current.getBoard().isGoal()) {
                buildPath(current);
                return;
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

        path = null; // No solution found
    }

    private void buildPath(Node goalNode) {
        path = new ArrayList<>();
        Node node = goalNode;
        while (node != null) {
            path.add(0, node.getBoard()); // prepend to list
            node = node.getParent();
        }
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

}

package kirisame.rush_solver.algorithm;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import kirisame.rush_solver.model.Node;
public class GBFS extends AbstractSearch {
    private PriorityQueue<Node> open;
    private ArrayList<Node> closed;

    public GBFS(Node startNode) {
        Comparator<Node> comparator = Comparator.comparingInt(Node::getHeuristicValue);
        open = new PriorityQueue<>(comparator);
        closed = new ArrayList<>();
        open.add(startNode);
    }
    @Override
    public ArrayList<Node> solve(){
        while (!open.isEmpty()){
            Node currentNode = open.poll();
            closed.add(currentNode);
            if (currentNode.getBoard().isGoal()){
                System.out.println("Found solution");
                buildPath(currentNode);
                return this.path;
            }
            Node[] children = currentNode.expand();
            for (Node child : children){
                if (!containsBoard(open, child) && !containsBoard(closed, child)){
                    open.add(child);
                }
            }
        }
        System.out.println("No solution found");
        this.path = null; // No solution found
        return this.path;
    }
    
}

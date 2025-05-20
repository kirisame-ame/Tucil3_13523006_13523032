package kirisame.rush_solver.algorithm;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import kirisame.rush_solver.model.Node;
public class GBFS extends AbstractSearch {
    public static ArrayList<Node> solve(Node initNode){
        PriorityQueue<Node> openNodes = new PriorityQueue<>(Comparator.comparingInt(Node::getHeuristicValue));
        ArrayList<Node> movesTaken = new ArrayList<>();
        openNodes.add(initNode);
        while (!openNodes.isEmpty()){
            Node currentNode = openNodes.poll();
            System.out.println("Current node: " + currentNode.getBoard().boardToString());
            System.out.println("Heuristic value: " + currentNode.getHeuristicValue());
            movesTaken.add(currentNode);
            if (currentNode.getBoard().isGoal()){
                System.out.println("Found solution");
                return movesTaken;
            }
            Node[] children = currentNode.expand();
            for (Node child : children){
                if (!containsBoard(openNodes, child) ){
                    openNodes.add(child);
                }else if(!containsBoard(movesTaken,child)){
                    openNodes.add(child);
                }
            }
        }
        System.out.println("No solution found");
        return null;
    }
}

package kirisame.rush_solver.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kirisame.rush_solver.algorithm.Astar;
import kirisame.rush_solver.algorithm.GBFS;
import kirisame.rush_solver.model.Board;
import kirisame.rush_solver.model.Node;

@RestController
@RequestMapping("/api")
public class MainController {
    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello, From 春ブーツ!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/solve")
    public ResponseEntity<Map<String, Object>> solve(
            @RequestParam MultipartFile file,
            @RequestParam(value = "algo", defaultValue = "astar") String algorithm,
            @RequestParam(value = "heur", defaultValue = "blocking") String heuristic) {
        try {
            String content = new String(file.getBytes(), java.nio.charset.StandardCharsets.UTF_8);
            Board rootBoard = ParserController.readFile(content);
            Node rootNode = new Node(rootBoard, null, 0, heuristic);

            ArrayList<Node> path;
            long executionTime;
            int visitedNodes;

            switch (algorithm.toLowerCase()) {
                case "gbfs" -> {
                    GBFS gbfs = new GBFS(rootNode);
                    path = gbfs.solve();
                    executionTime = gbfs.getExecutionTimeInMillis();
                    visitedNodes = gbfs.getNodeCount();
                }
                case "astar" -> {
                    Astar astar = new Astar(rootNode);
                    path = astar.solve();
                    executionTime = astar.getExecutionTimeInMillis();
                    visitedNodes = astar.getNodeCount();
                }
                case "ucs" -> {
                    rootNode.setHeuristic("ucs");
                    Astar astar = new Astar(rootNode);
                    path = astar.solve();
                    executionTime = astar.getExecutionTimeInMillis();
                    visitedNodes = astar.getNodeCount();
                }
                default -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("error", "Invalid algorithm. Supported values: 'astar', 'gbfs', 'ucs'");
                    return ResponseEntity.badRequest().body(errorResponse);
                }
            }

            if (path == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "No solution found");
                return ResponseEntity.status(500).body(errorResponse);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("algorithm", algorithm);
            response.put("heuristic", heuristic);
            response.put("executionTime", executionTime);
            response.put("visitedNodes", visitedNodes);
            
            // Convert path to list of board states and movements
            List<Map<String, Object>> pathStates = new ArrayList<>();
            for (Node node : path) {
                Map<String, Object> state = new HashMap<>();
                state.put("board", node.getBoard().boardToString());
                
                // Create movement info object
                if (node.getMovedPiece() != '-') {
                    Map<String, Object> movement = new HashMap<>();
                    movement.put("piece", String.valueOf(node.getMovedPiece()));
                    movement.put("distance", Math.abs(node.getMoveDistance()));
                    movement.put("direction", node.getDirection());
                    state.put("movement", movement);
                }
                pathStates.add(state);
            }
            response.put("path", pathStates);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to parse file: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}

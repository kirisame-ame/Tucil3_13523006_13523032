package kirisame.rush_solver.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kirisame.rush_solver.algorithm.Astar;
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

    @PostMapping("/parse")
    public ResponseEntity<String> parse(@RequestParam("file") MultipartFile file) {
        try {
            // Read file content as String (assuming text file)
            String content = new String(file.getBytes(), java.nio.charset.StandardCharsets.UTF_8);
            Board rootBoard = ParserController.readFile(content);
            Node rootNode = new Node(rootBoard, null, 0, "blocking");
            // Perform GBFS algorithm
            // ArrayList<Node> solution = GBFS.solve(rootNode);
            // for (Node node : solution) {
            // System.out.println(node.getBoard().boardToString());
            // }

            // perform A* algorithm
            Astar astar = new Astar(rootNode);
            ArrayList<Node> path = astar.path;
            for (Node node : path) {
                System.out.println(node.getBoard().boardToString());
            }

            return ResponseEntity.ok(rootBoard.boardToString());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to parse file: " + e.getMessage());
        }
    }
}

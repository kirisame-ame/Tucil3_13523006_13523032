package kirisame.rush_solver.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kirisame.rush_solver.model.Board;

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
            ParserController.readFile(content);
            return ResponseEntity.ok(Board.getInstance().boardToString());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to parse file: " + e.getMessage());
        }
    }
}

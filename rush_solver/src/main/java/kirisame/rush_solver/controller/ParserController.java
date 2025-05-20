package kirisame.rush_solver.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;

import kirisame.rush_solver.model.Board;

public class ParserController {
    /**
     * Parses the given file content representing a board configuration and constructs a Board object.
     * The file content should follow a specific format:
     * <ul>
     *   <li>The first line contains the board's height and width, separated by a space.</li>
     *   <li>The second line contains the number of normal pieces.</li>
     *   <li>The subsequent lines represent the board layout, with each character indicating a piece, empty space, or special marker.</li>
     * </ul>
     * The method processes the file content line by line, sets up the board's size, pieces, and layout,
     * and handles special cases such as the end goal ('K') and empty spaces.
     * 
     * @param fileContent the string content of the file to parse
     * @return a {@link Board} object representing the parsed board configuration, or {@code null} if an error occurs
     */
    public static Board readFile(String fileContent) {
        try(BufferedReader br = new BufferedReader(new StringReader(fileContent))) {
            Board rootBoard = new Board();
            int lineCount = 0;
            int normalPieceCount = 0;
            HashSet<Character> pieceIds = new HashSet<>();
            String line;
            ArrayList<String> tempBoard = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                lineCount++;
                switch (lineCount) {
                    case 1 -> {
                        String[] dimensions = line.split(" ");
                        int height = Integer.parseInt(dimensions[0]);
                        int width = Integer.parseInt(dimensions[1]);
                        rootBoard.setSize(height, width);
                    }
                    case 2 -> {
                        normalPieceCount = Integer.parseInt(line);
                        rootBoard.setPieceCount(normalPieceCount);
                    }
                    default -> {
                        tempBoard.add(line);
                    }
                }
            }
            int exitRow = -1, exitCol = -1;
            for (int i = 0; i < tempBoard.size(); i++) {
                line = tempBoard.get(i);
                for (int j = 0; j < line.length(); j++) {
                    if (line.charAt(j) == 'K') {
                        exitRow = i;
                        exitCol = j;
                        break;
                    }
                }
                if (exitRow != -1)
                    break;
            }
            if(exitRow==0){
                // Exit on top row, need padding
                int offset = 1;
                rootBoard.setBoardAt(0, exitCol+offset, 'K');
                for (int i = 1; i < tempBoard.size(); i++) {
                    line = tempBoard.get(i);
                    for (int j = 0; j < line.length(); j++) {
                        rootBoard.setBoardAt(i, j+offset, line.charAt(j));
                    }
                }
            }else if(exitRow==tempBoard.size()-1){
                // Exit on bottom row, need padding
                int offset = 1;
                rootBoard.setBoardAt(rootBoard.getHeight()-1, exitCol+offset, 'K');
                for (int i = 0; i < tempBoard.size()-1; i++) {
                    line = tempBoard.get(i);
                    for (int j = 0; j < line.length(); j++) {
                        rootBoard.setBoardAt(i+1, j+offset, line.charAt(j));
                    }
                }
            }else if(exitCol==0){
                // Exit on left column, no need padding
                int offset = 0;
                rootBoard.setBoardAt(exitRow+1, 0, 'K');
                for (int i = 0; i < tempBoard.size(); i++) {
                    line = tempBoard.get(i);
                    for (int j = 1; j < line.length(); j++) {
                        rootBoard.setBoardAt(i+1, j+offset, line.charAt(j));
                    }
                }
            }else if(exitCol==rootBoard.getWidth()-2){
                // Exit on right column, no need padding
                int offset = 1;
                rootBoard.setBoardAt(exitRow+1, rootBoard.getWidth()-2, 'K');
                for (int i = 0; i < tempBoard.size(); i++) {
                    line = tempBoard.get(i);
                    for (int j = 0; j < line.length(); j++) {
                        rootBoard.setBoardAt(i+1, j+offset, line.charAt(j));
                    }
                }
            }
            rootBoard.parsePieces();
            System.out.println("Piece Info:");
            rootBoard.printPieces();
            System.out.println(rootBoard.boardToString());
            return rootBoard;
        } catch (IOException e) {
        }
        return null;
    }
}

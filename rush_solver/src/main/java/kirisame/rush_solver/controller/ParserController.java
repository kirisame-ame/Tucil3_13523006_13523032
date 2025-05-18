package kirisame.rush_solver.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;

import kirisame.rush_solver.model.Board;

public class ParserController {
    public static void readFile(String fileContent) {
        try(BufferedReader br = new BufferedReader(new StringReader(fileContent))) {
            int lineCount = 0;
            int normalPieceCount = 0;
            HashSet<Character> pieceIds = new HashSet<>();
            String line;
            while ((line = br.readLine()) != null) {
                lineCount++;
                switch (lineCount) {
                    case 1 -> {
                        String[] dimensions = line.split(" ");
                        int height = Integer.parseInt(dimensions[0]);
                        int width = Integer.parseInt(dimensions[1]);
                        Board.getInstance().setSize(height, width);
                    }
                    case 2 -> {
                        normalPieceCount = Integer.parseInt(line);
                        Board.getInstance().setPieceCount(normalPieceCount);
                    }
                    default -> {
                        if(lineCount == 3 && line.charAt(0) != 'K' && line.charAt(0) !=' ') {
                            int offset = 1;
                            for (int i=0;i<Board.getInstance().getWidth()-1;i++) {
                                try {
                                    char value = line.charAt(i);
                                    if(i==0 && (value=='K'||value=='k'|| value==' ')){
                                      offset = 0;
                                    }else{
                                        value = Character.toUpperCase(value);
                                    }
                                    Board.getInstance().setBoardAt(1, i+offset,value);
                                } catch (Exception e) {
                                }
                            }
                            lineCount++;
                            break;
                        }
                        if(line.length() > Board.getInstance().getWidth() - 1) {
                            throw new IllegalArgumentException("Line length exceeds the specified width.");
                        }
                        if(lineCount > Board.getInstance().getHeight() + 3) {
                            break;
                        }
                        if(normalPieceCount > Board.getInstance().getPieceCount()) {
                            throw new IllegalArgumentException("Piece count exceeds the specified limit.");
                        }
                        for(int i=0;i<Board.getInstance().getWidth()-1;i++) {
                            int offset = 1;
                            try {
                                char value = line.charAt(i);
                                if(i==0 && (value=='K'||value=='k'|| value==' ')){
                                    offset = 0;
                                }
                                // WIP IF K/EMPTY, no need i+1
                                if(value>='a' && value<='Z') {
                                    value = Character.toUpperCase(value);
                                    if(value=='K'){
                                        Board.getInstance().setEndGoal(lineCount - 3, i+offset);
                                    }
                                    if(value!='K'&& value!='P'){
                                        if(pieceIds.add(value)){
                                            normalPieceCount++;
                                        }
                                    }
                                }
                                if(value==' '){
                                    value = '壁';
                                }
                                Board.getInstance().setBoardAt(lineCount - 3, i+offset, value);
                            } catch (Exception e) {
                            }
                            
                        }
                    }
                }
            }
        } catch (IOException e) {
        }
    }
}

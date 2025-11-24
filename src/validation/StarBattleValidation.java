package validation;


import java.util.Scanner;
import java.io.IOException;
import board.StarBattleBoard;

public class StarBattleValidation {

    private StarBattleBoard board;

    public StarBattleValidation(StarBattleBoard board) {      //Constructor
        this.board = board;
    }

    private boolean isValidMove(int row, int col) {

        int size = board.getSize();
        char[][] grid = board.getBoard();  //scope og grid just in this method
        char[][] regions = board.getRegions();
        int starsPerRow = board.getStarsPerRow();

        // Case 1: if user enter a star placement position out of the grid's bounds
        if (row < 0 || row >= size || col < 0 || col >= size) {
            System.out.println("Invalid move — out of bounds");
            return false;
        }
        // Case 2: if user already placed a star at this position
        if (grid[row][col] == '*') {
            System.out.println("Invalid move — already a star here");
            return false;
        }

        // Case 3: adjacency check (no stars around)
        // using 8 directions map (kinda like vectors)
        // (-1,-1) (0,-1) (1,-1)
        // (-1,0) *(0,0)* (1,0)
        // (-1,1)  (0,1)  (1,1)
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int r = row + i, c = col + j;
                if (r >= 0 && r < size && c >= 0 && c < size && grid[r][c] == '*') {
                    System.out.println("Invalid move — having star around");
                    return false;
                }
            }
        }
        // can just place k stars per Row
        int rowStars = 0;
        for (int j = 0; j < size; j++)
            if (grid[row][j] == '*')
                rowStars++;
        if (rowStars >= starsPerRow) {
            System.out.println("Invalid — can just place " + starsPerRow + " per Row");
            return false;
        }

        // can just place k stars per Column
        int collumnStars = 0;
        for (int i = 0; i < size; i++) //count the stars on this col
            if (grid[i][col] == '*')
                collumnStars++;
        if (collumnStars >= starsPerRow) {    //check if the number of placed stars > erlaubt stars
            System.out.println("Invalid — can just place " + starsPerRow + " per Column");
            return false;
        }

        //region limit
        char region = regions[row][col];
        int regionStars = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (regions[i][j] == region && grid[i][j] == '*')
                    regionStars++;
        if (regionStars >= starsPerRow) {
            System.out.println("Invalid — region is already full");
            return false;
        }
        return true;
    }
    //action
    public void placeStar(int row, int col) {
        if(isValidMove(row, col)) {
            board.setValue(row, col, '*');
            System.out.println("Valid Move. Star successfully placed at (" + row + "," + col + ")");
        }
    }

    public void removeStar(int row, int col) {
        char[][] grid = board.getBoard();
        if (grid[row][col] == '*') {
            board.setValue(row, col, '.');
            System.out.println("Star removed from (" + row + ", " + col + ")");
        } else {
            System.out.println("No star to remove at (" + row + ", " + col + ")");
        }
    }

    //for each move, stars can be placed only if its a valid move,
    //if not, star is not placed, so dont need to check row, column and adjacency one more time,
    //but just to check if the number of stars per row, col, region and total must-have stars
    //are equal to required number of stars or not

    public boolean validateSolution() {
        char[][] grid = board.getBoard();
        char[][] regions = board.getRegions();
        int size = board.getSize();
        int starsPerRow = board.getStarsPerRow();

        // Check rows
        for (int i = 0; i < size; i++) {
            int count = 0;
            for (int j = 0; j < size; j++)
                if (grid[i][j] == '*') count++;
            if (count != starsPerRow) {
                System.out.println("Incorrect solution — row " + i + " has " + count + " stars.");
                return false;
            }
        }

        // Check columns
        for (int j = 0; j < size; j++) {
            int count = 0;
            for (int i = 0; i < size; i++)
                if (grid[i][j] == '*') count++;
            if (count != starsPerRow) {
                System.out.println("Incorrect solution — column " + j + " has " + count + " stars.");
                return false;
            }
        }

        // Check regions
        for (char r = 'A'; r <= 'Z'; r++) { // assuming region labels A-Z
            int count = 0;
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    if (regions[i][j] == r && grid[i][j] == '*') count++;
            if (count != 0 && count != starsPerRow) {
                System.out.println("Incorrect solution — region " + r + " has " + count + " stars.");
                return false;
            }
        }

        return true;
    }
    // Main Game
    public void playGame() {
        Scanner input = new Scanner(System.in);
        board.printRegions();
        board.printBoard();

        System.out.println("Here are commands: place or remove at row r, column c, print, quit and check validation");

        while (true) {
            System.out.print("Enter command: ");
            String cmd = input.next();

            if (cmd.equals("quit"))
                break;
            else if (cmd.equals("print"))
                board.printBoard();
            else if (cmd.equals("place")) {
                int r = input.nextInt(), c = input.nextInt();
                placeStar(r, c);
            } else if (cmd.equals("remove")) {
                int r = input.nextInt(), c = input.nextInt();
                removeStar(r, c);
            } else if (cmd.equals("check")) {  //input.next() reads only one token — it reads up to the first space
                if (validateSolution()) {
                    System.out.println("Puzzle Solved!");
                } else {
                    System.out.println("Incorrect solution!");
                }
            }
            else {
                System.out.println("Invalid command.");
            }
        }
        input.close();
    }


    public static void main(String[] args) {
        try {
            StarBattleBoard boardInput = StarBattleBoard.fromFile("puzzle/puzzle.txt");
            StarBattleValidation game = new StarBattleValidation(boardInput);
            game.playGame();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}


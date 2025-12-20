package game;

import java.util.Scanner;
import java.io.IOException;
import board.StarBattleBoard;

public class StarBattleGame {
    //rule of thumbs:
    //variables in data field: private
    //method (including constructor): public
    private StarBattleBoard board;
    //data field: every object in this class can have this property
    //private (Encapsulation - keep the variable used just in this class, not being accidentally destroyed)
    //board: reference type - pointer to the array board stored in heap
    public StarBattleGame(StarBattleBoard board) {      //Constructor used to create object this class
        this.board = board; //the board inside the constructor parameter passed to the board in data field
    }

    private boolean isValidMove(int row, int col) {  //private: internal logic not user interface
        //use getter like a bridge so that this StarBattleGame class
        //also can use private variables in class StarBattleBoard
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
        for (int j = 0; j < size; j++) //count the stars on this row
            if (grid[row][j] == '*')
                rowStars++;
        if (rowStars >= starsPerRow) {    //check if the number of placed stars > erlaubt stars
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
            board.setValue(row, col, '*');  //setter to access private board from StarBattleBoard class
            System.out.println("Valid Move. Star successfully placed at (" + row + ", " + col + ")");
        }
    }

    public void removeStar(int row, int col) {
        char[][] grid = board.getBoard();
        //must declare here one more time to use
        //local variable storing the board reference
        if (grid[row][col] == '*') {
            board.setValue(row, col, '.');
            System.out.println("Star removed from (" + row + ", " + col + ")");
        } else {
            System.out.println("No star to remove at (" + row + ", " + col + ")");
        }
    }
    // Main Game
    public void playGame() {
        Scanner input = new Scanner(System.in);
        board.printRegions();
        board.printBoard();

        System.out.println("Here are commands: place or remove at row r, column c, print, quit");

        while (true) {
            System.out.print("Enter command: ");
            String cmd = input.next();

            if (cmd.equals("quit"))  //cannot write cmd == "quit"
                break;      //==: Compares references (memory addresses), not actual content.
                //So == checks whether two variables point to the exact same object in memory.
            else if (cmd.equals("print"))
                board.printBoard();
            else if (cmd.equals("place")) {
                int r = input.nextInt(), c = input.nextInt();
                placeStar(r, c);
            } else if (cmd.equals("remove")) {
                int r = input.nextInt(), c = input.nextInt();
                removeStar(r, c);
            } else {
                System.out.println("Invalid command.");
            }
        }
        input.close();
    }

    public static void main(String[] args) {
        try {
            //using method fromFile that return an object instead of creating a new object by using constructor
            StarBattleBoard boardInput = StarBattleBoard.fromFile("puzzle/puzzle.txt");
            StarBattleGame game = new StarBattleGame(boardInput);
            //create a new object on the constructor StarBattleGame (inside the constructor
            //game and board point to the same object in heap
            game.playGame();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}

//visual flow:
//private StarBattleBoard board; //default value
//inside constructor: this.board = board (link the board in constructor param to the board in field
//in main method: StarBattleBoard boardInput = StarBattleBoard.fromFile("puzzle/puzzle.txt");
// a board with full initialized input values
// linked together and the "private StarBattleBoard board;" in the class receives the values

//public class Example() {
//data fields
//private int age; // default 0
//private String name; // default null

//public Example(int age, String name) {
//this.age = age;    //passing the age in parameter to the age in data field
//this.name = name;
//}
//public main ... () {
//Example obj = new Example(5, "Alice"); }
// constructor is like a bridge to transport data between an object and its data field
// as each object can have different data ,ex: new Example(5, "Alice") or new Example(7, "Bob")
// but just has the properties which is declared in data field which are age and name
// each time with another object, data fields need to be changed (thru constructor)
//with obj 1: (5, "Alice") => constructor (this.) => data field from age = 0, name = null becomes age = 5, name = Alice
//with obj 1: (7, "Bob") => constructor (this.) => data field from age = 0, name = null becomes age = 7, name = Bob

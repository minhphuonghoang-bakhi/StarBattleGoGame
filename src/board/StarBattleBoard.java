package board;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
//Deliverable 1
public class StarBattleBoard {
    private int size;                // nxn size of the board
    private int starsPerRow;         // can just let k star per Row, Column and Region
    private char[][] regions;        // 2D Array to represent the regions
    private char[][] board;          // 2D Array to represent the current status of the board (no star placed)

    // Constructor an be used to set initial values for object attributes
    // with constructor will the arrays be allocated with correct sizes from Anfang
    // and we can create objects later on this constructor without mistakes of wrong size
    public StarBattleBoard(int size, int starsPerRow) {
        this.size = size;   //this keyword: refers to the class variable size
        this.starsPerRow = starsPerRow; // now need new as int can "live" on stack
        this.regions = new char[size][size]; //store array on a heap and return its reference and refer to the class array
        this.board = new char[size][size];
        //fill in the board with . as an empty board (no stars placed)
        for (int i = 0; i < size; i++) {
            Arrays.fill(board[i], '.');
        }
    }

    // read .txt file
    // 5 1
    // ABBBA
    // AACCB
    // DDCCB
    // DDEEB
    // DEEEB

    // firstLine: call readLine() and it points at the first line which is 5 1
    // ["5" , "1"]
    // convert into int 5 and 1 to allocate to variables size and starPerRow

    // method returns an object which ist stored all the read data
    // IOException If there is an error in reading the file

    public static StarBattleBoard fromFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("puzzle/puzzle.txt"));

        String[] firstLine = br.readLine().trim().split("\\s+");
        int size = Integer.parseInt(firstLine[0]);
        int starsPerRow = Integer.parseInt(firstLine[1]);

        StarBattleBoard sb = new StarBattleBoard(size, starsPerRow); //create an object from the class board.StarBattleBoard
        //and to call out the constructor
        //fill in the array regions with the input from file
        for (int i = 0; i < size; i++) {
            String line = br.readLine().trim(); //i = 0, readLine - line i = 0, trim(): line = A A B B C
            for (int j = 0; j < size; j++) {
                sb.regions[i][j] = line.charAt(j);    // example: line = A B B B A, A = sb.regions[0][0], B = sb.regions[0][1]
            }
        }

        br.close();
        return sb; //return the object (fully initialized board)
    }

    // Print the empty board
    public void printBoard() {
        System.out.println("Star Battle Board:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Print the regions
    public void printRegions() {
        System.out.println("Regions:");
        for (int i = 0; i < size; i++) {
            if (i == 0) System.out.print("   " + i );
            else System.out.print(" " + i );
        }
        System.out.println();
        for (int i = 0; i < size; i++) {
            System.out.print(" " + i + " ");
            for (int j = 0; j < size; j++) {
                System.out.print(regions[i][j] + " ");
            }
            System.out.println();
        }
    }

    // used for the Deliverable 2
    // getter setter method
    // getter method: size is in this class private (only can be used in this class), so if we want to read it in other classes
    // for ex: board.size it will be error, so we need a get method for other classes to access this private variable
    // board.getSize() will be correct
    public int getSize() { return size; }
    public int getStarsPerRow() { return starsPerRow; }
    public char[][] getRegions() { return regions; }
    public char[][] getBoard() { return board; }
    public void setValue(int row, int col, char value) { board[row][col] = value; }
    //A setter is a method that allows you to modify a private field of a class from outside that class.
    //use to change the board later when user place star

    // main method
    public static void main(String[] args) {
        try {
            //using method fromFile that return an object instead of creating a new object by using constructor
            // 2 ways of creating an object: constructor or a factory static method that returns an object
            StarBattleBoard test = StarBattleBoard.fromFile("puzzle/puzzle.txt");
            test.printRegions(); //call out defined method printRegions on object test
            test.printBoard();
        } catch (IOException e) {  //try catch exception
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}

//should comprehend and clarify the definitions and concepts of OOP in Java firstly
//so that can build projects properly and can logical and critical thinking: why use this but not this, why this works

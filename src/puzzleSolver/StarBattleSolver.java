package puzzleSolver;


import java.util.*;
import java.util.Scanner;
import java.io.IOException;
import board.StarBattleBoard;
import validation.StarBattleValidation;

public class StarBattleSolver {

    private StarBattleBoard board;
    private int size;
    private int starsPerRow;
    private char[][] grid;
    private char[][] regions;


    public StarBattleSolver(StarBattleBoard board) {      //Constructor
        this.board = board;
        this.size = board.getSize();  //size is private in class Board, when class Solver wants to read: using getter method
        this.grid = board.getBoard();
        this.regions = board.getRegions();
        this.starsPerRow = board.getStarsPerRow();

    }

    //action(print,...) void
    //return sth, need data type of return value
    public int[] countRegion() {
        int size = board.getSize();
        char[][] region = board.getRegions();
        int[] countRegion = new int[26];  //represent 27 alphabets for regions
        for (char r = 'A'; r <= 'Z'; r++) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (region[i][j] == r) {
                        countRegion[r - 'A']++;
                    }
                }
            }
        }
        return countRegion;
        // [A(0)=4, B(1)=7, C(3)=4,...]
    }

    //Map regions to their cells
    private Map<Character, ArrayList<int[]>> getRegionCells() {      //create a hashmap
        Map<Character, ArrayList<int[]>> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // hashmap: <key, value>
                map.putIfAbsent(regions[i][j], new ArrayList<>());  //arraylist: resizeable array
                // which region has which points: regionCells = <A → [(0,0), (0,1), (1,0), (1,1)]...>
                map.get(regions[i][j]).add(new int[]{i, j});
            }
        }
        return map;
    }

    //before i have used wrapper method in previous class so that i dont need to define again isValidMove() in this class
    //but the isValidMove in previous class is a version allowing user to enter them solution
    //so after placing a star and check all cases, if not valid it must be an instruction printed out
    //such as "Invalid Move - already has a star" so that they know where theyre wrong
    //but in this "version" we let the computer solves itself, printing out this thing is kinda a mess for me so
    //I declare the method again without printing the reason for each step failing to place star
    //check if places star is valid
    private boolean isValidMove(int row, int col) {

        int size = board.getSize();
        char[][] grid = board.getBoard();  //scope og grid just in this method
        char[][] regions = board.getRegions();
        int starsPerRow = board.getStarsPerRow();

        // Case 1: if user enter a star placement position out of the grid's bounds
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return false;
        }
        // Case 2: if user already placed a star at this position
        if (grid[row][col] == '*') {
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

                    return false;
                }
            }
        }
        // can just place k stars per Row
        int rowStars = 0;
        for (int j = 0; j < size; j++)
            if (grid[row][j] == '*')
                rowStars++;
        if (rowStars >= starsPerRow) {      //larger or equal not larger
            return false;
        }

        // can just place k stars per Column
        int collumnStars = 0;
        for (int i = 0; i < size; i++) //count the stars on this col
            if (grid[i][col] == '*')
                collumnStars++;
        if (collumnStars >= starsPerRow) {    //check if the number of placed stars > erlaubt stars
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
            return false;
        }
        return true;
    }

    // solve puzzle
    public boolean solve() {
        Map<Character, ArrayList<int[]>> regionCells = getRegionCells();
        int[] regionSizes = countRegion();
        //regionSizes = [4, 7, 4, 5, 4, 0, 0...,0]
        ArrayList<Character> regionOrder = new ArrayList<>();
        for (int i = 0; i < 26; i++)
            if (regionSizes[i] > 0)  //just store which region is available
                regionOrder.add((char) ('A' + i));
        // A = regionSizes[0] = 4 > 0 -> add
        // regionOrder = [A, B, C, D, E]
        regionOrder.sort(Comparator.comparingInt(r -> regionSizes[r - 'A']));
        //lambda function r -> regionSizes[r - 'A'] convert each region letter into its size(number)
        //so that method Comparator.comparingInt can compare and sort the regionOrder list
        //r= 'A' -> regionSizes[A-A=0] = 4
        //r= 'B' -> regionSizes[B-A=1] = 7

        /** class RegionComparator implements Comparator<Character> {  //using interface
         private int[] regionSizes;

         public RegionComparator(int[] regionSizes) {
         this.regionSizes = regionSizes;
         }

         public int compare(Character r1, Character r2) {
         return regionSizes[r1 - 'A'] - regionSizes[r2 - 'A'];
         }
         }

         regionOrder.sort(new RegionComparator(regionSizes));
         **/

        return solveRegion(regionOrder, regionCells, 0);
    }

    // recursively solve regions by regions
    private boolean solveRegion(ArrayList<Character> regionOrder,
                                Map<Character, ArrayList<int[]>> regionCells, int idx) {
        if (idx == regionOrder.size()) return true; // solved all regions

        //get the current region and list of its cells.
        //call placeStarsInRegion to try placing starsPerRow = 1 stars in this region.

        char region = regionOrder.get(idx); //region = get(0) = A
        ArrayList<int[]> cells = regionCells.get(region); //cells= get('A') = A → [(0,0), (0,1), (1,0), (1,1)]

        return placeStarsInRegion(cells, 0, 0, regionOrder, regionCells, idx);
    }

    //place/remove star
    private void placeStar(int row, int col) {
        grid[row][col] = '*';
    }

    private void removeStar(int row, int col) {
        grid[row][col] = '.';
    }

    //backtracking in each region
    private boolean placeStarsInRegion(ArrayList<int[]> cells, int start, int starPlaced,
                                       ArrayList<Character> regionOrder, Map<Character,
                    ArrayList<int[]>> regionCells, int regionsidx) {
        for (int i = start; i < cells.size(); i++) {   //A → [(0,0), (0,1), (1,0), (1,1)]
            //cells.get(1) = (0,1)
            int r = cells.get(i)[0]; // 0
            int c = cells.get(i)[1];  // 1
            if (isValidMove(r, c)) {
                //cannot call a private method of another class directly
                //cannot call a method of another class without a reference to the object of this class

                placeStar(r, c);
                //recursively call to check all the points in that region
                //(call with i + 2, inside i + 2 call i + 3; inside i+3, call i+4)
                //i++ automatically, if cannot place the star at i + 1, i + 2, i + 3, i + 4, remove star at i
                if (placeStarsInRegion(cells, i + 1, starPlaced + 1, regionOrder, regionCells, regionsidx)) {
                    return true;
                }
                removeStar(r, c); //backtrack
            }
        }
        if (starPlaced == starsPerRow) {
            return solveRegion(regionOrder, regionCells, regionsidx + 1);
            //move to next region if place enough required stars in a region
        }
        return false; //no stars placed in this region
    }

    //print board
    public void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) System.out.print(grid[i][j] + " ");
            System.out.println();
        }
    }

    //add color for each region
    private static final String[] ansiColors = {
            "\u001B[31m", // red
            "\u001B[32m", // green
            "\u001B[33m", // yellow
            "\u001B[34m", // blue
            "\u001B[35m", // magenta
            "\u001B[36m", // cyan
            "\u001B[92m", // bright green
            "\u001B[93m", // bright yellow
            "\u001B[94m", // bright blue
            "\u001B[95m", // bright magenta
            "\u001B[96m"  // bright cyan
    };

    private static final String ansiReset = "\u001B[0m";
    // still need reset although already mapped color to each letter char
    //otherwise will the zB index or the "dot" boards below also be colored lmao

    // maps region letters A–Z to colors
    private String getRegionColor(char region) {
        int index = region - 'A';
        return ansiColors[index % ansiColors.length];  // idxC = 3 % 12 = 3 = yellow
    }

    // Print the regions with colors
    public void printRegions() {
        System.out.println("Regions:");
        for (int i = 0; i < size; i++) {
            if (i == 0) System.out.print("   " + i);    //print index so that users know
            else System.out.print(" " + i);
        }
        System.out.println();

        for (int i = 0; i < size; i++) {
            System.out.print(" " + i + " ");
            for (int j = 0; j < size; j++) {
                char region = regions[i][j];
                System.out.print(getRegionColor(region) + region + ansiReset + " ");
            }
            System.out.println();
        }
    }


    public static void main (String[] args) throws IOException {
        StarBattleBoard board = StarBattleBoard.fromFile("puzzle/puzzle.txt");
        StarBattleSolver solver = new StarBattleSolver(board);

        if (solver.solve() == true) {
            solver.printRegions();
            System.out.println("Solution found:");
            solver.printBoard();
        } else {
            System.out.println("No solution available.");
        }
    }
}

//i have space in my input file
//like instead of AAABBBCC i write it like A A B B C C and the file can be still printed out but input cant be implemented and solved

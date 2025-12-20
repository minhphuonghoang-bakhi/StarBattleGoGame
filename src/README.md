# Final Report : Star Battle Go Puzzle (text-based)
## 1. The design of the program
The program contains totally six packages named: board, game, puzzle, puzzleSolver, statistics, validation and one Markdown file (this file for project report). Each package has one class representing one particular use. 

In this program, the classes are built further based on the previous classes. I did not let the whole program in one class because it would be hard to maintain or debug, instead i made use of encapsulation that we have learnt in the Vorlesung.

### Program Structure: 
- StarBattleBoard: this class reads the input (sample puzzle) from file .txt and prints out the regions in form of A,B,C and the empty board initialized with dots.
- StarBattleGame: this class "inherits" the output of StarBattleBoard and let the user interact with the puzzle. Particularly user can place star at one cell using the command place r, c; and also can remove stars if he wants. The program also let user know if a star cannot be placed because it did not follow the rules.
- StarBattleValidation: this class bases mostly on StarBattleGame and just additionally let the user check if they have completed the puzzle correctly or not. 
- SolveStatistics: this program defines methods to create the statistic analyzation for the class StarBattleSolver. 
- StarBattleSolver: this class is the complete program letting the puzzle solved by itself automatically. The output are firstly the regions, including row and column indices printed out and each region is identified by colors. Secondly will the solution board be printed out, with colors representing for each region. The board contains dots for empty cells and star icons for placed stars. Lastly is the statistic analyzation for the solution which provides us to see how much time it costs the program to solve this solution, how many move attempts, valid move attempts and invalid move attempts are there, etc...

### Skeleton of class StarBattleSolver 
**Data Fields:**
- board: StarBattleBoard - Reference to the puzzle board object
- size: int - Size of the Puzzle
- starsPerRow: int - Number of stars required per row/column/region
- grid: char[ ][ ] - Current state of the board ('.' for empty, '*' for star)
- regions: char[ ][ ] - Region assignments for each cell (labeled A-Z)
- stats: SolveStatistics - Tracks solving performance metrics

**Dependencies:**
- board.StarBattleBoard - Encapsulates puzzle configuration
- statistics.SolveStatistics - Records and reports solver performance

**Key Methods** 
- isValidMove(int row, int col) - Method to check if a star can be placed or not.
- countRegion( ) - Count how many cells each region has, stored the result in an array of size 26: representing 26 alphabet character and also regions A to Z: index 0 stands for A, 1 for B and so on.
- getRegionCells( ) - Store which cells a region has. For example: A -> [(0,0), (0,1), (1,0), (1,1)]
- computeCellConstraints - Count the levels of constraint of each cell.
- placeStarsInRegion( ) - Try to use backtracking and recursion to place stars in one region. 
- solveRegion( ) - Try to solve each region.
- solve( ) - Solve the whole puzzle.
- printRegion(), printBoard(), printStatistics() - Represent the puzzle, the solution and the statistic analyzation in the console.

### Key Algorithm and Strategy 
In this program, the key algorithm that I used is backtracking and recursion. However, letting the program to choose a random cell to start solving is not efficient. 
- **Backtracking and Recursion**: 

```
private boolean placeStarsInRegion(ArrayList<int[]> cells, int start, int starPlaced,
  ArrayList<Character> regionOrder, Map<Character,
  ArrayList<int[]>> regionCells, int regionsidx) {

        if (starPlaced == starsPerRow) {
            return solveRegion(regionOrder, regionCells, regionsidx + 1);
        }

        ...

            if (isValidMove(r, c)) {
                placeStar(r, c);
                int i = cells.indexOf(cell);
                if (placeStarsInRegion(cells, i + 1, starPlaced + 1, regionOrder, regionCells, regionsidx)) {
                    return true;
                }
                removeStar(r, c); //backtrack           
            } 
        }

        return false; //no stars placed in this region
  }
  ```
  
  Trying to place a star in a region, call the method recursively trying to place star at the remaining cells of the region. If the solution is found, return true and exit the method. Else remove star (backtracking).
  - **Efficiency Enhancement:**

    - **Region-Based Solving Strategy**

    Optimization: Solves smallest regions first

    ````
    regionOrder.sort(Comparator.comparingInt(r -> regionSizes[r - 'A'])); 
    ````

    Smaller regions have fewer valid placements

    Failing fast on constrained regions prunes the search tree early

    Reduces unnecessary backtracking

    - **Dynamic Cell Filtering**

    Instead of iterating through all region cells, the algorithm:

    Filters out cells that already contain stars
    
    Sorts remaining cells by constraint score
    
    Tries most constrained cells first

    ```
    ArrayList<int[]> availableCells = new ArrayList<>();
    for (int[] cell : cells)
    if (grid[cell[0]][cell[1]] != '*')
    availableCells.add(cell);

    availableCells.sort(Comparator.comparingInt(cell ->
    computeCellConstraint(cell[0], cell[1])));
    ```
### Data Structures
**HashMap for Region Mapping**

`` javaMap<Character, ArrayList<int[]>> regionCells ``

Key: Region identifier ('A'-'Z')

Value: List of cell coordinates [row, col]

Purpose: Quick lookup of all cells in a region

**2D Arrays**

grid[][] - Current board state (mutable during solving)

regions[][] - Fixed region assignments

**ArrayLists**

Dynamic cell lists for filtering and sorting

Region ordering by size

### OOP Principles 
4 pillars of OOP are applied in this program
#### Encapsulation
The class demonstrates encapsulation by:

Making data fields private and accessing board properties through getter methods

Hiding implementation details of the solving algorithm from external classes

Providing public interfaces (solve(), printBoard()) while keeping helper methods private

Example:
```
private StarBattleBoard board;  // Private field
this.size = board.getSize();    // Access via getter
```
#### Polymorphism 
Method Overriding: The toString() Method

````
@Override
public String toString() {
// Custom implementation
}
````
Implicit Calls
````
// Both produce the same result:
System.out.println(stats);               // Implicit - polymorphism
System.out.println(stats.toString());    // Explicit 
````
#### Composition
The class uses composition rather than inheritance:

Contains a **StarBattleBoard** object (has-a relationship)

Contains a **SolveStatistics** object for tracking

#### Abstraction
The solver abstracts complex operations into well-defined methods:

`solve()` provides a simple public interface

Internal complexity is hidden in private helper methods like `solveRegion(), placeStarsInRegion()`

## 2. Faced challenges and solutions
Firstly, the main challenge i must face with in this project is that i do not know how to build the skeleton of the whole project and how to organize and connect the classes together. For example, keeping the data fields private requires me to create getter and setter methods. Then i take time to sketch out and read carefully the lecture book that the professor has provided. With small exercises given by the book, i have gradually the idea how things work with class, object and its data fields and methods, etc...

Secondly, i had already the idea in my head how i can use backtracking to solve the problem, but it is not optimizing. The idea of sorting the region sizes and then solve according to the sizes is hard to implement and requires me to understand thoroughly the data structures. In addition, writing the method placeStarsInRegion() inside the method solveRegion(), which is inside the main solver method solve() is also a challenge which requires much logic. 

However, through this project i can understand the theories more profoundly and reinforce my knowledge, my coding as well as problem solving skills. 

## 3. Sample inputs and outputs
### Sample 1
```
10 2
AAAAABBBCC
AADAABEECC
ADDDEEEECF
ADDDEEEECF
GHDDEEEECF
GHHDDEEJCF
GHHDDEJJCC
GHGGIIJCCC
GGGIIIICCC
GGGIIICCCC

```
Output:

![output1.png](output1.png)

````
═══════════════════════════════════════════════
             SOLUTION STATISTICS
═══════════════════════════════════════════════
Puzzle Name: puzzle.txt
Status:            ✓ SOLVED
Solve Time:        1.056 seconds 
Backtrack Count:    69226
Total Move Attempts: 889855
Invalid Move Attempts: 820609
Valid Move Attempts: 69246
Efficiency Rate: 7.78
Start Time: 2025-12-20 16:04:07
End Time: 2025-12-20 16:04:08
═══════════════════════════════════════════════
````

Sample 2
````
10 2
AAABBBCCCC
ABBBBBCCCC
ABBEEBEDDD
AABEEEEDDD
ABBFFFEEEE
FFFFFFEEEE
GGGGHHHHHH
IIGIIHHKHH
IIIIIHHKHH
IIIIIIIKKH
```` 
Output: 

![output2.png](output2.png)

````
═══════════════════════════════════════════════
             SOLUTION STATISTICS
═══════════════════════════════════════════════
Puzzle Name: puzzle.txt
Status:            ✓ SOLVED
Solve Time:        1.097 seconds 
Backtrack Count:    70636
Total Move Attempts: 943549
Invalid Move Attempts: 872893
Valid Move Attempts: 70656
Efficiency Rate: 7.49
Start Time: 2025-12-20 16:07:05
End Time: 2025-12-20 16:07:06
═══════════════════════════════════════════════
```` 

**There i do not let output step-by-step reasoning (e.g., “Row 1 already has
required stars”) because as the statistics shows, there are total 889855 move attempts (for the sample 1) and the console could not print out all the steps. And i have tried to read the output but it is somehow confusing, so i removed printing out each move attempts of the program solving the puzzle itself.**


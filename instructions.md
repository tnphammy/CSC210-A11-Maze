# Assignment 4:  Graphs and Recursive Maze Solving

For this assignment, you will write a program to solving mazes using **recursion**. The "big idea" is as follows: from each position in the maze, you will recursively call the next steps. This means that when you find the Finish Line, the path taken there from the start will be stored _implicitly on the call stack_ based on which calls return `true`. Your goal is to write a program that uses recursion to complete mazes passed via the command line or as user input.

Any maze can be represented as a graph:  mazes consist, after all, of a set of locations that are connected to each other in different ways.  Our mazes will be defined on a planar grid, which means that they will all correspond to graphs of degree four or less.  It is not necessary to use an explicit graph representation here; for this assignment, you will encode a maze stored in a text file as an array of arrays (a.k.a. a 2D array). You will explore the maze by recursively examining cells within the maze to determine whether they are the finish line, unexplored squares, walls, etc.

## File Breakdown

There are a lot of files in this assignment. Before you get started, use the information below to hone in on which you will need to focus on.

### Support Code Classes

There is a lot of support code in this assignment that you **do not** need to change.

Maze Encoding:
- `MazeLocation` is the class describing individual cells (MazeLocations) within your maze. It has methods to retrieve information about a particular location in a maze, such as its Cartesian coordinates within the array of arrays (row, col) and its contents (see `MazeContents` below)
- `MazeDirection` uses an enum data type to encode the cardinal directions (`NORTH`, `SOUTH`, `EAST`, and `WEST`)
- `MazeContents` defines different types of MazeLocations:
   - Before the maze is explored, an individual cell within the maze can be a `WALL` or an `OPEN`
   - Once you have explored the neighbors of a `MazeLocation`, you should mark it `VISITED`
   - If you hit a dead end or find the Finish, you should update cells on that route to contain either `DEAD_END` or `PATH`, respectively. We recommend holding off on implementing this until you can recursively search the maze to find the Finish line (which will require the other 3 `MazeContents` types).
   Please see the example `initDemoMaze` method within `Maze.java` to get an idea of how `MazeContents` should be used to encode a maze.

You will need to understand each of these encoding classes, but you will not need to change them.

Interface:
- `DisplayableMaze` is the interface that you will implement in `Maze.java`. Once again, you'll need to understand this class, but you don't need to change anything in the file.

Graphics:
- `MazeViewer` provides code to display your maze in a window. You do not need to change or understand this class.  It only needs a `DisplayableMaze` object to operate.

### Classes You Will Write

- Use `Maze.java` to implement the `DisplayableMaze` interface
- Use `SolveMaze.java` to write code to explore a maze recursively. There is code provided to help you pass information from SolveMaze to the `MazeViewer`, which should allow you to visualize your maze once you've encoded it.
- Use `ShortestPath.java` to write code that finds the most efficient route to the finish.  Do this only after you have completed all the other parts of the assignment.

### Test Files

You are provided with three test mazes, `maze1`, `maze2`, and `maze3`.  
In Phase 3, you will add code to encode a maze from a file.
Your program will be tested on these and several other mazes during the grading process.

## Phase One: Encoding a Machine-Readable Maze

Your first goal should be to create a working `Maze` class. This class describes a maze object with all of the attributes needed to describe a maze. 
1. Think about what class variables are needed to describe one of the test mazes. 
2. Start by copying all the method call signatures from the `DisplayableMaze` interface and begin to fill them out (i.e., make stubs) 
3. Add the appropriate instance variables, including a variable `mazeGrid` storing the maze contents as a **2D array** (an array of arrays) of type `MazeContents`. 
4. Fill out the getter methods (`getStart`, `getFinish`, `getContents`)
5. Write `isExplorable`. For this method, think about what characteristics would make a `MazeLocation` exporable versus not worth exploring-- do you want to explore off the edge of the maze? Places you've already been? Walls?
We are providing a class, `initDemoMaze`, that manually encodes a maze rather than reading in a file. You can use this method to initialize a simple maze to test your code. If you are seeing problems (a.k.a. red squiggles), that likely means your `Maze` class isn't configured correctly yet.

## Phase Two: Recursive Exploration

For phase two you will implement a recursive "solver" for the maze.  Here are details of the process:

**Problem Statement**: 
Given a maze (a 2D array of `MazeContents`) and a location within it (which may be expressed as a `MazeLocation`), determine whether the Finish can be reached.
You can only move to adjacent open squares (not diagonal) and you cannot pass through walls or previously visited squares.

**Starting Condition**:
When initialized, the map of the maze should show every open square as unexplored.
Your initial location should be the maze's starting point, which should be an attribute of the `Maze` object`.

**Stop Criteria**:
There are two possible stop conditions: one for a success, and one for a failure.

1. If the current location is the **finish** of the maze, then:
    * Mark the current square as part of the path (`MazeContents.PATH`), and 
    * Report success by returning `true`
2. If the current location is not worth exploring (based on calls to `isExplorable` returning `false`), then return `false` to indicate that this is not the correct path to reach the Finish.

**Simplification Step**:
When you are not at a stop condition that allows you to return `True` or  `False`, you need to recursively call your maze solver.
To do this, first mark the current square as visited (`MazeContents.VISITED`).
This simplifies the problem by reducing the number of explorable squares we need to search. 

Logically, we know that the Finish is reachable from the current (non-Finish) location if and only if it is reachable through one of the adjacent squares, either `MazeDirection.NORTH`, `MazeDirection.SOUTH`, `MazeDirection.EAST`, or `MazeDirection.WEST`. 
This means that to find out whether we can reach the Finish from this location, we need to check what happens when we look at adjacent locations, known as neighbors.
If the first neighbor you explore does not lead to the Finish, then you will need to explore the second, and so on, until you run out of neighbors.
Therefore, you may need to explore up to four neighbors.

You can implement this approach using by combining the results of exploring all of the directions.
The `||` operator for the Logical Or is likely to be useful. 
If moving in any of the directions leads to the finish (meaning, a call returns `True`), you don't need to explore any further -- this is Logical Or.
While exploring, before you return `true` or `false`, make sure you mark the current square as either on the path (`MazeContents.PATH`) or a dead end (`MazeContents.DEAD_END`), according to the result you're returning.

**Output**:
When the recursive solver finishes, the program should print a message indicating whether or not a solution was found.

### Algorithm

Here is an outline of the pseudocode you'll want to write for this phase of the project:

    Start by defining the problem statement.

    Begin exploration from the starting point.
    Determine success or failure criteria:
        If the current location is the finish, report success.
        If the current location is not explorable, report failure.
    Simplify the problem by marking the current square as visited.
    Recursively explore adjacent squares until the Finish is reached or deemed unreachable.
    Print a message indicating whether a solution was found

### Animation

If you include a short delay in your program at the start of each recursive call, you will see the recursion animated by the viewer.  Here is code that will delay for 50 milliseconds:

    try { Thread.sleep(50);	} catch (InterruptedException e) {};

**Note**: Before turning in your assignment, please set the delay to **less than 10 milliseconds**, to facilitate faster grading.

## Phase Three: Reading Maze Files

Solving the same maze over and over again isn't all that interesting.  In this phase, you will make your program read maze files. 
A valid input file will contain a character grid representing the contents of a maze.
The dimensions can be different, and it is not necessarily surrounded by walls (see maze3 for a challenge).

### Opening Files

Starter code is provided in `SolveMaze.java` to help you open the maze file based on the command line argument is provided.
Usage looks like:
    `java MazeSolver maze1`

### Text File Maze Encoding

In a map file, a `#` represents a wall, and a `.` or ` ` (a space character) represents a passageway.  The symbols `S` and `F`, respectively, represent the start and finish of the maze, and each will appear exactly once. Here is an example of a simple 6x9 maze file:

    #########
    #S..#...#
    #.#.#.#.#
    #.###.#.#
    #.....#F#
    #########

Once you read in the file, you need to encode each `MazeLocation` with the appropriate `MazeContents` specified in the text file.
This will allow you to read a maze from your file and translate it to a machine-readable format that your MazeSolver can explore.

## Phase Four

For the final phase of the assignment, you will implement Dijkstra's shortest path algorithm (as described in class) to find the most efficient route between the start and finish.  Do this in a separate executable program called `ShortestPath.java`. When you have found a path that takes the smallest number of steps, mark it in the maze and display the result.  You do not need to animate anything for this phase.

Remember that Dijkstra's algorithm requires you to store some additional temporary information about each node (the current cost estimate, and the route pointer).  These should be local variables, not stored as fields of the class.  You have a choice of several different methods to keep track of this data:
* You can store each piece of data in a 2D array the same size as the maze itself.  The data for a particular location in the maze will be stored at the same index values in the supplementary arrays.
* You can set up one or more hash tables using each `MazeLocation` as a key to the data associated with that point.

Pick the method that seems most appropriate to you.  In your readme you should discuss the choice you made and give your reasons for it.

## Closing Notes

Test your code on maze1, maze2, and maze3.
The support code will allow you to animate your `SolveMaze` algorithm, turing dead ends red and the final path to the Finish green.

Good luck, Maze Explorers!
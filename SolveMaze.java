import java.io.*;
import java.util.Scanner;

class SolveMaze {

  /**
   * Checks if a maze is able to be solved
   * 
   * @param maze the given maze
   * @return whether it can be solved
   */
  public static Boolean canSolve(Maze maze) {
    // Call helper on start point
    Boolean canBeSolved = canSolveHelper(maze, maze.getStart());
    // Print message accordingly
    if (canBeSolved) {
      System.out.println("YES! This maze can be solved hehe congratulations!!");
      return true;
    } else {
      System.out.println("babe i'm so sorry you can't solve this one...");
      return false;
    }
  }

  /**
   * Helper function for canSolve() which also takes a specific MazeLocation
   * returns whether a solution can be found from that point
   * 
   * @param maze the given maze
   * @param loc  the current location
   * @return whether it can be solved from the location
   */
  public static Boolean canSolveHelper(Maze maze, MazeLocation loc) {
    // Delay for animation
    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
    }
    ;

    // get coordinates
    int i = loc.getRow();
    int j = loc.getCol();

    // Stop condition: FINISH or DEAD END
    if (loc == maze.getFinish()) {
      maze.mazeGrid[i][j] = MazeContents.PATH; /* record point in path */
      return true;
    } else if (!maze.checkExplorable(i, j)) {
      maze.mazeGrid[i][j] = MazeContents.DEAD_END; /* can't be explored */
      return false;
    }

    // Recursive Step
    maze.mazeGrid[i][j] = MazeContents.VISITED; /* mark point visited */
    // explore loc's neighbors
    return (canSolveHelper(maze, loc.neighbor(MazeDirection.NORTH))
        || canSolveHelper(maze, loc.neighbor(MazeDirection.EAST))
        || canSolveHelper(maze, loc.neighbor(MazeDirection.WEST))
        || canSolveHelper(maze, loc.neighbor(MazeDirection.SOUTH)));

  }

  /**
   * Reads in a maze file in .txt format
   * 
   * @param fname the file name
   * @return the returned file
   */
  public static Scanner readMaze(String fname) {
    Scanner file = null;
    try {
      file = new Scanner(new File(fname));
    } catch (FileNotFoundException e) {
      System.err.println("Cannot locate file.");
      System.exit(-1);
    }
    return file;
  }

  /**
   * Encode a file into a maze representation
   * 
   * @param fname the file
   * @return the file in Maze form
   */
  public static Maze encodeMaze(String fname) {
    Maze maze = new Maze();
    try (Scanner file = readMaze(fname)) {
      // Get height and width
      Scanner calc = readMaze(fname);
      int height = 0;
      int width = 0;
      while (calc.hasNextLine()) {
        height++;
        String line = calc.nextLine();
        if (width == 0) {
          width = line.length(); /* set column if not yet */
        }
      }
      calc.close();

      // Configure maze properties 
      maze.height = height;
      maze.width = width;
      maze.mazeGrid = new MazeContents[height][width];

      // Fill out mazeGrid
      int i = 0; /* current row counter */
      while (file.hasNextLine()) {
        String row = file.nextLine();
        int j = 0; /* reset column counter */
        for (char c : row.toCharArray()) {
          // Checking property of each element
          if (c == '#') {
            maze.mazeGrid[i][j] = MazeContents.WALL;
          } else if (c == '.' || c == ' ') {
            maze.mazeGrid[i][j] = MazeContents.OPEN;
          } else if (c == 'S') {
            maze.mazeGrid[i][j] = MazeContents.OPEN;
            maze.start = new MazeLocation(i, j);
          } else if (c == 'F') {
            maze.mazeGrid[i][j] = MazeContents.OPEN;
            maze.finish = new MazeLocation(i, j);
          }
          j++; /* increment column */
        }
        i++; /* increment row */
      }
    } 
    return maze;

  }

  public static void main(String[] args) {
    if (args.length <= 0) {
      System.err.println("Please provide the name of the maze file.");
      System.exit(-1);
    }
    Scanner file = readMaze(args[0]);

    Maze maze = encodeMaze(args[0]);
    MazeViewer viewer = new MazeViewer(maze);
    canSolve(maze);
  }
}

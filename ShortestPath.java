import java.util.Scanner;
import java.util.Hashtable;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Solves a maze with the shortest path using Dijkstra's algorithm
 * 
 * @param args the arguments
 */
public class ShortestPath {

    /**
     * Displays the solution to a maze using Dijkstra's algorithm
     * 
     * @param maze the provided maze
     */
    public static void dijkstraSolve(Maze maze) {
        // 0.1. Make a hashTable that stores the cost to get to each node from the
        // starting point
        Hashtable<MazeLocation, NodeInfo> paths = new Hashtable<MazeLocation, NodeInfo>();
        // 0.2. Make a queue for which node ('s neighbors) to check
        Queue<MazeLocation> toCheck = new LinkedList<MazeLocation>();
        // 0.3. Make counter for number of visited nodes
        int visitedCounter = 0;
        int totalNodes = maze.openCells;

        // 1. Add the Start of the maze to the official path + color it as such
        MazeLocation start = maze.getStart();
        toCheck.add(start);
        NodeInfo currInfo = new NodeInfo(0, null);
        paths.put(maze.getStart(), currInfo);
        maze.mazeGrid[start.getRow()][start.getCol()] = MazeContents.PATH;

        // 2. Until Finish is reached:
        // Add the unvisited neighbors of every visited node to `paths` (the Hashtable)

        // while there are unvisited nodes
        while (visitedCounter < totalNodes) {
            // 1. Visit the first OPEN node in queue + add it to `paths`
            visitedCounter++;
            MazeLocation curr = toCheck.poll();

            // 1.1. Input its OPEN neighbors into `paths`
            // Iterate through all neighbors
            for (MazeDirection dir : MazeDirection.values()) {
                if (dir == MazeDirection.NONE) {
                    continue; /* skip NONE direction */
                }

                // Get each neighbor
                MazeLocation neighbor = curr.neighbor(dir);
                // Check if the neighbor is explorable and valid (as .neighbor() always returns
                // something)
                // Case 1: New neighbor node
                if (!(paths.containsKey(neighbor))
                        && maze.checkExplorable(neighbor.getRow(), neighbor.getCol())
                        && neighbor != curr) {
                    toCheck.add(neighbor); /* add to queue */

                    // Add to hashtable
                    NodeInfo neighborInfo = new NodeInfo(currInfo.cost + 1, curr);
                    paths.put(neighbor, neighborInfo);
                }
                // Case 2: Neighbor node exists
                else if (paths.containsKey(neighbor)) {
                    int newCost = currInfo.cost + 1;
                    int oldCost = paths.get(neighbor).cost;

                    // Checking for better cost => (Maybe) update
                    if (newCost < oldCost) {
                        paths.replace(neighbor, new NodeInfo(newCost, curr));
                    }
                }
            }
        }

        // 3. Trace the path from finish -> start, color it green
        // Get the Finish's via node
        System.out.println(paths.get(maze.getFinish()));
        MazeLocation shortestPath = paths.get(maze.getFinish()).via;
        // Color current path
        maze.mazeGrid[maze.getFinish().getRow()][maze.getFinish().getCol()] = MazeContents.PATH;
        maze.mazeGrid[shortestPath.getRow()][shortestPath.getCol()] = MazeContents.PATH;
        // Repeat for every other node in the path
        while (shortestPath != maze.getStart()) {
            shortestPath = paths.get(shortestPath).via;
            maze.mazeGrid[shortestPath.getRow()][shortestPath.getCol()] = MazeContents.PATH;
        }

    }

    /** Contains the information of a node, used as a value in the hashTable */
    private static class NodeInfo {
        /** The cost to get to this node */
        private int cost;
        /** The node that leads to this node */
        private MazeLocation via;

        /**
         * Simple constructor
         * 
         * @param cost the cost to get to this node
         * @param via  the node that leads to this node
         */
        private NodeInfo(int cost, MazeLocation via) {
            this.cost = cost;
            this.via = via;
        }
    }

    public static void main(String[] args) {
        if (args.length <= 0) {
        System.err.println("Please provide the name of the maze file.");
        System.exit(-1);
        }
        Scanner file = SolveMaze.readMaze(args[0]);
        //Scanner file = SolveMaze.readMaze("maze2");

        Maze maze = SolveMaze.encodeMaze(args[0]);
        //Maze maze = SolveMaze.encodeMaze("maze2");
        MazeViewer viewer = new MazeViewer(maze);
        dijkstraSolve(maze);

    }
}

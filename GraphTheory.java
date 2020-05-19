import java.sql.SQLOutput;
import java.util.*;

public class GraphTheory {

    static class Steps {
        List<String> steps;
        int count;

        public Steps() {
            this.steps = new ArrayList<>();
            this.count = 0;
        }

        public List<String> getSteps() {
            return steps;
        }

        public void setSteps(List<String> steps) {
            this.steps = steps;
        }

        public Steps addSteps(int x, int y) {
            this.steps.add("(" + x + "," + y + ")");
            return this;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void incCount() {
            ++this.count;
        }

        public Steps notFound() {
            steps.add(0, "NO WAY TO REACH THE END ");
            return this;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = steps.size() - 1; i >= 0; i--) {
                if(i != 0) builder.append(steps.get(i) + " -> ");
                else builder.append(steps.get(i));
            }
            return builder.toString() + " " + this.count + " STEPS";
        }
    }

    public Steps solve(String[][] grid, int[] start, int[] end) {
        Steps steps = new Steps();
        Map<String, String> previousCells = bfs(grid, start, end, steps);
        String startKey = start[0] + "," + start[1];
        String endKey = end[0] + "," + end[1];
        return reconstructPath(previousCells, endKey, steps, startKey);
    }

    private Steps reconstructPath(Map<String, String> previousCells, String endKey, Steps steps, String startKey) {
        //if the map does not contain the end key then we could not find a path
        if(!previousCells.containsKey(endKey)) {
            return steps.notFound();
        }

        //create a variable that will hold the current key
        String currentKey = endKey;
        String[] currentRes = currentKey.split(",");
        Queue<String> pathQueue = new LinkedList<>();
        //add the end key to the queue;
        steps.addSteps(Integer.valueOf(currentRes[0]), Integer.valueOf(currentRes[1]));
        pathQueue.add(endKey);
        while (!pathQueue.isEmpty()) {
            //current key we are looking at
            currentKey = pathQueue.poll();
            currentRes = currentKey.split(",");
            String prevKey = previousCells.get(currentKey);
            if(startKey.equals(currentKey)) {
                break;
            } else {
                String[] prevRes = prevKey.split(",");
                //previous key
                pathQueue.add(prevKey);
                steps.addSteps(Integer.valueOf(prevRes[0]), Integer.valueOf(prevRes[1]));
            }
        }
        return steps;
    }

    public Map<String, String> bfs(String[][] grid, int[] start, int[] end, Steps steps) {
        //nodes in next layer is 0 because we are not there//BFS so layers contain all the nodes that the current node points to.
        int nodesInNextLayer = 0;
        //nodes in the starting layer are 1 since we have not started
        int nodesLeftInLayer = 1;
        //map to hold visited values
        Map<String, Boolean> visited = new HashMap<>();
        visited.put(0 + "," + 0, true);
        //map to track the previous cell to reconstruct the path later
        Map<String, String> previousCells = new HashMap<>();
        //x coordinate queue
        Queue<Integer> xQueue = new LinkedList<>();
        //y coordinate queue
        Queue<Integer> yQueue = new LinkedList<>();
        //add the starting nodes coordinates to the queues
        xQueue.add(start[0]);
        yQueue.add(start[1]);
        while(!xQueue.isEmpty()) {
            //grab the coordinates from the queues
            int currentX = xQueue.poll();
            int currentY = yQueue.poll();
            //if our current cell is the end then add the step and return;
            if(currentX == end[0] && currentY == end[1]) {
                return previousCells;
            }
            /**
             * perfect example of why java is pass by value.
             * nodesInNextLayer goes in as the int value and
             * not the reference which is why we must
             * set nodesInNextLayer = to exploreNeighboringCells(...)
             */
            nodesInNextLayer = exploreNeighboringCells(grid, xQueue, yQueue, currentX, currentY, visited, previousCells, nodesInNextLayer);
            //decrement as nodes in the current layer get processed
            nodesLeftInLayer--;
            //if there are no more nodes in this layer
            if(nodesLeftInLayer == 0) {
                //move to next layer and set current nodes = to nodes in this next layer
                nodesLeftInLayer = nodesInNextLayer;
                //set next layer to 0
                nodesInNextLayer = 0;
                //add the step since we visited this node
                steps.incCount();
            }

        }
        //will only execute if the end is not found
        return previousCells;
    }

    private int exploreNeighboringCells(String[][] grid, Queue<Integer> xQueue, Queue<Integer> yQueue, int currentX, int currentY, Map<String, Boolean> visited, Map<String, String> previous, int nodesInNextLayer) {
        //neighboring cell coordinates
        int xx, yy;

        //directions for x and y N, S, E, W
        //direction x
        int[] dx = new int[] {-1, 1, 0, 0};
        //direction y
        int[] dy = new int[] {0, 0, 1, -1};

        //for each neighboring cell N,S,E,W
        for (int i = 0; i < 4; i++) {

            //grab the neighboring cell coordinates
            xx = currentX + dx[i];
            yy = currentY + dy[i];
            //skip out of bounds locations
            if(xx < 0 || yy < 0) continue;
            if(xx >= grid[0].length || yy >= grid.length) continue;

            //dont visit if we already have
            String visitedKey = xx + "," + yy;
            if(visited.getOrDefault(visitedKey, false)) continue;

            //dont visit walls
            if(grid[xx][yy].equals("#")) continue;
            //add the coordinates the the queue
            xQueue.add(xx);
            yQueue.add(yy);

            //make it as visited
            visited.put(visitedKey, true);
            //previous node
            previous.put(visitedKey, currentX + "," + currentY);
            //add this neighboring node to the next layer
            nodesInNextLayer++;
        }
        //return this since java is pass by value and this variable is only in method scope
        return nodesInNextLayer;
    }

    public static void main(String[] args) {
        /**TODO
         * Is there a path from start to end?
         * Find the shortest path from start node to end node
         * Given a 2d grid [][] where cells can be either . or #,
         * where . indicates you can travel to it, and # indicates
         * there is a rock in the way and you can not go here.
         *     0   1   2   3   4
         *   ---------------------
         * 0 | S | . | # | . | . |
         *   ---------------------
         * 1 | . | # | . | # | . |
         *   ---------------------
         * 2 | . | # | . | . | . |
         *   ---------------------
         * 3 | . | . | . | # | . |
         *   ---------------------
         * 4 | . | # | . | . | E |
         *   ---------------------
         *   SOLUTION: X INDICATES THE PATH
         *   ---------------------
         *     0   1   2   3   4
         *   ---------------------
         * 0 | S | . | # | . | . |
         *   ---------------------
         * 1 | X | # | . | # | . |
         *   ---------------------
         * 2 | X | # | . | . | . |
         *   ---------------------
         * 3 | X | X | X | # | . |
         *   ---------------------
         * 4 | . | # | X | X | E |
         *   ---------------------
         * (0,0) -> (O,1) -> (O,2) -> (O,3) -> (1,3) -> (2,3) -> (2,2) -> (3,2) -> (4,2) -> (4,3) -> (4,4) 10 STEPS
         *
         *   SOLUTION: X INDICATES THE PATH
         *   ---------------------
         *     0   1   2   3   4
         *   ---------------------
         * 0 | S | . | # | . | . |
         *   ---------------------
         * 1 | X | # | . | # | . |
         *   ---------------------
         * 2 | X | # | . | . | . |
         *   ---------------------
         * 3 | X | X | X | # | . |
         *   ---------------------
         * 4 | . | # | E | . | . |
         *   ---------------------
         * (0,0) -> (O,1) -> (O,2) -> (O,3) -> (1,3) -> (2,3) -> (2,4) 6 STEPS
         *
         * steps
         * add the starting node to a queue
         * while queue is not empty go to surrounding cells
         * if cell is a .
         */
        String[][] grid = new String[5][5];
        grid[0][0] = ".";
        grid[1][0] = ".";
        grid[2][0] = "#";
        grid[3][0] = ".";
        grid[4][0] = ".";

        grid[0][1] = ".";
        grid[1][1] = "#";
        grid[2][1] = ".";
        grid[3][1] = "#";
        grid[4][1] = ".";

        grid[0][2] = ".";
        grid[1][2] = "#";
        grid[2][2] = ".";
        grid[3][2] = ".";
        grid[4][2] = ".";

        grid[0][3] = ".";
        grid[1][3] = ".";
        grid[2][3] = ".";
        grid[3][3] = "#";
        grid[4][3] = ".";

        grid[0][4] = ".";
        grid[1][4] = "#";
        grid[2][4] = ".";
        grid[3][4] = ".";
        grid[4][4] = ".";
        //set start and end for console display
        grid[0][0] = "S";
        grid[4][4] = "E";
        System.out.println("GRAPH INPUT");
        System.out.print(" | 0 | 1 | 2 | 3 | 4 |");
        for (int y = 0; y < grid.length; y++) {
            System.out.print("\n" + y + "| ");
            for (int x = 0; x < grid[0].length; x++) {
                System.out.print(grid[x][y] + " | ");
            }
        }
        System.out.println("\nEND GRAPH INPUT");
        grid[0][0] = ".";
        grid[4][4] = ".";
        GraphTheory graphTheory = new GraphTheory();

        Steps steps = graphTheory.solve(grid, new int[]{0, 0}, new int[]{4,4});
        List<String> path = steps.steps;

        System.out.println("expected (0,0) -> (0,1) -> (0,2) -> (0,3) -> (1,3) -> (2,3) -> (2,4) -> (3,4) -> (4,4) 8 STEPS");
        System.out.println("actual   " + steps.toString());
        System.out.println("(0,0) -> (0,1) -> (0,2) -> (0,3) -> (1,3) -> (2,3) -> (2,4) -> (3,4) -> (4,4) 8 STEPS".equals(steps.toString()));
        //drawing the path
        for (int i = 0; i < path.size(); i++) {
            grid[Integer.valueOf(path.get(i).substring(1, 2))][Integer.valueOf(path.get(i).substring(3, 4))] = "X";
        }
        System.out.println("GRAPH OUTPUT");
        System.out.print(" | 0 | 1 | 2 | 3 | 4 |");
        for (int y = 0; y < grid.length; y++) {
            System.out.print("\n" + y + "| ");
            for (int x = 0; x < grid[0].length; x++) {
                System.out.print(grid[x][y] + " | ");
            }
        }
        System.out.println("\nEND GRAPH OUTPUT");
    }
}

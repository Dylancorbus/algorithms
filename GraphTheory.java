import java.util.*;

public class GraphTheory {

    static class Steps {
        List<int[]> steps;
        String[] nodeOrder;
        int count;

        public Steps() {
            this.steps = new ArrayList<>();
            this.count = 0;
        }

        public Steps(int i) {
            this.steps = new ArrayList<>();
            this.count = 0;
            this.nodeOrder = new String[i];
        }

        public String[] getNodeOrder() {
            return nodeOrder;
        }

        public void setNodeOrder(String[] nodeOrder) {
            this.nodeOrder = nodeOrder;
        }

        public List<int[]> getSteps() {
            return steps;
        }

        public void setSteps(List<int[]> steps) {
            this.steps = steps;
        }

        public Steps addSteps(int x, int y) {
            this.steps.add(new int[]{x, y});
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
            steps = null;
            return this;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = steps.size() - 1; i >= 0; i--) {
                if(i != 0) builder.append(steps.get(i)[1] + "," +steps.get(i)[0]  + " -> ");
                else builder.append(steps.get(i));
            }
            return builder.toString() + " " + this.count + " STEPS";
        }
    }


    public Steps sortDependencyGraph(Map<String, String[]> adjList) {
        //get the nodes in the graph
        String[] nodes = adjList.keySet().toArray(new String[]{});
        Map<String, Boolean> visited = new HashMap<>();
        //reate the result array with a size the same as # of nodes
        Steps steps = new Steps(nodes.length);
        //i is the node we are currently processing
        int lastI = 0;
        for (int i = 0; i < nodes.length; i++) {
            //if we havent visited the node visit it
            if(!visited.containsKey(nodes[i])) {
                //search the node
                lastI = dfs(adjList, nodes, i, steps, visited, lastI);

            }
        }
        return steps;
    }

    private int dfs(Map<String, String[]> adjList, String[] nodes, int at, Steps steps, Map<String, Boolean> visited, int idx) {
        //node is now visited
        visited.put(nodes[at], true);
        //get nodes edges
        String[] edges = adjList.get(nodes[at]);
        //for each edge
        for (int i = 0; i < edges.length; i++) {
            //if its not visited visit it
            if (!visited.containsKey(edges[i]))  {
                //pass in the list, edges
               idx = dfs(adjList, edges , i, steps, visited, idx);
            }

        }
        steps.nodeOrder[idx] = nodes[at];
        return idx + 1;
    }

    public Steps solveMaze(String[][] grid, int[] start, int[] end) {
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
        int[] dy = new int[] {-1, 1, 0, 0};
        //direction y
        int[] dx = new int[] {0, 0, 1, -1};

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
            if(grid[yy][xx].equals("#")) continue;
            //add the coordinates the the queue
            xQueue.add(xx);
            yQueue.add(yy);

            //make it as visited
            visited.put(visitedKey, true);
//            grid[yy][xx] = ("0");
            //previous node
            previous.put(visitedKey, currentX + "," + currentY);
            //add this neighboring node to the next layer
            nodesInNextLayer++;
        }
        //return this since java is pass by value and this variable is only in method scope
        return nodesInNextLayer;
    }


    public static Integer GeneratePreferredNumbers(int[] listOfNotPreffered, int max, int min)
    {
        Random rand = new Random();
        int randomNum;
        int retry = 2; //increasing this lessons the likely of our non-preferred numbers to show up
        HashSet<Integer> notPrefer = new HashSet<>();

        //add all the numbers we don't want to generate into a HashSet for easy lookup
        for(int index = 0; index < listOfNotPreffered.length; index++)
            notPrefer.add(listOfNotPreffered[index]);

        do {
            randomNum = rand.nextInt((max - min) + 1) + min;
            if(notPrefer.contains(randomNum))
            {
                retry--;
            }
            //we found a good value, let's return it
            else{
                retry = 0;
            }
        } while (retry > 0);

        return randomNum;
    }

    public static void main(String[] args) {
        /** PATH FINDING ALGORITHM
         * 1. FIRST DETERMINE IF A PATH EXISTS(BFS)
         * 2. KEEP TRACK OF HOW YOU GET TO EACH NODE OR CELL
         * 3. WHEN A PATH TO END IS FOUND REBUILD LIST OF NODES USED TO GET TO END
         * 4. RETURN LIST REVERSED(END -> START => START -> END
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
        int times = 25;
        int[] listOfNumbers = {1, 2, 3};
        int max = 1, min = 0;
        String[][] grid = new String[100][100];
        String[] strings = {
                ".",
                "#",
        };

        while(times-- > 0) {
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    grid[i][j] = strings[GeneratePreferredNumbers(listOfNumbers, max, min)];
                }
            }
            System.out.print(GeneratePreferredNumbers(listOfNumbers, max, min) + " ");
        }
        System.out.println("\n");
//        set start and end for console display
        grid[0][0] = "S";
        grid[99][99] = "E";
        System.out.println("GRAPH INPUT");
        for (int i = 0; i < grid[0].length; i++) {
            System.out.print(" | " + i);
        }
        System.out.print(" |");
        for (int y = 0; y < grid.length; y++) {
            System.out.print("\n" + y + "| ");
            for (int x = 0; x < grid[0].length; x++) {
                System.out.print(grid[y][x] + " | ");
            }
        }
        System.out.println("\nEND GRAPH INPUT");
        grid[0][0] = ".";
        grid[99][99] = ".";
        GraphTheory graphTheory = new GraphTheory();

        Steps steps = graphTheory.solveMaze(grid, new int[]{0, 0}, new int[]{99,99});
        System.out.println(steps);
        List<int[]> path = steps.steps;

        System.out.println("expected (0,0) -> (0,1) -> (0,2) -> (0,3) -> (1,3) -> (2,3) -> (2,4) -> (3,4) -> (4,4) 8 STEPS");
//        System.out.println("actual   " + steps.toString());
//        System.out.println("(0,0) -> (0,1) -> (0,2) -> (0,3) -> (1,3) -> (2,3) -> (2,4) -> (3,4) -> (4,4) 8 STEPS".equals(steps.toString()));
//        drawing the path
        for (int i = 0; i < path.size(); i++) {
            grid[path.get(i)[1]][path.get(i)[0]] = "X";
        }
        System.out.println("GRAPH OUTPUT");
        for (int i = 0; i < grid[0].length; i++) {
            System.out.print(" |" + i);
        }
        System.out.print(" |");
        for (int y = 0; y < grid.length; y++) {
            System.out.print("\n" + y + "| ");
            for (int x = 0; x < grid[0].length; x++) {
                System.out.print(grid[y][x] + " | ");
            }
        }
        System.out.println("\nEND GRAPH OUTPUT");


        /** GRAPH SORTING ALGORITHM(TOPOLOGICAL SORT)
         * 1. STORE GRAPH AS AN ADJACENCY LIST(HashMap<Node,List<Node>>)
         * 2. GET THE NUMBER OF NODES IN THE GRAPH
         * 3. INITIALIZE A HashMap<Node,boolean> TO STORE INFO IF THE NODE WAS VISITED OR NOT
         * 4. INITIALIZE A List<Node> TO CONTAIN THE FINAL LIST OF NODES WITH A LENGTH EQUAL TO THE NUMBER OF NODES
         * 5. START AT THE FIRST NODE IN THE ADJ LIST AND DFS
         * 6. IF AT FINAL NODE AND NODE HAS NOT BEEN VISITED ADD IT TO THE LIST OF NODES
         * 7. RETURN THE ORDERED LIST OF NODES IN THE GRAPH
         * We need to compile a large enterprise program with a lot of dependencies
         * to external libraries. Given an adjacency list, bubbleSort the nodes in order of
         * least dependent to most dependent nodes.
         * {
         *      vertx: [core, jackson, netty],
         *      core: [oracle],
         *      sun: [],
         *      oracle: [sun],
         *      netty: [nio],
         *      nio: [sun],
         *      jackson: [oracle]
         * }
         * STEPS: sun -> nio -> netty -> oracle -> core -> jackson -> vertx
         *
         */
        //test input
        Map<String, String[]> adjList = new HashMap<>();
        adjList.put("vertx", new String[]{"core", "jackson", "netty"});
        adjList.put("core", new String[]{"oracle"});
        adjList.put("sun", new String[]{});
        adjList.put("oracle", new String[]{"sun"});
        adjList.put("netty", new String[]{"nio"});
        adjList.put("nio", new String[]{"sun"});
        adjList.put("jackson", new String[]{"oracle"});
        Steps steps2 = graphTheory.sortDependencyGraph(adjList);
        for (int i = 0; i < steps2.nodeOrder.length; i++) {
            System.out.println(steps2.nodeOrder[i]);
        }
        System.out.println(Math.floor(6/4));

        PriorityQueue<Integer> pq = new PriorityQueue<>();

        pq.add(6);
        pq.add(3);
        pq.add(4);
        pq.add(1);
        pq.add(2);
        pq.add(5);
        while (!pq.isEmpty()) {
            System.out.println(pq.poll());
        }

    }
}

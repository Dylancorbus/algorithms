import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {

    static class Node {
        public String name;
        public List<Node> childNodes;

        Node(String name, Node... nodes) {
            this.name = name;
            this.childNodes = new ArrayList<>();
            for (int i = 0; i < nodes.length; i++) {
                this.childNodes.add(nodes[i]);
            }
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public Node commonBoss(Node start, String emp1, String emp2) {
        Queue<Node> queue = new LinkedList<>();
        //add the first item to the queue
        //O(n)
        queue.add(start);
        Node current = null;
        Map<String, Node> found = new HashMap<>();
        //while queue is not empty
//        O(V)
        while (!queue.isEmpty()) {
//            process the current node
            current = queue.poll();
//            get the child node
//            O(E)
            for (int i = 0; i < current.childNodes.size(); i++) {
//                add them to a map as we go
                found.put(current.childNodes.get(i).name, current);
                //if the map has the key of the emps passed in we found the common boss just need to get it
                if (found.containsKey(emp1) && found.containsKey(emp2)) {
                    //need to get the nodes that both of the emps point to
                    Node potentialAnswer = found.get(emp1);
                    Node potentialAnswer2 = found.get(emp2);
                    //if they are the same then return the first one
                    if (potentialAnswer.name.equals(potentialAnswer2.name)) {
                        return potentialAnswer;
                    } else {
                        //else set previous = to the node that the answer 2 points to unless it doesnt point to anything then return it instead
                        return commonNode(found, potentialAnswer);
                    }
                }
                //add to the queue to coninue processing
                queue.add(current.childNodes.get(i));
            }
        }
        return current;
    }

    public Node commonNode(Map<String, Node> found, Node current) {
        if (found.get(current.name) == null) {
            return current;
        }
        return commonNode(found, found.get(current.name));
    }

    public int numOfSteps(int n, int[] moves) {
        //1. make sure steps is over 1
        if (n == 0) return 1;
        int[] nums = new int[n + 1];
        nums[0] = 1;
        for (int i = 1; i <= n; i++) {
            int total = 0;
            for (int j = 0; j < moves.length; j++) {
                if (i - moves[j] >= 0) {
                    total += nums[i - moves[j]];
                }
            }
            nums[i] = total;
        }
        //if steps is 1 make sure we have that move
        return nums[n];

    }

    public boolean findSumSorted(int[] nums, int findSum) {
        boolean sumExists = false;
        if (nums.length < 2) return sumExists;
        int end = nums.length - 1;
        for (int i = 0; i < end; ) {
            int sum = nums[i] + nums[end];
            if (sum == findSum) return true;
            else if (sum > findSum) {
                --end;
            } else {
                i++;
            }
        }
        return sumExists;
    }

    public boolean findSum(int[] nums, int findSum) {
        if (nums.length < 2) return false;
        //make a map to hold all the values we have seen
        Set<Integer> pastValues = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            //get the compliment value1
            int have = nums[i];
            int need = findSum - have;
            //add it to the map
            if (pastValues.contains(need)) {
                return true;
            }
            pastValues.add(have);
            //check if we have seen the compliment before

        }
        return false;
    }

    public boolean binarySearch(int[] nums, int find, int start, int end) {
        int midPointIndex = (end - start) / 2 + start;
        if (find < nums[start] || find > nums[end]) {
            return false;
        } else if (find < nums[midPointIndex]) {
            //on the left side of the array
            //n = nums.length O(log(n)) cut nums in half each time until we get to 1
            return binarySearch(nums, find, start, midPointIndex);
        } else if (find > nums[midPointIndex]) {
            // on the right side of the array
            return binarySearch(nums, find, midPointIndex, end);
        } else {
            return true;
        }
    }

    public boolean binarySearchIterative(int[] nums, int find) {
        boolean found = false;
        int start = 0, end = nums.length;
        while (found != true) {
            int midPointIndex = (end - start) / 2 + start;
            if (find < nums[start] || find > nums[end - 1]) {
                break;
            } else if (find < nums[midPointIndex]) {
                //on the left side of the array
                end = midPointIndex;
            } else if (find > nums[midPointIndex]) {
                // on the right side of the array
                start = midPointIndex;
            } else {
                found = true;
            }
        }
        return found;
    }

    //    log(V + E)
    public int[] sortDependencies(Map<Integer, int[]> adjList) {
        int[] sorted = new int[adjList.size()];
        Set<Integer> visited = new HashSet<>();
        Set<Integer> visiting = new HashSet<>();
        AtomicInteger index = new AtomicInteger();
        adjList.forEach((k, v) -> {
            if (!visited.contains(k)) {
                index.set(dfs(adjList, k, index.get(), visited, visiting, sorted));
            }
        });
        return sorted;
    }

    private int dfs(Map<Integer, int[]> adjList, int edge, int index, Set<Integer> visited, Set<Integer> visiting, int[] sorted) {
        visiting.add(edge);
        int[] newEdges = adjList.get(edge);
        for (int i = 0; i < newEdges.length; i++) {
            if (visiting.contains(newEdges[i])) throw new RuntimeException("Cycle detected");
            if (!visited.contains(newEdges[i])) {
                index = dfs(adjList, newEdges[i], index, visited, visiting, sorted);
            }
        }
        visiting.remove(edge);
        visited.add(edge);
        sorted[index] = edge;
        return index + 1;
    }

    public int[] bubbleSort(int[] ints) {
        for (int i = 0; i < ints.length; i++) {
            for (int j = i + 1; j < ints.length; j++) {
                swap(ints, i, j);
            }
        }
        return ints;
    }

    public void swap(int[] ints, int i, int j) {
        if (ints[i] > ints[j]) {
            int x = ints[i];
            ints[i] = ints[j];
            ints[j] = x;
        }
    }

    public char firstNonDuplicate(String str) {
        Map<Character, Integer> count = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            if (count.containsKey(str.charAt(i))) {
                count.put(str.charAt(i), count.get(str.charAt(i)) + 1);
                continue;
            }
            count.put(str.charAt(i), 1);
        }
        for (int i = 0; i < str.length(); i++) {
            if (count.get(str.charAt(i)) < 2) {
                return str.charAt(i);
            }
        }
        return '_';
    }

    public int firstDuplicate(int[] str) {
        for (int i = 0; i < str.length; i++) {
            if (str[str[i] - 1] != Math.abs(str[str[i] - 1])) return str[i];
            str[str[i] - 1] = -str[str[i] - 1];
        }
        return -1;
    }

    public void swap(int[][] ints, int i, int j) {
        int temp = ints[i][j];
        ints[i][j] = ints[j][i];
        ints[j][i] = temp;
    }

    public int[][] rotateImage(int[][] image, int degree) {
        int rotations = degree / 90; //will give us the number of times to rotate the image 90 = 1 180 = 2 270 = 3 360 =4
        while (rotations-- > 0) {
            for (int i = 0; i < image.length; i++) {
                for (int j = i; j < image[i].length; j++) {
                    swap(image, i, j);
                }
            }
            for (int i = 0; i < image.length; i++) {
                for (int j = 0; j < image[i].length / 2; j++) {
                    int temp = image[i][j];
                    image[i][j] = image[i][image[i].length - j - 1];
                    image[i][image[i].length - j - 1] = temp;
                }
            }
        }
        return image;
    }

    public static int[] twoNumberSum(int[] array, int targetSum) {
        Set<Integer> compliments = new HashSet<>();
        //time and space O(n)
        for(int i = 0; i < array.length; i++) {
            int compliment = targetSum - array[i];
            if(compliments.contains(compliment)) {
                int[] answer = new int[2];
                answer[0] = compliment;
                answer[1] = array[i];
                return answer;
            }
            Integer[] triplet = new Integer[]{1,2,3,4};
            compliments.add(compliment);
        }
        return new int[0];
    }

    public static List<Integer[]> threeNumberSum(int[] array, int targetSum) {
        // Write your code here.
        //[-8, -6, 1, 2, 3, 5, 6, 12]
        //O(nlog(n))
        Arrays.sort(array);
        List<Integer[]> answer = new ArrayList<>();
        int i = 0;
        int next = i + 1;
        int j = array.length - 1;
        while (i < array.length - 1) {
            if((array[i] + array[next] + array[j]) < targetSum) {
                ++next;
            } else if((array[i] + array[next] + array[j]) > targetSum) {
                --j;
            } else {
                answer.add(new Integer[]{array[i], array[next], array[j]});
                ++next;
                --j;
            }
            if(next >= j) {
                ++i;
                next = i + 1;
                j = array.length -1;
            }
        }
        return answer;
    }


    public static void main(String[] args) {

        Node dataEntry = new Node("dataEntry", new Node[]{});
        Node coordinator = new Node("coordinator", new Node[]{});
        Node bookkeeper = new Node("bookkeeper", new Node[]{});
        Node technician = new Node("technician", new Node[]{});
        Node dataAnalyst = new Node("dataAnalyst", new Node[]{});
        Node accountant = new Node("accountant", new Node[]{});
        Node accountingManager = new Node("accountingManager", accountant, bookkeeper);
        Node engineer = new Node("engineer", new Node[]{});
        Node salesman = new Node("salesman", new Node[]{});
        Node salesManager = new Node("salesManager", salesman, dataAnalyst, dataEntry, coordinator);
        Node engineeringManager = new Node("engineeringManager", dataAnalyst, engineer, dataEntry, technician);
        Node boss = new Node("ceo", salesManager, engineeringManager, accountingManager);
        Test test = new Test();
        //find the common boss of 2 employees
        System.out.println(test.commonBoss(boss, "bookkeeper", "salesman"));
        //find if there is a path between 2 nodes -> [] or [node1, node2]

        System.out.println(test.numOfSteps(4, new int[]{1, 2}));
        //1. make sure steps is over 1
        //2. for each move 
        //3. add move up with itself until if it equlas the num of steps
        //4. if it does increment a counter
        //5. return the counter
        int[] nums = {1, 2, 4, 4};
        int[] nums1 = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println(test.findSum(nums, 8));
        System.out.println(test.findSumSorted(nums, 8));
        System.out.println(test.binarySearch(nums1, 2, 0, nums1.length - 1));
        System.out.println(test.binarySearchIterative(nums1, 1));

        Map<Integer, int[]> adjList = new HashMap<>();
        adjList.put(0, new int[]{});
        adjList.put(1, new int[]{0});
        adjList.put(2, new int[]{0});
        adjList.put(3, new int[]{1, 2});
        adjList.put(4, new int[]{3});
        adjList.put(5, new int[]{1, 2, 4});
        adjList.put(6, new int[]{3, 4});
//        0, 1, 2, 3, 4
        try {
            System.out.println(Arrays.toString(test.sortDependencies(adjList)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int[][] image = new int[4][4];
        image[0] = new int[]{1,2,3,4};
        image[1] = new int[]{5,6,7,8};
        image[2] = new int[]{9,10,11,12};
        image[3] = new int[]{13,14,15,16};

        System.out.println(Arrays.toString(new int[]{1, 6, 3, 7, 2, 4, 9, 8, 5,4,5,6,7,8,7,6,5,6,7,8,7,6,44,3,2,1,2,939,5}));
        System.out.println(Arrays.toString(test.bubbleSort(new int[]{1, 6, 3, 7, 2, 4, 9, 8, 5,4,5,6,7,8,7,6,5,6,7,8,7,6,44,3,2,1,2,939,5})));
        System.out.println(test.firstNonDuplicate("abbccdeefgghh"));
        System.out.println(test.firstDuplicate(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 6, 1, 2, 3, 4, 5, 6, 7, 8}));

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                System.out.print(image[i][j]);
            }
            System.out.print("\n");
        }
        test.rotateImage(image, 360);
        // test.rotateImage(image, 90);
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                System.out.print(image[i][j]);
            }
            System.out.print("\n");
        }

        System.out.println(Arrays.toString(Test.twoNumberSum(new int[] {3, 5, -4, 8, 11, 1, -1, 6}, 10)));
        Test.threeNumberSum(new int[] {12, 3, 1, 2, -6, 5, -8, 6}, 0).forEach(str -> Arrays.toString(str));
    }
}
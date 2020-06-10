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
            String s = new String();

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


//    public List<String[]> avail(List<List<String>> cal1, List<List<String>> cal2, List<String> rang1, List<String> rang2, int duration) {
//        //O(1)
//        List<String[]> unavailTimes = new ArrayList<>();
//        List<String[]> potentialMeetingTimes = new ArrayList<>();
//        String previousStart = "";
//        String previousEnd = "";
//        int i = 0;
//        int j = 0;
//        int count = 0;
//        //O(n + m)
//        while(i < cal1.size() && j < cal2.size()) {
//            //O(1)
//            String[] unavailSlot = new String[2];
//            String stime1 = cal1.get(i).get(0);
//            String stime2 = cal2.get(j).get(0);
//            String etime1 = cal1.get(i).get(1);
//            String etime2 = cal2.get(j).get(1);
//            //O(1)
//            if(compareTime(stime1, stime2) == 1) {
//                unavailSlot[0] = stime2;
//            } else {
//                unavailSlot[0] = stime1;
//            }
//            if(compareTime(etime1, etime2) == 1) {
//                if(compareTime(stime1, etime2) == 1) {
//                    unavailSlot[1] = etime2;
//                    ++j;
//                } else {
//                    unavailSlot[1] = etime1;
//                    ++i;
//                }
//            } else {
//                unavailSlot[1] = etime2;
//                ++i;
//            }
//            if(!unavailSlot[0].equals(previousStart) && !unavailSlot[1].equals(previousEnd)) {
//                unavailTimes.add(unavailSlot);
//                previousStart  = unavailSlot[0];
//                previousEnd  = unavailSlot[1];
//            }
//            ++count;
//        }
//        System.out.println(count);
//        unavailTimes.forEach(strings -> System.out.print(Arrays.toString(strings)));
//        System.out.println();
//        //O(n)
//        for (int j2 = 0; j2 < unavailTimes.size() - 1; j2++) {
//            String currentEnd = unavailTimes.get(j2)[1];
//            String nextStart = unavailTimes.get(j2 + 1)[0];
//            String[] availSlot = new String[2];
//            availSlot[0] = currentEnd;
//            availSlot[1] = nextStart;
//            if(withinRange(rang1, rang2, availSlot[0], availSlot[1], duration)) {
//                potentialMeetingTimes.add(availSlot);
//            }
//        }
//
//
//        return potentialMeetingTimes;
//    }
    static class StringMeeting {
        public String start;
        public String end;

        public StringMeeting() {}

        public StringMeeting(String start, String end) {
            this.start = start;
            this.end = end;
        }
    }

    public static List<StringMeeting> calendarMatching(
            List<StringMeeting> calendar1,
            StringMeeting dailyBounds1,
            List<StringMeeting> calendar2,
            StringMeeting dailyBounds2,
            int meetingDuration) {
        // Write your code here.
        List<StringMeeting> unavailTimes = new ArrayList<>();
        List<StringMeeting> potentialMeetingTimes = new ArrayList<>();
        String previousStart = "";
        String previousEnd = "";
        int i = 0;
        int j = 0;
        //O(n + m)
        while (i < calendar1.size() && j < calendar2.size()) {
            //O(1)
            StringMeeting unavailSlot = new StringMeeting();
            String stime1 = calendar1.get(i).start;
            String stime2 = calendar2.get(j).start;
            String etime1 = calendar1.get(i).end;
            String etime2 = calendar2.get(j).end;
            //O(1)
            if (compareTime(stime1, stime2) == 1) {
                unavailSlot.start = stime2;
            } else {
                unavailSlot.start = stime1;
            }
            if (compareTime(etime1, etime2) == 1) {
                if (compareTime(stime1, etime2) == 1) {
                    unavailSlot.end = etime2;
                    ++j;
                } else {
                    unavailSlot.end = etime1;
                    ++i;
                }
            } else {
                unavailSlot.end = etime2;
                ++i;
            }
            if (!unavailSlot.start.equals(previousStart) && !unavailSlot.end.equals(previousEnd)) {
                unavailTimes.add(unavailSlot);
                previousStart = unavailSlot.start;
                previousEnd = unavailSlot.start;
            }
        }
        //O(n)
        for (int j2 = 0; j2 < unavailTimes.size() - 1; j2++) {
            String currentEnd = unavailTimes.get(j2).end;
            String nextStart = unavailTimes.get(j2 + 1).start;
            StringMeeting availSlot = new StringMeeting(currentEnd, nextStart);
            if (withinRange(dailyBounds1, dailyBounds1, availSlot.start, availSlot.end, meetingDuration)) {
                potentialMeetingTimes.add(availSlot);
            }
        }


        return potentialMeetingTimes;
    }

    public static boolean withinRange(StringMeeting rang1, StringMeeting rang2, String stime, String etime, int duration) {
        String rSTime1 = rang1.start;
        String rSTime2 = rang2.start;
        String rETime1 = rang1.end;
        String rETime2 = rang2.end;
        String[] timeArr1 = stime.split(":");
        String[] timeArr2 = etime.split(":");
        int time1 = Integer.parseInt(timeArr1[0]) * 60 + Integer.parseInt(timeArr1[1]);
        int time2 = Integer.parseInt(timeArr2[0]) * 60 + Integer.parseInt(timeArr2[1]);
        if(compareTime(rSTime1, stime) == 1 && compareTime(rSTime2, stime) == 1) {
            return false;
        }
        if(compareTime(rETime1, etime) != 1 && compareTime(rETime2, etime) != 1) {
            return false;
        }

        if ((time2 - time1) < duration) return false;
        return true;
    }

    public static int compareTime(String comp1, String comp2) {
        if(comp1 == null) return 1;
        if(comp2 == null) return -1;
        String[] timeArr1 = comp1.split(":");
        String[] timeArr2 = comp2.split(":");
        int time1 = Integer.parseInt(timeArr1[0]) * 60 + Integer.parseInt(timeArr1[1]);
        int time2 = Integer.parseInt(timeArr2[0]) * 60 + Integer.parseInt(timeArr2[1]);
        if(time1 >= time2) return 1;
        else return -1;
    }

    public static List<Integer> spiralTraverse(int[][] array) {
        int r = 0;
        int c = 0;
        List<Integer> answer = new ArrayList<>();
        Set<Integer> checked = new HashSet<>();
        while(r < array.length && c < array[r].length) {
            int[] newVals = navigate(r, c, array, answer, checked);
            checked.add(array[r][c]);
        }

        return answer;
    }

    static int[] navigate(int row, int col, int[][] array, List<Integer> answer, Set<Integer> checked) {
        int[] newVals = new int[2];
        if(checked.contains(array[row][col])) {
            if(row == 0) {
                ++row;
            } else {
                --row;
            }
            if(col == 0) {
                ++col;
            } else {
                --col;
            }
            newVals[0] = row;
            newVals[1] = col;
            return newVals;
        }
        answer.add(array[row][col]);
        if(col == array[row].length - 1) {
            if(row == array.length - 1) {
                --col;
                newVals[0] = row;
                newVals[1] = col;
                return newVals;
            }
            ++row;
            newVals[0] = row;
            newVals[1] = col;
            return newVals;
        }
        if(col == 0) {
            if(row != 0) {
                row = row == 1 ? col++ == 0 ? row : row : --row;
                newVals[0] = row;
                newVals[1] = col;
                return newVals;
            }
            ++col;

            newVals[0] = row;
            newVals[1] = col;
            return newVals;
        }
        if(row == array.length - 1) {
            --col;
        } else {
            ++col;
        }
        newVals[0] = row;
        newVals[1] = col;
        return newVals;
    }

    public static boolean isValidSubsequence(List<Integer> array, List<Integer> sequence) {
        // Write your code here.

            String arrS = array.toString().substring(1, array.toString().length());;
            String arrS2 = sequence.toString().substring(1, sequence.toString().length());
            if(arrS.contains(arrS2)) return true;
        //space O(n)
        Map<Integer, Integer> seqSet = new HashMap<>();
        //O(n)

        for(int i = 0; i < array.size(); i++) {
            seqSet.put(array.get(i), i);
        }
        //O(m)
        for(int i = 0; i < sequence.size(); i++) {
            if(!seqSet.containsKey(sequence.get(i))) return false;
            if(i < sequence.size() - 1) {
                try {
                    // if(sequence.get(i) == sequence.get(i + 1)) continue;
                    if(seqSet.get(sequence.get(i)) > seqSet.get(sequence.get(i + 1))) return false;
                    if(seqSet.get(sequence.get(i)) == seqSet.get(sequence.get(i + 1))) return false;
                } catch(Exception e) {
                    return false;
                }
            }
        }

        return true;
    }

    public static int[] smallestDifference(int[] arrayOne, int[] arrayTwo) {
        // Write your code here.
        //O(nlog(n))
        Arrays.sort(arrayOne);
        Arrays.sort(arrayTwo);
        int i = 0;
        int start = i;
        int j = arrayTwo.length - 1;
        int end = j;
        int best = Integer.MAX_VALUE;
        int[] smlDiff = new int[2];
        while(i < arrayOne.length && j >= 0) {
            int diff = arrayOne[i] - arrayTwo[j];
            int newBest = Math.abs(diff);
            if( newBest <= best) {
                best = newBest;
                smlDiff[0] = arrayOne[i];
                smlDiff[1] = arrayTwo[j];
            }
            if(i == arrayOne.length - 1 || j == 0) {
                i = ++start;
                j = --end;
                continue;
            }
            if(diff < 0) {
                ++i;
            }
            if(diff > 0) {
                --j;
            }
        }
        return smlDiff;
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

        //[[10:30, 12:00],[13:30, 14:00],[14:30, 16:00],[16:30, 18:00]]
        //[[8:30, 9:00],[10:30, 12:00],[13:00, 17:00]]
        //[8:30, 18:00]
        //[8:00, 18:00]
        //30
        //
        //[[8:30, 9:00],[10:30, 12:00],[13:00, 17:00]]
        //[[9:00,10:30], [12:00,13:00]]
        List<String> meeting1 = new ArrayList<>();
        meeting1.add("8:30");
        meeting1.add("9:00");
        List<String> meeting8 = new ArrayList<>();
        meeting8.add("9:00");
        meeting8.add("9:30");
        List<String> meeting2 = new ArrayList<>();
        meeting2.add("10:30");
        meeting2.add("12:00");
        List<String> meeting3 = new ArrayList<>();
        meeting3.add("13:00");
        meeting3.add("17:00");
        List<String> meeting4 = new ArrayList<>();
        meeting4.add("10:30");
        meeting4.add("12:00");
        List<String> meeting5 = new ArrayList<>();
        meeting5.add("13:30");
        meeting5.add("14:00");
        List<String> meeting6 = new ArrayList<>();
        meeting6.add("14:30");
        meeting6.add("15:00");
        List<String> meeting9 = new ArrayList<>();
        meeting9.add("15:00");
        meeting9.add("15:30");
        List<String> meeting10 = new ArrayList<>();
        meeting10.add("15:30");
        meeting10.add("16:00");
        List<String> meeting11 = new ArrayList<>();
        meeting11.add("16:30");
        meeting11.add("17:00");
        List<String> meeting12 = new ArrayList<>();
        meeting12.add("17:00");
        meeting12.add("17:30");
        List<String> meeting7 = new ArrayList<>();
        meeting7.add("17:30");
        meeting7.add("18:00");

        List<List<String>> cal1 = new ArrayList<>();
        cal1.add(meeting4);
        cal1.add(meeting5);
        cal1.add(meeting6);
        cal1.add(meeting9);
        cal1.add(meeting10);
        cal1.add(meeting11);
        cal1.add(meeting12);
        cal1.add(meeting7);
        List<List<String>> cal2 = new ArrayList<>();
        cal2.add(meeting1);
        cal2.add(meeting8);
        cal2.add(meeting2);
        cal2.add(meeting3);
        List<String> rang1 = new ArrayList<>();
        rang1.add("8:00");
        rang1.add("16:00");
        List<String> rang2 = new ArrayList<>();
        rang2.add("8:30");
        rang2.add("18:00");
        int duration = 60;
//        test.avail(cal1, cal2, rang1, rang2, duration).forEach(strings -> System.out.print(Arrays.toString(strings)));

        System.out.println(Arrays.toString(Test.twoNumberSum(new int[] {3, 5, -4, 8, 11, 1, -1, 6}, 10)));
        Test.threeNumberSum(new int[] {12, 3, 1, 2, -6, 5, -8, 6}, 0).forEach(str -> Arrays.toString(str));
        //1, 1, 1, 1, 1], "sequence": [1, 1, 1]}
        List<Integer> l = new ArrayList();
        l.add(1);
        l.add(1);
        l.add(1);
        l.add(1);
        List<Integer> l2 = new ArrayList();
        l2.add(1);
        l2.add(1);
        l2.add(1);
        System.out.println(Test.isValidSubsequence(l, l2));
        System.out.println(Arrays.toString(Test.smallestDifference(new int[] {10, 1000, 9124, 2142, 59, 24, 596, 591, 124, -123}, new int[] {-1441, -124, -25, 1014, 1500, 660, 410, 245, 530})));
        int[] ar1 = new int[]{1, 2, 3, 4};
        int[] ar2 = new int[]{12, 13, 14, 5};
        int[] ar3 = new int[]{11, 16, 15, 6};
        int[] ar4 = new int[]{10, 9, 8, 7};
        System.out.println(Test.spiralTraverse(new int[][]{ar1, ar2, ar3, ar4}));
    }
}
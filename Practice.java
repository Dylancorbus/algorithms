import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.*;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

public class Practice {

    public static int productSum(List<Object> array) {
        // Write your code here.
        return productSumHelper(array, 1);
    }

    private static int productSumHelper(List<Object> array, int level) {
        int sum = 0;
        for (int i = 0; i < array.size(); i++) {
            var current = array.get(i);
            if(current instanceof Integer) {
                sum += (Integer) array.get(i);
            } else {
                sum += productSumHelper((List<Object>)current, level + 1);
            }
        }
        return sum * level;
    }

    @FunctionalInterface
    interface Lambda
    {
        int nextIndex(int index, int[] array);
    }

    public static boolean hasSingleCycle(int[] array) {
        // Write your code here.
         Lambda func = (x, a) -> {
             int jump = a[x];
             int nextIndex = (x + jump) % a.length;
             return nextIndex >= 0 ? nextIndex : nextIndex + a.length;
        };
        int counter = 0;
        int i = 0;
        while (counter < array.length){
            if(i == 0 && counter > 0) return false;
            counter++;
            i = func.nextIndex(i, array);
        }
        return i == 0;
    }

    static int nextIndex(int index, int[] array) {
        int jump = array[index];
        int nextIndex = (index + jump) % array.length;
        return nextIndex >= 0 ? nextIndex : nextIndex + array.length;
    }

    public static List<Integer> topologicalSort(List<Integer> jobs, List<Integer[]> deps) {
        //creat adjacency list
        Map<Integer, List<Integer>> adjList = jobs.stream().collect(toMap(k -> k, v -> new ArrayList<>()));
        deps.forEach(arr -> adjList.get(arr[1]).add(arr[0]));
        List<Integer> visited = new ArrayList<>();
        HashSet<Integer> visiting = new HashSet<>();
        for (int i = 0; i < jobs.size(); i++) {
            int flag = dfs(adjList, visited, visiting, jobs.get(i));
            if(flag == -1) return new ArrayList<>();
        }
        return visited;
    }
    //return value indicates a cycle or not, -1 yes 1 no
    private static int dfs(Map<Integer, List<Integer>> adjList, List<Integer> visited, HashSet<Integer> visiting, int node) {
        List<Integer> children = adjList.get(node);
        if (visited.contains(node)) return 1;
        visiting.add(node);
        for (int i = 0; i < children.size(); i++) {
            if(visiting.contains(children.get(i))) {
                return -1;
            }
            int x = dfs(adjList, visited, visiting, children.get(i));
            if(x == -1) return x;
        }
        visiting.remove(node);
        visited.add(node);
        return 1;
    }


    public static int longestPeak(int[] array) {
        if (array.length < 3) return 0;
        //find the peaks
        List<Integer> peaks = findPeaks(array);
        //traverse peaks and return the longest
        return traversePeaks(peaks, array);
    }

    private static int traversePeaks(List<Integer> peaks, int[] array) {
        int longestPeak = 0;
        for (int i = 0; i < peaks.size(); i++) {
            List<Integer> peak = new ArrayList<>();
            peak.add(array[peaks.get(i)]); //4
            int left = peaks.get(i), right = left; //4, 4
            int leftPrev = array[left], rightPrev = array[right];
            //O(n)
            while (left-- > 0) { //3 >= 0
                if (array[left] >= leftPrev) break;
                peak.add(array[left]);
                leftPrev = array[left];
            }
            //O(n)
            while (right++ < array.length - 1) {
                if (array[right] >= rightPrev) break;
                peak.add(array[right]);
                rightPrev = array[right];
            }
            if (peak.size() >= 3) if (peak.size() > longestPeak) longestPeak = peak.size();
        }
        return longestPeak;
    }

    //returns the list of peaks
    private static List<Integer> findPeaks(int[] array) {
        int i = 0;
        var previous = array[0];
        List<Integer> list = new ArrayList<>();
        //O(n)
        while (i < array.length - 1) {
            var current = array[i];
            var next = array[i + 1];
            //we have a peak
            if (current > previous && current > next) {
                list.add(i);
                i += 2;
            } else ++i;
            previous = current;
        }
        return list;
    }

    public static void main(String[] args) {
        try {
            List<String> lines = Utils.readFile("lines.txt");
            lines.stream()
                    .map(str -> new RainForest.LogLine(str))
                    .collect(groupingBy(RainForest.LogLine::getId, mapping(RainForest.LogLine::getPath, toList())))
                    .entrySet()
                    .stream()
                    .map(entry -> String.format("%s %s", entry.getKey(), entry.getValue()
                            .stream()
                            .collect(groupingBy(identity(), counting()))
                            .entrySet()
                            .stream()
                            .sorted((e1, e2) -> e1.getValue() > e2.getValue() ? -1 : 1)
                            .map(mE -> mE.getKey())
                            .limit(3)
                            .collect(joining("/"))))
                    .collect(toMap(k -> k.split(" ")[0], v -> v.split(" ")[1]))
                    .entrySet()
                    .stream()
                    .forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //print 1 thru 10 in asc and desc
        IntStream.range(1, 10).forEach(System.out::print);
        System.out.println();
        IntStream.range(1, 10).mapToObj(Integer::valueOf).sorted(reverseOrder()).forEach(System.out::print);
        System.out.println();
//        find the most frequent item in a list
        Stream.of("test", "code", "test", "code", "code", "deploy")
                .collect(groupingBy(identity(), counting()))
                .entrySet()
                .stream()
                .max(comparing(Map.Entry::getValue))
                .get()
                .getKey()
                //this is extra code to print the value
                .chars()
                .mapToObj(x -> Character.valueOf((char) x))
                .forEach(System.out::println);

        Stream.of("1 phx 88.6", "2 sj 75.0", "3 cpg 0")
                .map(str -> str.split(" "))
                .sorted(comparing(str -> str[1]))
                .sorted(comparing(str -> str[2]))
                .map(strings -> Arrays.toString(strings))
                .forEach(System.out::println);

        Stream.of(12, 1, 10, 2, 24, 32, 8, 100)
                .collect(mapping(x -> x, maxBy(Integer::compareTo)))
                .ifPresent(System.out::println);
        Employee emp1 = new Employee("1", 30, 85000, "Technology");
        Employee emp2 = new Employee("2", 47, 75000, "Operations");
        Employee emp3 = new Employee("3", 50, 80001, "Quality");
        Employee emp4 = new Employee("4", 42, 80000, "Sales");
        Employee emp5 = new Employee("5", 26, 60000, "Technology");
        Employee emp6 = new Employee("6", 30, 95000, "Technology");
        Employee emp7 = new Employee("7", 21, 60000, "Technology");
        Employee emp8 = new Employee("8", 25, 85000, "Technology");
        Employee emp9 = new Employee("9", 44, 85000, "Technology");
        Employee emp10 = new Employee("10", 35, 80000, "Technology");
        //sorts employees in asc order based on salary
        System.out.println("employees in asc order based on salary");
        Stream.of(emp1, emp2, emp3, emp4, emp5, emp6, emp7, emp8, emp9, emp10)
                .sorted(comparing(Employee::getSalary))
                .peek(System.out::println)
                .forEach(e -> System.out.println("-----------------------------"));
        //sorts employees in desc order based on salary
        System.out.println("employees in desc order based on salary");
        Stream.of(emp1, emp2, emp3, emp4, emp5, emp6, emp7, emp8, emp9, emp10)
                .sorted((f1, f2) -> Integer.compare(f2.getSalary(), f1.getSalary()))
                .peek(System.out::println)
                .forEach(e -> System.out.println("-----------------------------"));
        //finds the avg age of employees in each department
        System.out.println("avg age of employees in each department");
        Stream.of(emp1, emp2, emp3, emp4, emp5, emp6, emp7, emp8, emp9, emp10)
                .collect(groupingBy(Employee::getDepartment, averagingDouble(Employee::getAge)))
                .entrySet()
                .stream()
                .peek(System.out::println)
                .forEach(e -> System.out.println("-----------------------------"));
        //finds the top employees in each department based on age
        System.out.println("top employees in each department based on age");
        Stream.of(emp1, emp2, emp3, emp4, emp5, emp6, emp7, emp8, emp9, emp10)
                .collect(groupingBy(Employee::getDepartment, maxBy(comparing(Employee::getAge))))
                .entrySet()
                .stream()
                .map(o -> o.getValue().get())
                .peek(System.out::println)
                .forEach(e -> System.out.println("-----------------------------"));
        //finds the top employees in each department based on salary
        System.out.println("top employees in each department based on salary");
        Stream.of(emp1, emp2, emp3, emp4, emp5, emp6, emp7, emp8, emp9, emp10)
                .collect(groupingBy(Employee::getDepartment, maxBy(comparing(Employee::getSalary))))
                .entrySet()
                .stream()
                .map(o -> o.getValue().get())
                .peek(System.out::println)
                .forEach(e -> System.out.println("-----------------------------"));
        //find the highest paying department
        System.out.println("highest paying department on avg");
        Stream.of(emp1, emp2, emp3, emp4, emp5, emp6, emp7, emp8, emp9, emp10)
                .collect(groupingBy(Employee::getDepartment, averagingInt(Employee::getSalary)))
                .entrySet()
                .stream()
                .max(comparing(Map.Entry::getValue))
                .ifPresent(System.out::println);
        //find the lowest paying department
        System.out.println("lowest paying department on avg");
        Stream.of(emp1, emp2, emp3, emp4, emp5, emp6, emp7, emp8, emp9, emp10)
                .collect(groupingBy(Employee::getDepartment, averagingInt(Employee::getSalary)))
                .entrySet()
                .stream()
                .min(comparing(Map.Entry::getValue))
                .ifPresent(System.out::println);
        //find the department with the highest age on avg
        System.out.println("department with the highest age on avg");
        Stream.of(emp1, emp2, emp3, emp4, emp5, emp6, emp7, emp8, emp9, emp10)
                .collect(groupingBy(Employee::getDepartment, averagingInt(Employee::getAge)))
                .entrySet()
                .stream()
                .max(comparing(Map.Entry::getValue))
                .ifPresent(System.out::println);
        //find the department with the lowest age on avg
        System.out.println("department with the lowest age on avg");
        Stream.of(emp1, emp2, emp3, emp4, emp5, emp6, emp7, emp8, emp9, emp10)
                .collect(groupingBy(Employee::getDepartment, averagingInt(Employee::getAge)))
                .entrySet()
                .stream()
                .min(comparing(Map.Entry::getValue))
                .ifPresent(System.out::println);

        Map<String, Integer> map = new ConcurrentHashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        map.put("D", 4);
        map.put("E", 5);
        map.put("F", 6);
        map.put("G", 7);
        map.put("H", 8);
        Deleter deleter = new Deleter(map, "dlt");
        Reader reader = new Reader(map, "read");
//        reader.start();
//        deleter.start();
        System.out.println(longestPeak(new int[]{1, 2, 3, 3, 4, 0, 10, 6, 5, -1, -3, 2, 3}));

        //tdd
//        List<Integer> answer1 = topologicalSort(Arrays.asList(1, 2, 3, 4), Arrays.asList(new Integer[]{1, 2}, new Integer[]{1, 3}, new Integer[]{3, 2}, new Integer[]{4, 2}, new Integer[]{4, 3}));
        List<Integer> answer2 = topologicalSort(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), Arrays.asList(new Integer[]{3, 1}, new Integer[]{8, 1}, new Integer[]{8, 7}, new Integer[]{5, 7}, new Integer[]{5, 2}, new Integer[]{1, 4}, new Integer[]{1, 6}, new Integer[]{1, 2}, new Integer[]{7, 6}));

//        System.out.println(String.format("Test %s: %s", Arrays.asList(4, 1, 3, 2).equals(answer1) || Arrays.asList(1, 4, 3, 2).equals(answer1) ? "PASSED" : "FAILED", answer1));
        System.out.println(String.format("Test %s: %s", Arrays.asList(4, 1, 3, 2).equals(answer2) || Arrays.asList(1, 4, 3, 2).equals(answer2) ? "PASSED" : "FAILED", answer2));
        System.out.println(hasSingleCycle(new int[]{10, 11, -6, -23, -2, 3, 88, 909, -26}));

        System.out.println(productSum(Arrays.asList(5, 2, Arrays.asList(7, -1), 3, Arrays.asList(6, Arrays.asList(-13, 8), 4))));
    }

    static class Deleter extends Thread {
        Map<String, Integer> data;

        public Deleter(Map<String, Integer> data, String name) {
            super(name);
            this.data = data;
        }

        @Override
        public void run() {
            super.run();
            Iterator<String> itr = data.keySet().iterator();
            while (itr.hasNext()) {
                var key = itr.next();
                System.out.println(String.format("%s REMOVED %s = %s", this.getName(), key, data.remove(key)));

            }
        }
    }

    static class Reader extends Thread {
        Map<String, Integer> data;

        public Reader(Map<String, Integer> data, String name) {
            super(name);
            this.data = data;
        }

        @Override
        public void run() {
            super.run();
            Iterator<String> itr = data.keySet().iterator();
            while (itr.hasNext()) {
                var key = itr.next();
                System.out.println(String.format("%s READ %s = %s", this.getName(), key, data.get(key)));
            }
        }
    }

    static class Employee {
        String name;
        int age;
        int salary;
        String department;

        public Employee(String name, int age, int salary, String department) {
            this.name = name;
            this.age = age;
            this.salary = salary;
            this.department = department;
        }

        @Override
        public String toString() {
            return name + " " + age + " " + salary + " " + department;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getSalary() {
            return salary;
        }

        public void setSalary(int salary) {
            this.salary = salary;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }
    }


}

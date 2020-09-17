import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class Amazon {
    static PrintStream out = System.out;
    static Map<String, Integer> idxMap = new HashMap<>();
    static String[] recentItems = new String[10];

    private static Map<String, List<Integer>> mapify(List<String> lines) {
        Map<String, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            String[] current = lines.get(i).split(" ");
            if (map.containsKey(current[0])) {
                map.get(current[0]).add(i);
                continue;
            }
            List<Integer> locations = new ArrayList<>();
            locations.add(i);
            map.put(current[0], locations);
        }
        return map;
    }

    private static void findSequence(List<String> lines, List<Integer> v, String k, Map<String, String> answer) {
        int i = 0;
        int best = -1;
        String topSeq = null;
        Map<String, Integer> countMap = new HashMap<>();
        while (i++ < v.size() - 3) {
            String seq = lines.get(v.get(i)).split(" ")[2] + "/" + lines.get(v.get(i + 1)).split(" ")[2] + "/" + lines.get(v.get(i + 2)).split(" ")[2];
            if (topSeq == null) topSeq = seq;
            int count = 0;
            if (countMap.containsKey(seq)) {
                count = countMap.get(seq);
                if (best <= count) {
                    best = count;
                    topSeq = seq;
                }
            }
            if (best == -1) {
                best = 1;
                topSeq = seq;
            }
            countMap.put(seq, count + 1);
        }
        answer.put(k, topSeq);
    }

    public static Map<String, String> topN3Sequence(List<String> lines) {
        Map<String, String> answer = new HashMap<>();
        // O(n) time | O(n) space
        Map<String, List<Integer>> map = mapify(lines);
        //O(n) time | O(n) space
        map.forEach((k, v) -> findSequence(lines, v, k, answer));
        return answer;
    }

    //5,3,8

    public static List<String> readFile(String path) throws IOException {
        List<String> fileStream = Files.readAllLines(Paths.get(path));
        return fileStream;
    }

    public static void shift(int current, int next) {
        if(recentItems[next] == recentItems[current])
        recentItems[current] = recentItems[next];
    }
    
    String s = "";
    
//    public static void addItem(String item) {
//        int index = 0;
//        if(idxMap.containsKey(item)) {
//            index = idxMap.get(item);
//        } else {
//
//        }
//
//        if(recentItems.)
//        for(int i = index; i < recentItems.length - 1; i++) {
//            shift(i, i + 1, item);
//        }
//
//
//
//    }

    static class LogLine {
        String id;
        String path;
        String time;

        public LogLine(String str) {
            String[] strings = str.split(" ");
            this.id = strings[0];
            this.path = strings[1];
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return "LogLine{" +
                    "id='" + id + '\'' +
                    ", path='" + path + '\'' +
                    ", time='" + time + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) {
        try {
            List<String> lines = Amazon.readFile("lines.txt");
            Map<String, List<String>> map = lines.stream()
                    .map(str -> new LogLine(str))
                    .collect(groupingBy(LogLine::getId, mapping(LogLine::getPath, toList())));

            Map.Entry<String, List<String>>  map2 = map.entrySet()
                            .stream()
                            .reduce(BinaryOperator.maxBy(Comparator.comparingInt(o -> Collections.frequency(lines, o))))
                            .orElse(null);
            Map.Entry<String, List<String>>  map3 = map.entrySet()
                    .stream()
                    .filter(stringListEntry -> !stringListEntry.getKey().equals(map2.getKey()))
                    .reduce(BinaryOperator.maxBy(Comparator.comparingInt(o -> Collections.frequency(lines, o))))
                    .orElse(null);
            List<List<String>> map4 = map.entrySet()
                    .stream()
                    .filter(stringListEntry -> !stringListEntry.getKey().equals(map3.getKey()) && !stringListEntry.getKey().equals(map2.getKey()))
                    .map(entry -> entry.getValue())
                    .collect(Collectors.toList())
                    .stream()
                    .map(strings -> strings.stream().sorted().limit(2).collect(Collectors.toList()))
                    .collect(Collectors.toList());

            out.println(map);
            out.println(map2);
            out.println(map3);
            out.println(map4);
            out.println(Amazon.topN3Sequence(lines).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
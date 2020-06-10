import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Amazon {
    static PrintStream out = System.out;

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
            String seq = lines.get(v.get(i)).split(" ")[2] + "/" + lines.get(v.get(i + 1)).split(" ")[2] + "/"
            + lines.get(v.get(i + 2)).split(" ")[2];
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

    public static List<String> readFile(String path) throws IOException {
        List<String> fileStream = Files.readAllLines(Paths.get(path));
        return fileStream;
    }

    public static void main(String[] args) {
        try {
            List<String> lines = Amazon.readFile("lines.txt");
            out.println(Amazon.topN3Sequence(lines).toString());
        } catch (Exception e) {

        }
    }
}
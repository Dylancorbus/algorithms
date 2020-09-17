import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class MoneyFriend {


    //count lines words and byes in a file
    public static Map<String, Integer> analyzeFile(String path) throws FileNotFoundException {
        File text = new File(path);

        //Creating Scanner instnace to read File in Java
        Scanner scnr = new Scanner(text);

        //Reading each line of file using Scanner class
        int bytes = 0, words = 0, lines = 0;
        while (scnr.hasNextLine()) {
            ++lines;
            String line = scnr.nextLine();
            words += line.split(" ").length;
            bytes += line.getBytes().length;
        }
        int finalBytes = bytes;
        int finalWords = words;
        int finalLines = lines;
        return Stream.of(1, 2, 3).collect(toMap(k -> {
            switch (k) {
                case 1:
                    return "bytes";
                case 2:
                    return "words";
                case 3:
                    return "lines";

            }
            return null;
        }, v -> {
            switch (v) {
                case 1:
                    return finalBytes;
                case 2:
                    return finalWords;
                case 3:
                    return finalLines;

            }
            return null;
        }));
    }


    //pascals triangle

    public static String printPascalsTriangle(int rows) {
        List<Integer> previousRow = new ArrayList<>();
        previousRow.add(1);
        StringBuilder builder = new StringBuilder();
        builder.append(previousRow.toString() + "\n");
        while (--rows >= 1) {
            List<Integer> nextRow = new ArrayList<>();
            nextRow.add(1);
            for (int i = 0; i < previousRow.size(); i++) {
                int sum = previousRow.get(i);
                if (i < previousRow.size() - 1) sum += previousRow.get(i + 1);
                nextRow.add(sum);
            }
            builder.append(nextRow.toString() + "\n");
            previousRow = nextRow;
        }
        return builder.toString();
    }

    public static void main(String[] args) {

        try {
            System.out.println(analyzeFile("/Users/dylancorbus/Desktop/dylancorbus.github/algorithms/lines.txt"));
            String str = printPascalsTriangle(1000);
            System.out.println(str);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}

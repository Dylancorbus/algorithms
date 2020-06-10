import java.util.*;
import java.io.*;

public class FAANGQuestion {


    // METHOD SIGNATURE BEGINS, THIS METHOD IS REQUIRED
    public ArrayList<String> popularNToys(int numToys,
                                          int topToys,
                                          List<String> toys,
                                          int numQuotes,
                                          List<String> quotes) {
        //if the value of topToys is more than numToys then return toys in quotes
        if (topToys > numToys) {
            ArrayList<String> toyInQuotes = new ArrayList<String>();
            //for each toy in the quotes add to a list and return it
            for (int i = 0; i < toys.size(); i++) {
                //check if toy is in each quoute O(n2)
                for (int j = 0; j < quotes.size(); j++) {
                    if (quotes.get(j).toLowerCase().contains(toys.get(i).toLowerCase())) {
                        //increment count of mentions
                        toyInQuotes.add(toys.get(i));
                    }
                }
            }
            return toyInQuotes;
        }

        //store count in this
        Map<String, Integer> topToysMap = new HashMap<>();
        //loop thru toys
        for (int i = 0; i < toys.size(); i++) {
            //keep track of mentions so we can update map
            int countOfMentions = 0;
            //check if toy is in each quoute O(n2)
            for (int j = 0; j < quotes.size(); j++) {
                //check if quote mentions toy
                if (quotes.get(j).toLowerCase().contains(toys.get(i).toLowerCase())) {
                    //increment count of mentions
                    ++countOfMentions;
                }
                //if we are at the last quote add the toy and count of mentions to the map
                //set count to 0 so we can use it to keep track for next toy
                if (j == quotes.size() - 1) {
                    if (countOfMentions != 0) {
                        topToysMap.put(toys.get(i), countOfMentions);
                        countOfMentions = 0;
                    }
                }
            }
        }

        //return a list of most mentions toys in descending order
        topToysMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> topToysMap.put(x.getKey(), x.getValue()));
        return new ArrayList<>(topToysMap.keySet());
    }
    // METHOD SIGNATURE ENDS

    // METHOD SIGNATURE BEGINS, THIS METHOD IS REQUIRED
    public List<String> reorderLines(int logFileSize, List<String> logLines) {
        //use java collections to compare
        Collections.sort(logLines, (s1, s2) -> {
            //trimming whitespace
            String sb1 = s1.substring(3).trim().toLowerCase();
            String sb2 = s2.substring(3).trim().toLowerCase();
            //need to count numeric values as lower so they are at the bottom
            if(Character.isDigit(sb1.charAt(0)) && Character.isDigit(sb2.charAt(0))) {
                compare(sb1, sb2);
            }
            //value is numeric
            if(Character.isDigit(sb1.charAt(0))) {
                return -0;
            }
            //value is numeric
            if(Character.isDigit(sb2.charAt(0))) {
                return -1;
            }
            return sb1.compareTo(sb2);
        });
        return logLines;
    }

    public int compare(String o1, String o2) {
        return extractInt(o1) - extractInt(o2);
    }

    int extractInt(String s) {
        String num = String.valueOf(s.charAt(0));
        // return 0 if no digits found
        return num.isEmpty() ? 0 : Integer.parseInt(num);
    }
    // METHOD SIGNATURE ENDS

    

    public static void main(String[] args) {
        List<String> toys = new ArrayList<>();
        toys.add("elmo");
        toys.add("legos");
        toys.add("elsa");
        toys.add("drone");
        toys.add("tablet");
        toys.add("warcraft");

        List<String> quotes = new ArrayList<>();

        quotes.add("Elmo this! elmo that");
        quotes.add("Elmo that");
        quotes.add("Elsa this");
        quotes.add("elmo this! Elsa that");
        quotes.add("Warcraft this!");
        quotes.add("Warcraft this");

        FAANGQuestion test = new FAANGQuestion();

        List result = test.popularNToys(6, 2, toys, 6, quotes);

        System.out.println(result);

    }
}


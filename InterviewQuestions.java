import java.util.*;
import java.util.stream.Collectors;

public class InterviewQuestions {

	public static double whatAngle(int hours, int minutes, int clockType) {
        double hourIncrement = 360/clockType;
        double miniteIncrement = 60/minutes;
        
        double minuteNormalClockNumber = clockType/miniteIncrement;
        
        double hourAngle = hours == 12 ? 0 : hours * hourIncrement;
    
        double angle = (minuteNormalClockNumber - hourAngle) * 30;
        return Math.abs(angle);

    }



	class Node {
		Node next;
		public Node(Node next) {
			this.next = next;
		}

		public Node getNext() {
			return next;
		}

		public void setNext(Node next) {
			this.next = next;
		}
	}

	public static Node reverse(Node node) {
		Node current = null;
		Node previous = null;
		Node next = node;
		//while we have more nodes to flip
		while(next != null) {
			//get the next node and assign it as the current one
			current = next;
			//set the next node to the node current points to
			next = current.next;
			//make current point to the previous node
			current.next = previous;
			//assign previous to the last processed node which is current
			previous = current;
		}
		//return the last processed node which is previous
		return previous;
	}



    public static String validate(String bracesBracketsParentheses) {
		//some algorithm code
		//scan and collect all the {[( and }])
		System.out.println("INPUT: " + bracesBracketsParentheses);
		Map<String, Integer> countOfBBP = new HashMap<>();
		countOfBBP.put("{", 0);
		countOfBBP.put("(", 0);
		countOfBBP.put("[", 0);
		countOfBBP.put("}", 0);
		countOfBBP.put(")", 0);
		countOfBBP.put("]", 0);
		for (int i = 0; i < bracesBracketsParentheses.length(); i++) {
			String checkChar = String.valueOf(bracesBracketsParentheses.charAt(i));
			switch (checkChar) {
				case "{":
					countOfBBP.replace("{", countOfBBP.get("{") + 1);
					break;
				case "[":
					countOfBBP.replace("[", countOfBBP.get("[") + 1);
					break;
				case "(":
					countOfBBP.replace("(", countOfBBP.get("(") + 1);
					break;
				case "}":
					countOfBBP.replace("}", countOfBBP.get("}") + 1);
					break;
				case "]":
					countOfBBP.replace("]", countOfBBP.get("]") + 1);
					break;
				case ")":
					countOfBBP.replace(")", countOfBBP.get(")") + 1);
					break;
			}
		}

		countOfBBP.forEach((k, v) -> {
			System.out.println("key: " + k + " val: " + v);
		});

		if(countOfBBP.get("{") != countOfBBP.get("}")) {
			return "NOT VALID";
		}

		if(countOfBBP.get("[") != countOfBBP.get("]")) {
			return "NOT VALID";
		}

		if(countOfBBP.get("(") != countOfBBP.get(")")) {
			return "NOT VALID";
		}
		return "VALID";
	}


    public static List<Integer> kMostFrequent(int[] values, int k) {
		Map<Integer, Integer> counts = new HashMap<>();

		for (int i = 0; i < values.length; i++) {
			int curVal = values[i];
			if(counts.containsKey(curVal)) {
				int curCount = counts.get(curVal);
				counts.put(curVal, curCount + 1);
			} else {
				counts.put(curVal, 1);
			}
		}
		List<Integer> kMost = new ArrayList<>();
		while (k-- > 0) {
			try {
				int key = Collections.max(counts.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
				counts.remove(key);
				kMost.add(key);
			} catch (Exception e) {

			}
        }
		return kMost;
	}

	// METHOD SIGNATURE BEGINS, THIS METHOD IS REQUIRED
    public static List<Integer> cellCompete(int[] states, int days) {
        int beforeFirstAfterLast = 0;
        int[] copy = new int[states.length];
        while (days-- > 0) {
            for (int i = 0; i < states.length; i++) {
                int icopy = i;
                int newState;
                //edge case check with i + 1 and -1 always in active
                if(i == 0) {
                    if (beforeFirstAfterLast == states[icopy + 1]) {
                        newState = 0;

                    } else {
                        newState = 1;
                    }
                } else if(i == states.length - 1){
                    if (beforeFirstAfterLast == states[icopy - 1]) {
                        newState = 0;

                    } else {
                        newState = 1;
                    }
                } else if(states[icopy - 1] == states[icopy + 1]) {
                    newState = 0;
                } else {
                    newState = 1;
                }
                copy[i] = newState;

            }
            for (int j = 0; j < states.length; j++) {
                states[j] = copy[j];
            }
        }
        return Arrays.stream(copy).mapToObj(Integer::valueOf)
                .collect(Collectors.toList());
    }


    public static int findGCD(int[] ints, int size) {
        int result = ints[0];
        for (int i = 1; i < size; i++)
            result = gcd(ints[i], result);

        return result;
    }

    public static int gcd(int a, int b) {
        if (a == 0)
            return b;
        return gcd(b % a, a);
    }

	public static ArrayList<String> popularNFeatures(int numFeatures,
                                              int topFeatures,
                                              List<String> possibleFeatures,
                                              int numFeatureRequests,
                                              List<String> featureRequests) {
        Map<String, Integer> map = new HashMap<>();
        for (int j = 0; j < possibleFeatures.size(); j++) {
            int count = 0;
            int newCount = 0;
            for (int i = 0; i < featureRequests.size(); i++) {
                String s = featureRequests.get(i).toLowerCase();
                if(s.contains(possibleFeatures.get(j))) {
                    newCount++;
                }
            }

            if(newCount > count) {
                map.put(possibleFeatures.get(j), newCount);
            }
            count = newCount;
        }
        ArrayList<String> list = new ArrayList<>();
        while (topFeatures-- > 0) {
            String key = map.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
            map.remove(key);
            list.add(key);

        }

        return list;
    }

    public static void main(String[] args) {


        System.out.println(validate("[({}){{{{{{{{{{{{{{{{}}}}}}}}}}}}}}}}}}]"));

        System.out.println(kMostFrequent(new int[]{2, 3, 4, 5, 6, 6, 6, 6, 6, 6, 2, 2, 2, 2, 2, 2, 2, 8, 8,}, 2));



		int[] array = {1,0,0,0,0,1,0,0};
        int[] ints = {1012, 56000, 100001, 500124};





        List<String> possible = new ArrayList<>();
        List<String> req = new ArrayList<>();
        possible.add("storage");
        possible.add("battery");
        possible.add("hover");
        possible.add("alexa");
        possible.add("waterproof");
        possible.add("solar");
        req.add("i want Storage");
        req.add("neeed battery");
        req.add("Hover");
        req.add("Alexa");
        req.add("Waterproof");
        req.add("Solar");
        req.add("Solar");


        List<Integer> list = cellCompete(array, 1);
        double gcd = findGCD(ints, ints.length);
        System.out.println(popularNFeatures(possible.size(), 2, possible, req.size(), req));
        System.out.println(gcd);
        System.out.println(list);
        System.out.println("{1,0,0,0,0,1,0,0}");
		System.out.println("{0,1,0,0,1,0,1,0}");
		

		System.out.println(String.format("The angle of the hands on the clock for 12:30 is %s",whatAngle(12, 30, 12)));
    }
}
import java.util.HashSet;
import java.util.Random;

public class Utils {

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
}

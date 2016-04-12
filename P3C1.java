import java.util.Collection;

/**
 * Outputs data relevant to the collection of words taken from standard in.
 */
public class P3C1 extends P3C {
    
    /**
     * Applies P3C methods to print data relevent to the stdin words collected.
     * @param args [description]
     */
    public static void main(String[] args) {
        P3C1 part3 = new P3C1();
        part3.readInWords();
        part3.giveMapType(new JavaHashSetWrapper<String, Integer>());
        part3.countOccurrences(-1);
        part3.reverseMap();
        int wordCount = part3.totalNumWords();
        Collection<String> mostFreqWords = part3.mostFrequentWords();
        Collection<String> less3Times = part3.freqLessThanThree();
        Collection<String> top10Percent = part3.topTenPercentWords();

        System.out.println("Total number of words: " + wordCount);
        System.out.println("Most frequent word(s): " + mostFreqWords);
        System.out.println("All words occurring at most 3 times: "
            + less3Times);

        System.out.println(
            "All words occurring int top 10% of word frequencies: "
            + top10Percent);

    }
}
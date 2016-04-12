import java.util.Collection;

public class P3C1 extends P3C {
    

    public static void main(String[] args) {
        P3C1 part3 = new P3C1();
        part3.readInWords();
        part3.countOccurrences(-1);
        part3.reverseMap();
        int wordCount = part3.totalNumWords();
        Collection<String> mostFreqWords = part3.mostFrequentWords();
        Collection<String> less3Times = part3.freqLessThanThree();
        Collection<String> top10Percent = part3.topTenPercentWords();

        System.out.println("Total number of words: " + wordCount);
        System.out.println("Most frequent word(s): " + mostFreqWords);
        System.out.println("All words occurring at most 3 times: "
            + mostFreqWords);

        System.out.println(
            "All words occurring int top 10% of word frequencies: "
            + top10Percent);

    }
}
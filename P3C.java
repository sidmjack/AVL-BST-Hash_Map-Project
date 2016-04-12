import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.AbstractMap;


public class P3C {
    

    protected Scanner sc;

    protected List<String> wordList;
    protected int uniqueWordCount;
    protected int wordCount;
    protected MapJHU<String, Integer> mp;
    protected int maxWordCount;
    protected AVLMap<Integer, Collection<String>> avl;


    public P3C() {
        this.maxWordCount = 0;
        this.sc = new Scanner(System.in);
        this.wordList = new ArrayList<String>();
        this.wordCount = 0;
        this.uniqueWordCount = 0;
    }


    public void readInWords() {

        //Create reference list of total words
        while (this.sc.hasNext()) {
            wordList.add(this.sc.next());
            wordCount++;
        }
    }

    /**
     * Counts the number of times each word comes up in the wordList.
     * @param  maxWords the maximum number of words to read or -1 to read all
     * @return          true if wordList not of sufficient size and not all of
     *                  maxWords was able to be read
     */
    public boolean countOccurrences(int maxWords) {
        for (String word : this.wordList) {

            Integer howMany = this.mp.get(word);
            if (howMany == null) {
                howMany = 1;
            } else {
                howMany++;
            }
            this.mp.put(word, howMany);

            maxWords--;

            if (maxWords == 0) {
                break;
            }
        }

        return maxWords > 0;
    }

    public void reverseMap() {

        this.avl = new AVLMap<Integer, Collection<String>>();

        for (Map.Entry<String, Integer> entry : this.mp.entries()) {

            String word = entry.getKey();
            Integer howMany = entry.getValue();

            if (howMany > this.maxWordCount) {
                this.maxWordCount = howMany;
            }

            Collection<String> wordsNTimes = this.avl.get(howMany);

            if (wordsNTimes == null) {
                wordsNTimes = new LinkedList<String>();
                this.uniqueWordCount++;
            }

            wordsNTimes.add(word);

            this.avl.put(howMany, wordsNTimes);
        }
    }

    public int totalNumWords() {
        return this.wordCount;
    }

    public Collection<String> mostFrequentWords() {
        Integer maxFreq = this.avl.lastKey();
        return this.avl.get(maxFreq);
    }

    public Collection<String> freqLessThanThree() {
        Collection<String> lt3 = this.avl.get(1);
        lt3.addAll(this.avl.get(2));
        final int three = 3;
        lt3.addAll(this.avl.get(three));
        return lt3;
    }

    public Collection<String> topTenPercentWords() {
        int ninetyPercent = this.maxWordCount * 9 / 10;
        if (this.maxWordCount * 9 % 10 != 0) {
            ninetyPercent++;
        }

        AVLMap<Integer, Collection<String>> topFreqMap
            = this.avl.subMap(ninetyPercent, this.maxWordCount);


        Collection<String> topFreqCollection = new HashSet<String>();

        for (Map.Entry<Integer, Collection<String>> entry : topFreqMap ) {
            topFreqCollection.addAll(entry.getValue());
        }

        return topFreqCollection;

    }
}
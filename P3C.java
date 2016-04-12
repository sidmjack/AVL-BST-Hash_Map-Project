import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Collection;
import java.util.Map;

/**
 * Class containing all relevant variables/functions for P3C1/2.
 */
public class P3C {
    
    /** Scanner that reads in standard input. */
    protected Scanner sc;
    /** Contains list of words from standard in. */
    protected List<String> wordList;
    /** Keeps track of the number of unique words. */
    protected int uniqueWordCount;
    /** Keeps track of the number of all words. */
    protected int wordCount;
     /** Entries used to associate a word with it's frequency. */
    protected MapJHU<String, Integer> mp;
     /** Keeps track of maximum word count. */
    protected int maxWordCount;
     /** Entires used to associate an occurence number with a set of words. */
    protected JavaTreeMapWrapper<Integer, Collection<String>> avl;

    /**
     * Constructor that sets up starting fields of P3C.
     */
    public P3C() {
        this.maxWordCount = 0;
        this.sc = new Scanner(System.in);
        this.wordList = new ArrayList<String>();
        this.wordCount = 0;
        this.uniqueWordCount = 0;
    }

    /**
     * Sets the internal map.
     * @param map map to set to the internal one
     */
    protected void giveMapType(MapJHU<String, Integer> map) {
        this.mp = map;
    }

    /**
     * Read in all words from standard in into an array list.
     */
    public void readInWords() {

        //Create reference list of total words
        while (this.sc.hasNext()) {
            this.wordList.add(this.sc.next());
            this.wordCount++;
        }
    }

    /**
     * Counts the number of times each word comes up in the wordList.
     * @param  maxWords the maximum number of words to read or -1 to read all
     * @return          true if wordList not of sufficient size and not all of
     *                  maxWords was able to be read
     */
    public boolean countOccurrences(int maxWords) {
        this.mp.clear();
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

    /**
     * Creates "inverse map", using the number of occurences as keys to words.
     */
    public void reverseMap() {

    this.uniqueWordCount = 0;
    this.maxWordCount = 0;

        this.avl = new JavaTreeMapWrapper<Integer, Collection<String>>();

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

    /**
     * Simply returns the total number of words.
     * @return total number of words.
     */
    public int totalNumWords() {
        return this.wordCount;
    }

    /**
     * Returns a set of the most frequently encountered words.
     * @return a set of most frequent word(s).
     */
    public Collection<String> mostFrequentWords() {
        Integer maxFreq = this.avl.lastKey();
        return this.avl.get(maxFreq);
    }

    /**
     * Returns a set of "unique" words appearing ni more than three times.
     * @return s set of unique words.
     */
    public Collection<String> freqLessThanThree() {
        Collection<String> lt3 = this.avl.get(1);
        lt3.addAll(this.avl.get(2));
        final int three = 3;
        lt3.addAll(this.avl.get(three));
        return lt3;
    }

    /**
     * Returns a set of words whose frequency ranged in the top 10% of words.
     * @return 10% of most frequently occuring words.
     */
    public Collection<String> topTenPercentWords() {
        final int nine = 9;
        final int ten = 10;
        int ninetyPercent = this.maxWordCount * nine / ten;
        if (this.maxWordCount * nine % ten != 0) {
            ninetyPercent++;
        }

        Set<Map.Entry<Integer, Collection<String>>> topFreqMap
            = this.avl.subMap(ninetyPercent, this.maxWordCount + 1).entrySet();
        //since exclusive


        Collection<String> topFreqCollection = new HashSet<String>();

        for (Map.Entry<Integer, Collection<String>> entry : topFreqMap) {
            topFreqCollection.addAll(entry.getValue());
        }

        return topFreqCollection;

    }
}
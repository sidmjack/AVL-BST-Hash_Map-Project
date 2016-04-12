// Name: Lawrence Wolf-Sonkin & Sidney Jackson
// JHU Login: lwolfso1  & sjacks85
// Course: Data Structure (600.226.02)
// Project: Project #3C (Fun with Maps)
// Due Date: 04-11-2016
// Last Modified: 04-11-2016


/**
 * Part 2 of "Fun With Maps" Prject (Project 3C.2).
 */
public class P3C2 extends P3C {


    /** Contains word sizes to be tested. */
    static final int[] SIZES = {100, 1000, 10000};


    // static String printIntro(int i) {
    //     String implementation;
    //     if (i == 0) {
    //         implementation = "Java HashSet";
    //     } else if (i == 1) {
    //         implementation = "BSTMap";
    //     } else {
    //         implementation = "AVLMap";
    //     }

    // }


     /**
      * Creates 1) hashmap,  2) a BSTMap, and 3) an AVLMap. 
      * This program outputs word count data while comparing the performance 
      * data computation between the 3 different data structures listed above.
      * @param  args arguments from main.
      */
    public static void main(String[] args) {

        P3C2 part3 = new P3C2();

        part3.readInWords();

        // loop varying size of the input
        for (int i = 0; i <= 2; i++) {

            // loop varying DS implementation
            for (int j = 0; j <= 2; j++) {

                long lStartTime = System.currentTimeMillis();

                if (j == 0) {
                    part3.giveMapType(
                        new JavaHashMapWrapper<String, Integer>());
                } else if (j == 1) {
                    part3.giveMapType(new BSTMap<String, Integer>());
                } else {
                    part3.giveMapType(new JavaTreeMapWrapper<String, Integer>());
                }

                part3.countOccurrences(SIZES[i]);


                long lEndTime = System.currentTimeMillis();
                long difference = lEndTime - lStartTime;
                // System.out.println("Elapsed milliseconds: " + difference);


                String implementation;
                if (j == 0) {
                    implementation = "Java HashMap";
                } else if (j == 1) {
                    implementation = "BSTMap";
                } else {
                    implementation = "Java TreeMap";
                }

                System.out.println("" + SIZES[i] + "-word - " + implementation
                    + " performance time: " + difference);
            }
        }
    }
}

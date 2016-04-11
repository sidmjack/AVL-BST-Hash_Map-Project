// Name: Lawrence Wolf-Sonkin & Sidney Jackson
// JHU Login: lwolfso1  & sjacks85
// Course: Data Structure (600.226.02)
// Project: Project #3C (Fun with Maps)
// Due Date: 04-11-2016
// Last Modified: 04-11-2016

import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.io.FileNotFoundException;
import java.util.EmptyStackException;

/**
 * Part 2 of "Fun With Maps" Prject (Project 3C.2)
 */
public class P3C2 {
	 
	final int SIZE1 = 100;
	final int SIZE2 = 1000;
	final int SIZE3 = 10000;

	 /**
	  * Creates 1) hashmap,  2) a BSTMap, and 3) an AVLMap. 
	  * This program outputs word count data while comparing the performance 
	  * data computation between the 3 different data structures listed above.
	  * @param args[] Word File.
	  */
	  public static void main(String args[]){

	  	//Variables.
	  	List wordList = new ArrayList();
	  	int wordCount = 0;
	  	
	  	//Create reference list of total words.
	  	Scanner sc;
        do {           
            sc = new Scanner(System.in);
            word = sc.next();
            wordList.add(word);
            wordCount++;
        } while (sc.hasNext());

        int currCount = 0;
       


	  	//Time the amount of time it takes to create the map (for each data structure).
	  	//There will be 3 Sets of Instantiaions for each data structure...
	  	// An instantion for 100, 1000, and 10,000 words.
	  	
	  	// Timing Code:
		// long lStartTime = System.currentTimeMillis();
		// WHATEVER TASK
		// long lEndTime = System.currentTimeMillis();
		// long difference = lEndTime - lStartTime;
		// System.out.println("Elapsed milliseconds: " + difference);

        // Arrays for storing time.
        long[] performacneTime = new long[]{0,0,0};

	  	//BELOW WILL BECOME A FUNCTION:
	    System.out.println("100 Word Map Insatiation: " + hmTime[0]);
	  	System.out.println("1,000 Word Map Insatiation: " + hmTime[1]);
	  	System.out.println("100,000 Word Map Insatiation: " + hmTime[2]);

	  	System.out.println("--HASH MAP--");
	  	System.out.println("Performance Time: ");

	  	System.out.println("--BST MAP--");
	  	System.out.println("Performance Time: ");

	    System.out.println("--AVL MAP--");
	  	System.out.println("Performance Time: ");

	  }
}

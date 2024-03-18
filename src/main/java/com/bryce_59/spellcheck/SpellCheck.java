package com.bryce_59.spellcheck;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import javax.swing.*;

/**
 * SpellCheck
 * 
 * @author Bryce-59
 * @version 17-03-2024
 */
public class SpellCheck extends JFrame {

    // *Constructors*

    /**
     * Constructor for objects of class SpellCheck
     * 
     * @param dictionaryPath the path to the dictionary text file
     * 
     * @throws IllegalArgumentException if parameter is invalid
     */
    public SpellCheck(String dictionaryPath) {
        dictionary = new HashMap<>();
        if (!loadDictionary(dictionaryPath)) {
            throw new IllegalArgumentException("Invalid file");
        }

    }

    // *Public Instance Methods*

    /**
     * Get a List of matches within the threshhold edit distance of the parameter
     * String
     * 
     * @param s the parameter String
     * @param n the threshold edit distance
     * 
     * @return the List of matches, sorted by frequency
     * @throws NullPointerException if String is null
     */
    public java.util.List<Map.Entry<String, Integer>> getMatches(String s, int n) {
        // preconditions
        if (s == null) {
            throw new NullPointerException("String cannot be null");
        }

        // retrieve List of valid comparisons at the minimum edit distance lower than
        // the threshold
        HashMap<String, Integer> result = new HashMap<>();

        int dMin = n;
        for (String t : dictionary.keySet()) {
            if (Math.abs(s.length() - t.length()) > dMin) {
                continue;
            }

            int d = wagnerfisherdameraulevenshtein(s, t);

            if (d <= dMin) {
                if (d < dMin) {
                    result.clear();
                    dMin = d;
                }

                result.put(t, dictionary.get(t));
            }
        }

        // sort List by frequency
        java.util.List<Map.Entry<String, Integer>> ret = new ArrayList<>(result.entrySet());
        ret.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return ret;
    }

    /**
     * Initialize the dictionary of the SpellCheck object
     * 
     * @param fileName the file path to the dictionary text file
     * @return true if the dictionary is loaded, else false
     */
    public boolean loadDictionary(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName));

            dictionary.clear();
            while (sc.hasNextLine()) {
                dictionary.put(sc.next().toLowerCase(), (int) Double.parseDouble(sc.next()));
                sc.nextLine();
            }
            sc.close();
        } catch (FileNotFoundException e) {
            return false; // File Not Found!
        } catch (InputMismatchException e) {
            dictionary.clear();
            return false; // File Not Well-Formed!
        }

        return true;
    }

    /**
     * Check if a String is contained in the loaded dictionary
     * 
     * @param s the String
     * @return true if the String is valid else false
     */
    public boolean isValid(String s) {
        return dictionary.containsKey(s.toLowerCase());
    }

    // *Public Static Methods*

    /**
     * Implementation of the Wagner-Fisher algorithm to calculate the
     * Damerau-Levenshtein distance between two Strings using a dynamic programming
     * solution.
     * 
     * @param a the first String to compare
     * @param b the second String to compare
     * @return the Damerau-Levenshtein distance
     * 
     * @throws NullPointerException if String is null
     */
    public static int wagnerfisherdameraulevenshtein(String a, String b) {
        // preconditions
        if (a == null || b == null) {
            throw new NullPointerException("String cannot be null");
        }

        // create matrix
        int[][] dynamic = new int[a.length() + 1][b.length() + 1];

        // fill zero row/column
        for (int i = 0; i <= a.length(); i++) {
            dynamic[i][0] = i;
        }

        for (int j = 0; j <= b.length(); j++) {
            dynamic[0][j] = j;
        }

        // fill matrix body
        for (int j = 1; j <= b.length(); j++) {
            for (int i = 1; i <= a.length(); i++) {
                // helper variables
                int is_matched = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                boolean transposable = i > 1 && j > 1
                        && a.charAt(i - 1) == b.charAt(j - 2) && a.charAt(i - 2) == b.charAt(j - 1);

                // calculate values
                int deletion = dynamic[i - 1][j] + 1;
                int insertion = dynamic[i][j - 1] + 1;
                int mismatch = dynamic[i - 1][j - 1] + is_matched;
                int transposition = !transposable ? Integer.MAX_VALUE : dynamic[i - 2][j - 2] + is_matched;

                // fill cell
                dynamic[i][j] = Math.min(deletion, Math.min(insertion, Math.min(mismatch, transposition)));
            }
        }

        return dynamic[a.length()][b.length()];
    }

    // *Instance Variables*

    Map<String, Integer> dictionary;
}
package com.ikalagaming.util;

import lombok.NonNull;

/**
 * Methods useful for Strings.
 *
 * @author Ches Burks
 */
public class StringOperations {
    /**
     * Counts the number of times the given character (toFind) occurs in the first string. If
     * toSearch is empty or does not contain the substring, 0 is returned.
     *
     * @param toSearch the string to search
     * @param toFind the character to count
     * @return how many times the character appears in the string
     */
    public static int countOccurances(@NonNull String toSearch, char toFind) {
        int count = 0;
        if (toSearch.isEmpty()) {
            return count;
        }
        int i;
        for (i = 0; i < toSearch.length(); ++i) {
            if (toSearch.charAt(i) == toFind) {
                ++count;
            }
        }
        return count;
    }

    /**
     * Counts the number of times the given string (toFind) occurs in the first string. If toSearch
     * is empty or does not contain the substring, 0 is returned.
     *
     * @param toSearch the string to search
     * @param toFind the string to count
     * @return how many times the substring appears in the string
     */
    public static int countOccurances(@NonNull String toSearch, @NonNull String toFind) {
        if (toSearch.isEmpty()) {
            return 0;
        }
        if (!toSearch.contains(toFind)) {
            return 0;
        }
        return (toSearch.length() - toSearch.replace(toFind, "").length()) / toFind.length();
    }

    /** Private constructor so that this plugin is not initialized. */
    private StringOperations() {
        throw new UnsupportedOperationException("This utility class should not be instantiated");
    }
}

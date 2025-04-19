package com.HotelHawk.Spring.PatternFinder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternFinder {
    public static String extractPrice(String input) {
        // Regular expression to match the price pattern
        String regex = "\\$?(\\d+(?:\\.\\d+)?)"; // Matches numbers with optional decimal point after an optional dollar sign
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // Find the first match
        if (matcher.find()) {
            // Extract and return the matched price
            return matcher.group(1);
        }

        // Return null if no match found
        return null;
    }
    public static void main(String[] args) {
        // Test inputs
        String[] inputs = {"Price is $40", "$40", "40"};

        for (String input : inputs) {
            String price = extractPrice(input);
            if (price != null) {
                System.out.println("Extracted price from '" + input + "': " + price);
            } else {
                System.out.println("No price found in '" + input + "'");
            }
        }
    }
}

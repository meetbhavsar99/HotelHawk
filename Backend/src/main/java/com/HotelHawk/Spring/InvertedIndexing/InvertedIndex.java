package com.HotelHawk.Spring.InvertedIndexing;

import java.io.*;
import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children;
    List<String> hotelIds;

    public TrieNode() {
        children = new HashMap<>();
        hotelIds = new ArrayList<>();
    }
}

public class InvertedIndex {
    TrieNode root;

    public InvertedIndex() {
        root = new TrieNode();
    }

    public void addDocument(String documentId, String[] terms) {
        for (String term : terms) {
            TrieNode current = root;
            for (char c : term.toCharArray()) {
                Character key = Character.valueOf(c); // Convert char to Character
                current.children.putIfAbsent(key, new TrieNode());
                current = current.children.get(key);
            }
            current.hotelIds.add(documentId);
        }
    }

    public List<String> search(String term, int limit) {
        TrieNode current = root;
        for (char c : term.toCharArray()) {
            Character key = Character.valueOf(c); // Convert char to Character
            current = current.children.get(key);
            if (current == null) {
                return new ArrayList<>();
            }
        }
        // Return only the first 'limit' number of hotels
        return current.hotelIds.subList(0, Math.min(limit, current.hotelIds.size()));
    }

    public static String initialize(String cityname) {
        // Read hotel data from file and build inverted index
        InvertedIndex invertedIndex = new InvertedIndex();
        try (BufferedReader br = new BufferedReader(new FileReader("inverted_index_data"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                String[] parts = line.split(":");
                String city = parts[0].trim();
                String[] hotels = parts[1].trim().split(", ");
                for (String hotel : hotels) {
                    invertedIndex.addDocument(hotel, new String[]{city});
                }
            }
        } catch (IOException e) {
            // Replace e.printStackTrace() with proper logging or exception handling mechanism
            e.printStackTrace();
        }

        // Searching for first 3 hotels in Toronto
        String searchQuery =cityname;
        List<String> torontoHotels = invertedIndex.search(searchQuery, 3);
        System.out.println("First 3 hotels in " + searchQuery + ":");
        String f_temp="";
        for (String hotel : torontoHotels) {
            System.out.println(hotel);
            f_temp+=hotel.concat(",");
        }
        return f_temp;
    }
    public static void main(String[] args) {
        initialize("Toronto".toUpperCase(Locale.ROOT));
    }
}

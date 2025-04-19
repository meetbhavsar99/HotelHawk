package com.HotelHawk.Spring.Filter;

import com.HotelHawk.Spring.spellcheck.SpellCheck;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Filter {
    // Regex pattern for numerical input
    public static String numericRegex = "^-?\\d+$";

    public static boolean isValidPrice(String price, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(price);
        return matcher.matches();
    }

    public static String filter_data(String city_name, int minPrice, int maxPrice,int minReview) throws IOException {
//        if (!isValidPrice(Integer.toString(minPrice), numericRegex) || (minPrice)<0 || (!isValidPrice(Integer.toString(maxPrice), numericRegex)) || ((maxPrice)<(minPrice))){
//            System.out.println("Invalid minimum price. Please input numerical value.");
//            return "BAD GATEWAY";
//        }
        String[] d={city_name};
        String cityname= SpellCheck.main(d);
        File file= new File((cityname.toLowerCase()).concat("_finaldata"));
        BufferedReader br= new BufferedReader(new FileReader(file));
        String jsonData= br.readLine();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonData);

        ObjectNode filteredData = mapper.createObjectNode();

        // Create empty arrays for each category
        ArrayNode bookingArray = mapper.createArrayNode();
        ArrayNode mmtArray = mapper.createArrayNode();
        ArrayNode hotelscaArray = mapper.createArrayNode();

        // Filter "booking" entries
        JsonNode bookingNode = rootNode.get("booking");
        if (bookingNode != null) {
            for (JsonNode entry : bookingNode) {
                System.out.println(entry.get("Review ").asText());
                if ((entry.get("MinPrice ").asText().matches("Not Available ")) || (entry.get("Review ").asText().matches(" "))){
                    //pass
                }
                else{
                    int price = entry.get("MinPrice ").asInt();

                    double review = Double.parseDouble(entry.get("Review ").asText().trim().split(" ")[0]);
                    if ((price >= minPrice && price <= maxPrice) && ((int)review >= minReview)) {
                        bookingArray.add(entry);
                    }
                }

            }
        }
        // Filter "mmt" entries
        JsonNode mmtNode = rootNode.get("mmt");
        if (mmtNode != null) {
            for (JsonNode entry : mmtNode) {
                int price = Integer.parseInt(entry.get("MinPrice ").asText().split(" ")[1]);
                double review = entry.get("Review ").asDouble();

                if (price >= minPrice && price <= maxPrice && (review+review) >= minReview) {
                    mmtArray.add(entry);
                }
            }
        }

        // Filter "hotelsca" entries
        JsonNode hotelscaNode = rootNode.get("hotelsca");
        if (hotelscaNode != null) {
            for (JsonNode entry : hotelscaNode) {
                int price = entry.get("MinPrice ").asInt();
                double review = entry.get("Review ").asDouble();
                if (price >= minPrice && price <= maxPrice && review >= minReview) {
                    hotelscaArray.add(entry);
                }
            }
        }

        // Add filtered arrays to the filteredData object
        filteredData.set("booking", bookingArray);
        filteredData.set("mmt", mmtArray);
        filteredData.set("hotelsca", hotelscaArray);

        // Convert filtered data to JSON string
        String filteredJson = mapper.writeValueAsString(filteredData);
        System.out.println(filteredJson);
        return filteredJson;
    }


    public static void main(String[] args) throws IOException {
        filter_data("calgary",600,400,6);
    }
}
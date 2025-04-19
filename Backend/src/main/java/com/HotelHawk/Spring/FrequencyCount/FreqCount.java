package com.HotelHawk.Spring.FrequencyCount;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class FreqCount {
    public static HashMap<String,Integer> count= new HashMap<String, Integer>();
    public static HashMap<String,String> states= new HashMap<String,String>();
    public static void initialize(String cityname,String searchString) throws IOException {
        int booking= fc_booking(cityname,searchString);
        int hotelsca= fc_booking(cityname,searchString);
        int mmt= fc_booking(cityname,searchString);

    }
    private static int countOccurrences(String text, String searchString) {
        int count = 0;
        int index = 0;
        while ((index = text.toLowerCase().indexOf(searchString.toLowerCase(), index)) != -1) {
            count++;
            index += searchString.length();
        }
        return count;
    }
    public static  int fc_booking(String cityname, String searchString) throws IOException {
        String text= searchString;
        int cnt = countOccurrences(text, cityname);
        return (cnt);
    }
    public static int fc_hotels(String cityname, String searchString){
        String text= searchString;
        int cnt = countOccurrences(text, cityname);
        return (cnt);

    }
    public static int fc_mmt(String cityname, String searchString){
        String text= searchString;
        int cnt = countOccurrences(text, cityname);
        return (cnt);
    }

    public static void save(String cityname, String count) throws FileNotFoundException {
        //String path= System.getProperty("user.dir");
        File f = new File((cityname).concat("_fc"));
        if(f.exists() && !f.isDirectory()) {

        }
        else{

        }
    }
    public static void main(String[] args) throws IOException {

    }
}

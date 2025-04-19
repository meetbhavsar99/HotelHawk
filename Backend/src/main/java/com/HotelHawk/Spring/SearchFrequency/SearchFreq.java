package com.HotelHawk.Spring.SearchFrequency;

import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchFreq {

    public static HashMap<String, Integer> map = new HashMap<String, Integer>();

    public static String get_data(String cityname) throws IOException {
        File file = new File(System.getProperty("user.dir") + "\\Backend/searchfreq");
        BufferedReader br = new BufferedReader(new FileReader(file));
//        JSONObject json= new JSONObject();
        String s;
        while ((s = br.readLine()) != null) {
//            System.out.println(s.substring(0,s.lastIndexOf(':')).toUpperCase());
//            System.out.println(cityname.toUpperCase());
//            System.out.println(s.substring(s.lastIndexOf(':')+1,s.length()));
            if (s.substring(0, s.lastIndexOf(':')).toUpperCase().matches(cityname.toUpperCase())) {
                return (s.substring(s.lastIndexOf(':') + 1, s.length()));
            }

        }
        return "";

    }

    public static void update(String cityname) throws IOException {
        File file = new File(System.getProperty("user.dir") + "\\Backend/searchfreq");
        BufferedReader br = new BufferedReader(new FileReader(file));
        ArrayList<String> temp = new ArrayList<String>();
        String s;
        while ((s = br.readLine()) != null) {
            String city = ((s.substring(0, s.lastIndexOf(':'))).toUpperCase());
            String counting = (s.substring(s.lastIndexOf(':') + 1, s.length()));

            if (city.matches(cityname.toUpperCase())) {
                int count = Integer.parseInt(counting) + 1;
                System.out.println("hhola");
                String fin = s.substring(0, s.lastIndexOf(':') + 1).concat(Integer.toString(count));
                System.out.println(fin);
                temp.add(fin);
            } else {
                temp.add(s);
            }
        }
        BufferedWriter outputWriter = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "\\Backend/searchfreq"));
        for (int i = 0; i < temp.size(); i++) {
            outputWriter.write(temp.get(i));
            //System.out.println(temp.get(i));
            outputWriter.newLine();
        }
        outputWriter.flush();
        outputWriter.close();

    }

    public static void initialize(String cityname) {

    }

    public static void first_time() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter("searchfreq");
        pw.println("Toronto:1");
        pw.println("Calgary:1");
        pw.println("Montreal:1");
        pw.println("Ottawa:1");
        pw.println("Vancouver:1");
        pw.println("Hamilton:1");
        pw.println("Edmonton:1");
        pw.println("Winnipeg:1");
        pw.println("Windsor:1");
        pw.println("Halifax:1");
        pw.close();

    }

    public static void main(String[] args) throws FileNotFoundException {
        first_time();

    }
}

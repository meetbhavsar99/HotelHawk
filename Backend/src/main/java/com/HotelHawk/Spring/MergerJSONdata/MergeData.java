package com.HotelHawk.Spring.MergerJSONdata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;

public class MergeData {

    public static void merge(String cityname) throws IOException {
        File file1 = new File(System.getProperty("user.dir") + "\\Backend/booking_json");
        File file2 = new File(System.getProperty("user.dir") + "\\Backend/hotelsca_json");
        File file3 = new File(System.getProperty("user.dir") + "\\Backend/MakeMy--Trip_json");

        BufferedReader br1 = new BufferedReader(new FileReader(file1));
        BufferedReader br2 = new BufferedReader(new FileReader(file2));
        BufferedReader br3 = new BufferedReader(new FileReader(file3));
        String data1 = (br1.readLine());
        String data2 = (br2.readLine());
        String data3 = (br3.readLine());

//        JSONArray jsonArray = new JSONArray();
//        JSONParser parser = new JSONParser();
//        Object obj = new JSONParser().parse(new FileReader(File.json));
        JSONObject json = new JSONObject();
        json.put("booking", data1);
        json.put("hotelsca", data2);
        json.put("mmt", data3);

//        ObjectMapper mapper = new ObjectMapper();
//        InputStream is = Test.class.getResourceAsStream("/test.json");
//        testObj = mapper.readValue(is, Test.class);
        BufferedWriter pw = new BufferedWriter(new FileWriter(System.getProperty("user.dir").concat("\\Backend/finaldata")));
        pw.write(json.toString());
        pw.close();
        remove_overwrite(cityname);
//
//
        //writing cityname_old data file

    }

    public static void remove_overwrite(String cityname) {
        String filePath = System.getProperty("user.dir").concat("\\Backend/finaldata");

        try {
            // Read content from the file
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();

            // Replace "\" with " "
            String mcontent = content.toString().replace("\\", " ");
            String c = mcontent.substring(0, mcontent.indexOf(":") + 1).concat(mcontent.substring(mcontent.indexOf("["), mcontent.length()));
            int i = c.indexOf("hotelsca");
            int j = c.indexOf("mmt");
            String ch = c.substring(0, i - 3).concat(c.substring(i - 2, i + 10)).concat(c.substring(i + 11, c.length()));
            String cj = ch.substring(0, j - 3).concat(ch.substring(j - 2, j + 5)).concat(ch.substring(j + 6, ch.length()));
            String cf = cj.substring(0, cj.lastIndexOf("]") + 1).concat(cj.substring(cj.lastIndexOf("]") + 2));

            // Write the modified content back to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(cf);
            writer.close();

            PrintWriter p = new PrintWriter(cityname.toLowerCase().concat("_finaldata"));
            p.println(cf);
            p.close();

            System.out.println("File overwritten successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        merge("Toronto");
    }
}

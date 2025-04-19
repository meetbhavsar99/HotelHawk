package com.HotelHawk.Spring.SelectHotel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class Select_hotel {
    public static String initialize(String cityname,String hotelname) throws IOException {
        File file= new File((cityname.toLowerCase()).concat("_finaldata"));
        BufferedReader br= new BufferedReader(new FileReader(file));
        String line= br.readLine();
        String cl= line.substring(line.indexOf('['),line.length());
        String cf= cl.substring(0,cl.indexOf("mmt")-2);
        //System.out.println(cf);
        JSONArray json_array= new JSONArray(cf);
        HashMap<Double, ArrayList<JSONObject>> map= new HashMap<Double,ArrayList<JSONObject>>();
        String address_loc="";
        String data="";
        int locs=0;
        for(int i=0;i< json_array.length();i++){
            System.out.println((json_array.getJSONObject(i).getString("Name ")));
            if ((json_array.getJSONObject(i).getString("Name ")).matches(hotelname)){
                data=json_array.getJSONObject(i).toString();
                locs=i;
                System.out.println("ok");
                address_loc= (location(json_array.getJSONObject(i).getString("Location ")));
            }
        }
        if (address_loc.length()>0){
            String[] s=address_loc.split(" ");
            double first_lat= Double.parseDouble(s[0]);
            double first_long= Double.parseDouble(s[1]);
            for(int i=0;i< json_array.length();i++){
                //System.out.println((json_array.getJSONObject(i).getString("Name ")));
                if (!((json_array.getJSONObject(i).getString("Name ")).matches(hotelname))){
                    String[] p= (location(json_array.getJSONObject(i).getString("Location "))).split(" ");
                    double second_lat= Double.parseDouble(p[0]);
                    double second_long= Double.parseDouble(p[1]);
                    double dist= calculateDistance(first_lat,first_long,second_lat,second_long);
                    if(map.containsKey(dist)){
                        map.get(dist).add((JSONObject) json_array.get(i));
                    }
                    else{
                        ArrayList<JSONObject> as= new ArrayList<JSONObject>();
                        as.add((JSONObject) json_array.get(i));
                        as.add((JSONObject) json_array.get(locs));
                        map.put(dist,as);

                    }
                }
            }
            double mins=100000.0;
            for(Double d:map.keySet()){
                if(mins>d){
                    mins=d;
                }
            }
            System.out.println(new JSONArray(map.get(mins)).toString());

            ArrayList<JSONObject> finalData= new ArrayList<JSONObject>();

            for(int i=0;i<map.get(mins).size();i++){
                System.out.println("hola");
                JSONObject json=new JSONObject();
                json.put("Name ",map.get(mins).get(i).getString("Name "));
                json.put("MinPrice ",map.get(mins).get(i).getString("MinPrice "));
                json.put("Review ",map.get(mins).get(i).getString("Review "));
                json.put("Images ",map.get(mins).get(i).getString("Images "));
                json.put("Location ", map.get(mins).get(i).getString("Location "));
                json.put("Facilities ", map.get(mins).get(i).getString("Facilities "));
                if (i!=map.get(mins).size()-1){
                    json.put("Distance ",Double.toString(mins));
                }
                else{
                    json.put("Distance ","");
                }
                System.out.println(json.toString());
                finalData.add(json);
            }
            return new JSONArray(finalData).toString();



        }
        else{
            return data;
        }

        //br.close();

    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double lon1Rad = Math.toRadians(lon1);
        double lon2Rad = Math.toRadians(lon2);

        double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        double y = (lat2Rad - lat1Rad);
        double distance = Math.sqrt(x * x + y * y) * 6371;

        return distance;
    }


    public static String location(String address){
        try{
            String encodedAddress = URLEncoder.encode(address, "UTF-8");
            String apiKey = "AIzaSyDk1gwQlnTWn1op72xrXSbrJBtzH09igz0"; // Replace with your Google Maps API Key
            String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + encodedAddress + "&key=" + apiKey;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));

            String output = "", full = "";
            while ((output = br.readLine()) != null) {
                //System.out.println(output);
                full += output;
            }
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode rootNode = mapper.readTree(full);
                JsonNode locationNode = rootNode.path("results").get(0).path("geometry").path("location");
                double latitude = locationNode.path("lat").asDouble();
                double longitude = locationNode.path("lng").asDouble();
                String fins= Double.toString(latitude).concat(" ").concat(Double.toString(longitude));

                System.out.println("Latitude: " + latitude);
                System.out.println("Longitude: " + longitude);

                return fins;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (IOException e){
            System.out.println(e);
        }
        return "";
    }


    public static void main(String[] args) throws IOException {
        System.out.println(initialize("calgary","Residence Inn by Marriott Calgary Downtown/Beltline District "));
    }
}

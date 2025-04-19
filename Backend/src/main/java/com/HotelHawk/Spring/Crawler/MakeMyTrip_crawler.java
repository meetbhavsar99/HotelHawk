package com.HotelHawk.Spring.Crawler;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;

import com.HotelHawk.Spring.FrequencyCount.FreqCount;
import com.HotelHawk.Spring.Parser.MakeMyTrip_parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class MakeMyTrip_crawler {

    public static Hashtable<String, Hashtable> extractCities(String[] cities, String checkin, String checkout) throws IOException {

        Hashtable<String, Hashtable> hs = new Hashtable<>();
        String[] incheck = checkin.split("/");
        String[] outcheck = checkout.split("/");
        System.out.println(checkout);
        System.out.println(outcheck.length);
        for (String city : cities) {
            // Create a new instance of the Chrome driver
            WebDriver driver = new ChromeDriver();

            // Open a website using Selenium
            String city_code = getcitycode(city);
            driver.get("https://www.makemytrip.com/hotels/hotel-listing/?checkin=" + incheck[1] + "" + incheck[2] + "" + incheck[0] + "&city=CT" + city.substring(0, 5).toUpperCase() + "&checkout=" + outcheck[1] + "" + outcheck[2] + "" + outcheck[0] + "&roomStayQualifier=2e0e&locusId=" + getcitycode(city) + "&country=CAN&locusType=city&searchText=" + city + "&regionNearByExp=3&rsc=1e2e0e");

            // wait for 5 seconds for the page to load
            //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            // Maximize the window
            driver.manage().window().maximize();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            for (int i = 0; i < 6; i++) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                // First scroll down , then scroll up a little and then scroll down to trigger loading of more jobs
                js.executeScript("window.scrollBy(0,2000)");
                js.executeScript("window.scrollBy(0,-4000)");
                js.executeScript("window.scrollBy(0,3000)");
                // Wait for the jobs to load
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            // Get the HTML content of the webpage using Selenium
            String html = driver.getPageSource();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // Parse the HTML using Jsoup
            Document document = Jsoup.parse(html);
            int count_cf = FreqCount.fc_mmt(city, document.text());
            //  System.out.println("document:" + document);
            Hashtable<String, String[]> hb = MakeMyTrip_parser.Make_My_Trip_Parser(document, city);

            hs.put(city, hb);
            fc_data(city, count_cf);
        }

        return hs;
    }

    public static void fc_data(String cityname, int count) throws IOException {
        System.out.println("Hola ".concat(Integer.toString(count)));
        System.out.println(cityname);
        String path = System.getProperty("user.dir");
        File f = new File(path.concat("\\Backend/").concat(cityname.toLowerCase()).concat("_fc"));
        BufferedReader br = new BufferedReader(new FileReader(f));
        String st;
        ArrayList<String> lines = new ArrayList<String>();
        while ((st = br.readLine()) != null) {
            lines.add(st);
        }
        lines.add("mmt:".concat(Integer.toString(count)));
        PrintWriter pw = new PrintWriter(f);
        for (String s : lines) {
            pw.println(s);
        }
        pw.close();
    }

    public static String getcitycode(String city) {
        Hashtable<String, String> citycodes = new Hashtable<String, String>();
        citycodes.put("TORONTO", "CTTORON");
        citycodes.put("CALGARY", "CTDIVISIONNO6");
        citycodes.put("VANCOUVER", "CTVANCO");
        citycodes.put("MONTREAL", "CTCOMMU");
        citycodes.put("OTTAWA", "CTOTTAW");
        citycodes.put("HAMILTON", "CTHAMILTO");
        citycodes.put("EDMONTON", "CTDIVIS");
        citycodes.put("WINNIPEG", "CTDIVISIONNO11");
        citycodes.put("WINDSOR", "CTESS");
        citycodes.put("HALIFAX", "CTHALIFA");

        return citycodes.get(city.toUpperCase());
    }

    public static void main(String[] args) throws IOException {

        String city = "toronto";
        String[] cities = {city};
        String checkindate = "2024-05-23";
        String checkoutdate = "2024-05-27";
        Hashtable<String, Hashtable> js = extractCities(cities, checkindate, checkoutdate);
        MakeMyTrip_parser.convert_json(js, city);
    }
}

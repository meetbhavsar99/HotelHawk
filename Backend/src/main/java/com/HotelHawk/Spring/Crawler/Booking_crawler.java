package com.HotelHawk.Spring.Crawler;

import com.HotelHawk.Spring.FrequencyCount.FreqCount;
import com.HotelHawk.Spring.Parser.Booking_parser;
import com.HotelHawk.Spring.SearchFrequency.SearchFreq;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;

public class Booking_crawler {

    public static String url = "https://www.booking.com";
    public static ArrayList<String> links = new ArrayList<String>();
    public static ArrayList<String> check = new ArrayList<String>();

    public static void extract_cities(String city) throws IOException {
        ///extracting hotel links from url
        String t = url + "/" + city + "-" + "Canada";
        Connection con = Jsoup.connect(t);
        Document doc = con.get();
        //getting frequency count for number of times cityname appears in the url and storing it in count_fc variable
        int count_fc = FreqCount.fc_booking(city, doc.text());

        Elements e = doc.getElementsByAttributeValueContaining("target", "_blank");

        for (Element l : e) {
            String str = l.attr("href");
            if (links.size() < 15) {
                if (!check.contains(str.substring(0, 100))) {
                    links.add(str);
                    check.add(str.substring(0, 100));
                    System.out.println(str);
                }
            }
        }
        //storing frequency count to seperate txt file that contains parser wise frequency count
        fc_data(city, count_fc);
        //saving hotel links in txt file
        save_links(links);

    }

    public static void fc_data(String cityname, int count) throws IOException {
        //storing frequency count to seperate txt file that contains parser wise frequency count
        System.out.println("Hola ".concat(Integer.toString(count)));
        String path = System.getProperty("user.dir");
        PrintWriter pw = new PrintWriter(path.concat("\\Backend/").concat(cityname.toLowerCase()).concat("_fc"));
        pw.println("booking:".concat(Integer.toString(count)));
        pw.close();

    }

    public static void save_links(ArrayList<String> links) throws FileNotFoundException {
        // saving all hotel links to txt file, for future parsing

        //System.out.println("https://www.booking.com/hotel/ca/days-inn-toronto-downtown.en-gb.html?label=gen173nr-1FCBcoggI46AdIM1gEaCeIAQGYAQm4ARfIAQzYAQHoAQH4AQmIAgGoAgO4Apntzq8GwAIB0gIkZWMxZTk2NDItNmMxMi00YTFiLTliOTYtMDAwNGYxZmFlOTAz2AIG4AIB&aid=304142&ucfs=1&arphpl=1&dest_id=-574890&dest_type=city&group_adults=2&req_adults=2&no_rooms=1&group_children=0&req_children=0&hpos=1&hapos=1&sr_order=popularity&srpvid=5182138d13ba0087&srepoch=1710470811&from_sustainable_property_sr=1&from=searchresults".length());
        PrintWriter pr = new PrintWriter("booking_links");
        for (int i = 0; i < links.size(); i++) {
            pr.println(links.get(i));
        }
        pr.close();
    }

    public static void main(String[] args) throws IOException {
        ///request is made to api stating the search city in search bar, crawler is called
        String url = "https://www.booking.com";
        String request_city = "Toronto";
        extract_cities(request_city);
        save_links(links);
    }
}

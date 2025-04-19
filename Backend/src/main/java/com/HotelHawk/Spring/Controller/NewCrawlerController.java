package com.HotelHawk.Spring.Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.HotelHawk.Spring.Crawler.Booking_crawler;
import com.HotelHawk.Spring.Crawler.Hotelsca_crawler;
import com.HotelHawk.Spring.Crawler.MakeMyTrip_crawler;
import com.HotelHawk.Spring.MergerJSONdata.MergeData;
import com.HotelHawk.Spring.Parser.Booking_parser;
import com.HotelHawk.Spring.Parser.Hotelsca_parser;
import com.HotelHawk.Spring.Parser.MakeMyTrip_parser;
import com.HotelHawk.Spring.SearchFrequency.SearchFreq;
import com.HotelHawk.Spring.spellcheck.SpellCheck;

@RestController
public class NewCrawlerController {

    /// user give http reques with city name
    @CrossOrigin
    @RequestMapping("/newsearch/{cityname}/{checkin_date}/{checkout_date}")
    public HttpEntity<String> crawl_all(@PathVariable String cityname, @PathVariable String checkin_date, @PathVariable String checkout_date) throws IOException, InterruptedException {
        SearchFreq.update(cityname);
        String[] d = {cityname};
        String fcityname = SpellCheck.main(d);
        System.out.println(checkin_date);

        booking_crawl(fcityname, checkin_date, checkout_date);
        hotel_crawl(fcityname, checkin_date, checkout_date);
        mmt_crawl(fcityname, checkin_date, checkout_date);

        ///mergering data from crawlers into one json file.
        MergeData.merge(cityname);

        String path = System.getProperty("user.dir");
        //System.out.println(path);
        File file = new File(path + "\\Backend/finaldata");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String temp = br.readLine();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("MyResponseHeader", br.readLine());
        return new HttpEntity<>(temp, responseHeaders);
    }

    @RequestMapping("/booking_crawler")
    public void booking_crawl(String cityname, String checkin_date, String checkout_date) throws IOException {
        Booking_crawler.extract_cities(cityname);
        Booking_parser.initialize(cityname, checkin_date, checkout_date);
    }

    @RequestMapping("/hotel_crawler")
    public void hotel_crawl(String cityname, String checkin_date, String checkout_date) throws IOException, InterruptedException {
        Hotelsca_crawler.cities(cityname, checkin_date, checkout_date);
        Hotelsca_parser.extract_links(checkin_date, checkout_date, cityname);
    }

    @RequestMapping("/mmt_crawler")
    public void mmt_crawl(String cityname, String checkin_date, String checkout_date) throws IOException {
        String[] city = {cityname};
        Hashtable<String, Hashtable> t = MakeMyTrip_crawler.extractCities(city, checkin_date, checkout_date);
        MakeMyTrip_parser.convert_json(t, cityname);

    }
}

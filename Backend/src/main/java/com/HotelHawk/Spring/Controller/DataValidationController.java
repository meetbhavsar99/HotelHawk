package com.HotelHawk.Spring.Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.HotelHawk.Spring.DataValidation.DataValidation;
import com.HotelHawk.Spring.MergerJSONdata.MergeData;
import com.HotelHawk.Spring.SearchFrequency.SearchFreq;
import com.HotelHawk.Spring.spellcheck.SpellCheck;

@RestController
@RequestMapping("/datavalidate")
public class DataValidationController {

    @CrossOrigin
    @GetMapping
    public HttpEntity<String> datavalidation(@RequestParam String cityname, @RequestParam String checkin_date, @RequestParam String checkout_date, @RequestParam String crawl_type) throws IOException, InterruptedException {
        System.out.println(cityname);
        System.out.println(checkin_date);
        System.out.println(checkout_date);
        String[] result = DataValidation.check(checkin_date, checkout_date, cityname);
        if (result.length == 2) {
            //go to call crawler,
            if ((crawl_type.toLowerCase()).matches("oldsearch")) {
                SearchFreq.update(cityname);
                String[] d = {cityname};
                String fcityname = SpellCheck.main(d);
                System.out.println(fcityname);
                String path = System.getProperty("user.dir").concat("\\Backend/").concat(fcityname.toLowerCase());
                //System.out.println(path);
                File file = new File(path + "_finaldata");
                BufferedReader br = new BufferedReader(new FileReader(file));
                String temp = br.readLine();
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.set("MyResponseHeader", br.readLine());
                return new HttpEntity<>(temp, responseHeaders);
            } else {
                SearchFreq.update(cityname);
                String[] d = {cityname};
                String fcityname = SpellCheck.main(d);
                System.out.println(checkin_date);
                NewCrawlerController c = new NewCrawlerController();
                c.booking_crawl(fcityname, checkin_date, checkout_date);
                c.hotel_crawl(fcityname, checkin_date, checkout_date);
                c.mmt_crawl(fcityname, checkin_date, checkout_date);

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
        } else {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("MyResponseHeader", "");
            return new ResponseEntity<>("Citynames should be of big cities only, checkin and checkout date should have good format and checkkout date should be greater than checkin date", HttpStatus.BAD_REQUEST);
        }
    }
}

package com.HotelHawk.Spring.Controller;

import com.HotelHawk.Spring.WordCompletion.WordCompletion;
import com.HotelHawk.Spring.spellcheck.SpellCheck;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@CrossOrigin
@org.springframework.stereotype.Controller

public class Controller {
    @RequestMapping("/")
    public String homepage(){
        return "index";
    }
//    @GetMapping("/search/{cityname}")
//    public HttpEntity<String> crawl_all(@PathVariable String cityname) throws IOException, InterruptedException {
//        String fcityname= SpellCheck.initialize(cityname);
//        //getting city name from HTTP Request
//        CrawlerController c=new CrawlerController();
//        c.booking_crawl(fcityname);
//        //c.hotel_crawl(fcityname);
//        String path = System.getProperty("user.dir");
//        //System.out.println(path);
//        File file=new File(path+"\\finaldata");
//        BufferedReader br=new BufferedReader(new FileReader(file));
//        String temp=br.readLine();
//        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.set("MyResponseHeader", br.readLine());
//        return new HttpEntity<>(temp, responseHeaders);
//        //return ResponseEntity.ok().body(br.readLine());
//
//    }
//    @RequestMapping("/find/{cityname}")
//    public HttpEntity<String> find_city(@PathVariable String cityname){
//        List<String> cities= WordCompletion.initialize(cityname);
//        String listString = String.join(", ", cities);
//        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.set("MyResponseHeader", "value");
//        return new HttpEntity<>(listString, responseHeaders);
//        ///getting search freq data
//        //SearchFrequencyController sc=new SearchFrequencyController();
//    }
}
package com.HotelHawk.Spring.Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.HotelHawk.Spring.SearchFrequency.SearchFreq;
import com.HotelHawk.Spring.spellcheck.SpellCheck;

@RestController
public class OldCrawlerController {

    @CrossOrigin
    @RequestMapping("/oldsearch/{cityname}")
    public HttpEntity<String> crawl_all(@PathVariable String cityname) throws IOException, InterruptedException {
        /// getting file from cityname_final_data
        SearchFreq.update(cityname);
        String[] d = {cityname};
        String fcityname = SpellCheck.main(d);
        String path = System.getProperty("user.dir").concat("\\Backend/").concat(fcityname.toLowerCase());
        //System.out.println(path);
        File file = new File(path + "_finaldata");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String temp = br.readLine();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("MyResponseHeader", br.readLine());
        return new HttpEntity<>(temp, responseHeaders);
    }
}

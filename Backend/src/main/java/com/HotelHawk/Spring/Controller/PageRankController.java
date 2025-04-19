package com.HotelHawk.Spring.Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PageRankController {

    @CrossOrigin
    @RequestMapping("/pg/")
    public static HttpEntity<String> pgrank() throws IOException {
        File file = new File(System.getProperty("user.dir") + "\\Backend/pagerank");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String temp = "";
        String fins = "";
        while ((fins = br.readLine()) != null) {
            temp += fins;
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("MyResponseHeader", temp);
        return new HttpEntity<>(temp, responseHeaders);
    }
}

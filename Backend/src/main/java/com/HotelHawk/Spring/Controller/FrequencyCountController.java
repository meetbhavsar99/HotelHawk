package com.HotelHawk.Spring.Controller;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
public class FrequencyCountController {
    @CrossOrigin
    @RequestMapping("/fc/{cityname}")
    public static HttpEntity<String> freqcount(@PathVariable String cityname) throws IOException {
        File file=new File(cityname.toLowerCase()+"_fc");
        BufferedReader br=new BufferedReader(new FileReader(file));
        String temp="";
        String s;
        JSONObject json= new JSONObject();
        while((s=br.readLine())!=null){
            temp+=s.concat(" ");
            json.put(s.substring(0,s.indexOf(":")),s.substring(s.indexOf(":")+1,s.length()));
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("MyResponseHeader", json.toString());
        return new HttpEntity<>(json.toString(), responseHeaders);
    }
}

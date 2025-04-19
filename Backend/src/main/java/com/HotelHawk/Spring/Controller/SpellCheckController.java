package com.HotelHawk.Spring.Controller;

import com.HotelHawk.Spring.spellcheck.SpellCheck;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpellCheckController {
    @CrossOrigin
    @RequestMapping("/spellcheck")
    @GetMapping
    public static HttpEntity<String> spell_check(@RequestParam String cityname){
        System.out.println(cityname);
    
        if(cityname.matches("")){
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("MyResponseHeader", "");
            return new ResponseEntity<>("Please enter city", HttpStatus.BAD_REQUEST);
        }
        String[] d={cityname};
        String fcityname= SpellCheck.main(d);
        String t="";
        if(cityname.matches(fcityname)){
            t="";
        }
        else{
            t= fcityname;
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("MyResponseHeader", t);
        return new HttpEntity<>(t, responseHeaders);
    }
}

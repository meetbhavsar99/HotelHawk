package com.HotelHawk.Spring.Controller;

import com.HotelHawk.Spring.Filter.Filter;
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

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class FilterController {
    @CrossOrigin
    @RequestMapping("/filter")
    @GetMapping
    public HttpEntity<String> filter(@RequestParam String cityname, @RequestParam String minprice, @RequestParam String maxprice, @RequestParam String minreviews) throws IOException {
        if (!isValidPrice(minprice, numericRegex) || Integer.parseInt(minprice)<0 || (!isValidPrice((maxprice), numericRegex)) || (Integer.parseInt(maxprice)<Integer.parseInt(minprice))){
            System.out.println("Invalid minimum price. Please input numerical value.");
            return new ResponseEntity<>("Invalid Price", HttpStatus.BAD_REQUEST);
        }
        String filtered_data= Filter.filter_data(cityname, Integer.parseInt(minprice), Integer.parseInt(maxprice), Integer.parseInt(minreviews));
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("MyResponseHeader", "");
        return new HttpEntity<>(filtered_data, responseHeaders);
    }

    // Regex pattern for numerical input
    public static String numericRegex = "^-?\\d+$";

    public static boolean isValidPrice(String price, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(price);
        return matcher.matches();
    }
}

package com.HotelHawk.Spring.Controller;

import com.HotelHawk.Spring.SelectHotel.Select_hotel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class SelectHotelController {
    @CrossOrigin
    @RequestMapping("/select/{cityname}/{hotelname}")
    public static HttpEntity<String> select_hotel(@PathVariable String cityname, @PathVariable String hotelname) throws IOException {
        System.out.println(hotelname);
        String ar= Select_hotel.initialize(cityname, hotelname.concat(" "));
        System.out.println(ar);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("MyResponseHeader", ar);
        return new HttpEntity<>(ar, responseHeaders);
    }
}


package com.HotelHawk.Spring.DataValidation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidation {
    //check that both dates are greater than todays date
    // year should be of 2 digits   (add 20)
    /// moth can be of single digits

    public static String dateFormatRegex = "^(?:\\d{4}[-/.]?\\d{2}[-/.]?\\d{2}|\\d{8})$";
    // Regex pattern for city name (only alphabets)
    public static String cityNameRegex = "^[A-Za-z]+$";

    private static final String URL_REGEX = "^(https?|ftp)://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(/\\S*)?$";

    public static boolean isValidURL(String url) {
        //checking whether url is valid or not by regex, url should contain https
        Pattern pattern = Pattern.compile(URL_REGEX);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    public static boolean checkCityName(String cityName, String regex) {
        //checking whether cityname contains only alphabets
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cityName);
        return matcher.matches();
    }

    public static String[] check(String checkInDateInput, String checkOutDateInput, String cityName) {
        //method to check whether cityname contains only alphabets , checkin and checkout dates are numnerical,
        //checkin and checkout dates are greater than current date and time and checkin date is less than checkout date
        if (!checkCityName(cityName, cityNameRegex)) {
            System.out.println("Invalid city name. Please input only alphabetic characters.");
            String [] s={""};
            return s;
        }
        if (!checkDateFormat(checkInDateInput, dateFormatRegex)) {
            System.out.println("Please input the date again.");
            String [] s={""};
            return s;
        }
        if (!checkDateFormat(checkOutDateInput, dateFormatRegex)) {
            System.out.println("Please input the date again.");
            String [] s={""};
            return s;
        }
        LocalDate currentDate = LocalDate.now();
        LocalDate checkInDate = parseDate(checkInDateInput);
        LocalDate checkOutDate = parseDate(checkOutDateInput);

        // Check if the dates are valid and in the correct order
        if (checkInDate == null || checkOutDate == null || currentDate.isAfter(checkInDate) || checkOutDate.isBefore(checkInDate)) {
            System.out.println("Invalid dates. Please check your input.");
            String [] s={""};
            return s;
        } else {
            String formattedCheckInDate = checkInDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
            String formattedCheckOutDate = checkOutDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

            // Return the dates in a string array
            String[] datesArray = {formattedCheckInDate, formattedCheckOutDate};
            System.out.println("Check-in date: " + formattedCheckInDate);
            System.out.println("Check-out date: " + formattedCheckOutDate);
            return new String[]{formattedCheckInDate,formattedCheckOutDate};
        }
    }
    // Function to check if date format is valid using regex
    public static boolean checkDateFormat(String date, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    // Function to parse date string into LocalDate object
    public static LocalDate parseDate(String date) {
        LocalDate parsedDate = null;
        try {
            parsedDate = LocalDate.parse(date.replace("-", "").replace("/", ""), DateTimeFormatter.BASIC_ISO_DATE);
        } catch (Exception e) {
            // Date parsing failed
        }
        return parsedDate;
    }

    public static void main(String[] args) {
        String checkinDate = "2024-06-01"; // Example date input
        String checkoutDate = "2024-06-04"; // Example date input
        String cityname= "tor";

        String[] result = check(checkinDate, checkoutDate,cityname);
        if (result.length == 2) {
            System.out.println("Valid dates: Checkin Date - " + result[0] + ", Checkout Date - " + result[1]);
        } else {
            System.out.println("Invalid dates or checkout date is not after checkin date.");
        }
    }
}


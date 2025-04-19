package com.HotelHawk.Spring.Crawler;

import com.HotelHawk.Spring.FrequencyCount.FreqCount;
import com.HotelHawk.Spring.Parser.Hotelsca_parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Hotelsca_crawler {

    public static ArrayList<String> linkss = new ArrayList<String>();

    public static void cities(String cityname, String checkin_date, String checkout_date) throws IOException, InterruptedException {
        // creating a map that stores cityname and its states
        HashMap<String, String> states = new HashMap<String, String>();
        states.put("toronto", "Ontario");
        states.put("calgary", "Alberta");
        states.put("vancouver", "British%20Columbia");
        states.put("montreal", "Quebec");
        states.put("windsor", "Ontario");
        states.put("winnipeg", "Manitoba");
        states.put("halifax", "Nova%20Scotia");
        states.put("edmonton", "Alberta");
        states.put("ottawa", "Alberta");
        states.put("hamilton", "Alberta");
        System.out.println(checkin_date);

        // Adding checkin date, checkout date, city and province to base url to access hotels url
        String url = ((((("https://ca.hotels.com/Hotel-Search?adults=2&d1=".concat(checkin_date)).concat("&d2=")).concat(checkout_date)).concat("&destination=")).concat(cityname)).concat("%2C%20".concat(states.get(cityname.toLowerCase())).concat("%2C%20Canada"));
        System.out.println(url);
        WebDriver driver = new ChromeDriver();
        driver.get(url);

        String html = driver.getPageSource();
        Document d = Jsoup.parse(html);
        //getting frequency count for number of times cityname appears in the url and storing it in count_fc variable
        int count_fc = FreqCount.fc_hotels(cityname, d.text());

        driver.manage().window().maximize();
        //scrolling website by Javascript Executor to load hotels for the cityname
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,5000)", "");
        Thread.sleep(1000);
        List<WebElement> links = driver.findElements(By.className("uitk-card-link"));
        //List<WebElement> links= driver.findElements(By.cssSelector("property-listing-results"));
        for (WebElement w : links) {
            //System.out.println(w.getAttribute("href"));
            if (linkss.size() <= 10) {
                if (w.getAttribute("href") != null) {
                    linkss.add(w.getAttribute("href"));
                }
            }
        }
        driver.quit();

//        System.out.println(linkss.size());
        fc_data(cityname, count_fc);
        save_links();

    }

    public static void fc_data(String cityname, int count) throws IOException {
        //storing frequency count to seperate txt file that contains parser wise frequency count

        System.out.println("Hola ".concat(Integer.toString(count)));
        System.out.println(cityname);
        String path = System.getProperty("user.dir");
        File f = new File(path.concat("\\Backend/").concat(cityname.toLowerCase()).concat("_fc"));
        BufferedReader br = new BufferedReader(new FileReader(f));
        String st;
        ArrayList<String> lines = new ArrayList<String>();
        int counter = 0;
        while ((st = br.readLine()) != null) {
            if (st.substring(0, st.indexOf(":")).matches("hotelsca")) {
                counter = 1;
                lines.add("hotelsca:".concat(Integer.toString(count)));
            } else {
                lines.add(st);
            }
        }
        if (counter == 0) {
            lines.add("hotelsca:".concat(Integer.toString(count)));
        }
        PrintWriter pw = new PrintWriter(f);
        for (String s : lines) {
            pw.println(s);
        }
        pw.close();
    }

    public static void save_links() throws FileNotFoundException {
        //saving hotel url in seperate txt file for future reference

        //System.out.println("https://www.booking.com/hotel/ca/days-inn-toronto-downtown.en-gb.html?label=gen173nr-1FCBcoggI46AdIM1gEaCeIAQGYAQm4ARfIAQzYAQHoAQH4AQmIAgGoAgO4Apntzq8GwAIB0gIkZWMxZTk2NDItNmMxMi00YTFiLTliOTYtMDAwNGYxZmFlOTAz2AIG4AIB&aid=304142&ucfs=1&arphpl=1&dest_id=-574890&dest_type=city&group_adults=2&req_adults=2&no_rooms=1&group_children=0&req_children=0&hpos=1&hapos=1&sr_order=popularity&srpvid=5182138d13ba0087&srepoch=1710470811&from_sustainable_property_sr=1&from=searchresults".length());
        PrintWriter pr = new PrintWriter("hotelsca_links");
        for (int i = 0; i < linkss.size(); i++) {
            pr.println(linkss.get(i));
        }

        pr.close();

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        //cities("Toronto");
        //Hotelsca_parser.extract_links();
    

///request is made to api stating the search city in search bar, crawler is called
        //just split url and add request city
        //String request_city= "Toronto";
        //String furl="https://ca.hotels.com/Hotel-Search?adults=2&d1=2024-05-21&d2=2024-05-22&destination=Toronto%2C%20Ontario%2C%20Canada&flexibility=0_DAY&regionId=4089&rooms=1&semdtl=&sort=RECOMMENDED&theme=&useRewards=false&userIntent=";
        //cities();
        //save_links();
    }
}

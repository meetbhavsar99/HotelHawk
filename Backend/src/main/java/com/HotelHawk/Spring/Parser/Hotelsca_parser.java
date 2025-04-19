package com.HotelHawk.Spring.Parser;

import com.HotelHawk.Spring.FrequencyCount.FreqCount;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hotelsca_parser {

    public static ArrayList<String> links = new ArrayList<String>();
    public static HashMap<String, ArrayList<String>> hotels = new HashMap<String, ArrayList<String>>();

    public static void extract_links(String checkin_date, String checkout_date, String cityname) throws IOException, InterruptedException {
        File file = new File("hotelsca_links");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        //int count=0;
        while ((st = br.readLine()) != null) {
            String t = "chkin=".concat(checkin_date).concat("&chkout=").concat(checkout_date);
            links.add((st.substring(0, st.indexOf('?') + 1)).concat(t).concat(st.substring(st.indexOf('?') + 35, st.length())));

        }
        hotels_parse(cityname);
    }

    public static void hotels_parse(String cityname) throws IOException, InterruptedException {
        WebDriver driver = new ChromeDriver();
        ArrayList<JSONObject> json_array = new ArrayList<JSONObject>();
        for (String link : links) {
            ArrayList<String> temp_data = new ArrayList<>();
            if (link != null) {
                System.out.println("hotelsca");
                System.out.println(link);
                if (true) {
                    try {
                        //counting frequency count
                        Document d = Jsoup.parse(driver.toString());
                        driver.get(link);
                        By locator = By.cssSelector(".uitk-text.uitk-type-300.uitk-text-default-theme.is-visually-hidden");
                        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
                        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("window.scrollBy(0,5000)", "");
                        js.executeScript("window.scrollBy(0,-2500)", "");
                        Thread.sleep(1000);
                        driver.manage().window().maximize();
                        //                driver.wait(10000);
                        //getting name of hotel
                        WebElement name = driver.findElement(By.tagName("h1"));
                        temp_data.add(name.getText());
                        //getting photos
                        List<WebElement> photos = driver.findElements(By.cssSelector(".uitk-image-placeholder.uitk-image-placeholder-image"));
                        System.out.println(name.getText());
                        String im = "";
                        int count = 0;
                        for (WebElement w : photos) {
                            List<WebElement> phots = w.findElements(By.tagName("img"));
                            for (WebElement p : phots) {
                                if (count < 6) {
                                    im += (p.getAttribute("src")) + " ";
                                } else {
                                    break;
                                }
                            }
                        }
                        temp_data.add(im);
                        //getting reviews
                        try {
                            WebElement rev = driver.findElement(By.xpath("//meta[@itemprop='ratingValue']"));
                            System.out.println(rev.getAttribute("content"));
                            temp_data.add(rev.getAttribute("content"));
                        } catch (NoSuchElementException e) {
                            temp_data.add("No reviews");
                            System.out.println(e);
                        }

                        //getting hotel location
                        try {
                            WebElement street_loc = driver.findElement(By.xpath("//meta[@itemprop='streetAddress']"));
                            WebElement city_loc = driver.findElement(By.xpath("//meta[@itemprop='name']"));
                            WebElement province_loc = driver.findElement(By.xpath("//meta[@itemprop='addressRegion']"));
                            System.out.println(street_loc.getAttribute("content").concat(",").concat(city_loc.getAttribute("content")).concat(",").concat(province_loc.getAttribute("content")));
                            temp_data.add((street_loc.getAttribute("content").concat(",").concat(city_loc.getAttribute("content")).concat(",").concat(province_loc.getAttribute("content"))));

                        } catch (NoSuchElementException e) {
                            temp_data.add("Location not Specified");
                        }

                        //getting hotel description
                        //                WebElement desc= driver.findElement(By.xpath("//*[@id=\"app-layer-base\"]/div/main/div/div/section/div[1]/div[1]/div[2]/div/div[3]/div[13]/div/div/section/div/div/div/div[2]/div/div/div[3]/div/div/div/div/div/div/div"));
                        //                System.out.println(desc.getText());
                        //                List<WebElement> desc=driver.findElements(By.cssSelector("uitk-layout-grid-item"));
                        //                for(WebElement w:desc){
                        //                    WebElement temp= w.findElement(By.cssSelector(".uitk-text.uitk-type-300.uitk-text-default-theme"));
                        //                    if (temp.getText()!=null){
                        //                        System.out.println(temp.getText());
                        //                    }
                        //                }
                        //getting facilities provided by the hotel
                        ArrayList<String> facilities_list = new ArrayList<String>();
                        List<WebElement> faci = driver.findElements(By.cssSelector(".uitk-layout-grid.uitk-layout-grid-has-auto-columns.uitk-layout-grid-has-columns-by-medium.uitk-layout-grid-has-columns-by-large.uitk-layout-grid-has-space.uitk-layout-grid-display-grid.uitk-spacing.uitk-spacing-padding-blockend-four"));
                        for (WebElement face : faci) {
                            if ((face.findElement(By.tagName("h3")).getText()).matches("Popular amenities")) {
                                List<WebElement> facilities = face.findElements(By.cssSelector(".uitk-typelist-item.uitk-typelist-item-bullet-icon-alternate.uitk-typelist-item-bullet-icon.uitk-typelist-item-orientation-stacked.uitk-typelist-item-size-2.uitk-typelist-item-bullet-icon-default-theme.uitk-typelist-item-indent"));
                                for (WebElement f : facilities) {
                                    System.out.println(f.getText());
                                    String te = (f.getText().replace("\n", ","));
                                    //System.out.println(te);
                                    String[] temp = te.split(",");
                                    for (String x : temp) {
                                        if (!facilities_list.contains(x)) {
                                            facilities_list.add(x);
                                        }
                                    }

                                }
                            }
                        }
                        String fs = "";
                        for (String x : facilities_list) {
                            fs += x.concat(",");
                        }
                        System.out.println("hola");
                        System.out.println(fs);
                        temp_data.add(fs);
                        //WebElement faci= driver.findElement(By.cssSelector(".uitk-layout-grid.uitk-layout-grid-has-auto-columns.uitk-layout-grid-has-columns.uitk-layout-grid-has-space.uitk-layout-grid-display-grid"));

                        //getting room type, its data and its images
                        ////                List<WebElement> room_types= driver.findElements(By.cssSelector(".uitk-layout-flex.uitk-layout-flex-block-size-full-size.uitk-layout-flex-flex-direction-column.uitk-layout-flex-justify-content-space-between.uitk-card.uitk-card-roundcorner-all.uitk-card-has-border.uitk-card-has-overflow.uitk-card-has-primary-theme"));
                        ////                for(WebElement rt:room_types){
                        ////                    JSONObject json= new JSONObject();
                        ////                    System.out.println(rt.findElement(By.cssSelector(".uitk-heading.uitk-heading-6")).getText());
                        ////                    json.put("RoomName",rt.findElement(By.cssSelector(".uitk-heading.uitk-heading-6")).getText());
                        ////                    String imgs="";
                        ////                    List<WebElement> img= rt.findElements(By.tagName("img"));
                        ////                    for(WebElement w:img){
                        ////                        imgs+=(w.getAttribute("src")).concat(" ");
                        ////                    }
                        ////                    json.put("RoomImage",imgs);
                        ////                    //facilities of different rooms
                        //////                    List<WebElement> fa_list= rt.findElements(By.cssSelector(".uitk-typelist-item.uitk-typelist-item-bullet-icon-standard.uitk-typelist-item-bullet-icon.uitk-typelist-item-orientation-stacked.uitk-typelist-item-size-2.uitk-typelist-item-bullet-icon-default-theme.uitk-typelist-item-indent"));
                        //////                    for(WebElement w: fa_list){
                        //////                        System.out.println(w.getText());
                        //////                    }
                        //
                        //                    List<WebElement> pric= rt.findElements(By.cssSelector(".uitk-layout-flex.uitk-layout-flex-align-items-center.uitk-layout-flex-flex-direction-row.uitk-layout-flex-justify-content-flex-start.uitk-layout-flex-gap-one.uitk-layout-flex-flex-wrap-wrap"));
                        //                    for(WebElement p:pric){
                        //                        if( (p.getAttribute("data-test-id")).matches("price-summary-message-line")){
                        //                            Pattern pattern = Pattern.compile("CA \\$(\\d+)");
                        //                            Matcher matcher = pattern.matcher(p.getText());
                        //                            if (matcher.find()) {
                        //                                // Extract and print the number
                        //                                String numberString = matcher.group(1);
                        //                                int number = Integer.parseInt(numberString);
                        //                                json.put("price",Integer.toString(number));
                        //                            }
                        //                        }
                        //                    }
                        //                    json_array.add(json);
                        //                }

                        //getting room prices, getting min price right now
                        //int min=100000;
                        String str = "";
                        List<WebElement> pric = driver.findElements(By.cssSelector(".uitk-text.uitk-type-300.uitk-text-default-theme.is-visually-hidden"));
                        for (WebElement w : pric) {
                            if (w.getText() != "") {
                                String t = w.getText();
                                String t_f = t.substring(t.lastIndexOf('$') + 1);
                                //                        if(min>Integer.parseInt(t_f)){
                                //                            min=Integer.parseInt(t_f);
                                //                        }
                                str += t_f + ' ';
                            }
                        }
                        String[] price = str.split(" ");
                        System.out.println(price[0]);
                        temp_data.add(price[0]);
                        hotels.put(temp_data.get(0), temp_data);
                    } catch (TimeoutException e) {
                        System.out.println(e);
                    }
                }
            }

        }

        convert_json(json_array);

    }

    public static void convert_json(ArrayList<JSONObject> json_array) throws IOException {
        JSONObject main_json = new JSONObject();
        ArrayList<JSONObject> ar = new ArrayList<JSONObject>();
        String fins = "[";
        for (String s : hotels.keySet()) {
//            fins+="{";
//            fins+="Name:".concat(hotels.get(s).get(0));
//            fins+="Image".concat(hotels.get(s).get(1));
//            fins+="Review".concat(hotels.get(s).get(2));
//            fins+="Price".concat(hotels.get(s).get(3));
//            fins+="}";
            JSONObject json = new JSONObject();
            json.put("Name", hotels.get(s).get(0));
            json.put("MinPrice", hotels.get(s).get(5));
            json.put("Review", hotels.get(s).get(2));
            json.put("Images", hotels.get(s).get(1));
            json.put("Location", hotels.get(s).get(3));
            json.put("Facilities", hotels.get(s).get(4));
            //json.put("RoomData", new JSONArray(json_array).toString());

            ar.add(json);
        }
        fins += "]";
        //main_json.put("Booking",new JSONArray(ar));
        String path = System.getProperty("user.dir");
        PrintWriter pw = new PrintWriter(path + "\\Backend/hotelsca_json");
        pw.println(new JSONArray(ar).toString());
        pw.close();
    }

    public static void main(String[] args) {

    }
}

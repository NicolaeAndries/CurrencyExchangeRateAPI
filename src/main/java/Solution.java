import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Double lastValue = (double) 0;

        try {
            while (true) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
                LocalDateTime currentDateTime = LocalDateTime.now();
                String dateTime = dateTimeFormatter.format(currentDateTime);

                URL url = new URL("https://api.apilayer.com/fixer/convert?to=USD&from=EUR&amount=100&apikey=6A52bwZH3zvlC0g7ususni99TbMmrAPe");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                if (connection.getResponseCode() == 200) {
                    StringBuilder jsonStringContent = new StringBuilder();
                    Scanner scanner = new Scanner(url.openStream());
                    while (scanner.hasNext()) {
                        jsonStringContent.append(scanner.nextLine());
                    }

                    JSONParser parse = new JSONParser();
                    JSONObject dataObject = (JSONObject) parse.parse(jsonStringContent.toString());
                    JSONObject object = (JSONObject) dataObject.get("info");
                    if (lastValue != object.get("rate")) {
                        System.out.println("________New Data________");
                        System.out.println("[EUR] -> [USD]: " + object.get("rate") + " at " + dateTime);
                        System.out.println("________________________");
                    }

                    lastValue = (Double) object.get("rate");
                    Thread.sleep(2 * 1000);
                }
            }
        } catch (IOException | ParseException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }
}

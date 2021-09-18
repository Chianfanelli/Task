import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseCountriesAndPhoneNumbers {

    public static String parseUrl(URL url)  {
        if (url == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static void parseCountriesJson(String resultJson) {
        try {
            JSONObject countriesJsonObject = (JSONObject) JSONValue.parseWithException(resultJson);
            HashMap<String, String> countryCodesAndNames = new HashMap<>();
            JSONArray countriesArray = (JSONArray) countriesJsonObject.get("countries");
            JSONObject countriesData = new JSONObject();
            Map<String,List<String>> phoneAndNames = new HashMap<>();
            for(int i = 0; i< countriesArray.size(); i++) {
                countriesData = (JSONObject) countriesArray.get(i);
                countryCodesAndNames.put(countriesData.get("country").toString(),countriesData.get("country_text").toString());
            }
            for(String countryCode : countryCodesAndNames.keySet()) {
                String NewUrl ="https://onlinesim.ru/api/getFreePhoneList?country=" + countryCode;
                URL urlTask2 = createUrl(NewUrl);
                String FinalResult = parseUrl(urlTask2);
                phoneAndNames.put(countryCodesAndNames.get(countryCode),parsePhonesJson(FinalResult));
            }
            for(String countryCodes : phoneAndNames.keySet()) {
                System.out.println(countryCodes);
                System.out.println(phoneAndNames.get(countryCodes));
            }
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }

    public static List<String> parsePhonesJson(String FinalResult) throws ParseException {
        JSONObject phoneJsonObject = (JSONObject) JSONValue.parseWithException(FinalResult);
        List<String> numbers = new ArrayList<>();
        JSONArray phonesArray = (JSONArray) phoneJsonObject.get("numbers");
        JSONObject phonesData =new JSONObject();
        for (int i = 0; i < phonesArray.size(); i++) {
            phonesData = (JSONObject) phonesArray.get(i);
            numbers.add((String) phonesData.get("full_number"));
        }
        return numbers;
    }

    public static URL createUrl(String link) {
        try {
            return new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
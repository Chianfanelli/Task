import java.net.URL;

public class Main {
    public static final String Task_URL1 = "https://onlinesim.ru/api/getFreeCountryList";

    public static void main(String[] args)  {

        URL urlTask1 = ParseCountriesAndPhoneNumbers.createUrl(Task_URL1);
        String resultJson1 = ParseCountriesAndPhoneNumbers.parseUrl(urlTask1);
        ParseCountriesAndPhoneNumbers.parseCountriesJson(resultJson1);
    }
}
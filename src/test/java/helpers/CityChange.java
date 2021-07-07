package helpers;

public class CityChange {
    static String New_city1 = "Москва",
            New_city2 = "Санкт-Петербург";

    public static String getCity(String old_city) {
        if (old_city.equals(New_city1))
            return New_city2;
        else
            return New_city1;
    }
}

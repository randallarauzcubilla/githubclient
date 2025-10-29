package util;

/**
 *
 * @author Randall AC
 */
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Formatter {
    public static String formatDate(String isoDate) {
        try {
            ZonedDateTime zdt = ZonedDateTime.parse(isoDate);
            return zdt.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"));
        } catch (Exception e) {
            return isoDate;
        }
    }
}
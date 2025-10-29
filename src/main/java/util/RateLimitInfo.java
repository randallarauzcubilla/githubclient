package util;

/**
 *
 * @author Randall AC
 */
import java.net.http.HttpResponse;

public class RateLimitInfo {
    public static String extract(HttpResponse<?> response) {
        String limit = response.headers().firstValue("X-RateLimit-Limit").orElse("?");
        String remaining = response.headers().firstValue("X-RateLimit-Remaining").orElse("?");
        String reset = response.headers().firstValue("X-RateLimit-Reset").orElse("?");
        return "⏳ Rate limit: " + remaining + "/" + limit + " (reset: " + reset + ")";
    }
}
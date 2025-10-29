package util;

import java.net.http.HttpResponse;

public class RateLimitInfo {
    public static String extract(HttpResponse<?> response) {
        if (response == null) {
            return "Rate limit: desconocido (no se recibió respuesta)";
        }

        String limit = response.headers().firstValue("X-RateLimit-Limit").orElse("?");
        String remaining = response.headers().firstValue("X-RateLimit-Remaining").orElse("?");
        String reset = response.headers().firstValue("X-RateLimit-Reset").orElse("?");

        return "Rate limit: " + remaining + "/" + limit + " (reset: " + reset + ")";
    }
}
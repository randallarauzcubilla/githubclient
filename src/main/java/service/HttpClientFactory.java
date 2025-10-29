package service;

/**
 *
 * @author Randall AC
 */
import java.net.http.HttpClient;
import java.time.Duration;

public class HttpClientFactory {
    private static final HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build();

    public static HttpClient getClient() {
        return client;
    }
}
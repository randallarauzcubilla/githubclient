package service;

/**
 *
 * @author Randall AC
 */
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Repo;
import domain.User;

import java.net.http.*;
import java.net.URI;
import java.util.List;

public class GitHubApi {

    private final HttpClient client = HttpClientFactory.getClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private HttpResponse<?> lastResponse;

    public User fetchUser(String username) throws Exception {
        String url = "https://api.github.com/users/" + username;
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url))
                .header("Accept", "application/vnd.github+json")
                .header("User-Agent", "java-github-client");

        String token = System.getenv("GITHUB_TOKEN");
        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }

        HttpResponse<String> response = client.send(builder.GET().build(), HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 404) {
            throw new RuntimeException("Usuario no encontrado");
        }
        if (response.statusCode() == 403) {
            throw new RuntimeException("Rate limit excedido");
        }

        return mapper.readValue(response.body(), User.class);
    }

    public List<Repo> fetchRepos(String username) throws Exception {
        String url = "https://api.github.com/users/" + username + "/repos?per_page=100&sort=updated";
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url))
                .header("Accept", "application/vnd.github+json")
                .header("User-Agent", "java-github-client");

        String token = System.getenv("GITHUB_TOKEN");
        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }

        HttpResponse<String> response = client.send(builder.GET().build(), HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 404) {
            throw new RuntimeException("Repos no encontrados");
        }
        if (response.statusCode() == 403) {
            throw new RuntimeException("Rate limit excedido");
        }

        return mapper.readValue(response.body(), new TypeReference<List<Repo>>() {
        });
    }

    public HttpResponse<?> getLastResponse() {
        return lastResponse;
    }
}

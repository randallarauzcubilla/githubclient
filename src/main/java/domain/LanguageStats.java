package domain;

/**
 *
 * @author Randall AC
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageStats {
    public static Map<String, Integer> countLanguages(List<Repo> repos) {
        Map<String, Integer> stats = new HashMap<>();
        for (Repo repo : repos) {
            String lang = repo.getLanguage();
            if (lang != null && !lang.isBlank()) {
                stats.put(lang, stats.getOrDefault(lang, 0) + 1);
            }
        }
        return stats;
    }
}
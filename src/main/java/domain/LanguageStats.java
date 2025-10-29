package domain;

/**
 *
 * @author Randall AC
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageStats {

    // Conteo simple por lenguaje
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

    // Porcentaje por lenguaje
    public static Map<String, Double> calculatePercentages(List<Repo> repos) {
        Map<String, Integer> counts = countLanguages(repos);
        int total = counts.values().stream().mapToInt(Integer::intValue).sum();

        Map<String, Double> percentages = new HashMap<>();
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            double percent = (entry.getValue() * 100.0) / total;
            percentages.put(entry.getKey(), Math.round(percent * 10.0) / 10.0); // redondeo a 1 decimal
        }
        return percentages;
    }
}
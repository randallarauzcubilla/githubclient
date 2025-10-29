package ui;

/**
 *
 * @author Randall AC
 */
import domain.LanguageStats;
import domain.Repo;
import domain.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XChartPanel;

import service.GitHubApi;
import util.Formatter;
import util.RateLimitInfo;

public class MainWindow extends JFrame {
    private final JTextField txtUsername = new JTextField(20);
    private final JButton btnBuscar = new JButton("Buscar");
    private final JTextArea txtOutput = new JTextArea(20, 50);
    private final JPanel chartContainer = new JPanel();

    public MainWindow() {
        setTitle("Cliente GitHub");
        setSize(800, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(new JLabel("Usuario GitHub:"));
        topPanel.add(txtUsername);
        topPanel.add(btnBuscar);

        txtOutput.setEditable(false);
        JScrollPane scroll = new JScrollPane(txtOutput);

        chartContainer.setLayout(new BorderLayout());
        chartContainer.setPreferredSize(new Dimension(600, 400));

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(chartContainer, BorderLayout.SOUTH);

        add(panel);

        btnBuscar.addActionListener(e -> buscarUsuario());
    }

    private void buscarUsuario() {
        String username = txtUsername.getText().trim();
        if (username.isEmpty()) {
            txtOutput.setText("Por favor ingrese un nombre de usuario.");
            return;
        }

        txtOutput.setText("Buscando usuario: " + username + "\n");
        chartContainer.removeAll();
        chartContainer.revalidate();
        chartContainer.repaint();

        GitHubApi api = new GitHubApi();

        new Thread(() -> {
            try {
                User user = api.fetchUser(username);
                List<Repo> repos = api.fetchRepos(username);
                Map<String, Integer> langsCount = LanguageStats.countLanguages(repos);
                Map<String, Double> langsPercent = LanguageStats.calculatePercentages(repos);

                SwingUtilities.invokeLater(() -> {
                    txtOutput.setText("");
                    txtOutput.append("Nombre: " + user.getName() + "\n");
                    txtOutput.append("Usuario: " + user.getLogin() + "\n");
                    txtOutput.append("Ubicación: " + user.getLocation() + "\n");
                    txtOutput.append("Blog: " + user.getBlog() + "\n");
                    txtOutput.append("Bio: " + user.getBio() + "\n");
                    txtOutput.append("Seguidores: " + user.getFollowers() + " | Siguiendo: " + user.getFollowing() + "\n");
                    txtOutput.append("Cuenta creada: " + Formatter.formatDate(user.getCreatedAt()) + "\n\n");

                    txtOutput.append("Repositorios públicos:\n");
                    for (Repo repo : repos) {
                        txtOutput.append("- " + repo.getName() + "\n");
                        txtOutput.append("  Descripción: " + (repo.getDescription() != null ? repo.getDescription() : "Sin descripción") + "\n");
                        txtOutput.append("  Lenguaje: " + (repo.getLanguage() != null ? repo.getLanguage() : "Sin lenguaje") + "\n");
                        txtOutput.append("  Estrellas: " + repo.getStars() + " | Forks: " + repo.getForks() + "\n");
                        txtOutput.append("  Última actualización: " + Formatter.formatDate(repo.getUpdatedAt()) + "\n\n");
                    }

                    txtOutput.append("Resumen de lenguajes:\n");
                    langsPercent.forEach((lang, percent) ->
                        txtOutput.append("  - " + lang + ": " + percent + "%\n")
                    );

                    txtOutput.append("\n" + RateLimitInfo.extract(api.getLastResponse()));

                    // Crear gráfico de barras verticales si hay datos
                    if (!langsPercent.isEmpty()) {
                        CategoryChart chart = new CategoryChartBuilder()
                            .width(700).height(400)
                            .title("Lenguajes más usados (%)")
                            .xAxisTitle("Lenguaje")
                            .yAxisTitle("Porcentaje")
                            .build();

                        // Estilo visual para fondo blanco
                        chart.getStyler().setChartBackgroundColor(new Color(245, 245, 245));
                        chart.getStyler().setPlotBackgroundColor(Color.WHITE);
                        chart.getStyler().setSeriesColors(new Color[]{new Color(0, 102, 204)});

                        List<String> langs = langsPercent.keySet().stream().collect(Collectors.toList());
                        List<Double> percents = langsPercent.values().stream().collect(Collectors.toList());

                        chart.addSeries("Uso", langs, percents);

                        chartContainer.add(new XChartPanel<>(chart), BorderLayout.CENTER);
                        chartContainer.revalidate();
                        chartContainer.repaint();
                    }
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> txtOutput.setText("Error: " + ex.getMessage()));
            }
        }).start();
    }
}
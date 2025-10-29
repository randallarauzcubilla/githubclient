package ui;

/**
 *
 * @author Randall AC
 */

import domain.LanguageStats;
import domain.Repo;
import domain.User;
import javax.swing.*;
import java.util.List;
import java.util.Map;
import service.GitHubApi;
import util.Formatter;
import util.RateLimitInfo;

public class MainWindow extends JFrame {
    private final JTextField txtUsername = new JTextField(20);
    private final JButton btnBuscar = new JButton("Buscar");
    private final JTextArea txtOutput = new JTextArea(20, 50);

    public MainWindow() {
        setTitle("Cliente GitHub");
        setSize(700, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        txtOutput.setEditable(false);
        JScrollPane scroll = new JScrollPane(txtOutput);

        panel.add(new JLabel("Usuario GitHub:"));
        panel.add(txtUsername);
        panel.add(btnBuscar);
        panel.add(scroll);

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

        GitHubApi api = new GitHubApi();

        new Thread(() -> {
            try {
                User user = api.fetchUser(username);
                List<Repo> repos = api.fetchRepos(username);
                Map<String, Integer> langs = LanguageStats.countLanguages(repos);

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
                        txtOutput.append("  Descripción: " + repo.getDescription() + "\n");
                        txtOutput.append("  Lenguaje: " + repo.getLanguage() + "\n");
                        txtOutput.append("  Estrellas: " + repo.getStars() + " | Forks: " + repo.getForks() + "\n");
                        txtOutput.append("  Última actualización: " + Formatter.formatDate(repo.getUpdatedAt()) + "\n\n");
                    }

                    txtOutput.append("Resumen de lenguajes:\n");
                    langs.forEach((lang, count) -> txtOutput.append("  - " + lang + ": " + count + "\n"));

                    txtOutput.append("\n" + RateLimitInfo.extract(api.getLastResponse()));
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> txtOutput.setText("Error: " + ex.getMessage()));
            }
        }).start();
    }
}
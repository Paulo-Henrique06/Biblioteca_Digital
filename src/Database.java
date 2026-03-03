import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.*;

public class Database {
    private static final String URL = "jdbc:sqlite:biblioteca.db";

    // Criação da tabela
    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS livros (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "autor TEXT NOT NULL," +
                "sinopse TEXT," +
                "imagem TEXT" +
                ");";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:biblioteca.db");
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela 'livros' criada ou já existe.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Inserção
    public static void addLivro(String nome, String autor, String sinopse, String imagem) {
        String sql = "INSERT INTO livros(nome, autor, sinopse, imagem) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, autor);
            pstmt.setString(3, sinopse);
            pstmt.setString(4, imagem);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Listar livros
    public static ResultSet listarLivros() {
        try {
            Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery("SELECT * FROM livros");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void removerLivro(int id) {
        String sql = "DELETE FROM livros WHERE id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:biblioteca.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

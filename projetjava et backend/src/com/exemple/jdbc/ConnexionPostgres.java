package com.exemple.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class ConnexionPostgres {

    private String URL = "jdbc:postgresql://localhost:5432/universite";
    private String UTILISATEUR = "postgres";
    private String MOT_DE_PASSE = "votre_mot_de_passe";

    public void Connection getConnexion() {
        Connection connexion = null;

        try {

            Class.forName("org.postgresql.Driver");


            connexion = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);

            if (connexion != null) {
                System.out.println("Connexion établie avec succès !");
            } else {
                System.out.println("Échec de la connexion.");
            }

        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC introuvable : " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données : " + e.getMessage());
        }

        return connexion;
    }

    public void testerConnexion() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnexion();

            if (conn != null) {
                stmt = conn.createStatement();

                rs = stmt.executeQuery("SELECT version()");

                if (rs.next()) {
                    System.out.println("Version de PostgreSQL : " + rs.getString(1));
                }

                rs = stmt.executeQuery("SELECT 'Connexion JDBC réussie !' as message");
                if (rs.next()) {
                    System.out.println(rs.getString("message"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        } finally {

            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
                System.out.println("Connexion fermée.");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture : " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Test de connexion JDBC à PostgreSQL ===");
        testerConnexion();
    }
}

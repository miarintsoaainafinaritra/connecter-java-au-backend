package com.exemple.jdbc;

import com.exemple.jdbc.modele.Etudiant;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtudiantDAO {

    private Connection connexion;

    public EtudiantDAO() {
        this.connexion = ConnexionPostgres.getConnexion();
    }

    public void creerTable() {
        String sql = "CREATE TABLE IF NOT EXISTS etudiants (" +
                "id SERIAL PRIMARY KEY, " +
                "nom VARCHAR(50) NOT NULL, " +
                "prenom VARCHAR(50) NOT NULL, " +
                "age INTEGER, " +
                "email VARCHAR(100) UNIQUE)";

        try (Statement stmt = connexion.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'etudiants' créée ou déjà existante.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la table : " + e.getMessage());
        }
    }

    public boolean ajouterEtudiant(Etudiant etudiant) {
        String sql = "INSERT INTO etudiants (nom, prenom, age, email) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setString(1, etudiant.getNom());
            pstmt.setString(2, etudiant.getPrenom());
            pstmt.setInt(3, etudiant.getAge());
            pstmt.setString(4, etudiant.getEmail());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout : " + e.getMessage());
            return false;
        }
    }

    public List<Etudiant> getAllEtudiants() {
        List<Etudiant> etudiants = new ArrayList<>();
        String sql = "SELECT * FROM etudiants ORDER BY id";

        try (Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Etudiant etudiant = new Etudiant(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getInt("age"),
                        rs.getString("email")
                );
                etudiants.add(etudiant);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération : " + e.getMessage());
        }

        return etudiants;
    }

    public boolean mettreAJourEtudiant(Etudiant etudiant) {
        String sql = "UPDATE etudiants SET nom = ?, prenom = ?, age = ?, email = ? WHERE id = ?";

        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setString(1, etudiant.getNom());
            pstmt.setString(2, etudiant.getPrenom());
            pstmt.setInt(3, etudiant.getAge());
            pstmt.setString(4, etudiant.getEmail());
            pstmt.setInt(5, etudiant.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerEtudiant(int id) {
        String sql = "DELETE FROM etudiants WHERE id = ?";

        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
            return false;
        }
    }

    public Etudiant trouverParId(int id) {
        String sql = "SELECT * FROM etudiants WHERE id = ?";
        Etudiant etudiant = null;

        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    etudiant = new Etudiant(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getInt("age"),
                            rs.getString("email")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche : " + e.getMessage());
        }

        return etudiant;
    }

    public void fermerConnexion() {
        try {
            if (connexion != null && !connexion.isClosed()) {
                connexion.close();
                System.out.println("Connexion fermée.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture : " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        System.out.println("=== Démonstration du DAO Etudiant ===");

        EtudiantDAO dao = new EtudiantDAO();

        dao.creerTable();

        Etudiant etudiant1 = new Etudiant("Dupont", "Jean", 20, "jean.dupont@email.com");
        Etudiant etudiant2 = new Etudiant("Martin", "Marie", 22, "marie.martin@email.com");

        if (dao.ajouterEtudiant(etudiant1)) {
            System.out.println("Étudiant 1 ajouté avec succès.");
        }

        if (dao.ajouterEtudiant(etudiant2)) {
            System.out.println("Étudiant 2 ajouté avec succès.");
        }

        System.out.println("\nListe des étudiants :");
        List<Etudiant> etudiants = dao.getAllEtudiants();
        for (Etudiant e : etudiants) {
            System.out.println(e);
        }
        if (!etudiants.isEmpty()) {
            Etudiant etudiantAModifier = etudiants.get(0);
            etudiantAModifier.setAge(21);
            etudiantAModifier.setEmail("jean.dupont.nouveau@email.com");

            if (dao.mettreAJourEtudiant(etudiantAModifier)) {
                System.out.println("\nÉtudiant mis à jour avec succès.");
            }
        }

        System.out.println("\nListe après modification :");
        for (Etudiant e : dao.getAllEtudiants()) {
            System.out.println(e);
        }

        dao.fermerConnexion();
    }
}
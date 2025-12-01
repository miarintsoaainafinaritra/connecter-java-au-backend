package com.exemple.jdbc.modele;

public class Etudiant {
    private int id;
    private String nom;
    private String prenom;
    private int age;
    private String email;


    public Etudiant() {}

    public Etudiant(String nom, String prenom, int age, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.email = email;
    }

    public Etudiant(int id, String nom, String prenom, int age, String email) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.email = email;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Etudiant [id=" + id + ", nom=" + nom + ", prenom=" + prenom
                + ", age=" + age + ", email=" + email + "]";
    }
}
package com.example.firedrive;
public class Utilisateur {
    private int idLivreur, notepublic, idvehicule;
    private String nom,prenom,email,mdp,tel;

    public Utilisateur(int idLivreur, String email, String mdp,String nom, String prenom, String tel, int idvehicule, int notepublic) {
        this.idLivreur = idLivreur;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.tel = tel;
        this.notepublic = notepublic;
        this.idvehicule = idvehicule;
    }
    public Utilisateur(String email, String mdp,String nom, String prenom, String tel, int idvehicule, int notepublic) {
        this.idLivreur = 0;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.tel = tel;
        this.notepublic = notepublic;
        this.idvehicule = idvehicule;
    }
    public Utilisateur(String email, String mdp,String nom, String prenom, String tel) {
        this.idLivreur = 0;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.tel = tel;
        this.notepublic = 5;
        this.idvehicule = 0;
    }
    public Utilisateur(String email, String mdp) {
        this.idLivreur = 0;
        this.nom = "";
        this.prenom = "";
        this.email = email;
        this.mdp = mdp;
        this.tel = "";
    }
    public Utilisateur(int IdLivreur) {
        this.idLivreur = IdLivreur;
        this.nom = "";
        this.prenom = "";
        this.email = "";
        this.mdp = "";
        this.tel = "";
    }

    public Utilisateur(int IdLivreur, String email, String mdp, String nom, String prenom, String tel) {
        this.idLivreur = IdLivreur;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.tel = tel;
    }

    public int getIdLivreur() {
        return idLivreur;
    }

    public void setIdLivreur(int IdLivreur) {
        this.idLivreur = IdLivreur;
    }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public void setPrenom(String prenom) {
            this.prenom = prenom;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
        public String getMdp() {
            return mdp;
        }

        public void setMdp(String mdp) {
            this.mdp = mdp;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public int getNotepublic() {
            return notepublic;
        }

        public void setNotepublic(int notepublic) {
            this.notepublic = notepublic;
        }
        public int getIdvehicule() {
            return notepublic;
        }

        public void setIdvehicule(int idvehicule) {
            this.idvehicule = idvehicule;
        }
}

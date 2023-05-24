package com.example.firedrive;

public class ListCommande {
    private int idCommande;
    private String chaineCommande;
    private int finished;

    public ListCommande(int idCommande, String chaineCommande) {
        this.idCommande = idCommande;
        this.chaineCommande = chaineCommande;
        this.finished = 0;
    }
    public ListCommande(int idCommande, String chaineCommande, int finished) {
        this.idCommande = idCommande;
        this.chaineCommande = chaineCommande;
        this.finished = finished;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public String getChaineCommande() {
        return chaineCommande;
    }

    public int getFinished() {
        return finished;
    }

}

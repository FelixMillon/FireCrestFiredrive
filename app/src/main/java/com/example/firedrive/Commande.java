package com.example.firedrive;

public class Commande {
    private int idCommande, idLivreur, idClient;

    private Float total;
    private String dateheureDebut,dateheureFinReel,dateheureFinEstimee,numrueDepot,rueDepot,villeDepot,cpDepot,numrueDest,rueDest,villeDest,cpDest;

    public Commande(int idCommande, int idLivreur,  int idClient,String dateheureDebut, String dateheureFinReel, String dateheureFinEstimee,String numrueDepot,String rueDepot,String villeDepot,String cpDepot, Float total,String numrueDest,String rueDest,String villeDest,String cpDest) {
        this.idCommande = idCommande;
        this.idLivreur = idLivreur;
        this.idClient = idClient;
        this.dateheureDebut = dateheureDebut;
        this.dateheureFinReel = dateheureFinReel;
        this.dateheureFinEstimee = dateheureFinEstimee;
        this.numrueDepot = numrueDepot;
        this.rueDepot = rueDepot;
        this.villeDepot = villeDepot;
        this.cpDepot = cpDepot;
        this.total = total;
        this.numrueDest = numrueDest;
        this.rueDest = rueDest;
        this.villeDest = villeDest;
        this.cpDest = cpDest;
    }

    public Commande(int idLivreur,  int idClient,String dateheureDebut, String dateheureFinReel, String dateheureFinEstimee,String numrueDepot,String rueDepot,String villeDepot,String cpDepot, Float total,String numrueDest,String rueDest,String villeDest,String cpDest) {
        this.idCommande = 0;
        this.idLivreur = idLivreur;
        this.idClient = idClient;
        this.dateheureDebut = dateheureDebut;
        this.dateheureFinReel = dateheureFinReel;
        this.dateheureFinEstimee = dateheureFinEstimee;
        this.numrueDepot = numrueDepot;
        this.rueDepot = rueDepot;
        this.villeDepot = villeDepot;
        this.cpDepot = cpDepot;
        this.total = total;
        this.numrueDest = numrueDest;
        this.rueDest = rueDest;
        this.villeDest = villeDest;
        this.cpDest = cpDest;
    }
    public Commande(int idCommande,int idClient,String dateheureDebut,String numrueDepot,String rueDepot,String villeDepot,String cpDepot, Float total,String numrueDest,String rueDest,String villeDest,String cpDest) {
        this.idCommande = idCommande;
        this.idLivreur = 0;
        this.idClient = idClient;
        this.dateheureDebut = dateheureDebut;
        this.dateheureFinReel = "";
        this.dateheureFinEstimee = "";
        this.numrueDepot = numrueDepot;
        this.rueDepot = rueDepot;
        this.villeDepot = villeDepot;
        this.cpDepot = cpDepot;
        this.total = total;
        this.numrueDest = numrueDest;
        this.rueDest = rueDest;
        this.villeDest = villeDest;
        this.cpDest = cpDest;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int IdCommande) {
        this.idCommande = IdCommande;
    }

    public int getIdLivreur() {
        return idLivreur;
    }

    public void setIdLivreur(int IdLivreur) {
        this.idLivreur = IdLivreur;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int IdClient) {
        this.idClient = IdClient;
    }

    public String getDateheureDebut(){return dateheureDebut;}

    public void setDateheureDebut(String DateheureDebut) {
        this.dateheureDebut = DateheureDebut;
    }

    public String getDateheureFinReel(){return dateheureFinReel;}

    public void setDateheureFinReel(String DateheureFinReel) {this.dateheureFinReel = DateheureFinReel;}

    public String getDateheureFinEstimee(){return dateheureFinEstimee;}

    public void setDateheureFinEstimee(String DateheureFinEstimee) {this.dateheureFinEstimee = DateheureFinEstimee;}

    public String getNumrueDepot(){return numrueDepot;}

    public void setNumrueDepot(String NumrueDepot) {this.numrueDepot = NumrueDepot;}

    public String getRueDepot(){return rueDepot;}

    public void setRueDepot(String RueDepot) {this.rueDepot = RueDepot;}

    public String getVilleDepot(){return villeDepot;}

    public void setVilleDepot(String VilleDepot) {this.villeDepot = VilleDepot;}

    public String getCpDepot(){return cpDepot;}

    public void setCpDepot(String CpDepot) {this.cpDepot = CpDepot;}
    public Float getTotal(){return total;}

    public void setTotal(Float Total) {this.total = Total;}

    public String getNumrueDest(){return numrueDest;}

    public void setNumrueDest(String NumrueDest) {this.numrueDest = NumrueDest;}

    public String getRueDest(){return rueDest;}

    public void setRueDest(String RueDest) {this.rueDest = RueDest;}

    public String getVilleDest(){return villeDest;}

    public void setVilleDest(String VilleDest) {this.villeDest = VilleDest;}

    public String getCpDest(){return cpDest;}

    public void setCpDest(String CpDest) {this.cpDepot = CpDest;}
}

package com.example.mainactivity_mapsactivity.modele;

public class DominosPizza {
    private long id;
    private String adresse;
    private String numeroTelephone;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNumeroTelephone() {
        return numeroTelephone;
    }
    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    @Override
    public String toString() {
        return "Adresse : " + adresse + "\nNuméro de téléphone : " + numeroTelephone;
    }
}
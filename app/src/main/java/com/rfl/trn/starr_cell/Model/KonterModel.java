package com.rfl.trn.starr_cell.Model;

public class KonterModel {
    private String key,namaKonter,alamatKonter,emailKonter,password;

    public KonterModel(){

    }

    public KonterModel(String key, String namaKonter, String alamatKonter, String emailKonter, String password) {
        this.key = key;
        this.namaKonter = namaKonter;
        this.alamatKonter = alamatKonter;
        this.emailKonter = emailKonter;
        this.password = password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNamaKonter() {
        return namaKonter;
    }

    public void setNamaKonter(String namaKonter) {
        this.namaKonter = namaKonter;
    }

    public String getAlamatKonter() {
        return alamatKonter;
    }

    public void setAlamatKonter(String alamatKonter) {
        this.alamatKonter = alamatKonter;
    }

    public String getEmailKonter() {
        return emailKonter;
    }

    public void setEmailKonter(String emailKonter) {
        this.emailKonter = emailKonter;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

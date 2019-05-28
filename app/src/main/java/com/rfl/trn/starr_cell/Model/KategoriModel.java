package com.rfl.trn.starr_cell.Model;

import java.io.Serializable;

public class KategoriModel implements Serializable {
    String idKategroi,namaKategori;

    public KategoriModel() {
    }

    public KategoriModel(String idKategroi, String namaKategori) {
        this.idKategroi = idKategroi;
        this.namaKategori = namaKategori;
    }

    public String getIdKategroi() {
        return idKategroi;
    }

    public void setIdKategroi(String idKategroi) {
        this.idKategroi = idKategroi;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public void setNamaKategori(String namaKategori) {
        this.namaKategori = namaKategori;
    }
}

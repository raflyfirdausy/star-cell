package com.rfl.trn.starr_cell.Model;

import java.io.Serializable;

public class KategoriModel implements Serializable {
    String idKategroi,namaKategori;
    Long waktuDiubah;

    public KategoriModel() {
    }

    public KategoriModel(String idKategroi, String namaKategori, Long waktuDiubah) {
        this.idKategroi = idKategroi;
        this.namaKategori = namaKategori;
        this.waktuDiubah = waktuDiubah;
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

    public Long getWaktuDiubah() {
        return waktuDiubah;
    }

    public void setWaktuDiubah(Long waktuDiubah) {
        this.waktuDiubah = waktuDiubah;
    }
}

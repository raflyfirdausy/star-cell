package com.rfl.trn.starr_cell.Model;

import java.io.Serializable;

public class KategoriModel implements Serializable {
    String idKategori,namaKategori;
    Long waktuDiubah;

    public KategoriModel() {
    }

    public KategoriModel(String idKategori, String namaKategori, Long waktuDiubah) {
        this.idKategori = idKategori;
        this.namaKategori = namaKategori;
        this.waktuDiubah = waktuDiubah;
    }

    public String getIdKategori() {
        return idKategori;
    }

    public void setIdKategori(String idKategori) {
        this.idKategori = idKategori;
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

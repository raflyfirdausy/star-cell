package com.rfl.trn.starr_cell.Model;

import java.io.Serializable;

public class KaryawanModel implements Serializable {
    String idKaryawan,jenisKelamin,photoUrl,statusKerja;
    int nomerHp;

    public KaryawanModel() {
    }

    public KaryawanModel(String idKaryawan, String jenisKelamin, String photoUrl, String statusKerja, int nomerHp) {
        this.idKaryawan = idKaryawan;
        this.jenisKelamin = jenisKelamin;
        this.photoUrl = photoUrl;
        this.statusKerja = statusKerja;
        this.nomerHp = nomerHp;
    }

    public String getIdKaryawan() {
        return idKaryawan;
    }

    public void setIdKaryawan(String idKaryawan) {
        this.idKaryawan = idKaryawan;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getStatusKerja() {
        return statusKerja;
    }

    public void setStatusKerja(String statusKerja) {
        this.statusKerja = statusKerja;
    }

    public int getNomerHp() {
        return nomerHp;
    }

    public void setNomerHp(int nomerHp) {
        this.nomerHp = nomerHp;
    }
}

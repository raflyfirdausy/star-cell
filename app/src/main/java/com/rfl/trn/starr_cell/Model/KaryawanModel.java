package com.rfl.trn.starr_cell.Model;

import java.io.Serializable;

public class KaryawanModel implements Serializable {
    String idKaryawan;
    String jenisKelamin;
    String photoUrl;
    String statusKerja;
    String namaKarywan;
    int nomerHp;
    Long tanggalDiubah;

    public KaryawanModel() {
    }

    public KaryawanModel(String idKaryawan, String jenisKelamin, String photoUrl, String statusKerja, String namaKarywan, int nomerHp, Long tanggalDiubah) {
        this.idKaryawan = idKaryawan;
        this.jenisKelamin = jenisKelamin;
        this.photoUrl = photoUrl;
        this.statusKerja = statusKerja;
        this.namaKarywan = namaKarywan;
        this.nomerHp = nomerHp;
        this.tanggalDiubah = tanggalDiubah;
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

    public String getNamaKarywan() {
        return namaKarywan;
    }

    public void setNamaKarywan(String namaKarywan) {
        this.namaKarywan = namaKarywan;
    }

    public int getNomerHp() {
        return nomerHp;
    }

    public void setNomerHp(int nomerHp) {
        this.nomerHp = nomerHp;
    }

    public Long getTanggalDiubah() {
        return tanggalDiubah;
    }

    public void setTanggalDiubah(Long tanggalDiubah) {
        this.tanggalDiubah = tanggalDiubah;
    }
}

package com.rfl.trn.starr_cell.Model;

import java.io.Serializable;

public class KaryawanModel implements Serializable {
    String idKaryawan;
    String jenisKelamin;
    String photoUrl;
    String statusKerja;
    String namaKaryawan;
    String alamatKaryawan;
    String nomerHp;
    Long tanggalDiubah;

    public KaryawanModel() {
    }

    public KaryawanModel(String idKaryawan, String jenisKelamin, String photoUrl, String statusKerja, String namaKaryawan, String alamatKaryawan, String nomerHp, Long tanggalDiubah) {
        this.idKaryawan = idKaryawan;
        this.jenisKelamin = jenisKelamin;
        this.photoUrl = photoUrl;
        this.statusKerja = statusKerja;
        this.namaKaryawan = namaKaryawan;
        this.alamatKaryawan = alamatKaryawan;
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

    public String getNamaKaryawan() {
        return namaKaryawan;
    }

    public void setNamaKaryawan(String namaKaryawan) {
        this.namaKaryawan = namaKaryawan;
    }

    public String getNomerHp() {
        return nomerHp;
    }

    public void setNomerHp(String nomerHp) {
        this.nomerHp = nomerHp;
    }

    public Long getTanggalDiubah() {
        return tanggalDiubah;
    }

    public void setTanggalDiubah(Long tanggalDiubah) {
        this.tanggalDiubah = tanggalDiubah;
    }

    public String getAlamatKaryawan() {
        return alamatKaryawan;
    }

    public void setAlamatKaryawan(String alamatKaryawan) {
        this.alamatKaryawan = alamatKaryawan;
    }
}

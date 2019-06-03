package com.rfl.trn.starr_cell.Model;

import java.io.Serializable;

public class AbsenModel implements Serializable {

    private String idKonter;
    private String idAbsen;
    private String idKaryawan;
    private Long tanggal;
    private Long waktuMasuk;
    private Long waktuKeluar;
    private String konfirmasi;
    private String pesan;
    private String urlFoto;
    private String jenisAbsen;

    public AbsenModel() {
    }

    public AbsenModel(String idKonter, String idAbsen, String idKaryawan, Long tanggal, Long waktuMasuk, Long waktuKeluar, String konfirmasi, String pesan, String urlFoto, String jenisAbsen) {
        this.idKonter = idKonter;
        this.idAbsen = idAbsen;
        this.idKaryawan = idKaryawan;
        this.tanggal = tanggal;
        this.waktuMasuk = waktuMasuk;
        this.waktuKeluar = waktuKeluar;
        this.konfirmasi = konfirmasi;
        this.pesan = pesan;
        this.urlFoto = urlFoto;
        this.jenisAbsen = jenisAbsen;
    }

    public String getIdKonter() {
        return idKonter;
    }

    public void setIdKonter(String idKonter) {
        this.idKonter = idKonter;
    }

    public String getIdAbsen() {
        return idAbsen;
    }

    public void setIdAbsen(String idAbsen) {
        this.idAbsen = idAbsen;
    }

    public String getIdKaryawan() {
        return idKaryawan;
    }

    public void setIdKaryawan(String idKaryawan) {
        this.idKaryawan = idKaryawan;
    }

    public Long getTanggal() {
        return tanggal;
    }

    public void setTanggal(Long tanggal) {
        this.tanggal = tanggal;
    }

    public Long getWaktuMasuk() {
        return waktuMasuk;
    }

    public void setWaktuMasuk(Long waktuMasuk) {
        this.waktuMasuk = waktuMasuk;
    }

    public Long getWaktuKeluar() {
        return waktuKeluar;
    }

    public void setWaktuKeluar(Long waktuKeluar) {
        this.waktuKeluar = waktuKeluar;
    }

    public String getKonfirmasi() {
        return konfirmasi;
    }

    public void setKonfirmasi(String konfirmasi) {
        this.konfirmasi = konfirmasi;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getJenisAbsen() {
        return jenisAbsen;
    }

    public void setJenisAbsen(String jenisAbsen) {
        this.jenisAbsen = jenisAbsen;
    }
}

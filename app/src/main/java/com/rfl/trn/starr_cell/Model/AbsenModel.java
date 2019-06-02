package com.rfl.trn.starr_cell.Model;

public class AbsenModel {
/*
    absen
    |-id_konter(ambil dari key node konter)
     |- {id_absen}(random id)
      |- id_karyawan(ambil dari key node karyawan)
      |- tanggal
      |- waktu_masuk
      |- waktu_keluar
      |- konfirmasi (pending, accept, reject)
*/

    private String idKonter;
    private String idAbsen;
    private String idKaryawan;
    private Long tanggal;
    private Long waktuMasuk;
    private Long waktuKeluar;
    private String konfirmasi;
    private String pesan;
    private String urlFoto;

    public AbsenModel() {
    }

    public AbsenModel(String idKonter, String idAbsen, String idKaryawan, Long tanggal, Long waktuMasuk, Long waktuKeluar, String konfirmasi, String pesan, String urlFoto) {
        this.idKonter = idKonter;
        this.idAbsen = idAbsen;
        this.idKaryawan = idKaryawan;
        this.tanggal = tanggal;
        this.waktuMasuk = waktuMasuk;
        this.waktuKeluar = waktuKeluar;
        this.konfirmasi = konfirmasi;
        this.pesan = pesan;
        this.urlFoto = urlFoto;
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
}

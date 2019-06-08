package com.rfl.trn.starr_cell.Model;

import java.io.Serializable;

public class BarangModel implements Serializable {
    private String namaBarang,stokBarang, harga1,harga2,harga3,idKonter,idKategori, idBarang, hargaBeli;
    private Long tanggalDiubah;
    private int jumlahMasukKeranjang;
    public BarangModel() {
    }

    public BarangModel(String namaBarang, String stokBarang, String harga1, String harga2, String harga3, String idKonter, String idKategori, String idBarang, String hargaBeli,  Long tanggalDiubah, int jumlahMasukKeranjang) {
        this.namaBarang = namaBarang;
        this.stokBarang = stokBarang;
        this.harga1 = harga1;
        this.harga2 = harga2;
        this.harga3 = harga3;
        this.idKonter = idKonter;
        this.idKategori = idKategori;
        this.idBarang = idBarang;
        this.hargaBeli = hargaBeli;
        this.tanggalDiubah = tanggalDiubah;
        this.jumlahMasukKeranjang = jumlahMasukKeranjang;
    }

    public String getHargaBeli() {
        return hargaBeli;
    }

    public void setHargaBeli(String hargaBeli) {
        this.hargaBeli = hargaBeli;
    }


    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getStokBarang() {
        return stokBarang;
    }

    public void setStokBarang(String stokBarang) {
        this.stokBarang = stokBarang;
    }

    public String getHarga1() {
        return harga1;
    }

    public void setHarga1(String harga1) {
        this.harga1 = harga1;
    }

    public String getHarga2() {
        return harga2;
    }

    public void setHarga2(String harga2) {
        this.harga2 = harga2;
    }

    public String getHarga3() {
        return harga3;
    }

    public void setHarga3(String harga3) {
        this.harga3 = harga3;
    }

    public String getIdKonter() {
        return idKonter;
    }

    public void setIdKonter(String idKonter) {
        this.idKonter = idKonter;
    }

    public String getIdKategori() {
        return idKategori;
    }

    public void setIdKategori(String idKategori) {
        this.idKategori = idKategori;
    }

    public Long getTanggalDiubah() {
        return tanggalDiubah;
    }

    public void setTanggalDiubah(Long tanggalDiubah) {
        this.tanggalDiubah = tanggalDiubah;
    }

    public int getJumlahMasukKeranjang() {
        return jumlahMasukKeranjang;
    }

    public void setJumlahMasukKeranjang(int jumlahMasukKeranjang) {
        this.jumlahMasukKeranjang = jumlahMasukKeranjang;
    }

    public String getIdBarang() {
        return idBarang;
    }

    public void setIdBarang(String idBarang) {
        this.idBarang = idBarang;
    }
}

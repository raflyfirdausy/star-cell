package com.rfl.trn.starr_cell.Model;

import java.io.Serializable;

public class ListPembelianBarangModel implements Serializable {
    private String idBarang;
    private String namaBarang;
    private String hargaBarang;
    private int jumlahMasukKeranjang;

    public ListPembelianBarangModel() {
    }

    public ListPembelianBarangModel(String idBarang, String namaBarang, String hargaBarang, int jumlahMasukKeranjang) {
        this.idBarang = idBarang;
        this.namaBarang = namaBarang;
        this.hargaBarang = hargaBarang;
        this.jumlahMasukKeranjang = jumlahMasukKeranjang;
    }

    public String getIdBarang() {
        return idBarang;
    }

    public void setIdBarang(String idBarang) {
        this.idBarang = idBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getHargaBarang() {
        return hargaBarang;
    }

    public void setHargaBarang(String hargaBarang) {
        this.hargaBarang = hargaBarang;
    }

    public int getJumlahMasukKeranjang() {
        return jumlahMasukKeranjang;
    }

    public void setJumlahMasukKeranjang(int jumlahMasukKeranjang) {
        this.jumlahMasukKeranjang = jumlahMasukKeranjang;
    }
}

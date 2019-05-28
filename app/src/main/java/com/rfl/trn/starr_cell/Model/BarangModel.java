package com.rfl.trn.starr_cell.Model;

import java.io.Serializable;

public class BarangModel implements Serializable {
    String namaBarang,stokBarang;
    double harga1,harga2,harga3;

    public BarangModel() {
    }

    public BarangModel(String namaBarang, String stokBarang, double harga1, double harga2, double harga3) {
        this.namaBarang = namaBarang;
        this.stokBarang = stokBarang;
        this.harga1 = harga1;
        this.harga2 = harga2;
        this.harga3 = harga3;
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

    public double getHarga1() {
        return harga1;
    }

    public void setHarga1(double harga1) {
        this.harga1 = harga1;
    }

    public double getHarga2() {
        return harga2;
    }

    public void setHarga2(double harga2) {
        this.harga2 = harga2;
    }

    public double getHarga3() {
        return harga3;
    }

    public void setHarga3(double harga3) {
        this.harga3 = harga3;
    }
}

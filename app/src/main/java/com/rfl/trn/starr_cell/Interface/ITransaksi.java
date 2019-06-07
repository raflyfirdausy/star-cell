package com.rfl.trn.starr_cell.Interface;

import com.rfl.trn.starr_cell.Model.BarangModel;
import com.rfl.trn.starr_cell.Model.KategoriModel;

public interface ITransaksi {
    void onItemBarangClick(BarangModel barangModel);
    void onKategoriClick(KategoriModel kategoriModel);
    void onItemBarangLongClick(BarangModel barangModel, int posisi);
}

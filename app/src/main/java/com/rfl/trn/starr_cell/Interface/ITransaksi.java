package com.rfl.trn.starr_cell.Interface;

import com.rfl.trn.starr_cell.Model.BarangModel;
import com.rfl.trn.starr_cell.Model.KategoriModel;
import com.rfl.trn.starr_cell.Model.ListPembelianBarangModel;

public interface ITransaksi {
    void onItemBarangClick(BarangModel barangModel, int posisi);
    void onKategoriClick(KategoriModel kategoriModel);
    void onItemBarangLongClick(BarangModel barangModel, int posisi);
    void onItemBarangDiKeranjangClick(ListPembelianBarangModel listPembelianBarangModel, int posisi);
}

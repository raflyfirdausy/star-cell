package com.rfl.trn.starr_cell.Fragment.Karyawan;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rfl.trn.starr_cell.R;

public class KaryawanTransaksiFragment extends Fragment {

    public KaryawanTransaksiFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_karyawan_transaksi, container, false);
    }
}

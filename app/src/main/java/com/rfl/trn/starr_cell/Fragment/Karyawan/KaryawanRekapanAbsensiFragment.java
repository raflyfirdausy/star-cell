package com.rfl.trn.starr_cell.Fragment.Karyawan;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rfl.trn.starr_cell.R;

public class KaryawanRekapanAbsensiFragment extends Fragment {

    public KaryawanRekapanAbsensiFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_karyawan_rekapan_absensi, container, false);
    }
}

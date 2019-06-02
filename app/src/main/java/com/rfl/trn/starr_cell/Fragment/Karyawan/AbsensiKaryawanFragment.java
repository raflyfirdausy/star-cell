package com.rfl.trn.starr_cell.Fragment.Karyawan;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rfl.trn.starr_cell.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AbsensiKaryawanFragment extends Fragment {


    public AbsensiKaryawanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_absensi_karyawan, container, false);
    }

}

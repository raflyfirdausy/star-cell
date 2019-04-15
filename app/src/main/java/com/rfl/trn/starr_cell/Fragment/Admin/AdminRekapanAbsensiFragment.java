package com.rfl.trn.starr_cell.Fragment.Admin;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rfl.trn.starr_cell.R;

public class AdminRekapanAbsensiFragment extends Fragment {

    public AdminRekapanAbsensiFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_rekapan_absensi, container, false);
    }
}

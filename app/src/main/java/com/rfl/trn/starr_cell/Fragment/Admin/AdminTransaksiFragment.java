package com.rfl.trn.starr_cell.Fragment.Admin;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rfl.trn.starr_cell.R;

public class AdminTransaksiFragment extends Fragment {

    public AdminTransaksiFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_barang, container, false);
        return view;
    }
    //TODO :: Fetch Data
    //TODO :: Bind(OnCLick dll)
    //TODO :: LifeCycle
}

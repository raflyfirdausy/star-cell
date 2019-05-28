package com.rfl.trn.starr_cell.Fragment.Admin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rfl.trn.starr_cell.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminDashboardFragment extends Fragment {


    public AdminDashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        return view;
    }

    //TODO :: Fetch Data

    //TODO :: Bind(OnCLick dll)

    //TODO :: LifeCycle

}

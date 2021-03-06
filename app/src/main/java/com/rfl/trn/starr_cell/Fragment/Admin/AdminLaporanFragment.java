package com.rfl.trn.starr_cell.Fragment.Admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.database.DatabaseReference;
import com.rfl.trn.starr_cell.Activity.AdminLaporanSemuaActivity;
import com.rfl.trn.starr_cell.Activity.AdminLaporanUmumActivity;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminLaporanFragment extends Fragment {


    @BindView(R.id.rl_laporanSemua)
    RelativeLayout rlLaporanSemua;
    @BindView(R.id.rl_laporanUmum)
    RelativeLayout rlLaporanUmum;
    private Unbinder unbinder;

    private boolean arrowIsUp = true;
    private DatabaseReference databaseReference;

    public AdminLaporanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_laporan, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    //TODO :: FetchData
    //TODO :: Bind
    @OnClick(R.id.rl_laporanUmum)
    public void kelaporanUmum() {
        startActivity(new Intent(getActivity(), AdminLaporanUmumActivity.class));
    }

    @OnClick(R.id.rl_laporanSemua)
    public void kelaporanSemua() {
        startActivity(new Intent(getActivity(), AdminLaporanSemuaActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

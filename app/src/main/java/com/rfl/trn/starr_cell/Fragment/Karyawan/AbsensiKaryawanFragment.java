package com.rfl.trn.starr_cell.Fragment.Karyawan;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AbsensiKaryawanFragment extends Fragment {


    @BindView(R.id.iv_karyawan)
    ImageView ivKaryawan;
    @BindView(R.id.tv_namaKonter)
    MyTextView tvNamaKonter;
    @BindView(R.id.tv_detailKaryawan)
    MyTextView tvDetailKaryawan;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.tv_karyawan1)
    TextView tvKaryawan1;
    @BindView(R.id.tv_karyawan2)
    TextView tvKaryawan2;
    @BindView(R.id.tv_karyawan3)
    TextView tvKaryawan3;
    @BindView(R.id.layout_karyawanSaatIni)
    LinearLayout layoutKaryawanSaatIni;
    @BindView(R.id.layout_absenMasuk)
    RelativeLayout layoutAbsenMasuk;
    @BindView(R.id.layout_absenKeluar)
    LinearLayout layoutAbsenKeluar;
    Unbinder unbinder;

    public AbsensiKaryawanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_absensi_karyawan, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.layout_karyawanSaatIni, R.id.layout_absenMasuk, R.id.layout_absenKeluar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_karyawanSaatIni:
                new Bantuan(getActivity()).swal_sukses("karyawan");
                break;
            case R.id.layout_absenMasuk:
                new Bantuan(getActivity()).swal_sukses("masuk");
                break;
            case R.id.layout_absenKeluar:
                new Bantuan(getActivity()).swal_sukses("keluar");
                break;
        }
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
}

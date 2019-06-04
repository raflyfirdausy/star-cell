package com.rfl.trn.starr_cell.Fragment.Admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.rfl.trn.starr_cell.Adapter.AdapterListAbsensi;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Interface.IKonfirmasiAbsen;
import com.rfl.trn.starr_cell.Model.AbsenModel;
import com.rfl.trn.starr_cell.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AdminAbsensiFragment extends Fragment {

    @BindView(R.id.iv_karyawan)
    ImageView ivKaryawan;
    @BindView(R.id.tv_namaKonter)
    MyTextView tvNamaKonter;
    @BindView(R.id.tv_detailKaryawan)
    MyTextView tvDetailKaryawan;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.rv_absensi)
    ShimmerRecyclerView rvAbsensi;
    Unbinder unbinder;
    @BindView(R.id.ll_belum_ada_absen)
    LinearLayout llBelumAdaAbsen;

    private DatabaseReference databaseReference;
    private List<AbsenModel> list = new ArrayList<>();
    private AdapterListAbsensi adapterListAbsensi;


    public AdminAbsensiFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_absensi, container, false);
        unbinder = ButterKnife.bind(this, view);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        rvAbsensi.showShimmerAdapter();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        semuaAbsen();

    }

    private void semuaAbsen() {
        final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText("Konfirmasi");


        databaseReference.child("absen")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.clear();
                        if (dataSnapshot.exists()) {
                            llBelumAdaAbsen.setVisibility(View.GONE);
                            AbsenModel model;
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                model = data.getValue(AbsenModel.class);
                                list.add(model);
                            }

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            rvAbsensi.setLayoutManager(layoutManager);
                            adapterListAbsensi = new AdapterListAbsensi(getActivity(), list, new IKonfirmasiAbsen() {
                                @Override
                                public void AlertKonfirmas(String Nama, String Konter, AbsenModel dataAbsen) {
                                    dialog.setContentText("Karyawan"+Nama+".\n Telah melakuan absen pada "+dataAbsen.getWaktuMasuk());
                                    dialog.setConfirmText("Terima");
                                    dialog.setCancelText("Tolak");
                                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            dialog.dismissWithAnimation();
                                            dialog.changeAlertType(SweetAlertDialog.NORMAL_TYPE);
                                        }
                                    });
                                    dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            dialog.dismissWithAnimation();
                                            dialog.changeAlertType(SweetAlertDialog.NORMAL_TYPE);
                                        }
                                    });
                                    dialog.show();
                                }
                            });
                            rvAbsensi.setAdapter(adapterListAbsensi);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        new Bantuan(getActivity()).swal_error(databaseError.getMessage());
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

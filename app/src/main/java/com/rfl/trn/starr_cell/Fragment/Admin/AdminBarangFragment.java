package com.rfl.trn.starr_cell.Fragment.Admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rfl.trn.starr_cell.Activity.TambahBarangActivity;
import com.rfl.trn.starr_cell.Adapter.AdapterListBarang;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Interface.IDialog;
import com.rfl.trn.starr_cell.Model.BarangModel;
import com.rfl.trn.starr_cell.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminBarangFragment extends Fragment {

    @BindView(R.id.iv_karyawan)
    ImageView ivKaryawan;
    @BindView(R.id.tv_namaKonter)
    MyTextView tvNamaKonter;
    @BindView(R.id.tv_detailKaryawan)
    MyTextView tvDetailKaryawan;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.bg_noData)
    LinearLayout bgNoData;
    @BindView(R.id.ll_belum_ada_barang)
    LinearLayout layoutBelumAdaBarang;
    @BindView(R.id.rv_barang)
    RecyclerView rvBarang;
    @BindView(R.id.fab_tambahBarang)
    FloatingActionButton fabTambahBarang;
    Unbinder unbinder;
    @BindView(R.id.spinner_konter)
    MaterialSpinner spinnerKonter;
    @BindView(R.id.spinner_kategori)
    MaterialSpinner spinnerKategori;

    private List<BarangModel> list;
    private List<String> listKey;
    private List<String> listKeyKategori;
    private List<String> listKeyKonter;
    private List<String> listNamaKonter;
    private List<String> listNamaKategori;
    private AdapterListBarang adapterListBarang;
    private DatabaseReference databaseReference;

    public AdminBarangFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_barang, container, false);
        unbinder = ButterKnife.bind(this, view);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        getAllBarang("semua", 0);
        getFilter();
        return view;
    }

    private void getFilter() {
        listKeyKategori = new ArrayList<>();
        listNamaKategori = new ArrayList<>();
        listKeyKonter = new ArrayList<>();
        listNamaKonter = new ArrayList<>();

        listNamaKategori.clear();
        listKeyKategori.clear();

        listNamaKategori.add("Semua");
        listKeyKategori.add("semua");

        listNamaKonter.clear();
        listKeyKonter.clear();

        listNamaKonter.add("Semua");
        listKeyKonter.add("semua");

        databaseReference.child("kategori")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                String NamaKategori = data.child("namaKategori").getValue(String.class);
                                String KeyKategori = data.child("idKategori").getValue(String.class);
                                listKeyKategori.add(KeyKategori);
                                listNamaKategori.add(NamaKategori);

                            }
                            spinnerKategori.setItems(listNamaKategori);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        databaseReference.child("konter")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                String NamaKonter = data.child("namaKonter").getValue(String.class);
                                String KeyKonter = data.getKey();

                                listKeyKonter.add(KeyKonter);
                                listNamaKonter.add(NamaKonter);
                            }
                            spinnerKonter.setItems(listNamaKonter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        spinnerKonter.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                 Snackbar.make(view, "Clicked " +" "+position+" "+ item, Snackbar.LENGTH_LONG).show();
                if (position == 0){
                    getAllBarang("semua",0);
                }else {
                    getAllBarang(listKeyKonter.get(position), 2);
                }
            }
        });
        spinnerKategori.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " +" "+position+" "+ item, Snackbar.LENGTH_LONG).show();
                if (position == 0){
                    getAllBarang("semua",0);
                }else {
                    getAllBarang(listKeyKategori.get(position), 1);
                }
            }
        });
    }

    //TODO :: Fetch Data
    private void getAllBarang(String param, int Filter) {

        if (Filter == 1) {

                databaseReference.child("barang")
                        .orderByChild("idKategori")
                        .equalTo(param)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                list = new ArrayList<>();
                                listKey = new ArrayList<>();
                                list.clear();
                                listKey.clear();
                                if (dataSnapshot.exists()) {
                                    layoutBelumAdaBarang.setVisibility(View.GONE);
                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                        BarangModel model;
                                        String key = data.getKey();

                                        model = data.getValue(BarangModel.class);
                                        list.add(model);
                                        listKey.add(key);
                                    }

                                    adapterListBarang = new AdapterListBarang(getActivity(), list, listKey, new IDialog() {
                                        @Override
                                        public void onItemClick(String id, String nama, boolean isDismiss) {

                                        }

                                        @Override
                                        public void onItemPopUpMenu(String id, int menu) {
                                            if (menu == 1) {
                                                //ke edit
                                                startActivity(new Intent(getActivity(), TambahBarangActivity.class)
                                                        .putExtra("id", id));

                                            } else if (menu == 2) {
                                                //ke detail

                                            }
                                        }
                                    });
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                    rvBarang.setLayoutManager(layoutManager);
                                    rvBarang.setAdapter(adapterListBarang);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

        } else if (Filter == 2) {

                databaseReference.child("barang")
                        .orderByChild("idKonter")
                        .equalTo(param)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                list = new ArrayList<>();
                                listKey = new ArrayList<>();
                                list.clear();
                                listKey.clear();
                                if (dataSnapshot.exists()) {
                                    layoutBelumAdaBarang.setVisibility(View.GONE);
                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                        BarangModel model;
                                        String key = data.getKey();

                                        model = data.getValue(BarangModel.class);
                                        list.add(model);
                                        listKey.add(key);
                                    }

                                    adapterListBarang = new AdapterListBarang(getActivity(), list, listKey, new IDialog() {
                                        @Override
                                        public void onItemClick(String id, String nama, boolean isDismiss) {

                                        }

                                        @Override
                                        public void onItemPopUpMenu(String id, int menu) {
                                            if (menu == 1) {
                                                //ke edit
                                                startActivity(new Intent(getActivity(), TambahBarangActivity.class)
                                                        .putExtra("id", id));

                                            } else if (menu == 2) {
                                                //ke detail

                                            }
                                        }
                                    });
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                    rvBarang.setLayoutManager(layoutManager);
                                    rvBarang.setAdapter(adapterListBarang);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

        } else {
            databaseReference.child("barang")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            list = new ArrayList<>();
                            listKey = new ArrayList<>();
                            list.clear();
                            listKey.clear();
                            if (dataSnapshot.exists()) {
                                layoutBelumAdaBarang.setVisibility(View.GONE);
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    BarangModel model;
                                    String key = data.getKey();

                                    model = data.getValue(BarangModel.class);
                                    list.add(model);
                                    listKey.add(key);
                                }

                                adapterListBarang = new AdapterListBarang(getActivity(), list, listKey, new IDialog() {
                                    @Override
                                    public void onItemClick(String id, String nama, boolean isDismiss) {

                                    }

                                    @Override
                                    public void onItemPopUpMenu(String id, int menu) {
                                        if (menu == 1) {
                                            //ke edit
                                            startActivity(new Intent(getActivity(), TambahBarangActivity.class)
                                                    .putExtra("id", id));

                                        } else if (menu == 2) {
                                            //ke detail

                                        }
                                    }
                                });
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                rvBarang.setLayoutManager(layoutManager);
                                rvBarang.setAdapter(adapterListBarang);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

    }

    //TODO :: Bind(OnCLick dll)
    @OnClick(R.id.fab_tambahBarang)
    void tambahBarang() {
        startActivity(new Intent(getActivity(), TambahBarangActivity.class));
    }

    //TODO :: LifeCycle
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

package com.rfl.trn.starr_cell.Fragment.Admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
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
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Interface.IDialog;
import com.rfl.trn.starr_cell.Model.BarangModel;
import com.rfl.trn.starr_cell.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    ShimmerRecyclerView rvBarang;
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
    private String idKategori = "semua";
    private String idKonter = "semua";
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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listKeyKategori = new ArrayList<>();
        listNamaKategori = new ArrayList<>();
        listKeyKonter = new ArrayList<>();
        listNamaKonter = new ArrayList<>();

        listNamaKonter.clear();
        listKeyKonter.clear();
        listNamaKategori.clear();
        listKeyKategori.clear();


        getAllBarang();
        getFilter();
    }

    private void getFilter() {
            databaseReference.child("kategori")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listNamaKategori.clear();
                        listKeyKategori.clear();
                        listNamaKategori.add("Semua");
                        listKeyKategori.add("semua");
                        if (dataSnapshot.exists()) {
                            try {
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    String NamaKategori = data.child("namaKategori").getValue(String.class);
                                    String KeyKategori = data.child("idKategori").getValue(String.class);
                                    listKeyKategori.add(KeyKategori);
                                    listNamaKategori.add(NamaKategori);

                                }
                                spinnerKategori.setItems(listNamaKategori);
                            }catch (NullPointerException e){
                                new Bantuan(getActivity()).swal_error(e.getMessage());
                            }


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
                        listNamaKonter.clear();
                        listKeyKonter.clear();
                        listNamaKonter.add("Semua");
                        listKeyKonter.add("semua");
                        if (dataSnapshot.exists()) {
                            try {
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    String NamaKonter = data.child("namaKonter").getValue(String.class);
                                    String KeyKonter = data.getKey();

                                    listKeyKonter.add(KeyKonter);
                                    listNamaKonter.add(NamaKonter);
                                }
                                spinnerKonter.setItems(listNamaKonter);
                            }catch (NullPointerException e){
                                new Bantuan(getActivity()).swal_error(e.getMessage());
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        new Bantuan(getActivity()).swal_error(databaseError.getMessage());
                    }
                });

        spinnerKonter.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                idKonter = listKeyKonter.get(position);
                if (item.equalsIgnoreCase("semua") && idKategori.equalsIgnoreCase("semua")){
                    getAllBarang();
                    new Bantuan(getActivity()).toastLong("semua barang");
                }else if (item.equalsIgnoreCase("semua")){
                    //getFilterKategori();
                    adapterListBarang.filterKategori(idKategori);
                    new Bantuan(getActivity()).toastLong("per kategori");
                }else if (idKategori.equalsIgnoreCase("semua")){
                    //getFilterKonter();
                    adapterListBarang.filterKonter(idKonter);
                    new Bantuan(getActivity()).toastLong("per konter");
                }else {
                    //getFilterKonterAndKategori();
                    adapterListBarang.filterKonterdanKategori(idKonter,idKategori);
                    new Bantuan(getActivity()).toastLong("per konter dan kategori");
                }

//
            }
        });
        spinnerKategori.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                idKategori = listKeyKategori.get(position);
                if (item.equalsIgnoreCase("semua") && idKonter.equalsIgnoreCase("semua")){
                    getAllBarang();
                    new Bantuan(getActivity()).toastLong("semua barang");
                }else if (item.equalsIgnoreCase("semua")){
                    //getFilterKonter();
                    adapterListBarang.filterKonter(idKonter);
                    new Bantuan(getActivity()).toastLong("per konter");
                }else if(idKonter.equalsIgnoreCase("semua")){
                    //getFilterKategori();
                    adapterListBarang.filterKonter(idKonter);
                    new Bantuan(getActivity()).toastLong("per kategori");
                }else {
                    //getFilterKonterAndKategori();
                    adapterListBarang.filterKonterdanKategori(idKonter,idKategori);
                    new Bantuan(getActivity()).toastLong("per kategori dan konter");
                }
//

            }
        });
    }


    /* TODO :: Fetch Data */
    private void getAllBarang() {
        rvBarang.showShimmerAdapter();
        databaseReference.child("barang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                listKey = new ArrayList<>();
                list.clear();
                listKey.clear();
                if (dataSnapshot.exists()) {
                    try {
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
                    }catch (NullPointerException e){
                        new Bantuan(getActivity()).swal_error(e.getMessage());
                    }
                }else {
                    layoutBelumAdaBarang.setVisibility(View.VISIBLE);
                    rvBarang.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                new Bantuan(getActivity()).swal_error(databaseError.getMessage());
            }
        });
    }

    private void getFilterKategori() {
        rvBarang.showShimmerAdapter();
        databaseReference.child("barang")
                .orderByChild("idKategori")
                .equalTo(idKategori)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                listKey = new ArrayList<>();
                list.clear();
                listKey.clear();
                if (dataSnapshot.exists()) {
                    try {
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
                    }catch (NullPointerException e) {
                        new Bantuan(getActivity()).swal_error(e.getMessage());
                    }
                }else {
                    layoutBelumAdaBarang.setVisibility(View.VISIBLE);
                    rvBarang.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                new Bantuan(getActivity()).swal_error(databaseError.getMessage());
            }
        });
    }

    private void getFilterKonter() {
        rvBarang.showShimmerAdapter();
        databaseReference.child("barang")
                .orderByChild("idKonter")
                .equalTo(idKonter)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                listKey = new ArrayList<>();
                list.clear();
                listKey.clear();
                if (dataSnapshot.exists()) {
                    try {
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
                    }catch (NullPointerException e){
                        new Bantuan(getActivity()).swal_error(e.getMessage());
                    }
                }else {
                    layoutBelumAdaBarang.setVisibility(View.VISIBLE);
                    rvBarang.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                new Bantuan(getActivity()).swal_error(databaseError.getMessage());
            }
        });
    }

    private void getFilterKonterAndKategori() {
        databaseReference.child("barang").orderByChild("idKonter").equalTo(idKonter).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                listKey = new ArrayList<>();
                list.clear();
                listKey.clear();
                if (dataSnapshot.exists()) {
                    try {
                        layoutBelumAdaBarang.setVisibility(View.GONE);
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            BarangModel model;
                            if (Objects.requireNonNull(data.child("idKategori").getValue(String.class)).equalsIgnoreCase(idKategori)) {

                                String key = data.getKey();
                                model = data.getValue(BarangModel.class);
                                list.add(model);
                                listKey.add(key);

                            } else if (idKategori.equalsIgnoreCase("semua")) {
                                String key = data.getKey();
                                model = data.getValue(BarangModel.class);
                                list.add(model);
                                listKey.add(key);

                            } else {
                              // layoutBelumAdaBarang.setVisibility(View.VISIBLE);
                            }

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
                    }catch (NullPointerException e){
                        new Bantuan(getActivity()).swal_error(e.getMessage());
                    }
                }else {
                    layoutBelumAdaBarang.setVisibility(View.VISIBLE);
                    rvBarang.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                new Bantuan(getActivity()).swal_error(databaseError.getMessage());
            }
        });
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //TODO : Action ketika tombol submit :v
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //TODO : Action ketika textnya berubah seperti dia yang sekarang bukan lagi yang dulu :(
                if (TextUtils.isEmpty(newText)) {
                    adapterListBarang.cariBarang("");
                } else {
                    adapterListBarang.cariBarang(newText);
                }
                return true;
            }
        });
    }
}

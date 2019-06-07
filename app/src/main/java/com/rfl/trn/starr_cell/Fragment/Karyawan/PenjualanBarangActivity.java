package com.rfl.trn.starr_cell.Fragment.Karyawan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.github.clans.fab.FloatingActionButton;
import com.github.javafaker.Faker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.rfl.trn.starr_cell.Adapter.AdapterBarangPenjualan;
import com.rfl.trn.starr_cell.Adapter.AdapterKategoriPenjualan;
import com.rfl.trn.starr_cell.Adapter.AdapterListPembelianBarang;
import com.rfl.trn.starr_cell.Custom.MyEditText;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Interface.ITransaksi;
import com.rfl.trn.starr_cell.Model.BarangModel;
import com.rfl.trn.starr_cell.Model.KategoriModel;
import com.rfl.trn.starr_cell.Model.ListPembelianBarangModel;
import com.rfl.trn.starr_cell.R;
import com.wajahatkarim3.easymoneywidgets.EasyMoneyTextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PenjualanBarangActivity extends AppCompatActivity implements ITransaksi {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvKategori)
    ShimmerRecyclerView rvKategori;
    @BindView(R.id.rvBarangPenjualan)
    ShimmerRecyclerView rvBarangPenjualan;
    @BindView(R.id.tvRupiahSementara)
    EasyMoneyTextView tvRupiahSementara;
    @BindView(R.id.layoutTombol)
    LinearLayout layoutTombol;
    @BindView(R.id.rvKeranjangBarang)
    ShimmerRecyclerView rvKeranjangBarang;
    @BindView(R.id.cardButtonBayar)
    CardView cardButtonBayar;
    @BindView(R.id.ivJumlahItemBayar)
    ImageView ivJumlahItemBayar;
    @BindView(R.id.btnPilihan)
    ImageView btnPilihan;
    @BindView(R.id.layoutKiri)
    LinearLayout layoutKiri;
    @BindView(R.id.layoutKanan)
    LinearLayout layoutKanan;
    @BindView(R.id.fabPay)
    FloatingActionButton fabPay;
    @BindView(R.id.ivBadgePay)
    ImageView ivBadgePay;
    @BindView(R.id.layoutPay)
    RelativeLayout layoutPay;
    @BindView(R.id.ivBtnModePilihBarang)
    ImageView ivBtnModePilihBarang;

    private Context context = PenjualanBarangActivity.this;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAnalytics firebaseAnalytics;
    private List<KategoriModel> listKategori = new ArrayList<>();
    private AdapterKategoriPenjualan adapterKategoriPenjualan;
    private List<BarangModel> listBarang = new ArrayList<>();
    private AdapterBarangPenjualan adapterBarangPenjualan;
    private List<ListPembelianBarangModel> listPembelianBarang = new ArrayList<>();
    private AdapterListPembelianBarang adapterListPembelianBarang;
    private int JUMLAH_BARANG_SEMENTARA = 0;
    private int PENAMBAHAN_DEFAULT = 1;
    private SearchView searchView;
    private String ID_CURRENT_KATEGORI = "semua";
    private boolean isPotrait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan_barang);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getAndSetKategori();
        getAndSetBarang();
        setJumlahBarangSementara();
        getAndSetListPembelianSementara();
    }

    private void init() {
        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.app_name));
        getSupportActionBar().setSubtitle("Transaksi Baru");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            isPotrait = true;
            layoutPay.setVisibility(View.VISIBLE);
        } else {
            isPotrait = false;
            layoutPay.setVisibility(View.GONE);
        }

        ivBtnModePilihBarang.setVisibility(View.GONE);

        fabPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setModeLihatHarga();
                ivBtnModePilihBarang.setVisibility(View.VISIBLE);
            }
        });

        ivBtnModePilihBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPotrait();
                ivBtnModePilihBarang.setVisibility(View.GONE);
            }
        });
    }

    private void setModeLihatHarga() {
        layoutKiri.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0f));
        layoutKanan.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 100f));
    }

    private void setPotrait() {
        layoutKiri.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 100f));
        layoutKanan.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 100f));
        layoutPay.setVisibility(View.VISIBLE);
    }

    private void setLandscape() {
        layoutKiri.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 70f));
        layoutKanan.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 30f));
        layoutPay.setVisibility(View.GONE);
    }

    private void getAndSetBarang() {
        databaseReference.child("barang")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listBarang.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                BarangModel model;
                                if (Objects.requireNonNull(data.child("idKonter").getValue(String.class))
                                        .equalsIgnoreCase(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())) {
                                    model = data.getValue(BarangModel.class);
                                    assert model != null;
                                    model.setIdBarang(data.getKey());
                                    model.setJumlahMasukKeranjang(0);
                                    listBarang.add(model);
                                }
                            }
                            adapterBarangPenjualan = new AdapterBarangPenjualan(context, listBarang);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                            rvBarangPenjualan.setLayoutManager(layoutManager);
                            rvBarangPenjualan.setAdapter(adapterBarangPenjualan);
                        } else {
                            new Bantuan(context).swal_error("Belum ada data barang di konter ini");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        new Bantuan(context).swal_error(databaseError.getMessage());
                    }
                });
    }

    private void getAndSetKategori() {
        databaseReference.child("kategori")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listKategori.clear();
                        if (dataSnapshot.exists()) {
                            listKategori.add(new KategoriModel("semua", "semua", new Date().getTime()));
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                listKategori.add(data.getValue(KategoriModel.class));
                            }
                            adapterKategoriPenjualan = new AdapterKategoriPenjualan(context, listKategori);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, 0, false);
                            rvKategori.setLayoutManager(layoutManager);
                            rvKategori.setAdapter(adapterKategoriPenjualan);
                        } else {
                            listKategori.add(new KategoriModel("Belum Ada Kategori", "Belum Ada Kategori", new Date().getTime()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        new Bantuan(context).swal_error(databaseError.getMessage());
                    }
                });
    }

    private void getAndSetListPembelianSementara() {
        adapterListPembelianBarang = new AdapterListPembelianBarang(context, this.listPembelianBarang);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rvKeranjangBarang.setLayoutManager(layoutManager);
        rvKeranjangBarang.setAdapter(adapterListPembelianBarang);
    }

    private void addIntoListPembelian(BarangModel barangModel) {
        boolean isSudahAda = false;
        int index = -1;
        for (int i = 0; i < listPembelianBarang.size(); i++) {
            if (listPembelianBarang.get(i).getIdBarang().toLowerCase(Locale.getDefault())
                    .contains(barangModel.getIdBarang().toLowerCase())) {
                index = i;
                isSudahAda = true;
                break;
            }
        }

        if (isSudahAda) {
            listPembelianBarang.get(index).setJumlahMasukKeranjang(
                    listPembelianBarang.get(index).getJumlahMasukKeranjang() + 1
            );
        } else {
            listPembelianBarang.add(new ListPembelianBarangModel(
                    barangModel.getIdBarang(),
                    barangModel.getNamaBarang(),
                    barangModel.getHarga1(),
                    barangModel.getJumlahMasukKeranjang()
            ));
        }
        getAndSetListPembelianSementara();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_penjualan, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        this.searchView = searchView;
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //TODO : Action ketika tombol submit :v
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //TODO : Action ketika textnya berubah seperti dia yang sekarang bukan lagi yang dulu :(
                if (TextUtils.isEmpty(newText)) {
                    searchBarangByNamaAndKategori("");
                } else {
                    searchBarangByNamaAndKategori(newText);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                tanyaKeluar();
                return true;
            case R.id.action_transaksiTambahan:
                showDialogTransaksiTambahan();
                return true;
            case R.id.action_simpanTransaksi:
                new Bantuan(context).swal_sukses("action_simpanTransaksi");
                return true;
            case R.id.action_clearTransaksi:
                resetTransaksi(false);
                return true;
            case R.id.action_daftarPesanan:
                new Bantuan(context).swal_sukses("action_daftarPesanan");
                return true;
            case R.id.action_ubahLayout:
                ubahLayout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("InflateParams")
    private void showDialogTransaksiTambahan() {
        View view = getLayoutInflater().inflate(R.layout.dialog_transaksi_tambahan, null);
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(true)
                .create();
        dialog.show();

        final MyEditText myetNamaBarang = view.findViewById(R.id.myetNamaBarang);
        final MyEditText myetKodeBarang = view.findViewById(R.id.myetKodeBarang);
        final MyEditText myetHargaJual = view.findViewById(R.id.myetHargaJual);
        final EditText etJumlahBarang = view.findViewById(R.id.etJumlahBarang);
        final ImageView btnKurang = view.findViewById(R.id.btnKurang);
        final ImageView btnTambah = view.findViewById(R.id.btnTambah);
        final LinearLayout btnBatal = view.findViewById(R.id.btnBatal);
        final LinearLayout btnOk = view.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(Objects.requireNonNull(myetNamaBarang.getText()).toString())
                        || TextUtils.isEmpty(Objects.requireNonNull(myetHargaJual.getText()).toString())) {
                    new Bantuan(context).toastLong("Nama barang dan Harga jual tidak boleh kosong !");
                } else if (TextUtils.isEmpty(Objects.requireNonNull(etJumlahBarang.getText()).toString())) {
                    new Bantuan(context).toastLong("Jumlah barang tidak boleh kosong !");
                } else {
                    String kodeBarang = null;
                    if (TextUtils.isEmpty(Objects.requireNonNull(myetKodeBarang.getText()).toString())) {
                        kodeBarang = "ID-" + new Faker().random().hex(10);
                    } else {
                        kodeBarang = myetKodeBarang.getText().toString();
                    }

                    String hargaBarang = null;
                    if (TextUtils.isEmpty(Objects.requireNonNull(myetHargaJual.getText()).toString())) {
                        hargaBarang = "0";
                    } else {
                        hargaBarang = myetHargaJual.getText().toString();
                    }

                    BarangModel model = new BarangModel();
                    model.setIdBarang(kodeBarang);
                    model.setNamaBarang(myetNamaBarang.getText().toString());
                    model.setHarga1(hargaBarang);
                    model.setHarga2(hargaBarang);
                    model.setHarga3(hargaBarang);
                    model.setJumlahMasukKeranjang(Integer.parseInt(etJumlahBarang.getText().toString()));
                    model.setIdKonter(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());
                    model.setIdKategori("ID-" + new Faker().random().hex(10));
                    model.setTanggalDiubah(new Date().getTime());
                    model.setStokBarang("1");

                    addIntoListPembelian(model);
                    jumlahkanHargaSementara(model.getHarga1(), model.getJumlahMasukKeranjang());
                    dialog.dismiss();
                }
            }
        });

        btnKurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etJumlahBarang.getText().toString()) ||
                        etJumlahBarang.getText().toString().equalsIgnoreCase("1")) {
                    etJumlahBarang.setText("1");
                } else {
                    etJumlahBarang.setText(
                            String.valueOf(Integer.parseInt(etJumlahBarang.getText().toString()) - 1)
                    );
                }
            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etJumlahBarang.getText().toString())) {
                    etJumlahBarang.setText("1");
                } else {
                    etJumlahBarang.setText(
                            String.valueOf(Integer.parseInt(etJumlahBarang.getText().toString()) + 1)
                    );
                }
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    private void ubahLayout() {
        isPotrait = !isPotrait;
        if (isPotrait) {
            setPotrait();
        } else {
            setLandscape();
        }
    }

    private void resetTransaksi(final boolean isKeluar) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Perhatian")
                .setContentText("Apakah kamu ingin menyimpan transaksi ini untuk sementara ?\n")
                .setConfirmText("Iya, Simpan")
                .setCancelText("Tidak, Hapus saja")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        new Bantuan(context).swal_sukses("Sabar ya bwambank :v");
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        getAndSetBarang();
                        setHargaSementara("0");
                        JUMLAH_BARANG_SEMENTARA = 0;
                        PENAMBAHAN_DEFAULT = 1;
                        setJumlahBarangSementara();
                        listPembelianBarang.clear();
                        getAndSetListPembelianSementara();
                        sweetAlertDialog.dismissWithAnimation();
                        if (isKeluar) {
                            finish();
                        }
                    }
                });
        dialog.show();
    }

    private void tanyaKeluar() {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Perhatian")
                .setContentText("Transaksi belum selesai, apakah kamu ingin tetap keluar ?\n")
                .setConfirmText("Iya, Keluar")
                .setCancelText("Batal")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        resetTransaksi(true);
                    }
                })
                .setCancelClickListener(null);
        dialog.show();
    }

    private void setJumlahBarangSementara() {
        TextDrawable gambar = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.BLACK)
                .useFont(Typeface.DEFAULT)
                .bold()
                .endConfig()
                .buildRoundRect(String.valueOf(JUMLAH_BARANG_SEMENTARA),
                        Color.WHITE, 8);

        TextDrawable badge = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .bold()
                .endConfig()
                .buildRound(String.valueOf(JUMLAH_BARANG_SEMENTARA), Color.parseColor("#F79A48"));
        ivJumlahItemBayar.setImageDrawable(gambar);
        ivBadgePay.setImageDrawable(badge);
    }

    private void setJumlahBarangSementaraTambahSatu() {
        JUMLAH_BARANG_SEMENTARA += PENAMBAHAN_DEFAULT;
        setJumlahBarangSementara();
    }

    private void jumlahkanHargaSementara(String harga) {
        jumlahkanHargaSementara(harga, 1);
    }

    private void jumlahkanHargaSementara(String harga, int jumlah) {
        double hargaAwal = Double.parseDouble(tvRupiahSementara.getValueString());
        double totalHargaTambahan = jumlah * Double.parseDouble(harga);
        double hargaSetelahDijumlah = hargaAwal + totalHargaTambahan;
        setHargaSementara(String.valueOf(hargaSetelahDijumlah));
    }

    private void setHargaSementara(String hargaSementara) {
        tvRupiahSementara.setText(hargaSementara);
        tvRupiahSementara.setCurrency("Rp");
        tvRupiahSementara.showCurrencySymbol();
        tvRupiahSementara.showCommas();
    }

    private void clearSearchView() {
        searchView.setQuery(null, false);
        searchView.setIconified(true);
        searchView.clearFocus();
    }

    private void searchBarangByNamaAndKategori(String text) {
        adapterBarangPenjualan.search(text, ID_CURRENT_KATEGORI);
    }

    @Override
    public void onBackPressed() {
        tanyaKeluar();
    }

    @Override
    public void onItemBarangClick(BarangModel barangModel) {
        jumlahkanHargaSementara(barangModel.getHarga1());
        setJumlahBarangSementaraTambahSatu();
        addIntoListPembelian(barangModel);
    }

    @Override
    public void onKategoriClick(KategoriModel kategoriModel) {
        clearSearchView();
        ID_CURRENT_KATEGORI = kategoriModel.getIdKategori();
        searchBarangByNamaAndKategori("");
    }
}

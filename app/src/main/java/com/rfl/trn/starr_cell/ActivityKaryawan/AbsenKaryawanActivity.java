package com.rfl.trn.starr_cell.ActivityKaryawan;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.github.javafaker.Faker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mindorks.paracamera.Camera;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.rfl.trn.starr_cell.Custom.MyEditText;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Helper.Permissions;
import com.rfl.trn.starr_cell.Helper.Waktu;
import com.rfl.trn.starr_cell.Model.AbsenModel;
import com.rfl.trn.starr_cell.R;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AbsenKaryawanActivity extends AppCompatActivity {

    private static int MAX_CURRENT_KARYAWAN = 2;
    private final int ALL_PERMISSION = 999;
    private final int CODE_CAMERA = 2;
    @BindView(R.id.iv_takeFoto)
    ImageView ivTakeFoto;
    @BindView(R.id.spinner_karyawan)
    MaterialSpinner spinnerKaryawan;
    @BindView(R.id.myet_pesan)
    MyEditText myetPesan;
    @BindView(R.id.btn_kirim)
    MyTextView btnKirim;
    private Context context = AbsenKaryawanActivity.this;
    private Camera camera;
    private List<String> listNamaKaryawan = new ArrayList<>();
    private List<String> listKeyKaryawan = new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String keyKaryawan = null;
    private Uri downloadURL;
    private long totalCurrentKaryawan = 0;
    private List<String> listKeyCurrentKaryawan = new ArrayList<>();
    private List<String> listKeyKaryawanSudahAbsenNormalHariIni = new ArrayList<>(); // :v bingung njenengine akwoakwo
    /*
        kodeAbsen
        0 - Absen Masuk Normal
        1 - Absen Keluar Normal
        2 - Absen Masuk Lembur
        3 - Absen Keluar Lembur
     */
    private int kodeAbsen = 0;
    private String namaFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen_karyawan);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        //TODO : ini urutanya jangan di balik ya bwambank :v
        getTotalAndKeyCurrentKaryawan();
        getDataKaryawan();
        getKeyKaryawanSudahAbsenNormalHariIni();
        setJenisAbsen();
        setSpinnerAndKeyKaryawanJikaJenisAbsenKeluar();

    }

    private void setSpinnerAndKeyKaryawanJikaJenisAbsenKeluar() {
        if (getIntent().hasExtra("jenis")) {
            if (getIntent().getStringExtra("jenis").equalsIgnoreCase("absenKeluarNormal") ||
                    getIntent().getStringExtra("jenis").equalsIgnoreCase("absenKeluarLembur")) {
                databaseReference.child("karyawan")
                        .child(getIntent().getStringExtra("idKaryawan"))
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    spinnerKaryawan.setEnabled(false);
                                    spinnerKaryawan.setItems(dataSnapshot.child("namaKaryawan").getValue(String.class));
                                    keyKaryawan = dataSnapshot.getKey();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                new Bantuan(context).swal_error("Error saat get karyawan : " + databaseError.getMessage());
                            }
                        });
            }
        }
    }

    private void setJenisAbsen() {
        if (getIntent().hasExtra("jenis")) {
            if (getIntent().getStringExtra("jenis").equalsIgnoreCase("absenMasukNormal")) {
                Objects.requireNonNull(getSupportActionBar()).setSubtitle("Absen Masuk Normal");
                kodeAbsen = 0;
            } else if (getIntent().getStringExtra("jenis").equalsIgnoreCase("absenKeluarNormal")) {
                Objects.requireNonNull(getSupportActionBar()).setSubtitle("Absen Keluar Normal");
                kodeAbsen = 1;
            } else if (getIntent().getStringExtra("jenis").equalsIgnoreCase("absenMasukLembur")) {
                Objects.requireNonNull(getSupportActionBar()).setSubtitle("Absen Masuk Lembur");
                kodeAbsen = 2;
            } else if (getIntent().getStringExtra("jenis").equalsIgnoreCase("absenKeluarLembur")) {
                Objects.requireNonNull(getSupportActionBar()).setSubtitle("Absen Keluar Lembur");
                kodeAbsen = 3;
            }
        } else {
            new Bantuan(context).toastLong("Jenis Absen Tidak Diketahui !");
            finish();
        }
    }

    private void getKeyKaryawanSudahAbsenNormalHariIni() {
        databaseReference.child("absen")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listKeyKaryawanSudahAbsenNormalHariIni.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                if (Objects.requireNonNull(data.child("idKonter").getValue(String.class))
                                        .equalsIgnoreCase(Objects.requireNonNull(firebaseAuth.getCurrentUser())
                                                .getUid())) {
                                    if (Objects.requireNonNull(data.child("jenisAbsen").getValue(String.class))
                                            .equalsIgnoreCase("Masuk")) {
                                        if (new Waktu(context).isHariSama(new Date().getTime(),
                                                data.child("tanggal").getValue(long.class))) {
                                            listKeyKaryawanSudahAbsenNormalHariIni
                                                    .add(data.child("idKaryawan").getValue(String.class));
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        new Bantuan(context).swal_error(databaseError.getMessage());
                    }
                });
    }

    private boolean isSudahAbsenNormalHariIni() {
        return listKeyKaryawanSudahAbsenNormalHariIni.contains(keyKaryawan);
    }

    private void getDataKaryawan() {
        databaseReference.child("karyawan")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listKeyKaryawan.clear();
                        listNamaKaryawan.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                listKeyKaryawan.add(data.getKey());
                                listNamaKaryawan.add(data.child("namaKaryawan").getValue(String.class));
                            }
                        } else {
                            listKeyKaryawan.add("kosong");
                            listNamaKaryawan.add("Belum ada data karyawan");
                        }
                        spinnerKaryawan.setItems(listNamaKaryawan);
                        keyKaryawan = listKeyKaryawan.get(0);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        new Bantuan(context).swal_error("databaseError : " + databaseError.getMessage());
                    }
                });

        spinnerKaryawan.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                keyKaryawan = listKeyKaryawan.get(position);
            }
        });
    }

    @OnClick({R.id.iv_takeFoto, R.id.btn_kirim})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_takeFoto:
                ambilFoto();
                break;
            case R.id.btn_kirim:
                prosesAbsen();
                break;
        }
    }

    private void ambilFoto() {
        String[] PERMISSIONS = {
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (!Permissions.hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, ALL_PERMISSION);
        } else {
            startCamera();
        }
    }

    private void startCamera() {
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(CODE_CAMERA)
                .setDirectory("pics")
                .setName("absen_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
        try {
            camera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
            new Bantuan(context).swal_error(e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
            Uri imageUri = getImageUri(context, camera.getCameraBitmap());
            if (imageUri != null) {
                startCrop(imageUri);
            } else {
                new Bantuan(context).swal_error("Gagal mengambil gambar !");
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri hasilCrop = UCrop.getOutput(Objects.requireNonNull(data));
            if (hasilCrop != null) {
                ivTakeFoto.setImageURI(hasilCrop);
                Picasso.get()
                        .load(hasilCrop)
                        .into(ivTakeFoto);
            } else {
                new Bantuan(context).swal_warning("gambar null hehe");
            }
        }
    }

    private void startCrop(Uri uri) {
        String tujuan = String.valueOf(System.currentTimeMillis() % 1000);
        tujuan += ".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), tujuan)));
        uCrop.withAspectRatio(9, 16);
        uCrop.withMaxResultSize(900, 1600);
        uCrop.withOptions(getCropOption());
        uCrop.start(AbsenKaryawanActivity.this);
    }

    private UCrop.Options getCropOption() {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(80);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        options.setToolbarTitle("Crop Gambar");
        return options;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ALL_PERMISSION:
                if (grantResults.length > 0) {
                    boolean bolehSemua = false;
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            bolehSemua = true;
                        } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            bolehSemua = false;
                            break;
                        }
                    }
                    if (bolehSemua) {
                        startCamera();
                    } else {
                        new Bantuan(context).swal_error("Akses Di Tolak !");
                    }
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void prosesAbsen() {
        if (ivTakeFoto.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.bg_take_pict).getConstantState()) {
            final SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
            dialog.setTitleText("Peringatan");
            dialog.setContentText("Foto Diri belum di tambahkan.\nSilahkan foto diri terlebih dahulu untuk bukti absen");
            dialog.setConfirmText("Ambil Foto");
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    ambilFoto();
                    dialog.dismissWithAnimation();
                }
            });
            dialog.show();
        } else {
            if (kodeAbsen == 0) {
                if (isSudahAbsen()) {
                    new Bantuan(context).swal_error(getString(R.string.tidak_bisa_absen_lagi));
                } else {
                    if (totalCurrentKaryawan == MAX_CURRENT_KARYAWAN) {
                        new Bantuan(context).swal_error(getString(R.string.karyawan_lebih_dari_dua));
                    } else {
                        simpanKeDatabase(kodeAbsen);
                    }
                }
            } else if (kodeAbsen == 2) {
                if (isSudahAbsenNormalHariIni()) {
                    if (isSudahAbsen()) {
                        new Bantuan(context).swal_error(getString(R.string.tidak_bisa_absen_lagi));
                    } else {
                        simpanKeDatabase(kodeAbsen);
                    }
                } else {
                    new Bantuan(context).swal_error(getString(R.string.belum_absen_normal));
                }
            } else if (kodeAbsen == 1 || kodeAbsen == 3) { //absen keluar
                simpanKeDatabase(kodeAbsen);
            } else {
                new Bantuan(context).swal_error("Kode Absen Salah!");
            }
        }
    }

    private void getTotalAndKeyCurrentKaryawan() {
        databaseReference.child("currentKaryawan")
                .child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listKeyCurrentKaryawan.clear();
                        totalCurrentKaryawan = dataSnapshot.getChildrenCount();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                listKeyCurrentKaryawan.add(data.child("idKaryawan").getValue(String.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        new Bantuan(context).swal_error(databaseError.getMessage());
                    }
                });
    }

    private boolean isSudahAbsen() {
        return listKeyCurrentKaryawan.contains(keyKaryawan);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.deleteImage();
        }
    }

    private void simpanKeDatabase(final int kodeAbsen) {
        final SweetAlertDialog loading = new Bantuan(context).swal_loading("Tunggu beberapa saat, proses absen");
        loading.show();
        String pesanAbsen = null;
        if (TextUtils.isEmpty(myetPesan.getText())) {
            pesanAbsen = "Tidak Ada Pesan";
        } else {
            pesanAbsen = Objects.requireNonNull(myetPesan.getText()).toString();
        }

        Bitmap bitmap = ((BitmapDrawable) ivTakeFoto.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        if (kodeAbsen == 0) {
            loading.setContentText("Tunggu beberapa saat, proses absen masuk normal");
            namaFoto = "absenMasukNormal_";
        } else if (kodeAbsen == 1) {
            loading.setContentText("Tunggu beberapa saat, proses absen keluar normal");
            namaFoto = "absenKeluarNormal_";
        } else if (kodeAbsen == 2) {
            loading.setContentText("Tunggu beberapa saat, proses absen masuk lembur");
            namaFoto = "absenMasukLembur_";
        } else if (kodeAbsen == 3) {
            loading.setContentText("Tunggu beberapa saat, proses absen keluar lembur");
            namaFoto = "absenKeluarLembur_";
        }

        namaFoto += new Date().getTime() + ".jpeg";

        final StorageReference ref = storageReference.child("absen")
                .child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                .child(namaFoto);
        UploadTask uploadTask = ref.putBytes(data);
        final String finalPesanAbsen = pesanAbsen;
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadURL = task.getResult();
                    String keyPushAbsen = databaseReference.push().getKey();

                    final AbsenModel dataAbsen = new AbsenModel();
                    dataAbsen.setIdKaryawan(keyKaryawan);
                    dataAbsen.setIdKonter(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());
                    dataAbsen.setNamaFoto(namaFoto);
                    dataAbsen.setPesan(finalPesanAbsen);
                    dataAbsen.setStatus("Pending");
                    dataAbsen.setTanggal(new Date().getTime());
                    dataAbsen.setUrlFoto(Objects.requireNonNull(downloadURL).toString());

                    if (kodeAbsen == 0) {
                        simpanAbsenMasuk(dataAbsen, keyPushAbsen, "Masuk", false, loading);
                    } else if (kodeAbsen == 1) {
                        simpanAbsenKeluar(dataAbsen, keyPushAbsen, "Keluar", false, loading);
                    } else if (kodeAbsen == 2) {
                        simpanAbsenMasuk(dataAbsen, keyPushAbsen, "Masuk", true, loading);
                    } else if (kodeAbsen == 3) {
                        simpanAbsenKeluar(dataAbsen, keyPushAbsen, "Keluar", true, loading);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismissWithAnimation();
                new Bantuan(context).swal_error(e.getMessage());
            }
        });
    }

    private void simpanAbsenMasuk(AbsenModel dataAbsen, String keyPushAbsen, String jenisAbsen, final boolean isLembur, final SweetAlertDialog loading) {

        dataAbsen.setWaktuMasuk(new Date().getTime());
        dataAbsen.setLembur(isLembur);
        dataAbsen.setJenisAbsen(jenisAbsen);

        final Map<String, String> dataCurrentKaryawan = new HashMap<>();
        dataCurrentKaryawan.put("idAbsen", Objects.requireNonNull(keyPushAbsen));
        dataCurrentKaryawan.put("idKaryawan", dataAbsen.getIdKaryawan());

        databaseReference.child("absen")
                .child(Objects.requireNonNull(keyPushAbsen))
                .setValue(dataAbsen)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        databaseReference.child("currentKaryawan")
                                .child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                                .child("ID-" + new Faker(new Locale("in-ID")).random().hex(20))
                                .setValue(dataCurrentKaryawan)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        loading.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                        loading.showContentText(true);
                                        loading.setTitleText("Sukses");
                                        if (isLembur) {
                                            loading.setContentText("Berhasil Absen Masuk Lembur");
                                        } else {
                                            loading.setContentText("Berhasil Absen Masuk Normal");
                                        }
                                        loading.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                new Bantuan(context).swal_error("Erorr saat mengisi current karyawan : " + e.getMessage());
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismissWithAnimation();
                new Bantuan(context).swal_error("Error saat menyimpan ke database : " + e.getMessage());
            }
        });
    }

    private void simpanAbsenKeluar(AbsenModel dataAbsen, String keyPushAbsen, String jenisAbsen, final boolean isLembur, final SweetAlertDialog loading) {
        dataAbsen.setWaktuKeluar(new Date().getTime());
        dataAbsen.setLembur(isLembur);
        dataAbsen.setJenisAbsen(jenisAbsen);
        dataAbsen.setIdAbsenMasuk(getIntent().getStringExtra("idAbsenMasuk"));

        databaseReference.child("absen")
                .child(Objects.requireNonNull(keyPushAbsen))
                .setValue(dataAbsen)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        databaseReference.child("currentKaryawan")
                                .child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                                .child(getIntent().getStringExtra("idCurrentKaryawan"))
                                .setValue(null)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        loading.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                        loading.showContentText(true);
                                        loading.setTitleText("Sukses");
                                        if (isLembur) {
                                            loading.setContentText("Berhasil Absen Keluar Lembur");
                                        } else {
                                            loading.setContentText("Berhasil Absen Keluar Normal");
                                        }
                                        loading.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                new Bantuan(context).swal_error("Erorr saat menghapus current karyawan : " + e.getMessage());
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismissWithAnimation();
                new Bantuan(context).swal_error("Error saat menyimpan ke database : " + e.getMessage());
            }
        });
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}

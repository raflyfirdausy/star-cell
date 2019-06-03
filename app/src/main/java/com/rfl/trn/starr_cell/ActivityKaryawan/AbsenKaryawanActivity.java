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
import com.rfl.trn.starr_cell.Model.AbsenModel;
import com.rfl.trn.starr_cell.R;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AbsenKaryawanActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen_karyawan);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        getSupportActionBar().setSubtitle(R.string.absensi);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        getTotalAndKeyCurrentKaryawan();
        getDataKaryawan();
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
            if (isSudahAbsen()) {
                new Bantuan(context).swal_error("Kamu sudah absen");
            } else {
                if (totalCurrentKaryawan == 2) {
                    new Bantuan(context).swal_error(getString(R.string.karyawan_lebih_dari_dua));
                } else {
                    simpanKeDatabase("ID-" +
                            new Faker(new Locale("in-ID")).random().hex(10));
                }
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
                                listKeyCurrentKaryawan.add(data.getValue(String.class));
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

    private void simpanKeDatabase(final String childKaryawan) {
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

        final StorageReference ref = storageReference.child("absen")
                .child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                .child("absen_" + new Date().getTime() + ".jpeg");
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
                    final AbsenModel dataAbsen = new AbsenModel();
                    dataAbsen.setIdKaryawan(keyKaryawan);
                    dataAbsen.setTanggal(new Date().getTime());
                    dataAbsen.setWaktuMasuk(new Date().getTime());
                    dataAbsen.setWaktuKeluar(null);
                    dataAbsen.setKonfirmasi("pending");
                    dataAbsen.setPesan(finalPesanAbsen);
                    dataAbsen.setUrlFoto(Objects.requireNonNull(downloadURL).toString());
                    dataAbsen.setIdKonter(firebaseAuth.getCurrentUser().getUid());
                    dataAbsen.setJenisAbsen("masuk");

                    String keyAbsen = databaseReference.push().getKey();

                    databaseReference.child("absen")
                            .child(Objects.requireNonNull(keyAbsen))
                            .setValue(dataAbsen)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //TODO : set current karyawan tp ngko lah :v
                                    databaseReference.child("currentKaryawan")
                                            .child(firebaseAuth.getCurrentUser().getUid())
                                            .child(childKaryawan)
                                            .setValue(dataAbsen.getIdKaryawan())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    loading.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                    loading.showContentText(true);
                                                    loading.setTitleText("Sukses");
                                                    loading.setContentText("Berhasil Absen");
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
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismissWithAnimation();
                new Bantuan(context).swal_error(e.getMessage());
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

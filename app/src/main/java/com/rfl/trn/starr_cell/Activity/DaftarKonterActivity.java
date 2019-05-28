package com.rfl.trn.starr_cell.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mindorks.paracamera.Camera;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.rfl.trn.starr_cell.Custom.MyEditText;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Fragment.Custom.BottomSheetDialogFotoKonter;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Helper.Permissions;
import com.rfl.trn.starr_cell.Model.KonterModel;
import com.rfl.trn.starr_cell.R;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DaftarKonterActivity extends AppCompatActivity implements BottomSheetDialogFotoKonter.BottomSheetListener {

    private final int CODE_GALLERY = 1;
    private final int CODE_CAMERA = 2;
    private final int ALL_PERMISSION = 999;
    @BindView(R.id.iv_karyawan)
    ImageView ivKonter;
    @BindView(R.id.tv_detailKaryawan)
    TextView tvDetailKonter;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.iv_gambarKonter)
    ImageView ivGambarKonter;
    @BindView(R.id.myet_namaKaryawan)
    MyEditText myetNamaKonter;
    @BindView(R.id.myet_noHpKaryawan)
    MyEditText myetAlamatKonter;
    @BindView(R.id.myet_emailKonter)
    MyEditText myetEmailKonter;
    @BindView(R.id.myet_passwordKonter)
    MyEditText myetPasswordKonter;
    @BindView(R.id.myet_konfirmasiPasswordKonter)
    MyEditText myetKonfirmasiPasswordKonter;
    @BindView(R.id.btn_daftar)
    MyTextView btnDaftar;
    private String currentPhotoPath;
    private Context context = DaftarKonterActivity.this;
    private FirebaseAuth firebaseAuth, firebaseAuth2;
    private DatabaseReference databaseReference;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Camera camera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_konter);
        ButterKnife.bind(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        getSupportActionBar().setSubtitle(R.string.tambah_data_konter);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //custom font buat type password
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf");
        myetPasswordKonter.setTypeface(typeface);
        myetKonfirmasiPasswordKonter.setTypeface(typeface);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @OnClick({R.id.iv_gambarKonter, R.id.btn_daftar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_gambarKonter:
                BottomSheetDialogFotoKonter bottomSheetDialogFotoKonter = new BottomSheetDialogFotoKonter();
                bottomSheetDialogFotoKonter.show(getSupportFragmentManager(), "Ambil Foto Konter");
                break;
            case R.id.btn_daftar:
                prosesDaftar();
                break;
        }
    }

    private void prosesDaftar() {
        if (TextUtils.isEmpty(myetNamaKonter.getText()) ||
                TextUtils.isEmpty(myetAlamatKonter.getText()) ||
                TextUtils.isEmpty(myetEmailKonter.getText()) ||
                TextUtils.isEmpty(myetPasswordKonter.getText()) ||
                TextUtils.isEmpty(myetKonfirmasiPasswordKonter.getText())) {
            new Bantuan(context).swal_warning("Masih Ada Data yang belum diisi !");
        } else if (!Objects.requireNonNull(myetPasswordKonter.getText()).toString()
                .equals(Objects.requireNonNull(myetKonfirmasiPasswordKonter.getText()).toString())) {
            new Bantuan(context).swal_error("Konfirmasi Password Salah !");
            myetKonfirmasiPasswordKonter.setError("Konfirmasi Password Salah !");
        } else {
            final SweetAlertDialog loading = new Bantuan(context).swal_loading("Tunggu beberapa saat, proses pendaftaran konter");
            loading.show();

            FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                    .setDatabaseUrl("https://star-cell-rafly-trian.firebaseio.com/")
                    .setApiKey("AIzaSyCIuABdUJX-_5uTBEGrYU2qRpTrpOUU9tk")
                    .setApplicationId("com.rfl.trn.starr_cell")
                    .build();

            try {
                FirebaseApp firebaseApp = FirebaseApp.initializeApp(getApplicationContext(),
                        firebaseOptions,
                        getString(R.string.app_name));
                firebaseAuth2 = FirebaseAuth.getInstance(firebaseApp);
            } catch (IllegalStateException e) {
                firebaseAuth2 = FirebaseAuth.getInstance(FirebaseApp.getInstance(getString(R.string.app_name)));
            }

            firebaseAuth2.createUserWithEmailAndPassword(Objects.requireNonNull(myetEmailKonter.getText()).toString(),
                    myetPasswordKonter.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            String key = authResult.getUser().getUid();
                            KonterModel konterModel = new KonterModel();
                            konterModel.setNamaKonter(Objects.requireNonNull(myetNamaKonter.getText()).toString());
                            konterModel.setAlamatKonter(Objects.requireNonNull(myetAlamatKonter.getText()).toString());
                            konterModel.setEmailKonter(Objects.requireNonNull(myetEmailKonter.getText()).toString());
                            konterModel.setPassword(Objects.requireNonNull(myetPasswordKonter.getText()).toString());

                            databaseReference.child("konter")
                                    .child(key)
                                    .setValue(konterModel)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            loading.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                            loading.showContentText(true);
                                            loading.setTitleText("Sukses");
                                            loading.setContentText("Berhasil Mendaftarkan Konter Baru");
                                            loading.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    finish();
                                                }
                                            });
                                            firebaseAuth2.signOut();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            loading.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                                            loading.showContentText(true);
                                            loading.setTitleText("Gagal");
                                            loading.setContentText(e.getMessage());
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loading.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                            loading.showContentText(true);
                            loading.setTitleText("Gagal");
                            loading.setContentText(e.getMessage());
                        }
                    });
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

    private void startCrop(Uri uri) {
        String tujuan = String.valueOf(System.currentTimeMillis() % 1000);
        tujuan += ".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), tujuan)));
        uCrop.withAspectRatio(16, 9);
        uCrop.withMaxResultSize(1600, 900);
        uCrop.withOptions(getCropOption());
        uCrop.start(DaftarKonterActivity.this);
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
    public void onOptionClick(String text) {
        if (text.equalsIgnoreCase("gallery")) {
            startActivityForResult(new Intent()
                    .setAction(Intent.ACTION_GET_CONTENT)
                    .setType("image/*"), CODE_GALLERY);
        } else {
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_GALLERY && resultCode == RESULT_OK) {
            Uri imageUri = Objects.requireNonNull(data).getData();
            if (imageUri != null) {
                startCrop(imageUri);
            }
        } else if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
            Uri imageUri = getImageUri(context, camera.getCameraBitmap());
            if (imageUri != null) {
                startCrop(imageUri);
            } else {
                new Bantuan(context).swal_error("Gagal mengambil gambar !");
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri hasilCrop = UCrop.getOutput(Objects.requireNonNull(data));
            if (hasilCrop != null) {
                ivGambarKonter.setImageURI(hasilCrop);
                Picasso.get()
                        .load(hasilCrop)
                        .into(ivGambarKonter);
            } else {
                new Bantuan(context).swal_warning("gambar null hehe");
            }
        }
    }

    private void startCamera() {
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(CODE_CAMERA)
                .setDirectory("pics")
                .setName("konter_" + System.currentTimeMillis())
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

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.deleteImage();
    }
}

package com.rfl.trn.starr_cell.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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
    @BindView(R.id.tv_judul)
    TextView tvJudul;
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
    private Context context = DaftarKonterActivity.this;
    private FirebaseAuth firebaseAuth, firebaseAuth2;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Uri downloadURL;
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
        storageReference = FirebaseStorage.getInstance().getReference();

        if (getIntent().hasExtra("jenis")) {
            if (getIntent().getStringExtra("jenis").equalsIgnoreCase("edit")) {
                setAndGetTampilanEdit();
            }
        }
    }

    private void setAndGetTampilanEdit() {
        myetEmailKonter.setEnabled(false);
        myetPasswordKonter.setVisibility(View.GONE);
        myetKonfirmasiPasswordKonter.setVisibility(View.GONE);
        btnDaftar.setText(getString(R.string.simpan));
        tvJudul.setText(getString(R.string.edit_data_konter));

        if (getIntent().hasExtra("urlFoto")) {
            Picasso.get()
                    .load(getIntent().getStringExtra("urlFoto"))
                    .placeholder(R.drawable.bg_take_pict)
                    .into(ivGambarKonter);
        }
        myetNamaKonter.setText(getIntent().getStringExtra("namaKonter"));
        myetAlamatKonter.setText(getIntent().getStringExtra("alamatKonter"));
        myetEmailKonter.setText(getIntent().getStringExtra("emailKonter"));
    }


    @OnClick({R.id.iv_gambarKonter, R.id.btn_daftar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_gambarKonter:
                ambilFoto();
                break;
            case R.id.btn_daftar:
                if (getIntent().hasExtra("jenis")) {
                    if (getIntent().getStringExtra("jenis").equalsIgnoreCase("edit")) {
                        prosesEdit();
                    } else if (getIntent().getStringExtra("jenis").equalsIgnoreCase("password")) {
                        prosesGantiPassword();
                    }
                } else {
                    prosesDaftar();
                }
                break;
        }
    }

    private void prosesEdit() {
        if (TextUtils.isEmpty(myetNamaKonter.getText()) ||
                TextUtils.isEmpty(myetAlamatKonter.getText()) ||
                TextUtils.isEmpty(myetEmailKonter.getText())) {
            new Bantuan(context).swal_warning("Masih Ada Data yang belum diisi !");
        } else if (ivGambarKonter.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.bg_take_pict).getConstantState()) {
            final SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
            dialog.setTitleText("Peringatan");
            dialog.setContentText("Foto konter belum di tambahkan.\nApakah tetap ingin menyimpan data tanpa foto ?");
            dialog.setConfirmText("Iya, Simpan");
            dialog.setCancelText("Tambahkan Foto");
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    simpanKeDatabaseEdit(false);
                    dialog.dismissWithAnimation();
                }
            });
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    ambilFoto();
                    dialog.dismissWithAnimation();
                }
            });
            dialog.show();
        } else {
            simpanKeDatabaseEdit(true);
        }
    }

    private void prosesGantiPassword() {
        new Bantuan(context).swal_warning("coming soon hehe");
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
        } else if (ivGambarKonter.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.bg_take_pict).getConstantState()) {
            final SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
            dialog.setTitleText("Peringatan");
            dialog.setContentText("Foto konter belum di tambahkan.\nApakah tetap ingin menyimpan data tanpa foto ?");
            dialog.setConfirmText("Iya, Simpan");
            dialog.setCancelText("Tambahkan Foto");
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    simpanKeDatabase(false);
                    dialog.dismissWithAnimation();
                }
            });
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    ambilFoto();
                    dialog.dismissWithAnimation();
                }
            });
            dialog.show();
        } else {
            simpanKeDatabase(true);
        }
    }

    private void simpanKeDatabaseEdit(final boolean isAdaFoto) {
        final SweetAlertDialog loading = new Bantuan(context).swal_loading("Tunggu beberapa saat, proses mengedit konter");
        loading.show();

        final String key = getIntent().getStringExtra("key");
        final KonterModel konterModel = new KonterModel();

        Bitmap bitmap = ((BitmapDrawable) ivGambarKonter.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        final StorageReference ref = storageReference.child("konter")
                .child(key)
                .child(key + ".jpeg");

        UploadTask uploadTask = ref.putBytes(data);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                    konterModel.setNamaKonter(Objects.requireNonNull(myetNamaKonter.getText()).toString());
                    konterModel.setAlamatKonter(Objects.requireNonNull(myetAlamatKonter.getText()).toString());
                    konterModel.setEmailKonter(Objects.requireNonNull(myetEmailKonter.getText()).toString());
                    if (isAdaFoto) {
                        konterModel.setUrl_foto(downloadURL.toString());
                    } else {
                        konterModel.setUrl_foto(null);
                    }
                    databaseReference.child("konter")
                            .child(key)
                            .setValue(konterModel)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    loading.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    loading.showContentText(true);
                                    loading.setTitleText("Sukses");
                                    loading.setContentText("Berhasil Edit Data Konter");
                                    loading.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            finish();
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
                } else {
                    new Bantuan(context).swal_error(Objects.requireNonNull(task.getException()).getMessage());
                }
            }
        });
    }

    private void simpanKeDatabase(final boolean isAdaFoto) {
        final SweetAlertDialog loading = new Bantuan(context).swal_loading("Tunggu beberapa saat, proses pendaftaran konter");
        loading.show();

        FirebaseOptions firebaseOptions = new Bantuan(context).getFirebaseOptions();

        try {
            FirebaseApp firebaseApp = FirebaseApp.initializeApp(getApplicationContext(),
                    firebaseOptions,
                    getString(R.string.app_name));
            firebaseAuth2 = FirebaseAuth.getInstance(firebaseApp);
        } catch (IllegalStateException e) {
            firebaseAuth2 = FirebaseAuth.getInstance(FirebaseApp.getInstance(getString(R.string.app_name)));
        }

        firebaseAuth2.createUserWithEmailAndPassword(Objects.requireNonNull(myetEmailKonter.getText()).toString(),
                Objects.requireNonNull(myetPasswordKonter.getText()).toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        final String key = authResult.getUser().getUid();
                        final KonterModel konterModel = new KonterModel();

                        Bitmap bitmap = ((BitmapDrawable) ivGambarKonter.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        final StorageReference ref = storageReference.child("konter")
                                .child(key)
                                .child(key + ".jpeg");

                        UploadTask uploadTask = ref.putBytes(data);
                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                                    konterModel.setNamaKonter(Objects.requireNonNull(myetNamaKonter.getText()).toString());
                                    konterModel.setAlamatKonter(Objects.requireNonNull(myetAlamatKonter.getText()).toString());
                                    konterModel.setEmailKonter(Objects.requireNonNull(myetEmailKonter.getText()).toString());
                                    konterModel.setPassword(Objects.requireNonNull(myetPasswordKonter.getText()).toString());
                                    if (isAdaFoto) {
                                        konterModel.setUrl_foto(downloadURL.toString());
                                    } else {
                                        konterModel.setUrl_foto(null);
                                    }
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
                                } else {
                                    new Bantuan(context).swal_error(Objects.requireNonNull(task.getException()).getMessage());
                                }
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

    private void ambilFoto() {
        BottomSheetDialogFotoKonter bottomSheetDialogFotoKonter = new BottomSheetDialogFotoKonter();
        bottomSheetDialogFotoKonter.show(getSupportFragmentManager(), "Ambil Foto Konter");
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
        } else if (text.equalsIgnoreCase("kamera")) {
            String[] PERMISSIONS = {
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            if (!Permissions.hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, ALL_PERMISSION);
            } else {
                startCamera();
            }
        } else {

            final SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
            dialog.setTitleText("Peringatan");
            dialog.setContentText("Apakah anda yakin ingin menghapus foto konter ?");
            dialog.setConfirmText("Iya, hapus");
            dialog.setCancelText("Batal");
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    ivGambarKonter.setImageDrawable(getDrawable(R.drawable.bg_take_pict));
                    new Bantuan(context).toastShort("Foto konter berhasil di hapus");
                    dialog.dismissWithAnimation();
                }
            });
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dialog.dismissWithAnimation();
                }
            });
            dialog.show();
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
        if (camera != null) {
            camera.deleteImage();
        }
    }
}

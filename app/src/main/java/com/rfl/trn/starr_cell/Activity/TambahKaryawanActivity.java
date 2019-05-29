package com.rfl.trn.starr_cell.Activity;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import com.rfl.trn.starr_cell.Model.KaryawanModel;
import com.rfl.trn.starr_cell.Model.KonterModel;
import com.rfl.trn.starr_cell.R;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TambahKaryawanActivity extends AppCompatActivity implements BottomSheetDialogFotoKonter.BottomSheetListener {
    private final int CODE_GALLERY = 1;
    private final int CODE_CAMERA = 2;
    private final int ALL_PERMISSION = 999;

    @BindView(R.id.iv_karyawan)
    ImageView ivKonter;
    @BindView(R.id.tv_detailKaryawan)
    MyTextView tvDetailKonter;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.iv_gambarKaryawan)
    ImageView ivGambarKaryawan;
    @BindView(R.id.myet_namaKaryawan)
    MyEditText myetNamaKaryawan;
    @BindView(R.id.myet_noHpKaryawan)
    MyEditText myetNoHpKaryawan;
    @BindView(R.id.radioLakiLaki)
    RadioButton radioLakiLaki;
    @BindView(R.id.radioPerempuan)
    RadioButton radioPerempuan;
    @BindView(R.id.radioGroupJenisKelamin)
    RadioGroup radioGroupJenisKelamin;
    @BindView(R.id.btn_daftar)
    MyTextView btnDaftar;

    private Context context = TambahKaryawanActivity.this;
    private FirebaseAuth firebaseAuth, firebaseAuth2;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Uri downloadURL;
    private Camera camera;
    private RadioButton radioButtonSex;
    private Date date = new Date();
    private Long timestamp = date.getTime();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_karyawan);
        ButterKnife.bind(this);
        context = TambahKaryawanActivity.this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        getSupportActionBar().setSubtitle(R.string.tambah_data_karyawan);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }
    //TODO :: Fetch Data

    //TODO :: Bind(OnCLick dll)
    @OnClick(R.id.iv_gambarKaryawan)
    void ambilGambar() {

        BottomSheetDialogFotoKonter bottomSheetDialogFotoKonter = new BottomSheetDialogFotoKonter();
        bottomSheetDialogFotoKonter.show(getSupportFragmentManager(), "Ambil Foto Konter");

    }

    @OnClick(R.id.btn_daftar)
    void tambahKaryawan() {
        if (cekInput()) {
            try {
                if (ivGambarKaryawan.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.bg_take_pict).getConstantState()) {
                    final SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                    dialog.setTitleText("Peringatan");
                    dialog.setContentText("Foto konter belum di tambahkan.\nApakah tetap ingin menyimpan data tanpa foto ?");
                    dialog.setConfirmText("Iya, Simpan");
                    dialog.setCancelText("Enggak Usah");
                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            simpanKeDatabase();
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

            } catch (NullPointerException e) {
                e.printStackTrace();
                new Bantuan(context).swal_error(e.getMessage());
            }
        } else {
            new Bantuan(context).swal_error("Ada data yang belum diisi !!");
        }
    }

    private void simpanKeDatabase() {
        final SweetAlertDialog loading = new Bantuan(context).swal_loading("Tunggu beberapa saat, proses pendaftaran konter");
        loading.show();
        final String key = databaseReference.push().getKey();
        Bitmap bitmap = ((BitmapDrawable) ivGambarKaryawan.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        final StorageReference ref = storageReference.child("karyawan")
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
                            int selectedId = radioGroupJenisKelamin.getCheckedRadioButtonId();
                            radioButtonSex = (RadioButton) findViewById(selectedId);
                            KaryawanModel model = new KaryawanModel(
                                    key,
                                    radioButtonSex.getText().toString(),
                                    downloadURL.toString(),
                                    "belum aktif",
                                    myetNamaKaryawan.getText().toString(),
                                    Integer.parseInt(myetNoHpKaryawan.getText().toString()),
                                    new Bantuan(context).getDayTimestamp(timestamp));

                            databaseReference.child("karyawan")
                                    .child(key)
                                    .setValue(model)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            loading.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                            loading.showContentText(true);
                                            loading.setTitleText("Sukses");
                                            loading.setContentText("Berhasil Menambahkan Karyawan Baru");
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

    private boolean cekInput() {
        if (TextUtils.isEmpty(myetNamaKaryawan.getText()) ||
                TextUtils.isEmpty(myetNoHpKaryawan.getText())) {
            return false;
        } else {
            return true;
        }
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
                ivGambarKaryawan.setImageURI(hasilCrop);
                Picasso.get()
                        .load(hasilCrop)
                        .into(ivGambarKaryawan);
            } else {
                new Bantuan(context).swal_warning("gambar null hehe");
            }
        }
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void startCrop(Uri uri) {
        String tujuan = String.valueOf(System.currentTimeMillis() % 1000);
        tujuan += ".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), tujuan)));
        uCrop.withAspectRatio(16, 9);
        uCrop.withMaxResultSize(1600, 900);
        uCrop.withOptions(getCropOption());
        uCrop.start(TambahKaryawanActivity.this);
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


    //TODO :: LifeCycle
}






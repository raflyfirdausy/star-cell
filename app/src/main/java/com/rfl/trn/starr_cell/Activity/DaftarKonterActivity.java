package com.rfl.trn.starr_cell.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rfl.trn.starr_cell.Custom.MyEditText;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.R;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;


import java.io.File;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DaftarKonterActivity extends AppCompatActivity {

    @BindView(R.id.iv_konter)
    ImageView ivKonter;
    @BindView(R.id.tv_detailKonter)
    TextView tvDetailKonter;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.iv_gambarKonter)
    ImageView ivGambarKonter;
    @BindView(R.id.myet_namaKonter)
    MyEditText myetNamaKonter;
    @BindView(R.id.myet_alamatKonter)
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
    private final int CODE_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_konter);
        ButterKnife.bind(this);

        //custom font buat type password

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf");
        myetPasswordKonter.setTypeface(typeface);
        myetKonfirmasiPasswordKonter.setTypeface(typeface);
    }

    @OnClick({R.id.iv_gambarKonter, R.id.btn_daftar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_gambarKonter:
                startActivityForResult(new Intent()
                        .setAction(Intent.ACTION_GET_CONTENT)
                        .setType("image/*"), CODE_GALLERY);
                break;
            case R.id.btn_daftar:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == CODE_GALLERY && resultCode == RESULT_OK){
            Uri imageUri = Objects.requireNonNull(data).getData();
            if(imageUri != null){
                startCrop(imageUri);
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK){
            Uri hasilCrop = UCrop.getOutput(Objects.requireNonNull(data));
            if(hasilCrop != null){
                ivGambarKonter.setImageURI(hasilCrop);

                Picasso.with(this)
                        .load(hasilCrop)
                        .into(ivGambarKonter);
            } else {
                new Bantuan(context).swal_warning("gambar null hehe");
            }
        }
    }

    private void startCrop(Uri uri){
        String tujuan = String.valueOf(System.currentTimeMillis() % 1000);
        tujuan += ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), tujuan)));
        uCrop.withAspectRatio(16, 9);
        uCrop.withMaxResultSize(1600, 900);
        uCrop.withOptions(getCropOption());
        uCrop.start(DaftarKonterActivity.this);
    }

    private UCrop.Options getCropOption(){
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
}

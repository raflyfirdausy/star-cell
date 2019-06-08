package com.rfl.trn.starr_cell;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wajahatkarim3.easymoneywidgets.EasyMoneyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PembayaranActivity extends AppCompatActivity {

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.btnLanjut)
    ImageView btnLanjut;
    @BindView(R.id.tvRpBayar)
    EasyMoneyTextView tvRpBayar;
    @BindView(R.id.btnCash)
    CardView btnCash;
    @BindView(R.id.btnTransfer)
    CardView btnTransfer;
    @BindView(R.id.btn7)
    LinearLayout btn7;
    @BindView(R.id.btn8)
    LinearLayout btn8;
    @BindView(R.id.btn9)
    LinearLayout btn9;
    @BindView(R.id.btnC)
    LinearLayout btnC;
    @BindView(R.id.btn4)
    LinearLayout btn4;
    @BindView(R.id.btn5)
    LinearLayout btn5;
    @BindView(R.id.btn6)
    LinearLayout btn6;
    @BindView(R.id.btnHapus)
    LinearLayout btnHapus;
    @BindView(R.id.btn1)
    LinearLayout btn1;
    @BindView(R.id.btn2)
    LinearLayout btn2;
    @BindView(R.id.btn3)
    LinearLayout btn3;
    @BindView(R.id.btnKosong)
    LinearLayout btnKosong;
    @BindView(R.id.btn0)
    LinearLayout btn0;
    @BindView(R.id.btn00)
    LinearLayout btn00;
    @BindView(R.id.btn000)
    LinearLayout btn000;
    @BindView(R.id.btnKosongManing)
    LinearLayout btnKosongManing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);
        ButterKnife.bind(this);
        init();
    }

    @OnClick({R.id.btnBack, R.id.btnLanjut, R.id.btnCash, R.id.btnTransfer, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnC, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btnHapus, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn0, R.id.btn00, R.id.btn000})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                break;
            case R.id.btnLanjut:
                break;
            case R.id.btnCash:
                break;
            case R.id.btnTransfer:
                break;
            case R.id.btn7:
                tulisAngka("7");
                break;
            case R.id.btn8:
                tulisAngka("8");
                break;
            case R.id.btn9:
                tulisAngka("9");
                break;
            case R.id.btnC:
                break;
            case R.id.btn4:
                tulisAngka("4");
                break;
            case R.id.btn5:
                tulisAngka("5");
                break;
            case R.id.btn6:
                tulisAngka("6");
                break;
            case R.id.btnHapus:
                break;
            case R.id.btn1:
                tulisAngka("1");
                break;
            case R.id.btn2:
                tulisAngka("2");
                break;
            case R.id.btn3:
                tulisAngka("3");
                break;
            case R.id.btn0:
                tulisAngka("0");
                break;
            case R.id.btn00:
                tulisAngka("00");
                break;
            case R.id.btn000:
                tulisAngka("000");
                break;
        }
    }

    private void init(){
        tvRpBayar.setText("0");
        setFormatCurrency();
        setWarnaAndTombol();
    }

    @SuppressLint("SetTextI18n")
    private void tulisAngka(String angka){
        tvRpBayar.setText(tvRpBayar.getValueString() + angka);
        setFormatCurrency();
        setWarnaAndTombol();
    }

    private void setWarnaAndTombol(){
        if(Double.parseDouble(tvRpBayar.getValueString()) <= 0){
            tvRpBayar.setTextColor(Color.parseColor("#55595c"));
            btnLanjut.setBackgroundColor(Color.parseColor("#55595c"));
            btnLanjut.setEnabled(false);
        } else {
            tvRpBayar.setTextColor(Color.parseColor("#23A352"));
            btnLanjut.setBackgroundColor(Color.parseColor("#23A352"));
            btnLanjut.setEnabled(true);
        }
    }

    private void setFormatCurrency(){
        tvRpBayar.setCurrency("Rp");
        tvRpBayar.showCurrencySymbol();
        tvRpBayar.showCommas();
    }
}

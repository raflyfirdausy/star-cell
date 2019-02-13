package rfl.trn.com.starr_cell.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rfl.trn.com.starr_cell.Custom.MyEditText;
import rfl.trn.com.starr_cell.Custom.MyTextView;
import rfl.trn.com.starr_cell.R;

public class Login extends AppCompatActivity {
    private Context  context;

    @BindView(R.id.myet_email)
    MyEditText myetEmail;
    @BindView(R.id.myet_password)
    MyEditText myetPassword;
    @BindView(R.id.btn_login)
    MyTextView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        context = Login.this;
    }

    @OnClick(R.id.btn_login)
    void login(){
        startActivity(new Intent(context,Dashboard.class));
    }
}

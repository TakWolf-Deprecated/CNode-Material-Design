package org.cnodejs.android.md.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.listener.NavigationFinishClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_QRCODE = 100;

    @Bind(R.id.login_toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.login_edt_access_token)
    protected MaterialEditText edtAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
    }

    @OnClick(R.id.login_btn_login)
    protected void onBtnLoginClick() {
        if (edtAccessToken.getText().length() < 36) {
            edtAccessToken.setError("令牌格式错误，应为32位UUID（四段分割）字符串");
        } else {
            // TOOD 访问网络
        }
    }

    @OnClick(R.id.login_btn_qrcode)
    protected void onBtnQrcodeClick() {
        startActivityForResult(new Intent(this, QrCodeActivity.class), REQUEST_QRCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_QRCODE && resultCode == RESULT_OK) {
            edtAccessToken.setText(data.getStringExtra("qrcode"));
            edtAccessToken.setSelection(edtAccessToken.getText().length());
        }
    }

}

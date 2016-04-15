package org.cnodejs.android.md.display.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.base.StatusBarActivity;
import org.cnodejs.android.md.display.dialog.DialogUtils;
import org.cnodejs.android.md.display.dialog.ProgressDialog;
import org.cnodejs.android.md.display.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.display.widget.ThemeUtils;
import org.cnodejs.android.md.display.widget.ToastUtils;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.DefaultToastCallback;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.util.FormatUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends StatusBarActivity {

    public static final int REQUEST_LOGIN = R.id.request_login;

    public static void startForResult(@NonNull Activity activity) {
        activity.startActivityForResult(new Intent(activity, LoginActivity.class), REQUEST_LOGIN);
    }

    public static boolean startForResultWithAccessTokenCheck(@NonNull final Activity activity) {
        if (TextUtils.isEmpty(LoginShared.getAccessToken(activity))) {
            DialogUtils.createAlertDialogBuilder(activity)
                    .setMessage(R.string.need_login_tip)
                    .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startForResult(activity);
                        }

                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
            return false;
        } else {
            return true;
        }
    }

    @Bind(R.id.login_toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.login_edt_access_token)
    protected MaterialEditText edtAccessToken;

    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        progressDialog = DialogUtils.createProgressDialog(this);
        progressDialog.setMessage(R.string.logging_in_$_);
    }

    @OnClick(R.id.login_btn_login)
    protected void onBtnLoginClick() {
        final String accessToken = edtAccessToken.getText().toString().trim();
        if (!FormatUtils.isAccessToken(accessToken)) {
            edtAccessToken.setError(getString(R.string.access_token_format_error));
            edtAccessToken.requestFocus();
        } else {
            final Call<Result.Login> call = ApiClient.service.accessToken(accessToken);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    call.cancel();
                }

            });
            progressDialog.show();
            call.enqueue(new DefaultToastCallback<Result.Login>(this) {

                @Override
                public boolean onResultOk(Response<Result.Login> response, Result.Login loginInfo) {
                    LoginShared.login(LoginActivity.this, accessToken, loginInfo);
                    ToastUtils.with(LoginActivity.this).show(R.string.login_success);
                    setResult(RESULT_OK);
                    finish();
                    return false;
                }

                @Override
                public boolean onResultErrorAuth(Response<Result.Login> response, Result.Error error) {
                    edtAccessToken.setError(getString(R.string.access_token_auth_error));
                    edtAccessToken.requestFocus();
                    return false;
                }

                @Override
                public void onFinish() {
                    progressDialog.setOnCancelListener(null);
                    progressDialog.dismiss();
                }

            });
        }
    }

    @OnClick(R.id.login_btn_qrcode)
    protected void onBtnQrcodeClick() {
        QRCodeActivity.startForResult(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QRCodeActivity.REQUEST_QRCODE && resultCode == RESULT_OK) {
            edtAccessToken.setText(data.getStringExtra(QRCodeActivity.EXTRA_QRCODE));
            edtAccessToken.setSelection(edtAccessToken.length());
            onBtnLoginClick();
        }
    }

}

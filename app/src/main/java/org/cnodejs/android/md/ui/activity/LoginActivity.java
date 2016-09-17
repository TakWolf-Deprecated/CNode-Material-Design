package org.cnodejs.android.md.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.presenter.contract.ILoginPresenter;
import org.cnodejs.android.md.presenter.implement.LoginPresenter;
import org.cnodejs.android.md.ui.base.FullLayoutActivity;
import org.cnodejs.android.md.ui.dialog.AlertDialogUtils;
import org.cnodejs.android.md.ui.dialog.ProgressDialog;
import org.cnodejs.android.md.ui.listener.DialogCancelCallListener;
import org.cnodejs.android.md.ui.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.ui.util.ThemeUtils;
import org.cnodejs.android.md.ui.util.ToastUtils;
import org.cnodejs.android.md.ui.view.ILoginView;
import org.cnodejs.android.md.util.FormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class LoginActivity extends FullLayoutActivity implements ILoginView {

    public static final int REQUEST_LOGIN = FormatUtils.generateRequestCode();

    public static void startForResult(@NonNull Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, LoginActivity.class), requestCode);
    }

    public static void startForResult(@NonNull Activity activity) {
        startForResult(activity, REQUEST_LOGIN);
    }

    public static boolean startForResultWithLoginCheck(@NonNull final Activity activity, final int requestCode) {
        if (TextUtils.isEmpty(LoginShared.getAccessToken(activity))) {
            AlertDialogUtils.createBuilderWithAutoTheme(activity)
                    .setMessage(R.string.need_login_tip)
                    .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startForResult(activity, requestCode);
                        }

                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
            return false;
        } else {
            return true;
        }
    }

    public static boolean startForResultWithLoginCheck(@NonNull Activity activity) {
        return startForResultWithLoginCheck(activity, REQUEST_LOGIN);
    }

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.edt_access_token)
    protected MaterialEditText edtAccessToken;

    private ProgressDialog progressDialog;

    private ILoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight_FitsStatusBar, R.style.AppThemeDark_FitsStatusBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        progressDialog = ProgressDialog.createWithAutoTheme(this);
        progressDialog.setMessage(R.string.logging_in_$_);

        loginPresenter = new LoginPresenter(this, this);
    }

    @OnClick(R.id.btn_login)
    protected void onBtnLoginClick() {
        loginPresenter.loginAsyncTask(edtAccessToken.getText().toString().trim());
    }

    @OnClick(R.id.btn_qrcode)
    protected void onBtnQrcodeClick() {
        QRCodeActivity.startForResultWithPermissionCheck(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == QRCodeActivity.PERMISSIONS_REQUEST_QRCODE) {
            QRCodeActivity.startForResultWithPermissionHandle(this);
        }
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

    @OnClick(R.id.btn_login_tip)
    protected void onBtnLoginTipClick() {
        AlertDialogUtils.createBuilderWithAutoTheme(this)
                .setMessage(R.string.how_to_get_access_token_tip_content)
                .setPositiveButton(R.string.confirm, null)
                .show();
    }

    @Override
    public void onAccessTokenError(@NonNull String message) {
        edtAccessToken.setError(message);
        edtAccessToken.requestFocus();
    }

    @Override
    public void onLoginOk(@NonNull String accessToken, @NonNull Result.Login loginInfo) {
        LoginShared.login(this, accessToken, loginInfo);
        ToastUtils.with(this).show(R.string.login_success);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onLoginStart(@NonNull Call<Result.Login> call) {
        progressDialog.setOnCancelListener(new DialogCancelCallListener(call));
        progressDialog.show();
    }

    @Override
    public void onLoginFinish() {
        progressDialog.setOnCancelListener(null);
        progressDialog.dismiss();
    }

}

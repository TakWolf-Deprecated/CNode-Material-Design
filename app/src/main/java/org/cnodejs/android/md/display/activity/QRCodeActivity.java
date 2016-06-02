package org.cnodejs.android.md.display.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.base.StatusBarActivity;
import org.cnodejs.android.md.display.dialog.AlertDialogUtils;
import org.cnodejs.android.md.display.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.util.FormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QRCodeActivity extends StatusBarActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private static final String[] PERMISSIONS = {Manifest.permission.CAMERA};
    public static final int PERMISSIONS_REQUEST_QRCODE = FormatUtils.createRequestCode();
    public static final int REQUEST_QRCODE = FormatUtils.createRequestCode();
    public static final String EXTRA_QRCODE = "qrcode";

    public static void startForResultWithPermissionCheck(@NonNull final Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                AlertDialogUtils.createBuilderWithAutoTheme(activity)
                        .setMessage(R.string.qrcode_request_permission_rationale_tip)
                        .setPositiveButton(R.string.open_permissions_request, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSIONS_REQUEST_QRCODE);
                            }

                        })
                        .show();
            } else {
                ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSIONS_REQUEST_QRCODE);
            }
        } else {
            startForResult(activity);
        }
    }

    public static void startForResultWithPermissionHandle(@NonNull final Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            AlertDialogUtils.createBuilderWithAutoTheme(activity)
                    .setMessage(R.string.qrcode_permission_denied_tip)
                    .setPositiveButton(R.string.confirm, null)
                    .setNeutralButton(R.string.go_to_setting, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", activity.getPackageName(), null)));
                        }

                    })
                    .show();
        } else {
            startForResult(activity);
        }
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    private static void startForResult(@NonNull Activity activity) {
        activity.startActivityForResult(new Intent(activity, QRCodeActivity.class), REQUEST_QRCODE);
    }

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.qr_view)
    protected QRCodeReaderView qrCodeReaderView;

    @BindView(R.id.icon_line)
    protected View iconLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        qrCodeReaderView.setOnQRCodeReadListener(this);

        iconLine.startAnimation(AnimationUtils.loadAnimation(this, R.anim.qrcode_line_anim));
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.getCameraManager().startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.getCameraManager().stopPreview();
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_QRCODE, text);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void cameraNotFound() {
        AlertDialogUtils.createBuilderWithAutoTheme(this)
                .setMessage(R.string.can_not_open_camera)
                .setPositiveButton(R.string.confirm, null)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }

                })
                .show();
    }

    @Override
    public void QRCodeNotFoundOnCamImage() {}

}

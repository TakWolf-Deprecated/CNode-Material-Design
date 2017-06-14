package org.cnodejs.android.md.ui.activity;

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
import org.cnodejs.android.md.ui.base.StatusBarActivity;
import org.cnodejs.android.md.ui.dialog.AlertDialogUtils;
import org.cnodejs.android.md.ui.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.util.FormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanQRCodeActivity extends StatusBarActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private static final String[] PERMISSIONS = {Manifest.permission.CAMERA};
    public static final int PERMISSIONS_REQUEST_DEFAULT = FormatUtils.generateRequestCode();
    public static final String EXTRA_QR_CODE = "qrCode";

    public static void startForResultWithPermissionCheck(@NonNull final Activity activity, int requestCode) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                AlertDialogUtils.createBuilderWithAutoTheme(activity)
                        .setMessage(R.string.qr_code_request_permission_rationale_tip)
                        .setPositiveButton(R.string.open_permissions_request, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSIONS_REQUEST_DEFAULT);
                            }

                        })
                        .show();
            } else {
                ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSIONS_REQUEST_DEFAULT);
            }
        } else {
            startForResult(activity, requestCode);
        }
    }

    public static void startForResultWithPermissionHandle(@NonNull final Activity activity, int requestCode) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            AlertDialogUtils.createBuilderWithAutoTheme(activity)
                    .setMessage(R.string.qr_code_permission_denied_tip)
                    .setPositiveButton(R.string.confirm, null)
                    .setNeutralButton(R.string.go_to_setting, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", activity.getPackageName(), null)));
                        }

                    })
                    .show();
        } else {
            startForResult(activity, requestCode);
        }
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    private static void startForResult(@NonNull Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, ScanQRCodeActivity.class), requestCode);
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.qr_view)
    QRCodeReaderView qrCodeReaderView;

    @BindView(R.id.icon_line)
    View iconLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        qrCodeReaderView.setOnQRCodeReadListener(this);

        iconLine.startAnimation(AnimationUtils.loadAnimation(this, R.anim.qr_code_scan_line));
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        qrCodeReaderView.stopCamera();
        super.onPause();
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_QR_CODE, text);
        setResult(RESULT_OK, intent);
        finish();
    }

}

package org.cnodejs.android.md.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.ui.listener.NavigationFinishClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QrCodeActivity extends BaseActivity implements QRCodeReaderView.OnQRCodeReadListener {

    @Bind(R.id.qrcode_toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.qrcode_qr_view)
    protected QRCodeReaderView qrCodeReaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        qrCodeReaderView.setOnQRCodeReadListener(this);
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
        intent.putExtra("qrcode", text);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void cameraNotFound() {
        new MaterialDialog.Builder(this)
                .content(R.string.can_not_open_camera)
                .positiveText(R.string.confirm)
                .cancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }

                })
                .show();
    }

    @Override
    public void QRCodeNotFoundOnCamImage() {
    }

}

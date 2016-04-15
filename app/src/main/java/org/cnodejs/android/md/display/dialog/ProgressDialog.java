package org.cnodejs.android.md.display.dialog;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.cnodejs.android.md.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProgressDialog extends AppCompatDialog {

    public ProgressDialog(Context context) {
        super(context);
        init();
    }

    public ProgressDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    protected ProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    @Bind(R.id.dialog_progress_tv_message)
    protected TextView tvMessage;

    private void init() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);
        ButterKnife.bind(this);
    }

    public void setMessage(CharSequence text) {
        tvMessage.setText(text);
        tvMessage.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    public void setMessage(@StringRes int resId) {
        tvMessage.setText(resId);
        tvMessage.setVisibility(resId == 0 ? View.GONE : View.VISIBLE);
    }

}

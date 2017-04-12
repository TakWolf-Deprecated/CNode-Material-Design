package org.cnodejs.android.md.ui.dialog;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.view.Window;

import com.pnikosis.materialishprogress.ProgressWheel;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.storage.SettingShared;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgressDialog extends AppCompatDialog {

    public static ProgressDialog createWithAutoTheme(@NonNull Activity activity) {
        return new ProgressDialog(activity, SettingShared.isEnableThemeDark(activity) ? R.style.AppDialogDark : R.style.AppDialogLight);
    }

    @BindView(R.id.progress_wheel)
    protected ProgressWheel progressWheel;

    private ProgressDialog(@NonNull Activity activity, int theme) {
        super(activity, theme);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressWheel.spin();
    }

    @Override
    protected void onStop() {
        super.onStop();
        progressWheel.stopSpinning();
    }

}

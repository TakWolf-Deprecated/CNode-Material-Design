package org.cnodejs.android.md.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.ui.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.ui.util.Navigator;
import org.cnodejs.android.md.ui.util.ThemeUtils;
import org.joda.time.DateTime;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CrashLogActivity extends StatusBarActivity implements Toolbar.OnMenuItemClickListener {

    private static final String EXTRA_E = "e";

    public static void start(@NonNull Context context, @NonNull Throwable e) {
        Intent intent = new Intent(context, CrashLogActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_E, e);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_info)
    TextView tvInfo;

    private String crashLog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_log);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.inflateMenu(R.menu.crash_log);
        toolbar.setOnMenuItemClickListener(this);

        Throwable e = (Throwable) getIntent().getSerializableExtra(EXTRA_E);

        crashLog = "生产厂商：\n" +
                Build.MANUFACTURER + "\n\n" +
                "手机型号：\n" +
                Build.MODEL + "\n\n" +
                "系统版本：\n" +
                Build.VERSION.RELEASE + "\n\n" +
                "异常时间：\n" +
                new DateTime() + "\n\n" +
                "异常类型：\n" +
                e.getClass().getName() + "\n\n" +
                "异常信息：\n" +
                e.getMessage() + "\n\n" +
                "异常堆栈：\n" +
                Log.getStackTraceString(e);

        tvInfo.setText(crashLog);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                Navigator.openEmail(
                        this,
                        "takwolf@foxmail.com",
                        "来自 CNodeMD-" + AboutActivity.VERSION_TEXT + " 的客户端崩溃日志",
                        crashLog
                );
                return true;
            default:
                return false;
        }
    }

}

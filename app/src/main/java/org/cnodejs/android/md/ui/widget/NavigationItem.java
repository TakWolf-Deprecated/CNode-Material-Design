package org.cnodejs.android.md.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.util.ResUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigationItem extends FrameLayout implements Checkable {

    @BindView(R.id.icon_item)
    View iconItem;

    @BindView(R.id.img_icon)
    ImageView imgIcon;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_badge)
    TextView tvBadge;

    private Drawable iconNormal;
    private Drawable iconChecked;
    private boolean checked;

    public NavigationItem(@NonNull Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public NavigationItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public NavigationItem(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NavigationItem(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        LayoutInflater.from(context).inflate(R.layout.widget_navigation_item, this, true);
        ButterKnife.bind(this, this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NavigationItem, defStyleAttr, defStyleRes);

        iconNormal = a.getDrawable(R.styleable.NavigationItem_iconNormal);
        iconChecked = a.getDrawable(R.styleable.NavigationItem_iconChecked);
        checked = a.getBoolean(R.styleable.NavigationItem_android_checked, false);

        imgIcon.setImageDrawable(checked ? iconChecked : iconNormal);
        tvTitle.setTextColor(ResUtils.getThemeAttrColor(context, checked ? R.attr.colorAccent : android.R.attr.textColorPrimary));
        iconItem.setBackgroundColor(checked ? ResUtils.getThemeAttrColor(context, R.attr.widgetBackgroundDark) : Color.TRANSPARENT);

        tvTitle.setText(a.getText(R.styleable.NavigationItem_title));
        setBadge(a.getInt(R.styleable.NavigationItem_badge, 0));

        a.recycle();
    }

    @Override
    public void setChecked(boolean checked) {
        if (this.checked != checked) {
            this.checked = checked;
            imgIcon.setImageDrawable(checked ? iconChecked : iconNormal);
            tvTitle.setTextColor(ResUtils.getThemeAttrColor(getContext(), checked ? R.attr.colorAccent : android.R.attr.textColorPrimary));
            iconItem.setBackgroundColor(checked ? ResUtils.getThemeAttrColor(getContext(), R.attr.widgetBackgroundDark) : Color.TRANSPARENT);
        }
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        setChecked(!checked);
    }

    public void setBadge(int count) {
        if (count > 99) {
            tvBadge.setText(R.string.navigation_badge_count_max);
        } else if (count <= 0) {
            tvBadge.setText(null);
        } else {
            tvBadge.setText(String.valueOf(count));
        }
        tvBadge.setVisibility(count > 0 ? VISIBLE : GONE);
    }

}

package org.cnodejs.android.md.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.databinding.WidgetNavigationItemBinding;

public class NavigationItem extends FrameLayout implements Checkable {
    private WidgetNavigationItemBinding binding;

    private ColorStateList contentColorNormal;
    private ColorStateList contentColorChecked;
    @ColorInt
    private int backgroundColorChecked;

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

    public NavigationItem(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        binding = WidgetNavigationItemBinding.inflate(LayoutInflater.from(getContext()), this, true);

        TypedArray a = context.obtainStyledAttributes(new int[] {
                android.R.attr.textColorSecondary,
                android.R.attr.colorAccent,
        });
        contentColorNormal = a.getColorStateList(0);
        contentColorChecked = a.getColorStateList(1);
        backgroundColorChecked = contentColorNormal.withAlpha(24).getDefaultColor();
        a.recycle();

        a = context.obtainStyledAttributes(attrs, R.styleable.NavigationItem, defStyleAttr, defStyleRes);
        setIconDrawable(a.getDrawable(R.styleable.NavigationItem_app_icon));
        setTitle(a.getText(R.styleable.NavigationItem_app_title));
        setBadge(a.getInt(R.styleable.NavigationItem_app_badge, 0));
        checked = a.getBoolean(R.styleable.NavigationItem_android_checked, false);
        a.recycle();

        updateViews();
    }

    private void updateViews() {
        if (checked) {
            binding.imgIcon.setImageTintList(contentColorChecked);
            binding.tvTitle.setTextColor(contentColorChecked);
            binding.tvBadge.setTextColor(contentColorChecked);
            binding.getRoot().setBackgroundColor(backgroundColorChecked);
        } else {
            binding.imgIcon.setImageTintList(contentColorNormal);
            binding.tvTitle.setTextColor(contentColorNormal);
            binding.tvBadge.setTextColor(contentColorNormal);
            binding.getRoot().setBackground(null);
        }
    }

    public void setIconDrawable(@Nullable Drawable drawable) {
        binding.imgIcon.setImageDrawable(drawable);
    }

    public void setTitle(CharSequence text) {
        binding.tvTitle.setText(text);
    }

    public void setBadge(int count) {
        if (count > 0) {
            binding.tvBadge.setVisibility(View.VISIBLE);
            if (count > 99) {
                binding.tvBadge.setText(R.string.navigation_badge_count_max);
            } else {
                binding.tvBadge.setText(String.valueOf(count));
            }
        } else {
            binding.tvBadge.setVisibility(GONE);
        }
    }

    @Override
    public void setChecked(boolean checked) {
        if (this.checked != checked) {
            this.checked = checked;
            updateViews();
        }
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }
}

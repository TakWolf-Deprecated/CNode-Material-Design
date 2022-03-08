package org.cnodejs.android.md.ui.widget;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.webkit.WebView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StyleRes;

public class StableWebView extends WebView {
    public StableWebView(@NonNull Context context) {
        super(context);
    }

    public StableWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StableWebView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StableWebView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        saveState(savedState.webState);
        savedState.url = getUrl();
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        restoreState(savedState.webState);
        loadUrl(savedState.url);
        super.onRestoreInstanceState(savedState.getSuperState());
    }

    private static class SavedState extends BaseSavedState {
        Bundle webState;
        String url;

        public SavedState(Parcelable superState) {
            super(superState);
            webState = new Bundle();
        }

        public SavedState(Parcel source) {
            super(source);
            readValues(source, null);
        }

        @RequiresApi(Build.VERSION_CODES.N)
        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            readValues(source, loader);
        }

        private void readValues(@NonNull Parcel source, @Nullable ClassLoader loader) {
            webState = source.readBundle(loader);
            url = source.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBundle(webState);
            out.writeString(url);
        }

        public static final Creator<SavedState> CREATOR = new ClassLoaderCreator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source, ClassLoader loader) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    return new SavedState(source, loader);
                } else {
                    return new SavedState(source);
                }
            }

            @Override
            public SavedState createFromParcel(Parcel source) {
                return createFromParcel(source, null);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}

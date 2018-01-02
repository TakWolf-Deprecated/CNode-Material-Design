package org.cnodejs.android.md.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.glide.GlideApp;
import org.cnodejs.android.md.ui.base.StatusBarActivity;
import org.cnodejs.android.md.ui.listener.NavigationFinishClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImagePreviewActivity extends StatusBarActivity {

    private static final String EXTRA_IMAGE_URL = "imageUrl";

    public static void start(@NonNull Context context, String imageUrl) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_IMAGE_URL, imageUrl);
        context.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.photo_view)
    PhotoView photoView;

    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        loadImageAsyncTask();
    }

    private void loadImageAsyncTask() {
        progressWheel.spin();
        GlideApp.with(this).load(getIntent().getStringExtra(EXTRA_IMAGE_URL)).error(R.drawable.image_error).dontAnimate().listener(new RequestListener<Drawable>() {

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressWheel.stopSpinning();
                return false;
            }

            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressWheel.stopSpinning();
                return false;
            }

        }).into(photoView);
    }

}

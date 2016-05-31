package org.cnodejs.android.md.display.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.base.StatusBarActivity;
import org.cnodejs.android.md.display.listener.NavigationFinishClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

public class ImagePreviewActivity extends StatusBarActivity {

    private static final String EXTRA_IMAGE_URL = "imageUrl";

    public static void start(@NonNull Context context, String imageUrl) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_IMAGE_URL, imageUrl);
        context.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.photo_view)
    protected PhotoView photoView;

    @BindView(R.id.progress_wheel)
    protected ProgressWheel progressWheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        loadImageAsyncTask();
    }

    private void loadImageAsyncTask() {
        progressWheel.spin();
        Glide.with(this).load(getIntent().getStringExtra(EXTRA_IMAGE_URL)).error(R.drawable.image_error).dontAnimate().listener(new RequestListener<String, GlideDrawable>() {

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progressWheel.stopSpinning();
                return false;
            }

            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                progressWheel.stopSpinning();
                return false;
            }

        }).into(photoView);
    }

}

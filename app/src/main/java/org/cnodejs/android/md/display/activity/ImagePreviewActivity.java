package org.cnodejs.android.md.display.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.base.StatusBarActivity;
import org.cnodejs.android.md.display.listener.NavigationFinishClickListener;

import butterknife.Bind;
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

    @Bind(R.id.image_preview_toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.image_preview_photo_view)
    protected PhotoView photoView;

    @Bind(R.id.image_preview_progress_wheel)
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
        Glide.with(this).load(getIntent().getStringExtra(EXTRA_IMAGE_URL)).error(R.drawable.image_error).into(new ImageViewTarget<GlideDrawable>(photoView) {

            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                progressWheel.spin();
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                progressWheel.stopSpinning();
            }

            @Override
            protected void setResource(GlideDrawable resource) {
                getView().setImageDrawable(resource);
                progressWheel.stopSpinning();
            }

        });
    }

}

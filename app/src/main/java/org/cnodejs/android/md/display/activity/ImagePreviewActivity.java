package org.cnodejs.android.md.display.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;

import com.squareup.picasso.Picasso;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.base.StatusBarActivity;
import org.cnodejs.android.md.display.listener.NavigationFinishClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

public class ImagePreviewActivity extends StatusBarActivity {

    private static final String EXTRA_IMAGE_URL = "imageUrl";

    public static void start(@NonNull Activity activity, String imageUrl) {
        Intent intent = new Intent(activity, ImagePreviewActivity.class);
        intent.putExtra(EXTRA_IMAGE_URL, imageUrl);
        activity.startActivity(intent);
    }

    @Bind(R.id.image_preview_toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.image_preview_photo_view)
    protected PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        Picasso.with(this).load(getIntent().getStringExtra(EXTRA_IMAGE_URL)).placeholder(R.drawable.image_placeholder).into(photoView);
    }

}

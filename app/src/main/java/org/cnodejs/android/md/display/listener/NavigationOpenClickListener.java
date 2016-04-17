package org.cnodejs.android.md.display.listener;

import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

public class NavigationOpenClickListener implements View.OnClickListener {

    private final DrawerLayout drawerLayout;

    public NavigationOpenClickListener(@NonNull DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    @Override
    public void onClick(View v) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

}

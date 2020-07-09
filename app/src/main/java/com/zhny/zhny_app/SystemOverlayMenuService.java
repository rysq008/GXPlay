package com.zhny.zhny_app;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class SystemOverlayMenuService extends FragmentActivity {

    private FloatingActionButton rightLowerButton;
    private FloatingActionButton topCenterButton;

    private FloatingActionMenu rightLowerMenu;
    private FloatingActionMenu topCenterMenu;

    private boolean serviceWillBeDismissed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceWillBeDismissed = false;

        // Set up the white button on the lower right corner
        // more or less with default parameter
        ImageView fabIconNew = new ImageView(this);
        fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_circle));
        FloatingActionButton.LayoutParams params = new FloatingActionButton.LayoutParams(-2,-2);

        rightLowerButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIconNew)
                .setLayoutParams(params)
                .build();

        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
        ImageView rlIcon1 = new ImageView(this);
        ImageView rlIcon2 = new ImageView(this);
        ImageView rlIcon3 = new ImageView(this);
        ImageView rlIcon4 = new ImageView(this);

        rlIcon1.setImageDrawable(getResources().getDrawable(R.drawable.chart_bar_line_bar_img_average_bg));
        rlIcon2.setImageDrawable(getResources().getDrawable(R.drawable.icon_car));
        rlIcon3.setImageDrawable(getResources().getDrawable(R.drawable.icon_multi_point_plot));
        rlIcon4.setImageDrawable(getResources().getDrawable(R.drawable.icon_zoom));

        // Build the menu with default options: light theme, 90 degrees, 72dp radius.
        // Set 4 default SubActionButtons
        SubActionButton rlSub1 = rLSubBuilder.setContentView(rlIcon1).build();
        SubActionButton rlSub2 = rLSubBuilder.setContentView(rlIcon2).build();
        SubActionButton rlSub3 = rLSubBuilder.setContentView(rlIcon3).build();
        SubActionButton rlSub4 = rLSubBuilder.setContentView(rlIcon4).build();
        rightLowerMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(rlSub1, rlSub1.getLayoutParams().width, rlSub1.getLayoutParams().height)
                .addSubActionView(rlSub2, rlSub2.getLayoutParams().width, rlSub2.getLayoutParams().height)
                .addSubActionView(rlSub3, rlSub3.getLayoutParams().width, rlSub3.getLayoutParams().height)
                .addSubActionView(rlSub4, rlSub4.getLayoutParams().width, rlSub4.getLayoutParams().height)
                .setStartAngle(180)
                .setEndAngle(270)
                .attachTo(rightLowerButton)
                .build();

        ////////////////////////////////////////////////////////

        // Set up the large red button on the top center side
        // With custom button and content sizes and margins
        int redActionButtonSize = getResources().getDimensionPixelSize(R.dimen.dp_108);
        int redActionButtonMargin = getResources().getDimensionPixelOffset(R.dimen.dp_12);
        int redActionButtonContentSize = getResources().getDimensionPixelSize(R.dimen.dp_16);
        int redActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.dp_36);
        int redActionMenuRadius = getResources().getDimensionPixelSize(R.dimen.dp_96);
        int blueSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.dp_48);
        int blueSubActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.dp_16);

        ImageView fabIconStar = new ImageView(this);
        fabIconStar.setImageDrawable(getResources().getDrawable(R.drawable.icon_multi_point_plot));

        FloatingActionButton.LayoutParams fabIconStarParams = new FloatingActionButton.LayoutParams(redActionButtonContentSize, redActionButtonContentSize);
        fabIconStarParams.setMargins(redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin);

        FloatingActionButton.LayoutParams params2 = new FloatingActionButton.LayoutParams(-2,-2);
        params2.width = redActionButtonSize;
        params2.height = redActionButtonSize;

        topCenterButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIconStar, fabIconStarParams)
                .setBackgroundDrawable(R.drawable.selector_fence_draw_bg)
                .setPosition(FloatingActionButton.POSITION_TOP_CENTER)
                .setLayoutParams(params2)
                .build();

        // Set up customized SubActionButtons for the right center menu
        SubActionButton.Builder tCSubBuilder = new SubActionButton.Builder(this);
        tCSubBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_monitor_select_bg));

        SubActionButton.Builder tCRedBuilder = new SubActionButton.Builder(this);
        tCRedBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_fence_draw_bg));

        FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        blueContentParams.setMargins(blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin);

        // Set custom layout params
        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
        tCSubBuilder.setLayoutParams(blueParams);
        tCRedBuilder.setLayoutParams(blueParams);

        ImageView tcIcon1 = new ImageView(this);
        ImageView tcIcon2 = new ImageView(this);
        ImageView tcIcon3 = new ImageView(this);
        ImageView tcIcon4 = new ImageView(this);
        ImageView tcIcon5 = new ImageView(this);
        ImageView tcIcon6 = new ImageView(this);

        tcIcon1.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_circle));
        tcIcon2.setImageDrawable(getResources().getDrawable(R.drawable.icon_agricultural_machinery));
        tcIcon3.setImageDrawable(getResources().getDrawable(R.drawable.icon_agricultural_machinery_default));
        tcIcon4.setImageDrawable(getResources().getDrawable(R.drawable.icon_agricultural_machinery_location));
        tcIcon5.setImageDrawable(getResources().getDrawable(R.drawable.icon_agricultural_machinery_offline));
        tcIcon6.setImageDrawable(getResources().getDrawable(R.drawable.icon_agricultural_machinery_online));

        SubActionButton tcSub1 = tCSubBuilder.setContentView(tcIcon1, blueContentParams).build();
        SubActionButton tcSub2 = tCSubBuilder.setContentView(tcIcon2, blueContentParams).build();
        SubActionButton tcSub3 = tCSubBuilder.setContentView(tcIcon3, blueContentParams).build();
        SubActionButton tcSub4 = tCSubBuilder.setContentView(tcIcon4, blueContentParams).build();
        SubActionButton tcSub5 = tCSubBuilder.setContentView(tcIcon5, blueContentParams).build();
        SubActionButton tcSub6 = tCRedBuilder.setContentView(tcIcon6, blueContentParams).build();


        // Build another menu with custom options
        topCenterMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(tcSub1, tcSub1.getLayoutParams().width, tcSub1.getLayoutParams().height)
                .addSubActionView(tcSub2, tcSub2.getLayoutParams().width, tcSub2.getLayoutParams().height)
                .addSubActionView(tcSub3, tcSub3.getLayoutParams().width, tcSub3.getLayoutParams().height)
                .addSubActionView(tcSub4, tcSub4.getLayoutParams().width, tcSub4.getLayoutParams().height)
                .addSubActionView(tcSub5, tcSub5.getLayoutParams().width, tcSub5.getLayoutParams().height)
                .addSubActionView(tcSub6, tcSub6.getLayoutParams().width, tcSub6.getLayoutParams().height)
                .setRadius(redActionMenuRadius)
                .setStartAngle(0)
                .setEndAngle(180)
                .attachTo(topCenterButton)
                .build();

        topCenterMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {

            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                if(serviceWillBeDismissed) {
                    serviceWillBeDismissed = false;
                }
            }
        });

        // make the red button terminate the service
        tcSub6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceWillBeDismissed = true; // the order is important
                topCenterMenu.close(true);
            }
        });
    }

    @Override
    public void onDestroy() {
        if(rightLowerMenu != null && rightLowerMenu.isOpen()) rightLowerMenu.close(false);
        if(topCenterMenu != null && topCenterMenu.isOpen()) topCenterMenu.close(false);
        if(rightLowerButton != null) rightLowerButton.detach();
        if(topCenterButton != null) topCenterButton.detach();

        super.onDestroy();
    }
}

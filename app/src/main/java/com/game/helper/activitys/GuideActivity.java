package com.game.helper.activitys;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.jude.swipbackhelper.SwipeBackHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.router.Router;

public class GuideActivity extends XBaseActivity implements OnPageChangeListener {

    @BindView(R.id.viewpager)
    ViewPager vp;
    private PagerAdapter vpAdapter;
    private List<View> views;

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        for (View v : views) {
            BitmapDrawable bd = (BitmapDrawable) v.getBackground();
            if (null != bd) {
                bd.setCallback(null);
                bd.getBitmap().recycle();
            }
            v.setBackgroundResource(0);
        }
    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 3) {
            views.get(position).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Router.newIntent(GuideActivity.this).to(MainActivity.class).launch();
                    GuideActivity.this.finish();
                }
            });
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        views = new ArrayList<View>();
        int[] images = {R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3, R.mipmap.guide4};
        for (int i = 0; i < images.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setScaleType(ScaleType.FIT_XY);
            iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), images[i]);
            BitmapDrawable bd = new BitmapDrawable(getResources(), bmp);
            iv.setBackgroundDrawable(bd);
            views.add(iv);
        }

        // 初始化Adapter
        vpAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup view, int position) {
                view.addView(views.get(position));
                return views.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position));
            }
        };

        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.setOnPageChangeListener(this);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public Object newP() {
        return null;
    }
}

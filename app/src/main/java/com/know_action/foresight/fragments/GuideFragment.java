package com.know_action.foresight.fragments;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.know_action.foresight.R;
import com.know_action.foresight.fragments.BaseFragment.XBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GuideFragment extends XBaseFragment implements ViewPager.OnPageChangeListener {

    @BindView(R.id.guide_viewpager)
    ViewPager vp;
    @BindView(R.id.guide_rg)
    RadioGroup radioGroup;
    private PagerAdapter vpAdapter;
    private List<View> views;

    public static GuideFragment newInstance() {
        return new GuideFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        radioGroup.check(radioGroup.getChildAt(position).getId());
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        views = new ArrayList<View>();
        int[] imgs = {R.mipmap.heart, R.mipmap.record, R.mipmap.mark};
        for (int i = 0; i < 3; i++) {
//            ImageView iv_body = guide_v.findViewById(R.id.guide_iv);
//            TextView tv_enter = guide_v.findViewById(R.id.guide_enter_tv);
//            iv_body.setImageResource(imgs[i]);
//            tv_enter.setVisibility(i == 2 ? View.VISIBLE : View.GONE);
//            View.OnClickListener clickListener = v -> {
//                DetailFragmentsActivity.launch(context,null,LoginFragment.newInstance());
//                context.finish();
//            };
//            tv_skip.setOnClickListener(clickListener);
//            tv_enter.setOnClickListener(clickListener);
//            views.add(guide_v);
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
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_guide;
    }

    @Override
    public Object newP() {
        return null;
    }
}

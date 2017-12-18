package com.game.helper.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.fragments.RangeFriendListFragment;
import com.game.helper.fragments.RangeIncomeListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RankingListActivity extends XBaseActivity implements View.OnClickListener{

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.superStarIv)
    ImageView superStarIv;
    @BindView(R.id.renmaiIv)
    ImageView renmaiIv;
    @BindView(R.id.incomeIv)
    ImageView incomeIv;

    List<Fragment> fragments = new ArrayList<>();
    private MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mHeadTittle.setText("排行榜");
        initListeners();
        init();

    }

    private void init() {
        fragments.add(RangeFriendListFragment.newInstance());
        fragments.add(RangeFriendListFragment.newInstance());
        fragments.add(RangeIncomeListFragment.newInstance());

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
    }

    private void initListeners() {

        mHeadBack.setOnClickListener(this);
        superStarIv.setOnClickListener(this);
        renmaiIv.setOnClickListener(this);
        incomeIv.setOnClickListener(this);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        superStarIv.setBackgroundResource(R.mipmap.bg_mingxing_select);
                        renmaiIv.setBackgroundResource(R.mipmap.bg_renmai_unselect);
                        incomeIv.setBackgroundResource(R.mipmap.bg_income_unselect);
                        break;
                    case 1:
                        superStarIv.setBackgroundResource(R.mipmap.bg_mingxing_unselect);
                        renmaiIv.setBackgroundResource(R.mipmap.bg_renmai_select);
                        incomeIv.setBackgroundResource(R.mipmap.bg_income_unselect);
                        break;
                    case 2:
                        superStarIv.setBackgroundResource(R.mipmap.bg_mingxing_unselect);
                        renmaiIv.setBackgroundResource(R.mipmap.bg_renmai_unselect);
                        incomeIv.setBackgroundResource(R.mipmap.bg_income_select);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_ranking_list;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.action_bar_back:
                onBackPressed();
                break;
            case R.id.superStarIv:
                vp.setCurrentItem(0);
                break;
            case R.id.renmaiIv:
                vp.setCurrentItem(1);
                break;
            case R.id.incomeIv:
                vp.setCurrentItem(2);
                break;
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);

        }
    }
}

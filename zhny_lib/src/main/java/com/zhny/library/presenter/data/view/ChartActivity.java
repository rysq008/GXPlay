package com.zhny.library.presenter.data.view;

import android.os.Bundle;
import android.view.View;

import com.zhny.library.R;
import com.zhny.library.databinding.ActivityChartBinding;
import com.zhny.library.presenter.data.fragment.BarLineFragment;
import com.zhny.library.presenter.data.fragment.TableDataFragment;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class ChartActivity extends BaseChartActivity implements View.OnClickListener {

    private ActivityChartBinding binding;
    private BarLineFragment barLineFragment = new BarLineFragment();
    private TableDataFragment tableDataFragment = new TableDataFragment();
    private boolean isBarLineView = true;

    @Override
    public void initParams(Bundle params) {

    }

    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chart);
        binding.rlBarLine.setOnClickListener(this);
        binding.rlTableData.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void initBusiness() {
        binding.setLifecycleOwner(this);
        setToolBarTitle(getString(R.string.chart_title));
    }

    @Override
    protected void onStart() {
        super.onStart();
        selectTable(isBarLineView);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }

    @Override
    public void onClick(View v) {
        if (R.id.rl_bar_line == v.getId()) {
            selectTable(true);
        } else if (R.id.rl_table_data == v.getId()) {
            selectTable(false);
        }
    }

    //选择table
    private void selectTable(boolean isBarLineView) {
        this.isBarLineView = isBarLineView;
        binding.setShowBarLine(isBarLineView);
        if (isBarLineView) {
            setFragment(barLineFragment);
        } else {
            setFragment(tableDataFragment);
        }
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_chart, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

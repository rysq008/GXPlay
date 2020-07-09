package com.zhny.library.presenter.playback.view;

import android.os.Bundle;

import com.blankj.utilcode.util.SPUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhny.library.R;
import com.zhny.library.base.BaseActivity;
import com.zhny.library.common.Constant;
import com.zhny.library.databinding.ActivityPlayBackListBinding;
import com.zhny.library.presenter.playback.adapter.PlaybackListAdapter;
import com.zhny.library.presenter.playback.listener.OnItemClickListener;
import com.zhny.library.presenter.playback.model.PlaybackInfoDto;
import com.zhny.library.presenter.playback.viewmodel.PlayBackListViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 回放列表
 */
public class PlayBackListActivity extends BaseActivity implements OnItemClickListener, OnRefreshListener {

    public static final String MACHINE_NAME = "machine_name";
    public static final String MACHINE_SN = "machine_sn";

    private PlayBackListViewModel viewModel;
    private ActivityPlayBackListBinding binding;
    private PlaybackListAdapter adapter;
    private List<PlaybackInfoDto> data = new ArrayList<>();

    @Override
    public void initParams(Bundle params) {

    }

    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(PlayBackListViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_play_back_list);
        return binding.getRoot();
    }

    @Override
    public void initBusiness() {
        viewModel.setParams(this);

        binding.setLifecycleOwner(this);
        setToolBarTitle(getString(R.string.playback_title));

        adapter = new PlaybackListAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.playbackRecyclerView.setLayoutManager(gridLayoutManager);
        binding.playbackRecyclerView.setAdapter(adapter);

        binding.smoothRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        requestData(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (data.size() == 0) {
            requestData(false);
        }
    }

    private void requestData(boolean isDropDown) {
        if (!isDropDown) {
            //开始加载
            showLoading();
        }
        viewModel.getDevices(SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, ""),
                SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.USER_ID, ""))
                .observe(this, baseDto -> {
                            if (baseDto.getContent() != null && baseDto.getContent().size() > 0) {
                                binding.setHasData(true);
                                data.clear();
                                data.addAll(baseDto.getContent());
                                adapter.updateData(data);
                            } else {
                                binding.setHasData(false);
                            }
                            if (isDropDown) {
                                binding.smoothRefreshLayout.finishRefresh();
                            } else {
                                //关闭加载
                                dismissLoading();
                            }
                        }
                );
    }


    @Override
    protected boolean isShowBacking() {
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        data.clear();
        if (binding != null) {
            binding.unbind();
        }
    }

    @Override
    public void onItemClick(Object dto) {
        Bundle bundle = new Bundle();
        bundle.putString(MACHINE_NAME, ((PlaybackInfoDto) dto).name);
        bundle.putString(MACHINE_SN, ((PlaybackInfoDto) dto).sn);

        startActivity(PlayBackMonthDetailActivity.class, bundle);
    }


}

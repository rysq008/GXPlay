package com.zhny.library.presenter.playback.view;

import android.os.Bundle;

import com.blankj.utilcode.util.SPUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhny.library.R;
import com.zhny.library.base.BaseActivity;
import com.zhny.library.common.Constant;
import com.zhny.library.databinding.ActivityPlayBackMonthDetailBinding;
import com.zhny.library.presenter.playback.PlayBackConstants;
import com.zhny.library.presenter.playback.adapter.PlaybackMonthListAdapter;
import com.zhny.library.presenter.playback.listener.OnItemClickListener;
import com.zhny.library.presenter.playback.model.PlaybackMonthDetailDto;
import com.zhny.library.presenter.playback.model.WorkDate;
import com.zhny.library.presenter.playback.viewmodel.PlayBackMonthDetailViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class PlayBackMonthDetailActivity extends BaseActivity implements OnItemClickListener, OnRefreshListener {

    public static final String MACHINE_NAME = "machine_name";
    public static final String MACHINE_SN = "machine_sn";

    private PlayBackMonthDetailViewModel viewModel;
    private ActivityPlayBackMonthDetailBinding binding;
    private PlaybackMonthListAdapter adapter;
    private String name;
    private String sn;
    private List<WorkDate> data = new ArrayList<>();


    @Override
    public void initParams(Bundle params) {
        if (params != null) {
            name = params.getString(MACHINE_NAME);
            sn = params.getString(MACHINE_SN);
            params.clear();
        }
    }

    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(PlayBackMonthDetailViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_play_back_month_detail);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void initBusiness() {
        viewModel.setParams(this);
        binding.setLifecycleOwner(this);
        setToolBarTitle(name);

        adapter = new PlaybackMonthListAdapter(this, this);
        binding.expandLv.setAdapter(adapter);
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
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "");
        viewModel.getWorkDate(organizationId, sn)
                .observe(this, baseDto -> {
                            if (isDropDown) {
                                binding.smoothRefreshLayout.finishRefresh();
                            } else {
                                //关闭加载
                                dismissLoading();
                            }

                            if (baseDto.getContent() != null && baseDto.getContent().size() > 0) {
                                viewModel.emptyData.setValue(false);
                                data.clear();
                                data.addAll(baseDto.getContent());
                                updateView();
                            } else {
                                viewModel.emptyData.setValue(true);
                            }
                        }
                );
    }


    private void updateView() {
        viewModel.emptyData.setValue(false);
        //月份数组
        List<String> mGroupList = new ArrayList<>();
        //明细数组
        ArrayList<ArrayList<PlaybackMonthDetailDto>> mItemSet = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            ArrayList<PlaybackMonthDetailDto> itemList = new ArrayList<>();
            WorkDate workDate = data.get(i);
            mGroupList.add(workDate.date);
            for (int j = 0; j < workDate.content.size(); j++) {
                PlaybackMonthDetailDto playbackMonthDetailDto = new PlaybackMonthDetailDto();
                String month = workDate.date.substring(5, 7);
                String day = workDate.content.get(j).day;
                playbackMonthDetailDto.month = month;
                playbackMonthDetailDto.date = month + "." + day;
                playbackMonthDetailDto.workDetail = workDate.content.get(j).detail.get(0);
                playbackMonthDetailDto.startDate = workDate.date + "-" + day + " 00:00:00";
                playbackMonthDetailDto.endDate = workDate.date + "-" + day + " 23:59:59";
                itemList.add(playbackMonthDetailDto);
            }
            mItemSet.add(itemList);
        }
        adapter.updateData(mGroupList, mItemSet);
        binding.expandLv.expandGroup(0);
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
        if (dto instanceof PlaybackMonthDetailDto) {
            Bundle bundle = new Bundle();
            bundle.putString(PlayBackConstants.PLAY_BACK_MOVE_AN, name);
            bundle.putString(PlayBackConstants.PLAY_BACK_MOVE_NAME, sn);
            bundle.putString(PlayBackConstants.PLAY_BACK_MOVE_START_DATE, ((PlaybackMonthDetailDto) dto).startDate);
            bundle.putString(PlayBackConstants.PLAY_BACK_MOVE_END_DATE, ((PlaybackMonthDetailDto) dto).endDate);
            startActivity(PlayBackMoveActivity.class, bundle);
        }
    }

}

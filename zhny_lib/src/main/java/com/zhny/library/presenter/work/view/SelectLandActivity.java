package com.zhny.library.presenter.work.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.TileOverlay;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhny.library.R;
import com.zhny.library.base.BaseActivity;
import com.zhny.library.databinding.ActivitySelectLandBinding;
import com.zhny.library.presenter.monitor.model.dto.SelectFarmDto;
import com.zhny.library.presenter.work.WorkConstants;
import com.zhny.library.presenter.work.adapter.FarmAdapter;
import com.zhny.library.presenter.work.listener.SelectPlotListener;
import com.zhny.library.presenter.work.util.CharacterParserUtil;
import com.zhny.library.presenter.work.viewmodel.SelectLandViewModel;
import com.zhny.library.utils.MapUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class SelectLandActivity extends BaseActivity implements SelectPlotListener, TextWatcher, OnRefreshListener {


    private ActivitySelectLandBinding binding;
    private SelectLandViewModel viewModel;

    List<SelectFarmDto> dataList = new ArrayList<>();

    private TileOverlay tileOverlay;


    private MapView mapView;
    private AMap aMap;
    private FarmAdapter farmAdapter;
    private CharacterParserUtil characterParser;

    @Override
    public void initParams(Bundle params) {

    }

    @Override
    protected boolean isShowBacking() {
        return true;
    }

    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(SelectLandViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_land);
        mapView = binding.landMapView;
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        return binding.getRoot();
    }

    @Override
    public void initBusiness() {
        viewModel.setParams(this);
        setToolBarTitle(getString(R.string.select_land));
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        if (tileOverlay != null) {
            tileOverlay.remove();
        }

        tileOverlay = MapUtils.addRemoteOverlay(aMap);
        characterParser = CharacterParserUtil.getInstance();

        initAdapter();

    }

    private void initAdapter() {
        farmAdapter = new FarmAdapter(this, this, aMap.getProjection());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.farmList.setLayoutManager(manager);
        binding.farmList.setAdapter(farmAdapter);

        binding.smoothRefreshLayout.setOnRefreshListener(this);

        binding.etSelectLand.addTextChangedListener(this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        binding.etSelectLand.setText("");
        viewModel.setIsShowClear(false);
        requestData(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (dataList.size() == 0) {
            requestData(false);
        }
    }


    private void requestData(boolean isDropDown) {
        //请求农场数据
        if (!isDropDown) {
            //开始加载
            showLoading();
        }
        viewModel.getFarmData().observe(this, baseDto -> {
            if (baseDto.getContent() != null && baseDto.getContent().size() > 0) {
                dataList.clear();
                dataList.addAll(baseDto.getContent());
                farmAdapter.refreshData(dataList, false);
            }
            if (isDropDown) {
                binding.smoothRefreshLayout.finishRefresh();
            }else {
                //关闭加载
                dismissLoading();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataList.clear();
        if (mapView != null) mapView.onDestroy();
        if (binding != null) binding.unbind();
    }


    @Override
    public void onPlotSelected(SelectFarmDto.SelectPlotDto selectPlotDto) {
        Bundle bundle = new Bundle();
        bundle.putString(WorkConstants.BUNDLE_FIELD_CODE, selectPlotDto.fieldCode);
        bundle.putString(WorkConstants.BUNDLE_FIELD_NAME, selectPlotDto.fieldName);
        bundle.putString(WorkConstants.BUNDLE_FIELD_CENTER, selectPlotDto.center);
        bundle.putString(WorkConstants.BUNDLE_FIELD_COORDINATES, selectPlotDto.coordinates);
        startActivity(SelectWorkActivity.class, bundle);
    }

    //点击右侧清空
    public void clearLandSearch(View view) {
        binding.etSelectLand.setText("");
        viewModel.setIsShowClear(false);
        //设置元=原数据
        if (dataList.size() > 0) {
            farmAdapter.refreshData(dataList, false);
        }
    }

    //查询并重设数据
    private void searchItem(String key) {
        if (dataList.size() == 0 || TextUtils.isEmpty(key)) {
            return;
        }
        viewModel.setIsShowClear(true);

        List<SelectFarmDto> result = new ArrayList<>();
        Single.just(dataList).doOnSubscribe(disposable -> {
            for (SelectFarmDto dto : dataList) {
                if (dto.fieldList == null || dto.fieldList.size() == 0) continue;
                List<SelectFarmDto.SelectPlotDto> resultPlots = new ArrayList<>();
                for (SelectFarmDto.SelectPlotDto plotDto : dto.fieldList) {
                    if (!TextUtils.isEmpty(plotDto.fieldName) && checkPlot(key, plotDto.fieldName)) {
                        resultPlots.add(plotDto);
                    }
                }
                if (resultPlots.size() > 0) {
                    result.add(new SelectFarmDto(dto.farmName, resultPlots));
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doAfterSuccess(selectFarmDtos ->
                farmAdapter.refreshData(result, true))
                .subscribe();
    }

    //校验
    private boolean checkPlot(String key, String fieldName) {
        return fieldName.toUpperCase().contains(key.toUpperCase()) ||
                characterParser.getSelling(fieldName).toUpperCase().startsWith(key.toUpperCase());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (start == 0 && count == 0 && before > start) {
            viewModel.setIsShowClear(false);
            //设置元=原数据
            if (dataList.size() > 0) {
                farmAdapter.refreshData(dataList, false);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        searchItem(s.toString());
    }


}

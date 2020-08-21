package com.ikats.shop.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;

import com.ikats.shop.App;
import com.ikats.shop.R;
import com.ikats.shop.adapters.SearchItemAdapter;
import com.ikats.shop.database.OrderTableEntiry;
import com.ikats.shop.dialog.CommonDialogFragment;
import com.ikats.shop.dialog.DialogFragmentHelper;
import com.ikats.shop.fragments.BaseFragment.XBaseFragment;
import com.ikats.shop.model.BaseModel.HttpResultModel;
import com.ikats.shop.model.SearchItemBean;
import com.ikats.shop.utils.RxLoadingUtils;
import com.ikats.shop.views.XReloadableListContentLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.objectbox.Box;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

public class SearchFragment extends XBaseFragment {

    @BindView(R.id.search_enter_et)
    EditText search_et;
    @BindView(R.id.search_reload_list_layout)
    XReloadableListContentLayout xReloadableListContentLayout;
    SearchItemAdapter searchItemAdapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        xReloadableListContentLayout.getRecyclerView().verticalLayoutManager(context);
        searchItemAdapter = new SearchItemAdapter(context);
        searchItemAdapter.setRecItemClick(new RecyclerItemCallback<SearchItemBean, SearchItemAdapter.ViewHolder>() {
            @Override
            public void onItemClick(int position, SearchItemBean model, int tag, SearchItemAdapter.ViewHolder holder) {
                super.onItemClick(position, model, tag, holder);
                DialogFragmentHelper.builder(new CommonDialogFragment.OnCallDialog() {
                    @Override
                    public Dialog getDialog(Context context) {
                        return new AlertDialog.Builder(context).setMessage("点你妹！有没有数据心里没点逼数！")
                                .setPositiveButton("确定", (dialog, which) -> {
                                    dialog.dismiss();
                                }).create();
                    }
                }, true).show(getChildFragmentManager(), "");
            }
        });
        xReloadableListContentLayout.setInterceptTouch(true);
        xReloadableListContentLayout.getRecyclerView().setAdapter(searchItemAdapter);
        xReloadableListContentLayout.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                xReloadableListContentLayout.postDelayed(() -> {
                    xReloadableListContentLayout.refreshState(false);
                }, 1000);
            }

            @Override
            public void onLoadMore(int page) {

            }
        });
        Flowable<HttpResultModel<List<SearchItemBean>>> flowable = Flowable.create(emitter -> {
            List<SearchItemBean> beanList = new ArrayList<>();
            Box<OrderTableEntiry> box = App.getBoxStore().boxFor(OrderTableEntiry.class);
            /*List<OrderTableEntiry> entiryList = */
            box.query().filter(entity -> entity._orderId<=50/*!Kits.Empty.check(entity.outBizNo) && !"null".equals(entity.outBizNo)*/).build().forEach(entiry -> {
                SearchItemBean bean = new SearchItemBean();
                bean.sort = String.valueOf(entiry._orderId);
                bean.order = entiry.outBizNo;
                bean.count = String.valueOf(entiry.count);
                bean.amount = entiry.amount;
                bean.person = entiry.purchaser;
                bean.phone = entiry.phone;
                bean.status = entiry.status;
                bean.createtime = entiry.startTime;
                bean.action = entiry.action;
                beanList.add(bean);
            });
            HttpResultModel resultModel = new HttpResultModel();
            resultModel.resultData = beanList;
            resultModel.resultCode = 1;
            emitter.onNext(resultModel);
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
        RxLoadingUtils.subscribeWithDialog(context, flowable, bindToLifecycle(), listHttpResultModel -> searchItemAdapter.setData(listHttpResultModel.resultData), false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_layout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }


}

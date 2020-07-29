package com.ikats.shop.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;

import com.ikats.shop.R;
import com.ikats.shop.adapters.SearchItemAdapter;
import com.ikats.shop.dialog.CommonDialogFragment;
import com.ikats.shop.dialog.DialogFragmentHelper;
import com.ikats.shop.fragments.BaseFragment.XBaseFragment;
import com.ikats.shop.model.SearchItemBean;
import com.ikats.shop.views.XReloadableListContentLayout;

import butterknife.BindView;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerView;

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
        for (int i = 0; i < 10; i++) {
            SearchItemBean bean = new SearchItemBean();
            bean.sort = String.valueOf(i);
            bean.order = "E32343566677889";
            bean.count = String.valueOf(i);
            bean.amount = String.valueOf(500);
            bean.person = "余与";
            bean.phone = "13400000000";
            bean.status = "放行";
            bean.createtime = "2020-07-22 15:00";
            bean.action = "核放";
            searchItemAdapter.addElement(bean);
        }
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

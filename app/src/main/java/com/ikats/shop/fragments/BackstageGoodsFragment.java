package com.ikats.shop.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ikats.shop.App;
import com.ikats.shop.R;
import com.ikats.shop.adapters.BackstageGoodsItemAdapter;
import com.ikats.shop.database.OrderTableEntiry;
import com.ikats.shop.dialog.CommonDialogFragment;
import com.ikats.shop.dialog.DialogFragmentHelper;
import com.ikats.shop.fragments.BaseFragment.XBaseFragment;
import com.ikats.shop.model.BackstageGoodsItemBean;
import com.ikats.shop.model.BaseModel.HttpResultModel;
import com.ikats.shop.net.DataService;
import com.ikats.shop.net.api.ApiService;
import com.ikats.shop.utils.RxLoadingUtils;
import com.ikats.shop.views.XReloadableListContentLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.objectbox.Box;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class BackstageGoodsFragment extends XBaseFragment {

    @BindView(R.id.backstage_goods_search_enter_et)
    EditText search_et;
    @BindView(R.id.backstage_goods_search_commit_btn)
    Button search_btn;
    @BindView(R.id.backstage_goods_search_page_tips_tv)
    TextView page_tip_tv;
    @BindView(R.id.backstage_goods_search_prepage_tv)
    TextView prepage_tv;
    @BindView(R.id.backstage_goods_search_nextpage_tv)
    TextView nextpage_tv;
    @BindView(R.id.backstage_goods_search_reload_list_layout)
    XReloadableListContentLayout xReloadableListContentLayout;
    BackstageGoodsItemAdapter backstageGoodsItemAdapter;
    List<BackstageGoodsItemBean> beanList = new ArrayList<>();
    int cur_page, total_page;

    @Override
    public void initData(Bundle savedInstanceState) {
        xReloadableListContentLayout.getRecyclerView().verticalLayoutManager(context);
        backstageGoodsItemAdapter = new BackstageGoodsItemAdapter(context);
        backstageGoodsItemAdapter.setRecItemClick(new RecyclerItemCallback<BackstageGoodsItemBean, BackstageGoodsItemAdapter.ViewHolder>() {
            @Override
            public void onItemClick(int position, BackstageGoodsItemBean model, int tag, BackstageGoodsItemAdapter.ViewHolder holder) {
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
        xReloadableListContentLayout.getRecyclerView().setAdapter(backstageGoodsItemAdapter);
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
        Flowable<HttpResultModel<List<BackstageGoodsItemBean>>> flowable = Flowable.create(emitter -> {
            Box<OrderTableEntiry> box = App.getBoxStore().boxFor(OrderTableEntiry.class);
            /*List<OrderTableEntiry> entiryList = */
            box.query().filter(entity -> entity._id <= 50/*!Kits.Empty.check(entity.outBizNo) && !"null".equals(entity.outBizNo)*/).build().forEach(entiry -> {
                BackstageGoodsItemBean bean = new BackstageGoodsItemBean();
                bean.index = (int) entiry._id;
                bean.name = "清风纸巾";
                bean.barcode = String.valueOf(309891123);
                bean.code = "309891123";
                bean.unit = "包";
                bean.price = "15.6";
                bean.place = "韩国";
                bean.brank = "清风";
                bean.spec = "300ML";
                bean.rough_weight = "0.34";
                bean.net_weight = "0.34";
                beanList.add(bean);
            });
            HttpResultModel resultModel = new HttpResultModel();
            resultModel.resultData = beanList;
            resultModel.resultCode = 1;
            emitter.onNext(resultModel);
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
        RxLoadingUtils.subscribeWithDialog(context, flowable, bindToLifecycle(), listHttpResultModel -> {
            cur_page = 0;
            total_page = (beanList.size() % 10 == 0 ? beanList.size() / 10 : beanList.size() / 10 + 1);
            page_tip_tv.setText(String.format("%d页/%d", cur_page + 1, total_page));
            int next = (cur_page * 10 + 10) > beanList.size() ? beanList.size() - 0 : cur_page * 10 + 10;
            backstageGoodsItemAdapter.setData(beanList.subList(cur_page * 10, next));
        }, false);
    }

    @OnClick({R.id.backstage_goods_search_prepage_tv, R.id.backstage_goods_search_nextpage_tv, R.id.backstage_goods_search_commit_btn})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.backstage_goods_search_prepage_tv:
                cur_page = (--cur_page < 0 ? 0 : cur_page);
                page_tip_tv.setText(String.format("%d页/%d", cur_page + 1, total_page));
                int next = (cur_page * 10 + 10) > beanList.size() ? beanList.size() - 0 : cur_page * 10 + 10;
                backstageGoodsItemAdapter.setData(beanList.subList(cur_page * 10, next));
                break;
            case R.id.backstage_goods_search_nextpage_tv:
                cur_page = (++cur_page < total_page ? cur_page : total_page - 1);
                page_tip_tv.setText(String.format("%d页/%d", cur_page + 1, total_page));
                int next1 = (cur_page * 10 + 10) > beanList.size() ? beanList.size() - 0 : cur_page * 10 + 10;
                backstageGoodsItemAdapter.setData(beanList.subList(cur_page * 10, next1));
                break;
            case R.id.backstage_goods_search_commit_btn:
                break;
        }
    }

    private void getData(String key, String begin, String end, int page) {
        Flowable<HttpResultModel> search = DataService.builder().buildReqUrl("")
                .buildReqParams("", key).buildReqParams("", begin).buildReqParams("", end)
                .buildReqParams("", page).request(ApiService.HttpMethod.POST);
        RxLoadingUtils.subscribeWithDialog(context, search, bindToLifecycle(), new Consumer<HttpResultModel>() {
            @Override
            public void accept(HttpResultModel httpResultModel) throws Exception {

            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_backstage_goods_layout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static BackstageGoodsFragment newInstance() {
        return new BackstageGoodsFragment();
    }


}

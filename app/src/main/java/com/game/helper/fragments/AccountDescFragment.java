package com.game.helper.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.event.BusProvider;
import com.game.helper.event.MsgEvent;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.GameAccountResultModel;
import com.game.helper.model.MineGameDesclistResults;
import com.game.helper.net.DataService;
import com.game.helper.net.api.Api;
import com.game.helper.net.model.BindOrUnBindVipRequestBody;
import com.game.helper.net.model.SingleGameIdRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.Utils;
import com.game.helper.views.GXPlayDialog;
import com.game.helper.views.XReloadableRecyclerContentLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountDescFragment extends XBaseFragment implements View.OnClickListener {
    public static final String TAG = AccountDescFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;

    @BindView(R.id.iv_avatara)
    ImageView avatar;
    @BindView(R.id.iv_vip_levela)
    ImageView vip;
    @BindView(R.id.tv_namea)
    TextView name;
    @BindView(R.id.tv_platforma)
    TextView platform;
    @BindView(R.id.tv_identya)
    TextView identy;
    @BindView(R.id.tv_timea)
    TextView time;
    @BindView(R.id.tv_value_lefta)
    TextView payValue;
    @BindView(R.id.tv_value_righta)
    TextView freeValue;
    @BindView(R.id.iv_bind)
    ImageView bindIv;

    @BindView(R.id.rc_lista)
    XReloadableRecyclerContentLayout mContent;

    private GameDescAdapter adapter;
    private GameAccountResultModel.ListBean game;
    private List<MineGameDesclistResults.MineGameDesclistItem> data = new ArrayList<>();
    private GXPlayDialog dialog;

    public static AccountDescFragment newInstance() {
        return new AccountDescFragment();
    }

    public AccountDescFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_account_desc;
    }

    private void initView() {
        mHeadTittle.setText(getResources().getString(R.string.common_account_desc));
        mHeadBack.setOnClickListener(this);

        if (getArguments() == null) {
            Toast.makeText(getContext(), "数据异常！请重试", Toast.LENGTH_SHORT).show();
            return;
        }

        game = (GameAccountResultModel.ListBean) getArguments().getSerializable(TAG);
        ILFactory.getLoader().loadNet(avatar, Api.API_PAY_OR_IMAGE_URL + game.getGame_logo(), ILoader.Options.defaultOptions());
        name.setText(game.getGame_name());
        platform.setText(game.getGame_channel_name());
        identy.setText(game.getGame_account());
        time.setText(game.getCreate_time().split(" ")[0]);
        payValue.setText(game.getTotal_recharge());
        freeValue.setText(game.getTotal_save());
        bindIv.setImageResource(game.isIs_vip() ? R.mipmap.btn_unbangding : R.mipmap.btn_bangding);
        bindIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVipBindDialog(game.isIs_vip());
            }
        });
        if (game.getVip_level() == 0) {
            vip.setVisibility(View.GONE);
        } else {
            vip.setImageResource(Utils.getExtensionVipIcon(game.getVip_level()));
        }

        mContent.showLoading();
        initList();
    }

    /**
     * type
     * true：解綁VIP帳號
     * false：綁定VIP帳號
     */
    private void showVipBindDialog(boolean isVip) {
        String content = "", title = "";
        if (isVip) {
            title = "解绑VIP的提示窗";
            content = "一个月只能操作一次解绑行为，请谨慎操作！您确定解绑该账号的VIP权限吗？";
        } else {
            title = "绑定VIP的提示框";
            content = "您将占用一个VIP账号名额，确定此操作吗？";
        }
        dialog = null;
        dialog = new GXPlayDialog(GXPlayDialog.Ddialog_With_All_Full_Confirm, title, content);
        dialog.addOnDialogActionListner(new GXPlayDialog.onDialogActionListner() {
            @Override
            public void onCancel() {
                dialog.dismiss();
            }

            @Override
            public void onConfirm() {
                dialog.dismiss();
                Flowable<HttpResultModel<Map<String, Integer>>> flowable = DataService.getApiBindOrUnBindVip(new BindOrUnBindVipRequestBody(game.getId(), !game.isIs_vip()));
                RxLoadingUtils.subscribeWithDialog(context, flowable, AccountDescFragment.this.bindToLifecycle(), new Consumer<HttpResultModel<java.util.Map<String, Integer>>>() {
                    @Override
                    public void accept(HttpResultModel<Map<String, Integer>> httpResultModel) throws Exception {
                        if (httpResultModel.isSucceful()) {
                            game.setIs_vip(!game.isIs_vip());
                            game.setVip_level(httpResultModel.data.get("vip_level"));
                            BusProvider.getBus().post(new MsgEvent<GameAccountResultModel.ListBean>(game));
                            Toast.makeText(context, !game.isIs_vip() ? "解绑成功" : "绑定成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, game.isIs_vip() ? "解绑失败" : "绑定失败", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(context, httpResultModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        bindIv.setImageResource(game.isIs_vip() ? R.mipmap.btn_unbangding : R.mipmap.btn_bangding);
                        if (!game.isIs_vip()) {
                            vip.setVisibility(View.GONE);
                        } else {
                            vip.setVisibility(View.VISIBLE);
                            vip.setImageResource(Utils.getExtensionVipIcon(game.getVip_level()));
                        }
                    }
                });
            }
        });
        dialog.show(getChildFragmentManager(), GXPlayDialog.TAG);
    }

    private void initList() {
        adapter = null;
        adapter = new GameDescAdapter();
        mContent.getLoadingView().setVisibility(View.GONE);
        mContent.getRecyclerView().setHasFixedSize(true);
        mContent.getRecyclerView().verticalLayoutManager(context);
        mContent.getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        mContent.getRecyclerView().setAdapter(adapter);
        mContent.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                getMineGameDescList(game.getId(), 1);
            }

            @Override
            public void onLoadMore(int page) {
                getMineGameDescList(game.getId(), page);
            }
        });
        mContent.getRecyclerView().useDefLoadMoreView();
    }

    @Override
    public void onResume() {
        super.onResume();
        getMineGameDescList(game.getId(), 1);
    }

    private void getMineGameDescList(int gameid, final int page) {
        Flowable<HttpResultModel<MineGameDesclistResults>> fr = DataService.getMineGameDescList(new SingleGameIdRequestBody(gameid));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<MineGameDesclistResults>>() {
            @Override
            public void accept(HttpResultModel<MineGameDesclistResults> mineGameDesclistResultsHttpResultModel) throws Exception {
                if (mineGameDesclistResultsHttpResultModel.isSucceful()) {
                    if (page == 1) {
                        data.clear();
                    }
                    data.addAll(mineGameDesclistResultsHttpResultModel.data.list);
                    notifyData();
                    mContent.getRecyclerView().setPage(mineGameDesclistResultsHttpResultModel.current_page, mineGameDesclistResultsHttpResultModel.total_page);
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
                Toast.makeText(getContext(), netError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void notifyData() {
        adapter.notifyDataSetChanged();
        mContent.getLoadingView().setVisibility(View.GONE);
        mContent.refreshState(false);
        if (adapter.getItemCount() < 1) {
            mContent.showEmpty();
            return;
        } else {
            mContent.showContent();
        }
    }

    public void showError(NetError error) {
        mContent.getLoadingView().setVisibility(View.GONE);
        mContent.refreshState(false);
        mContent.showError();
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public Object newP() {
        return null;
    }

    class GameDescAdapter extends RecyclerView.Adapter<GameDescHolder> {

        @Override
        public GameDescHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new GameDescHolder(inflater.inflate(R.layout.item_game_desc, parent, false));
        }

        @Override
        public void onBindViewHolder(GameDescHolder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    class GameDescHolder extends RecyclerView.ViewHolder {
        public TextView time;
        public TextView value;
        public TextView disccount;
        public TextView pay;
        public View rootView;

        public GameDescHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            time = itemView.findViewById(R.id.tv_item_time);
            value = itemView.findViewById(R.id.tv_item_value);
            disccount = itemView.findViewById(R.id.tv_item_discount);
            pay = itemView.findViewById(R.id.tv_item_pay);
        }

        void onBind(int position) {
            MineGameDesclistResults.MineGameDesclistItem item = data.get(position);
            if (item == null) return;
            time.setText(item.create_time.split(" ")[0]);
            value.setText(item.amount);
            disccount.setText(Utils.m2(item.discount) + "");
            pay.setText(item.consume_amount);
        }
    }
}

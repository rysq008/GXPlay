package com.game.helper.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.GameAccountResultModel;
import com.game.helper.model.MineGameDesclistResults;
import com.game.helper.model.NotConcernResults;
import com.game.helper.net.DataService;
import com.game.helper.net.api.Api;
import com.game.helper.net.model.SingleGameIdRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountDescFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = AccountDescFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_setting)
    View mHeadShare;
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
    @BindView(R.id.rc_lista)
    RecyclerView list;
    @BindView(R.id.tv_value_lefta)
    TextView payValue;
    @BindView(R.id.tv_value_righta)
    TextView freeValue;

    private GameDescAdapter adapter;
    private GameAccountResultModel.ListBean game;
    private List<MineGameDesclistResults.MineGameDesclistItem> data = new ArrayList<>();

    public static AccountDescFragment newInstance(){
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

    private void initView(){
        mHeadTittle.setText(getResources().getString(R.string.common_account_desc));
        mHeadShare.setVisibility(View.VISIBLE);
        mHeadBack.setOnClickListener(this);
        mHeadShare.setOnClickListener(this);

        if (getArguments() == null){
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
        if (game.getVip_level() == 0){
            vip.setVisibility(View.GONE);
        }else {
            vip.setImageResource(Utils.getExtensionVipIcon(game.getVip_level()));
        }

        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setItemAnimator(new DefaultItemAnimator());
        adapter = new GameDescAdapter();
        list.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getMineGameDescList(game.getId());
    }

    private void getMineGameDescList(int id) {
        Flowable<HttpResultModel<MineGameDesclistResults>> fr = DataService.getMineGameDescList(new SingleGameIdRequestBody(id));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<MineGameDesclistResults>>() {
            @Override
            public void accept(HttpResultModel<MineGameDesclistResults> mineGameDesclistResultsHttpResultModel) throws Exception {
                if (mineGameDesclistResultsHttpResultModel.isSucceful()) {
                    data = mineGameDesclistResultsHttpResultModel.data.list;
                    adapter.notifyDataSetChanged();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Toast.makeText(getContext(), netError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == mHeadBack){
            getActivity().onBackPressed();
        }
        if (v == mHeadShare){
            Toast.makeText(getContext(), "share", Toast.LENGTH_SHORT).show();
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
            return new GameDescHolder(inflater.inflate(R.layout.item_game_desc,parent,false));
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

        void onBind(int position){
            MineGameDesclistResults.MineGameDesclistItem item = data.get(position);
            if (item== null) return;
            time.setText(item.create_time.split(" ")[0]);
            value.setText(item.consume_amount);
            disccount.setText(Utils.m2(item.discount)+"");
            pay.setText(item.consume_amount);
        }
    }
}
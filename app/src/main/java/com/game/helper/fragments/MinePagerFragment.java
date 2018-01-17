package com.game.helper.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.activitys.HuanxinKefuLoginActivity;
import com.game.helper.activitys.MainActivity;
import com.game.helper.data.RxConstant;
import com.game.helper.event.BusProvider;
import com.game.helper.event.MsgEvent;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.fragments.coupon.CouponFragment;
import com.game.helper.fragments.login.LoginFragment;
import com.game.helper.fragments.login.RegistFragment;
import com.game.helper.fragments.recharge.RechargeFragment;
import com.game.helper.fragments.wallet.WalletFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.BaseModel.XBaseModel;
import com.game.helper.model.LoginUserInfo;
import com.game.helper.model.MemberInfoResults;
import com.game.helper.net.DataService;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.utils.StringUtils;
import com.game.helper.utils.Utils;
import com.game.helper.views.HeadImageView;

import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class MinePagerFragment extends XBaseFragment implements View.OnClickListener {
    private static final String TAG = MinePagerFragment.class.getSimpleName();
    //view
    @BindView(R.id.layout_login)
    View mLoginView;
    @BindView(R.id.layout_unlogin)
    View mUnLoginView;
    //login
    @BindView(R.id.iv_messagea)
    ImageView mMessage;
    @BindView(R.id.tv_name)
    TextView mName;
    @BindView(R.id.ll_vip)
    View mVip;
    @BindView(R.id.tv_vip_level)
    TextView mVipLevel;
    @BindView(R.id.iv_vip_level)
    ImageView mVipIcon;
    @BindView(R.id.tv_money)
    TextView mMoney;
    @BindView(R.id.ll_wallet)
    View mWallet;
    @BindView(R.id.ll_mine_game)
    View mMineGame;
    @BindView(R.id.ll_mine_gift)
    View mMineGift;
    @BindView(R.id.ll_mine_order)
    View mMineOrder;
    @BindView(R.id.ll_mine_vip)
    View mMineVip;
    @BindView(R.id.rc_mine_list)
    RecyclerView mSettingList;
    @BindView(R.id.ll_edit_user_info)
    LinearLayout mEditUserInfo;
    @BindView(R.id.iv_avatar)
    HeadImageView mAvatar;
    //unlogin
    @BindView(R.id.tv_regist)
    TextView mRegist;
    @BindView(R.id.tv_login)
    TextView mLogin;
    @BindView(R.id.tv_recharge)
    TextView mRecharge;
    @BindView(R.id.mine_coupon_count_tv)
    TextView couponTv;

    //args
    private SettingListAdapter mAdapter;
    private int[] resIcon;
    private int[] resName;

    private MemberInfoResults userInfo;
    private boolean enable_click = false;//防止多重点击
    private int couponCount = 0;

    public static MinePagerFragment newInstance() {
        return new MinePagerFragment();
    }

    public MinePagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        onResume();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }


    @Override
    public void onResume() {
        super.onResume();
        enable_click = false;
        if (getUserVisibleHint()) {
            LoginUserInfo info = SharedPreUtil.getLoginUserInfo();
            BusProvider.getBus().post(new MsgEvent<String>(RxConstant.Head_Image_Change_Type, RxConstant.Head_Image_Change_Type, info == null ? "" : info.icon));
        }
        Log.e("ll", "onresume");
        if (mLoginView == null || mUnLoginView == null)
            return;
        if (SharedPreUtil.isLogin()) {
            loginInit();
        } else {
            unloginInit();
        }
    }

    @Override
    public void onClick(View v) {
        if (enable_click) return;
        enable_click = true;
        if (v == mRegist) {
            DetailFragmentsActivity.launch(getContext(), null, RegistFragment.newInstance());
        }
        if (v == mLogin) {
            DetailFragmentsActivity.launch(getContext(), null, LoginFragment.newInstance());
        }
        if (v == mEditUserInfo || v == mAvatar) {
            DetailFragmentsActivity.launch(getContext(), null, SettingUserFragment.newInstance());
        }
        if (v == mWallet) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(WalletFragment.TAG, userInfo);
            DetailFragmentsActivity.launch(getContext(), bundle, WalletFragment.newInstance());
        }
        if (v == mMineGame) {
            DetailFragmentsActivity.launch(getContext(), null, MineGameFragment.newInstance());
        }
        if (v == mMineGift) {
            DetailFragmentsActivity.launch(getContext(), null, MineGiftFragment.newInstance());
        }
        if (v == mMineOrder) {
            DetailFragmentsActivity.launch(getContext(), null, MineOrderFragment.newInstance());
        }
        if (v == mMineVip) {
            DetailFragmentsActivity.launch(getContext(), null, CouponFragment.newInstance());
        }
        if (v == mRecharge) {
            Bundle bundle = new Bundle();
            bundle.putString(RechargeFragment.TAG, userInfo.vip_level.level);
            RechargeFragment rechargeFragment = RechargeFragment.newInstance();
            rechargeFragment.setArguments(bundle);
            DetailFragmentsActivity.launch(getContext(), bundle, rechargeFragment);
        }
        if (v == mMessage) {
            DetailFragmentsActivity.launch(getContext(), null, MessageFragment.newInstance());
        }
        if (v == mVip) {
            Bundle bundle = new Bundle();
            bundle.putString(WebviewFragment.PARAM_TITLE, "VIP");
            bundle.putString(WebviewFragment.PARAM_URL, SharedPreUtil.getH5url(SharedPreUtil.H5_URL_VIP));
            DetailFragmentsActivity.launch(getContext(), bundle, WebviewFragment.newInstance());
        }
    }

    /*****************    unlogin ui start   *******************/
    private void unloginInit() {
        mLoginView.setVisibility(View.GONE);
        mUnLoginView.setVisibility(View.VISIBLE);
        mRegist.setOnClickListener(this);
        mLogin.setOnClickListener(this);
    }
    /*****************    unlogin ui start   *******************/


    /*****************    login ui start   *******************/
    private void loginInit() {
        mUnLoginView.setVisibility(View.GONE);
        mLoginView.setVisibility(View.VISIBLE);
        mWallet.setOnClickListener(this);
        mMineGame.setOnClickListener(this);
        mMineGift.setOnClickListener(this);
        mMineOrder.setOnClickListener(this);
        mMineVip.setOnClickListener(this);
        mRecharge.setOnClickListener(this);
        mMessage.setOnClickListener(this);
        mVip.setOnClickListener(this);
        initLoginData();
        initLoginView();
        getMemberInfo();
    }

    private void initLoginData() {
        resIcon = new int[]{
                R.mipmap.ic_mine_yaoqing,
                R.mipmap.ic_mine_tuiguang,
                R.mipmap.ic_mine_kefu,
                R.mipmap.ic_mine_account,
                R.mipmap.ic_mine_download,
                R.mipmap.ic_mine_fankui,
                R.mipmap.ic_mine_setting};
        resName = new int[]{
                R.string.mine_name_0,
                R.string.mine_name_1,
                R.string.mine_name_2,
                R.string.mine_name_5,
                R.string.mine_name_6,
                R.string.mine_name_3,
                R.string.mine_name_4};
    }

    private void initLoginView() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        mAdapter = new SettingListAdapter(resIcon.length);
        mSettingList.setLayoutManager(manager);
        mSettingList.setHasFixedSize(true);
        mSettingList.setAdapter(mAdapter);

        //
        mEditUserInfo.setOnClickListener(this);
        mAvatar.setOnClickListener(this);
    }

    private void setUserData(MemberInfoResults userData) {
        mName.setText(userData.nick_name);
        mMoney.setText("¥" + userData.total_balance);
        mVipLevel.setCompoundDrawablePadding(Utils.dip2px(getContext(), 5));
        Drawable d = getResources().getDrawable(Utils.getVipLevel(Integer.valueOf(userData.vip_level.level)));
        d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
        mVipLevel.setCompoundDrawables(null, null, d, null);
        mVipIcon.setImageResource(Utils.getMineVipLevel(Integer.valueOf(userData.vip_level.level)));
        if (!StringUtils.isEmpty(userData.icon_thumb)) {
//            Glide.with(getContext()).load(userData.icon).into(mAvatar.getAvatarView());
            BusProvider.getBus().post(new MsgEvent<String>(RxConstant.Head_Image_Change_Type, RxConstant.Head_Image_Change_Type, userData.icon_thumb));
        }
    }

    class SettingListAdapter extends RecyclerView.Adapter<SettingListHolder> {
        private int size;

        public SettingListAdapter(int size) {
            this.size = size;
        }

        @Override
        public SettingListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_mine_list_item, parent, false);
            return new SettingListHolder(view);
        }

        @Override
        public void onBindViewHolder(SettingListHolder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return size;
        }
    }

    class SettingListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View mRootView;
        private View interval;
        private TextView mName;
        private ImageView mIcon;

        public SettingListHolder(View itemView) {
            super(itemView);
            mRootView = itemView.findViewById(R.id.root_item);
            mName = itemView.findViewById(R.id.tv_name);
            mIcon = itemView.findViewById(R.id.iv_icon);

            //间隔的标志位
            interval = itemView.findViewById(R.id.v_list_interval);
        }

        public void onBind(int position) {
            if (position > resIcon.length)
                return;

            if (position == 2)
                interval.setVisibility(View.VISIBLE);
            mRootView.setOnClickListener(this);
            mRootView.setTag(resName[position]);
            mIcon.setImageResource(resIcon[position]);
            mName.setText(resName[position]);
        }

        @Override
        public void onClick(View v) {//intent
            if (enable_click) return;
            enable_click = true;
            if (v.getTag() != null) {
                int res = (int) v.getTag();
                Fragment fra = null;
                switch (res) {
                    case R.string.mine_name_0:
                        fra = ExtensionHistoryFragment.newInstance();
                        break;
                    case R.string.mine_name_1:
                        fra = ExtensionProfitFragment.newInstance();
                        break;
                    case R.string.mine_name_2:
                        enable_click = false;
                        Intent kefuIntent = new Intent(context, HuanxinKefuLoginActivity.class);
                        startActivity(kefuIntent);
                        break;
                    case R.string.mine_name_3:
                        fra = FeedBackFragment.newInstance();
                        break;
                    case R.string.mine_name_4:
                        fra = SettingSystemFragment.newInstance();
                        break;
                    case R.string.mine_name_5:
                        fra = AccountManageFragment.newInstance();
                        break;
                    case R.string.mine_name_6:
                        fra = DownloadManageFragment.newInstance();
                        break;
                }
                if (fra != null) {
                    DetailFragmentsActivity.launch(getContext(), null, fra);
                }
            }
        }
    }

    /*****************    login ui end   *******************/

    @Override
    public Object newP() {
        return null;
    }

    private void getMemberInfo() {
        final ArrayMap map = new ArrayMap();
        Flowable<HttpResultModel<MemberInfoResults>> fr = DataService.getMemberInfo();
        Flowable<HttpResultModel<Map<String, Integer>>> fc = DataService.getCouponCount();
        Flowable<MineData> fa = Flowable.zip(fr, fc, new BiFunction<HttpResultModel<MemberInfoResults>, HttpResultModel<Map<String, Integer>>, MineData>() {
            @Override
            public MineData apply(HttpResultModel<MemberInfoResults> memberInfoResultsHttpResultModel, HttpResultModel<Map<String, Integer>> mapHttpResultModel) throws Exception {
//                map.put(0, memberInfoResultsHttpResultModel.data);
//                map.put(1, mapHttpResultModel.data);
                MineData data = new MineData();
                data.memberInfoResults = memberInfoResultsHttpResultModel.data;
                data.coupon = mapHttpResultModel.data;
                couponCount = mapHttpResultModel.data.get("red_count");
                return data;
            }
        });
//        RxLoadingUtils.subscribe(fa, bindToLifecycle(), new Consumer<ArrayMap>() {
//            @Override
//            public void accept(ArrayMap arrayMap) throws Exception {
////                if (true/*memberInfoResultsHttpResultModel.isSucceful()*/) {
//////                    userInfo = memberInfoResultsHttpResultModel.data;
////                    userInfo = (MemberInfoResults) arrayMap.get(0);
////                    setUserData(userInfo);
////                } else {
////                    Toast.makeText(getContext(), memberInfoResultsHttpResultModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
////                }
////            }
//        }, new Consumer<NetError>() {
//            @Override
//            public void accept(NetError netError) throws Exception {
//                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
//            }
//        });
        RxLoadingUtils.subscribeWithDialog(context, fa, this.bindToLifecycle(), new Consumer<MineData>() {
            @Override
            public void accept(MineData mineData) throws Exception {
                userInfo = mineData.memberInfoResults;
                setUserData(userInfo);
                couponTv.setText(couponCount + "");
                couponTv.setVisibility(couponCount > 0 ? View.VISIBLE : View.GONE);
//                BusProvider.getBus().post(new MsgEvent<String>(couponCount, 3, "CustomBadgeItem"));
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Toast.makeText(getContext(), netError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MineData extends XBaseModel {
        MemberInfoResults memberInfoResults;
        Map<String, Integer> coupon;
    }
}

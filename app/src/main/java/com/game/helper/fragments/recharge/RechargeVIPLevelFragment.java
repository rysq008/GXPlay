package com.game.helper.fragments.recharge;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.MemberInfoResults;
import com.game.helper.model.UserInfoAndVipLevelResults;
import com.game.helper.model.VipLevelResults;
import com.game.helper.net.DataService;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.views.XReloadableStateContorller;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * Created by Tian on 2018/1/2.
 */

public class RechargeVIPLevelFragment extends XBaseFragment {
    public static final String TAG = RechargeVIPLevelFragment.class.getSimpleName();
    @BindView(R.id.tv_vip1_recharge_vip_level)
    TextView tvVip1;
    @BindView(R.id.tv_vip2_recharge_vip_level)
    TextView tvVip2;
    @BindView(R.id.tv_vip3_recharge_vip_level)
    TextView tvVip3;
    @BindView(R.id.iv_weixin_recharge_vip_level)
    ImageView ivWeixin;
    @BindView(R.id.iv_zhifubao_recharge_vip_level)
    ImageView ivZhifubao;
    @BindView(R.id.tv_total_recharge_vip_level)
    TextView tvTotal;
    @BindView(R.id.xController_vip_level_recharge)
    XReloadableStateContorller xController;
    private boolean isWeixin = true;
    private int vipLevel = 0;

    public static RechargeVIPLevelFragment newInstance() {
        return new RechargeVIPLevelFragment();
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        initData(true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recharge_vip_level;
    }

    @Override
    public Object newP() {
        return null;
    }

    @OnClick({R.id.iv_weixin_recharge_vip_level, R.id.iv_zhifubao_recharge_vip_level,
            R.id.tv_vip1_recharge_vip_level, R.id.tv_vip2_recharge_vip_level, R.id.tv_vip3_recharge_vip_level})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_weixin_recharge_vip_level:
                if (isWeixin) {
                    /*isWeixin = !isWeixin;
                    ivWeixin.setImageResource(R.mipmap.qrddz_btn_wgx);
                    ivZhifubao.setImageResource(R.mipmap.kxcz_btn_hg);*/
                } else {
                    isWeixin = !isWeixin;
                    ivWeixin.setImageResource(R.mipmap.kxcz_btn_hg);
                    ivZhifubao.setImageResource(R.mipmap.qrddz_btn_wgx);
                }
                break;
            case R.id.iv_zhifubao_recharge_vip_level:
                if (isWeixin) {
                    isWeixin = !isWeixin;
                    ivWeixin.setImageResource(R.mipmap.qrddz_btn_wgx);
                    ivZhifubao.setImageResource(R.mipmap.kxcz_btn_hg);
                } else {
                    /*isWeixin = !isWeixin;
                    ivWeixin.setImageResource(R.mipmap.kxcz_btn_hg);
                    ivZhifubao.setImageResource(R.mipmap.qrddz_btn_wgx);*/
                }
                break;
            case R.id.tv_vip1_recharge_vip_level:
                tvTotal.setText(tvVip1.getText().toString());
                changeState(1);
                break;
            case R.id.tv_vip2_recharge_vip_level:
                tvTotal.setText(tvVip2.getText().toString());
                changeState(2);
                break;
            case R.id.tv_vip3_recharge_vip_level:
                tvTotal.setText(tvVip3.getText().toString());
                changeState(3);
                break;
        }
    }

    private void changeState(int vipLevel) {
        if (vipLevel == 1) {
            tvVip1.setBackgroundColor(getResources().getColor(R.color.color_00aeff));
            tvVip1.setTextColor(getResources().getColor(R.color.colorWhite));
            tvVip2.setBackgroundResource(R.mipmap.vip_recharge_bg_b);
            tvVip2.setTextColor(getResources().getColor(R.color.color_00aeff));
            tvVip3.setBackgroundResource(R.mipmap.vip_recharge_bg_b);
            tvVip3.setTextColor(getResources().getColor(R.color.color_00aeff));
        } else if (vipLevel == 2) {
            if (tvVip1.isClickable()) {
                tvVip1.setBackgroundResource(R.mipmap.vip_recharge_bg_b);
                tvVip1.setTextColor(getResources().getColor(R.color.color_00aeff));
            }
            tvVip2.setBackgroundColor(getResources().getColor(R.color.color_00aeff));
            tvVip2.setTextColor(getResources().getColor(R.color.colorWhite));
            tvVip3.setBackgroundResource(R.mipmap.vip_recharge_bg_b);
            tvVip3.setTextColor(getResources().getColor(R.color.color_00aeff));
        } else if (vipLevel == 3) {
            tvVip3.setBackgroundColor(getResources().getColor(R.color.color_00aeff));
            tvVip3.setTextColor(getResources().getColor(R.color.colorWhite));
            tvVip1.setBackgroundResource(R.mipmap.vip_recharge_bg_b);
            tvVip1.setTextColor(getResources().getColor(R.color.color_00aeff));
            tvVip2.setBackgroundResource(R.mipmap.vip_recharge_bg_b);
            tvVip2.setTextColor(getResources().getColor(R.color.color_00aeff));

            if (tvVip1.isClickable()) {
                tvVip1.setBackgroundResource(R.mipmap.vip_recharge_bg_b);
                tvVip1.setTextColor(getResources().getColor(R.color.color_00aeff));
            }
            if (tvVip2.isClickable()) {
                tvVip2.setBackgroundResource(R.mipmap.vip_recharge_bg_b);
                tvVip2.setTextColor(getResources().getColor(R.color.color_00aeff));
            }

        }
    }

    public void setVipLevelYearFree(int vipLevel, String cost) {
        if (vipLevel == 1) {
            tvVip1.setText(String.valueOf(cost));
        } else if (vipLevel == 2) {
            tvVip2.setText(String.valueOf(cost));
        } else if (vipLevel == 3) {
            tvVip3.setText(String.valueOf(cost));
        }
    }

    public void currentVipLevel(int vipLevel) {
        if (vipLevel == 0) {

        } else if (vipLevel == 1) {
            tvVip1.setClickable(false);
            tvVip1.setTextColor(getResources().getColor(R.color.color_999));
        } else if (vipLevel == 2) {
            tvVip1.setClickable(false);
            tvVip1.setTextColor(getResources().getColor(R.color.color_999));
            tvVip2.setClickable(false);
            tvVip2.setTextColor(getResources().getColor(R.color.color_999));
        } else if (vipLevel == 3) {
            tvVip1.setClickable(false);
            tvVip1.setTextColor(getResources().getColor(R.color.color_999));
            tvVip2.setClickable(false);
            tvVip2.setTextColor(getResources().getColor(R.color.color_999));
            tvVip3.setClickable(false);
            tvVip3.setTextColor(getResources().getColor(R.color.color_999));
        }
    }

    private void setTotalData(int ivpLevel) {

    }

    private void initData(Boolean isShowLoading) {
        Flowable<UserInfoAndVipLevelResults> fa;
        Flowable<HttpResultModel<VipLevelResults>> fv = DataService.getVipLevel();
        Flowable<HttpResultModel<MemberInfoResults>> fm = DataService.getMemberInfo();
        fa = Flowable.zip(fv, fm, new BiFunction<HttpResultModel<VipLevelResults>, HttpResultModel<MemberInfoResults>, UserInfoAndVipLevelResults>() {
            @Override
            public UserInfoAndVipLevelResults apply(HttpResultModel<VipLevelResults> vipLevelListResultsHttpResultModel, HttpResultModel<MemberInfoResults> memberInfoResultsHttpResultModel) throws Exception {
                UserInfoAndVipLevelResults userInfoAndVipLevelResults = new UserInfoAndVipLevelResults(vipLevelListResultsHttpResultModel.data, memberInfoResultsHttpResultModel.data);
                return userInfoAndVipLevelResults;
            }
        });

        RxLoadingUtils.subscribeWithReload(xController, fa, this.bindToLifecycle(), new Consumer<UserInfoAndVipLevelResults>() {
            @Override
            public void accept(UserInfoAndVipLevelResults userInfoAndVipLevelResults) throws Exception {
                if (userInfoAndVipLevelResults != null) {
                    xController.showContent();
                    MemberInfoResults memberInfoResults = userInfoAndVipLevelResults.memberInfoResults;
                    String level = memberInfoResults.vip_level.get("level");
                    currentVipLevel(Integer.valueOf(level));
                    VipLevelResults levelResults = userInfoAndVipLevelResults.vipLevelResults;
                    for (VipLevelResults.VipBean vipBean : levelResults.list) {
                        setVipLevelYearFree(vipBean.level, vipBean.year_fee);
                    }
                } else {
                    xController.showEmpty();
                }
            }
        }, null, null, isShowLoading);

    }
}

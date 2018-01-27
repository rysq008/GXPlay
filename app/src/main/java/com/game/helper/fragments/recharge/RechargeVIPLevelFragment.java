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
import com.game.helper.model.VIPUpGradeCostResults;
import com.game.helper.model.VipLevelResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.VIPUpGradfeRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.views.GXPlayDialog;
import com.game.helper.views.XReloadableStateContorller;

import java.util.List;

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

    @BindView(R.id.tv_bind_account_vip1_recharge_vip_level)
    TextView bindAccountVip1;
    @BindView(R.id.tv_bind_account_vip2_recharge_vip_level)
    TextView bindAccountVip2;
    @BindView(R.id.tv_bind_account_vip3_recharge_vip_level)
    TextView bindAccountVip3;
    private boolean isWeixin = true;//true 默认是微信，false 是支付宝
    private int currentVIPLevel = 0;//当前用户的等级
    private int selectedVIPLevel = 0;//选中VIP的等级
    private int previousVIPLevel = -1;//前一次选中的VIP等级
    private List<VipLevelResults.VipBean> vipBeans;
    private GXPlayDialog dialog;

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
                selectedVIPLevel = 1;
                if (currentVIPLevel == 3) {
                    showVipHintDialog(2);
                } else if (selectedVIPLevel <= currentVIPLevel) {
                    showVipHintDialog(1);
                } else {
                    changeState(selectedVIPLevel);
                    //获取vip升级的费用
                    setTotalData(selectedVIPLevel);
                }
                break;
            case R.id.tv_vip2_recharge_vip_level:
                selectedVIPLevel = 2;
                if (currentVIPLevel == 3) {
                    showVipHintDialog(2);
                } else if (selectedVIPLevel <= currentVIPLevel) {
                    showVipHintDialog(1);
                } else {
                    changeState(selectedVIPLevel);
                    //获取vip升级的费用
                    setTotalData(selectedVIPLevel);
                }
                break;
            case R.id.tv_vip3_recharge_vip_level:
                selectedVIPLevel = 3;
                if (currentVIPLevel == 3) {
                    showVipHintDialog(2);
                } else if (selectedVIPLevel <= currentVIPLevel) {
                    showVipHintDialog(1);
                } else {
                    changeState(selectedVIPLevel);
                    //获取vip升级的费用
                    setTotalData(selectedVIPLevel);
                }
                break;
        }
    }

    private void changeState(int selvipLevel) {
        //设置选中的状态
        if (selvipLevel == 1) {
            tvVip1.setBackgroundColor(getResources().getColor(R.color.color_00aeff));
            tvVip1.setTextColor(getResources().getColor(R.color.colorWhite));
        } else if (selvipLevel == 2) {
            tvVip2.setBackgroundColor(getResources().getColor(R.color.color_00aeff));
            tvVip2.setTextColor(getResources().getColor(R.color.colorWhite));
        } else if (selvipLevel == 3) {
            tvVip3.setBackgroundColor(getResources().getColor(R.color.color_00aeff));
            tvVip3.setTextColor(getResources().getColor(R.color.colorWhite));
        }
        //将前一次的选中的状态变成没选中状态
        if (previousVIPLevel != -1 && selvipLevel != previousVIPLevel) {
            if (previousVIPLevel == 1) {
                tvVip1.setBackgroundResource(R.mipmap.vip_recharge_bg_b);
                tvVip1.setTextColor(getResources().getColor(R.color.color_00aeff));
            } else if (previousVIPLevel == 2) {
                tvVip2.setBackgroundResource(R.mipmap.vip_recharge_bg_b);
                tvVip2.setTextColor(getResources().getColor(R.color.color_00aeff));
            } else if (previousVIPLevel == 3) {
                tvVip3.setBackgroundResource(R.mipmap.vip_recharge_bg_b);
                tvVip3.setTextColor(getResources().getColor(R.color.color_00aeff));
            }
        }
        previousVIPLevel = selvipLevel;

    }

    public void setVipLevelYearFree(VipLevelResults.VipBean vipLevel) {
        if (vipLevel.level == 1) {
            tvVip1.setText(String.valueOf(vipLevel.year_fee));
            bindAccountVip1.setText("2、黑钻会员可以绑定"+vipLevel.num_account+"个享受VIP折扣价格的游戏帐号");
        } else if (vipLevel.level == 2) {
            tvVip2.setText(String.valueOf(vipLevel.year_fee));
            bindAccountVip2.setText("3、红钻会员可以绑定"+vipLevel.num_account+"个享受VIP折扣价格的游戏帐号");
        } else if (vipLevel.level  == 3) {
            tvVip3.setText(String.valueOf(vipLevel.year_fee));
            bindAccountVip3.setText("4、皇冠会员可以绑定"+vipLevel.num_account+"个享受VIP折扣价格的游戏帐号");
        }
    }

    public void currentVipLevel(int vipLevel) {
        if (vipLevel == 0) {

        } else if (vipLevel == 1) {
            //tvVip1.setClickable(false);
            tvVip1.setTextColor(getResources().getColor(R.color.color_999));
        } else if (vipLevel == 2) {
            //tvVip1.setClickable(false);
            tvVip1.setTextColor(getResources().getColor(R.color.color_999));
            //tvVip2.setClickable(false);
            tvVip2.setTextColor(getResources().getColor(R.color.color_999));
        } else if (vipLevel == 3) {
            //tvVip1.setClickable(false);
            tvVip1.setTextColor(getResources().getColor(R.color.color_999));
            //tvVip2.setClickable(false);
            tvVip2.setTextColor(getResources().getColor(R.color.color_999));
            //tvVip3.setClickable(false);
            tvVip3.setTextColor(getResources().getColor(R.color.color_999));
        }
    }

    private void setTotalData(final int iVPLevel) {
        int id = vipBeans.get(iVPLevel).id;
        Flowable<HttpResultModel<VIPUpGradeCostResults>> fv = DataService.getVIPUpGradeCost(new VIPUpGradfeRequestBody(id));
        RxLoadingUtils.subscribe(fv, this.bindToLifecycle(), new Consumer<HttpResultModel<VIPUpGradeCostResults>>() {
            @Override
            public void accept(HttpResultModel<VIPUpGradeCostResults> vipUpGradeCost) throws Exception {
                tvTotal.setText(String.valueOf(vipUpGradeCost.data.getCost()));
            }
        });
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
                    int level = memberInfoResults.vip_level.getLevel();
                    currentVIPLevel = level;
                    currentVipLevel(currentVIPLevel);
                    VipLevelResults levelResults = userInfoAndVipLevelResults.vipLevelResults;
                    vipBeans = levelResults.list;
                    for (VipLevelResults.VipBean vipBean : vipBeans) {
                        setVipLevelYearFree(vipBean);
                    }
                } else {
                    xController.showEmpty();
                }
            }
        }, null, null, isShowLoading);

    }

    public Boolean getPayType() {
        return isWeixin;
    }

    public int getTotal() {
        return Integer.valueOf(tvTotal.getText().toString());
    }

    public String getSelectedVIPLevel() {
        return String.valueOf(selectedVIPLevel);
    }


    /**
     * type
     * 0：仅提示消耗vip数量
     * 1：升级vip
     * 2：vip最高联系管理员
     */
    private void showVipHintDialog(final int type) {
        String content = "";
        if (type == 0) content = "您当前没有选择选择VIP等级，请选择VIP等级";
        if (type == 1) content = "请选择比当前高的VIP等级";
        if (type == 2) content = "您是皇冠会员，已无法再升级会员，请联系客服";
        dialog = null;
        dialog = new GXPlayDialog(GXPlayDialog.Ddialog_With_All_Full_Confirm, "温馨提示", content);
        dialog.addOnDialogActionListner(new GXPlayDialog.onDialogActionListner() {
            @Override
            public void onCancel() {
                dialog.dismiss();
            }

            @Override
            public void onConfirm() {
                dialog.dismiss();
            }
        });
        dialog.show(getChildFragmentManager(), GXPlayDialog.TAG);
    }
}

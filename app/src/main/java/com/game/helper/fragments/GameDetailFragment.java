package com.game.helper.fragments;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.DownLoad.DownloadController;
import com.game.helper.model.GameDetailAllResults;
import com.game.helper.model.GamePackageInfoResult;
import com.game.helper.model.H5UrlListResults;
import com.game.helper.model.MemberInfoResults;
import com.game.helper.net.DataService;
import com.game.helper.net.api.Api;
import com.game.helper.net.model.GameDetailSendCommentContentRequestBody;
import com.game.helper.net.model.GamePackageInfoRequestBody;
import com.game.helper.utils.DownLoadReceiveUtils;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SPUtils;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.utils.ToastUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;
import zlc.season.rxdownload2.entity.DownloadBean;
import zlc.season.rxdownload2.entity.DownloadEvent;
import zlc.season.rxdownload2.entity.DownloadStatus;

import static zlc.season.rxdownload2.function.Utils.dispose;

public class GameDetailFragment extends XBaseFragment implements View.OnClickListener {
    public static final String TAG = GameDetailFragment.class.getSimpleName();
    List<Fragment> list = new ArrayList<Fragment>();
    @BindView(R.id.action_bar_tittle)
    TextView mTvTittle;
    @BindView(R.id.iv_game_detail_logothumb)
    ImageView ivLogothumb;
    @BindView(R.id.tv_game_detail_name)
    TextView tvName;
    @BindView(R.id.tv_game_detail_discount_vip)
    TextView tvDiscount;
    @BindView(R.id.tv_game_detail_type_name)
    TextView tvTypeName;
    @BindView(R.id.tv_game_detail_package_filesize)
    TextView tvPackageFilesize;
    @BindView(R.id.tv_game_detail_content)
    TextView tvContent;
    @BindView(R.id.tv_discount_game_detail_common)
    TextView tvDiscountCommon;
    @BindView(R.id.tv_discount_game_detail_vip)
    TextView tvDiscountVip;
    @BindView(R.id.game_detail_tabs)
    MagicIndicator tabStrip;
    @BindView(R.id.game_detail_viewpager)
    ViewPager viewPager;
    @BindView(R.id.tv_game_detail_bottom_download)
    TextView tvBottomDownload;
    @BindView(R.id.et_content_comment_game_detail)
    EditText etEditContent;
    @BindView(R.id.btn_send_comment_game_detail)
    Button btnSend;
    @BindView(R.id.ll_comment_game_detail)
    LinearLayout etitLayout;
    @BindView(R.id.tv_game_detail_plat)
    TextView tvPlat;
    @BindView(R.id.pb_game_detail)
    ProgressBar pb;
    @BindView(R.id.percent)
    TextView mPercent;
    @BindView(R.id.size)
    TextView mSize;
    @BindView(R.id.status)
    TextView mStatusText;
    @BindView(R.id.btn_game_detail_load)
    Button btnLoad;
    @BindView(R.id.ll_Progress_bar_game_detail)
    RelativeLayout llProgressBar;
    @BindView(R.id.ll_discount_navigation_game_detail)
    LinearLayout llNavigation;
    private H5UrlListResults mH5UrlList;

    private int gamepackeId;
    private int gameId;
    private int channelId;
    private GameDetailInfoFragment gameDetailInfoFragment;
    private GameDetailRechargeFragment rechargeGameFragment;
    private GameDetailGiftFragment gameDetailGiftFragment;
    private GameDetailCommunityFragment gameDetailCommunityFragment;
    private GamePackageInfoResult packageInfo = new GamePackageInfoResult();
    private MemberInfoResults memberInfoResults;
    private Disposable disposable;
    private DownloadController mDownloadController;
    private DownloadBean downloadBean;


    public static GameDetailFragment newInstance() {
        return new GameDetailFragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mTvTittle.setText("游戏详情");
        Bundle arguments = getArguments();
        tvDiscountCommon.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvDiscountVip.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        if (arguments != null) {
            gamepackeId = arguments.getInt("gamepackeId");
            gameId = arguments.getInt("gameId");
            channelId = arguments.getInt("channelId");
            initGamePackage();
        }
        if (gameDetailInfoFragment == null) {
            gameDetailInfoFragment = GameDetailInfoFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putInt("gameId", gameId);
            gameDetailInfoFragment.setArguments(bundle);
        }
        if (rechargeGameFragment == null) {
            rechargeGameFragment = GameDetailRechargeFragment.newInstance();
            Bundle bundle = new Bundle();

            rechargeGameFragment.setArguments(bundle);
        }
        if (gameDetailGiftFragment == null) {
            gameDetailGiftFragment = GameDetailGiftFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putInt("gameId", gameId);
            gameDetailGiftFragment.setArguments(bundle);
        }
        if (gameDetailCommunityFragment == null) {
            gameDetailCommunityFragment = GameDetailCommunityFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putInt("gameId", gameId);
            gameDetailCommunityFragment.setArguments(bundle);
        }
        list.add(rechargeGameFragment);
        list.add(gameDetailInfoFragment);
        list.add(gameDetailGiftFragment);
        list.add(gameDetailCommunityFragment);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        });
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tvBottomDownload.setVisibility(View.GONE);
                        etitLayout.setVisibility(View.GONE);
                        break;
                    case 1:
                        tvBottomDownload.setVisibility(View.VISIBLE);
                        etitLayout.setVisibility(View.GONE);
                        break;
                    case 2:
                        tvBottomDownload.setVisibility(View.GONE);
                        etitLayout.setVisibility(View.GONE);
                        break;
                    case 3:
                        tvBottomDownload.setVisibility(View.GONE);
                        etitLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        loadData();
        mDownloadController = new DownloadController(mStatusText, btnLoad);
    }

    private void initGamePackage() {
        Flowable<GameDetailAllResults> fa;
        Flowable<HttpResultModel<GamePackageInfoResult>> fg = DataService.getGamePackageInfo(new GamePackageInfoRequestBody(gamepackeId));
        Flowable<HttpResultModel<H5UrlListResults>> fh = DataService.getH5UrlList();
        if (SharedPreUtil.isLogin()) {
            //获取用户的会员级别
            Flowable<HttpResultModel<MemberInfoResults>> fm = DataService.getMemberInfo();
            fa = Flowable.zip(fg, fh, fm, new Function3<HttpResultModel<GamePackageInfoResult>, HttpResultModel<H5UrlListResults>, HttpResultModel<MemberInfoResults>, GameDetailAllResults>() {
                @Override
                public GameDetailAllResults apply(HttpResultModel<GamePackageInfoResult> gamePackageInfoResult, HttpResultModel<H5UrlListResults> h5UrlListResults, HttpResultModel<MemberInfoResults> memberInfoResults) throws Exception {
                    GameDetailAllResults gameDetailAllResults = new GameDetailAllResults(gamePackageInfoResult.data, h5UrlListResults.data, memberInfoResults.data);
                    return gameDetailAllResults;
                }
            });
        } else {
            fa = Flowable.zip(fg, fh, new BiFunction<HttpResultModel<GamePackageInfoResult>, HttpResultModel<H5UrlListResults>, GameDetailAllResults>() {

                @Override
                public GameDetailAllResults apply(HttpResultModel<GamePackageInfoResult> gamePackageModel, HttpResultModel<H5UrlListResults> h5UrlListtModel) throws Exception {
                    GameDetailAllResults gameDetailAllResults = new GameDetailAllResults(gamePackageModel.data, h5UrlListtModel.data);
                    return gameDetailAllResults;
                }
            });
        }

        RxLoadingUtils.subscribe(fa, this.bindToLifecycle(), new Consumer<GameDetailAllResults>() {
            @Override
            public void accept(GameDetailAllResults gameDetailAllResults) throws Exception {
                if (gameDetailAllResults.h5UrlListResults != null) {
                    mH5UrlList = gameDetailAllResults.h5UrlListResults;
                }
                if (gameDetailAllResults.gamePackageInfoResult != null) {
                    packageInfo = gameDetailAllResults.gamePackageInfoResult;
                    ILFactory.getLoader().loadNet(ivLogothumb, Api.API_PAY_OR_IMAGE_URL.concat(packageInfo.getGame().getLogo()), ILoader.Options.defaultOptions());
                    tvName.setText(packageInfo.getGame().getName());
                    tvDiscount.setText(String.valueOf(packageInfo.getDiscount_vip()));
                    tvTypeName.setText(packageInfo.getGame().getType().getName());
                    tvPackageFilesize.setText(String.valueOf(packageInfo.getFilesize() + "M"));
                    tvContent.setText(packageInfo.getGame().getIntro());
                    tvPlat.setText(packageInfo.getChannel().getName());
                    SPUtils.putString(context, SPUtils.CHANNEL_NAME, packageInfo.getChannel().getName());
                    SPUtils.putString(context, SPUtils.GAME_NAME, packageInfo.getGame().getName());
                    SPUtils.putInt(context, SPUtils.CHANNEL_ID, packageInfo.getChannel().getId());
                    SPUtils.putInt(context, SPUtils.GAME_ID, packageInfo.getGame().getId());
                    downloadBean = new DownloadBean
                            .Builder(packageInfo.getGame().getUrl())
                            .setSaveName(null)      //not need.
                            .setSavePath(null)      //not need
                            .setExtra1(packageInfo.getGame().getLogo())   //save extra info into database.
                            .setExtra2(packageInfo.getGame().getName())  //save extra info into database.
                            .build();
                    DownLoadReceiveUtils.receiveDownloadEvent(context, packageInfo.getGame().getUrl(), disposable, mDownloadController, new DownLoadReceiveUtils.OnDownloadEventReceiveListener() {
                        @Override
                        public void receiveDownloadEvent(DownloadEvent event, boolean isDisposable) {
                            updateProgressStatus(event.getDownloadStatus());
                            if (isDisposable) {
                                dispose(disposable);
                            }
                        }
                    });
                }
                if (gameDetailAllResults.memberInfoResults != null) {
                    memberInfoResults = gameDetailAllResults.memberInfoResults;
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
            }
        });
    }

    private void updateProgressStatus(DownloadStatus status) {
        pb.setIndeterminate(status.isChunked);
        pb.setMax((int) status.getTotalSize());
        pb.setProgress((int) status.getDownloadSize());
        mPercent.setText(status.getPercent());
        mSize.setText(status.getFormatStatusString());
    }

    @OnClick({R.id.btn_game_detail_load, R.id.action_bar_back, R.id.iv_action})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_game_detail_load:
                mDownloadController.handleClick(new DownloadController.Callback() {
                    @Override
                    public void startDownload() {
                        DownLoadReceiveUtils.startDownload(context, getRxPermissions(), downloadBean);
                    }

                    @Override
                    public void pauseDownload() {
                        DownLoadReceiveUtils.pauseDownload(context, Kits.Empty.check(packageInfo.getGame()) ? "" : packageInfo.getGame().getUrl());
                    }

                    @Override
                    public void cancelDownload() {
                    }

                    @Override
                    public void installApk() {
                        DownLoadReceiveUtils.installApk(context, Kits.Empty.check(packageInfo.getGame()) ? "" : packageInfo.getGame().getUrl());
                    }

                    @Override
                    public void openApp() {
                        //Intent intent = context.getPackageManager().getLaunchIntentForPackage()
                        Toast.makeText(context, "open", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.action_bar_back:
                getActivity().finish();
                break;
            case R.id.iv_action:
                //分享
                if (getActivity() instanceof DetailFragmentsActivity) {
                    ((DetailFragmentsActivity) getActivity()).umShare(packageInfo);
                }
                break;
        }
    }

    public void loadData() {
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return list == null ? 0 : list.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                String title = null;
                switch (index) {
                    case 0:
                        title = "充值";
                        break;
                    case 1:
                        title = "详情";
                        break;
                    case 2:
                        title = "礼包";
                        break;
                    case 3:
                        title = "评论";
                        break;
                }
                colorTransitionPagerTitleView.setText(title);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator;
            }
        });
        tabStrip.setNavigator(commonNavigator);
        ViewPagerHelper.bind(tabStrip, viewPager);
        viewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_game_detail_layout;
    }

    @Override
    public Object newP() {
        return null;
    }

    @OnClick({R.id.tv_game_detail_bottom_download, R.id.btn_send_comment_game_detail,
            R.id.tv_discount_game_detail_common, R.id.tv_discount_game_detail_vip,
            R.id.ll_discount_navigation_game_detail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_game_detail_bottom_download:

                break;
            case R.id.btn_send_comment_game_detail:
                String content = etEditContent.getText().toString().trim();
                if (content.length() == 0) {
                    ToastUtil.showToast("内容不能为空");
                } else {
                    sendCommentContent(gameId, content);
                }
                break;
            case R.id.tv_discount_game_detail_common:
                createCommonDialog(llNavigation);
                break;
            case R.id.tv_discount_game_detail_vip:
                createVipDialog(llNavigation);
                break;
            case R.id.ll_discount_navigation_game_detail:
                Bundle bundle = new Bundle();
                bundle.putString(WebviewFragment.PARAM_URL, mH5UrlList.getAccount_guide_url());
                bundle.putString(WebviewFragment.PARAM_TITLE, mTvTittle.getText().toString());
                DetailFragmentsActivity.launch(context, bundle, WebviewFragment.newInstance());
                break;
        }
    }

    private void sendCommentContent(final int gameId, String content) {
        Flowable<HttpResultModel<Object>> fr = DataService.sendCommentContent(new GameDetailSendCommentContentRequestBody(gameId, content));

        RxLoadingUtils.subscribe(fr, this.bindToLifecycle(), new Consumer<HttpResultModel<Object>>() {
            @Override
            public void accept(HttpResultModel<Object> stringHttpResultModel) throws Exception {
                if ((stringHttpResultModel.isSucceful())) {
                    etEditContent.setText("");
                    etEditContent.setCursorVisible(false);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etEditContent.getWindowToken(), 0);
                    gameDetailCommunityFragment.loadAdapterData(1, gameId, true);
                }

            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {

            }
        });
    }

    public void createVipDialog(View view) {
        final AlertDialog dialog = new AlertDialog.Builder(context, R.style.CustomAlertDialogBackground).create();// 创建自定义样式dialog
        View v = View.inflate(context, R.layout.dialog_vip, null);
        LinearLayout currentVip = v.findViewById(R.id.ll_current_vip_dialog_game_detail);
        TextView tvDiscount1 = v.findViewById(R.id.tv_discount1_dialog_game_detail);
        TextView tvDiscount2 = v.findViewById(R.id.tv_discount2_dialog_game_detail);
        TextView tvDiscount3 = v.findViewById(R.id.tv_discount3_dialog_game_detail);
        final TextView tvVip1 = v.findViewById(R.id.tv_vip1_dialog_game_detail);
        final TextView tvVip2 = v.findViewById(R.id.tv_vip2_dialog_game_detail);
        final TextView tvVip3 = v.findViewById(R.id.tv_vip3_dialog_game_detail);
        tvVip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                VipPassage();
            }
        });
        tvVip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                VipPassage();
            }
        });
        tvVip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                VipPassage();
            }
        });
        //用户当前的信息
        final ImageView ivVipLevel = v.findViewById(R.id.iv_vip_level_user_dialog_game_detail);
        final TextView tvNameLevel = v.findViewById(R.id.tv_name_level_user_dialog_game_detail);
        final TextView tvDiscount = v.findViewById(R.id.tv_discount_level_user_dialog_game_detail);

        tvDiscount1.setText(String.valueOf(packageInfo.getZhekou_shouchong()) + "折");
        tvDiscount2.setText(String.valueOf(packageInfo.getZhekou_shouchong()) + "折");
        tvDiscount3.setText(String.valueOf(packageInfo.getDiscount_vip()) + "折");
        boolean b = SharedPreUtil.isLogin();
        //用户已登录，就可以拿到用户详情信息
        if (memberInfoResults != null) {
            currentVip.setVisibility(View.VISIBLE);
            MemberInfoResults.VipLevel vip_level = memberInfoResults.vip_level;
            String url = vip_level.image;
            String name = vip_level.name;
            //String descs = vip_level.descs + "折";
            String level = vip_level.level;
            //ILFactory.getLoader().loadNet(ivVipLevel, Api.API_BASE_URL.concat(url), ILoader.Options.defaultOptions());
            tvNameLevel.setText(name);
            //tvDiscount.setText(descs);
            //根据等级显示升级会员是否可以点击到下一页
            if ("0".equals(level)) {
                ivVipLevel.setImageResource(R.mipmap.vip0);
            } else if ("1".equals(level)) {
                ivVipLevel.setImageResource(R.mipmap.vip1);
                tvVip1.setClickable(false);
                tvVip1.setTextColor(getResources().getColor(R.color.color_666));
            } else if ("2".equals(level)) {
                ivVipLevel.setImageResource(R.mipmap.vip2);
                tvVip1.setClickable(false);
                tvVip1.setTextColor(getResources().getColor(R.color.color_666));
                tvVip2.setClickable(false);
                tvVip2.setTextColor(getResources().getColor(R.color.color_666));

            } else if ("3".equals(level)) {
                ivVipLevel.setImageResource(R.mipmap.vip3);
                tvVip1.setClickable(false);
                tvVip1.setTextColor(getResources().getColor(R.color.color_666));
                tvVip2.setClickable(false);
                tvVip2.setTextColor(getResources().getColor(R.color.color_666));
                tvVip3.setClickable(false);
                tvVip3.setTextColor(getResources().getColor(R.color.color_666));
            }
        } else {
            currentVip.setVisibility(View.GONE);
        }
        Log.d(TAG, "createVipDialog: tvVip1是否可以点击"+tvVip1.isClickable());
        Log.d(TAG, "createVipDialog: tvVip2是否可以点击"+tvVip2.isClickable());
        Log.d(TAG, "createVipDialog: tvVip3是否可以点击"+tvVip3.isClickable());

        //dialog.setCanceledOnTouchOutside(false);// 点击空白区域消失
        // 不可以用“返回键”取消
        dialog.setCancelable(true);
        //dialog.setView(v);// 设置布局
        Window dialogWindow = dialog.getWindow();
        /*实例化Window*/
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //实例化Window操作者
        //lp.x = 100; // 新位置X坐标
        lp.y = view.getBottom()+20; // 新位置Y坐标
        dialogWindow.setAttributes(lp);
        // 不可以用“返回键”取消
        dialog.setCancelable(true);
        dialog.setView(v, 30, 0, 30, 0);// 设置布局
        dialog.show();



    }

    private void VipPassage() {
        Bundle bundle = new Bundle();
        bundle.putString(WebviewFragment.PARAM_TITLE, mTvTittle.getText().toString());
        if (mH5UrlList != null) {
            bundle.putString(WebviewFragment.PARAM_URL, mH5UrlList.getVip_url());
        }
        DetailFragmentsActivity.launch(getContext(), bundle, WebviewFragment.newInstance());
    }


    public void createCommonDialog(View view) {
        View v = View.inflate(context, R.layout.dialog_common_discount, null);
        TextView tvDiscountFirst = v.findViewById(R.id.tv_first_recharge_discount_common_dialog);
        TextView goFirstCharge = v.findViewById(R.id.tv_first_go_recharge_common_dialog);
        TextView tvDiscountAdd = v.findViewById(R.id.tv_add_recharge_discount_common_dialog);
        TextView goAddCharge = v.findViewById(R.id.tv_add_go_recharge_common_dialog);
        tvDiscountFirst.setText("(享受皇冠会员" + packageInfo.getDiscount_vip() + "折)");
        tvDiscountAdd.setText(+packageInfo.getZhekou_xuchong() + "折");
        final AlertDialog CommonDialog = new AlertDialog.Builder(context, R.style.CustomAlertDialogBackground).create();// 创建自定义样式dialog
        //dialog.setCanceledOnTouchOutside(false);// 点击空白区域消失
        Window dialogWindow = CommonDialog.getWindow();
        //实例化Window
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //实例化Window操作者
        //lp.x = 100; // 新位置X坐标
        lp.y = view.getTop();; // 新位置Y坐标
        dialogWindow.setAttributes(lp);
        Log.d(TAG, "dialogWindow的坐标" + lp.x + "----------" + lp.y);
        // 不可以用“返回键”取消
        CommonDialog.setCancelable(true);
        CommonDialog.setView(v, 30, 0, 30, 0);// 设置布局
        CommonDialog.show();
        //CommonDialog.setContentView(v);
        //CommonDialog.getWindow().setLayout(v.getWidth(), v.getHeight());
        goFirstCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonDialog.dismiss();
                viewPager.setCurrentItem(0);

            }
        });
        goAddCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonDialog.dismiss();
                viewPager.setCurrentItem(0);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        DownLoadReceiveUtils.receiveDownloadEvent(context, Kits.Empty.check(packageInfo.getGame()) ? "" : packageInfo.getGame().getUrl(), disposable, mDownloadController, new DownLoadReceiveUtils.OnDownloadEventReceiveListener() {
            @Override
            public void receiveDownloadEvent(DownloadEvent event, boolean isDisposable) {
                updateProgressStatus(event.getDownloadStatus());
                if (isDisposable) {
                    dispose(disposable);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dispose(disposable);
    }
}

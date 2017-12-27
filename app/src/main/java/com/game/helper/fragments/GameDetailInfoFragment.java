package com.game.helper.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.GamePackageInfo_DetailResult;
import com.game.helper.net.DataService;
import com.game.helper.net.api.Api;
import com.game.helper.net.model.GamePackageInfo_InfoRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.Utils;
import com.game.helper.views.XReloadableStateContorller;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class GameDetailInfoFragment extends XBaseFragment {
    private final static String TAG = GameDetailInfoFragment.class.getSimpleName();
    @BindView(R.id.ll_img_container)
    LinearLayout mImgContainer;
    @BindView(R.id.tv_content_game_detail_detail)
    TextView tvContent;
    @BindView(R.id.xrcontorller_content_game_detail_detail)
    XReloadableStateContorller xrcontorller;

    public static GameDetailInfoFragment newInstance() {
        GameDetailInfoFragment fragment = new GameDetailInfoFragment();
        return fragment;
    }

    private int gameId;

    @Override
    public void initData(Bundle savedInstanceState) {
        Log.d(TAG, "----------------=======================1");
        Bundle bundle = getArguments();
        if (bundle != null) {
            gameId = bundle.getInt("gameId");
            if (gameId > 0) {
                loadTata(true);
            }
        }
    }

    private void loadTata(boolean showLoaing) {
        Flowable<HttpResultModel<GamePackageInfo_DetailResult>> fr = DataService.getGamePackageInfo_Info(new GamePackageInfo_InfoRequestBody(gameId));
        RxLoadingUtils.subscribeWithReload(xrcontorller, fr, this.bindToLifecycle(), new Consumer<HttpResultModel<GamePackageInfo_DetailResult>>() {
            @Override
            public void accept(HttpResultModel<GamePackageInfo_DetailResult> gameInfo) throws Exception {
                tvContent.setText(gameInfo.data.getDetail());
                List<GamePackageInfo_DetailResult.ImagesBean> images = gameInfo.data.getImages();
                if (images.size() > 0) {
                    xrcontorller.showContent();
                    for (GamePackageInfo_DetailResult.ImagesBean image : images) {
                        ImageView img = (ImageView) View.inflate(context, R.layout.game_detail_img_item_layout, null);
                        ILFactory.getLoader().loadNet(img, Api.API_PAY_OR_IMAGE_URL.concat(image.getImg()), ILoader.Options.defaultOptions());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.dip2px(getContext(), 150), Utils.dip2px(getContext(), 240));
                        layoutParams.setMargins(Utils.dip2px(getContext(), 10), Utils.dip2px(getContext(), 10), 0, 0);
                        mImgContainer.addView(img, layoutParams);
                    }
                }else{
                    xrcontorller.showEmpty();
                }


            }
        }, null, null, showLoaing);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_game_detail_info;
    }

    @Override
    public Object newP() {
        return null;
    }


}

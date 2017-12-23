package com.game.helper.fragments;

import android.os.Bundle;
import android.util.Log;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.GamePackageInfoResult;
import com.game.helper.model.GamePackageInfo_DetailResult;
import com.game.helper.net.DataService;
import com.game.helper.net.api.Api;
import com.game.helper.net.model.GamePackageInfoRequestBody;
import com.game.helper.utils.RxLoadingUtils;

import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by Tian on 2017/12/23.
 */

public class GameDetailRechargeFragment extends XBaseFragment {


    private static final String TAG = GameDetailRechargeFragment.class.getSimpleName();
    public static GameDetailRechargeFragment newInstance() {
        GameDetailRechargeFragment fragment = new GameDetailRechargeFragment();
        return fragment;
    }
    @Override
    public void initData(Bundle savedInstanceState) {
        Log.d(TAG,"GameDetailRechargeFragment----------------0");


    }



    @Override
    public int getLayoutId() {
        return R.layout.fragment_recharge_game_detail;
    }

    @Override
    public Object newP() {
        return null;
    }
}

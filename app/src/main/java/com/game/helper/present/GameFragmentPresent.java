package com.game.helper.present;

import com.game.helper.fragments.BaseFragment.GameBasePagerFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.BaseModel.XBaseModel;
import com.game.helper.model.ClassicalResults;
import com.game.helper.model.CommonResults;
import com.game.helper.net.DataService;
import com.game.helper.utils.RxLoadingUtils;

import java.util.ArrayList;
import java.util.List;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import zlc.season.practicalrecyclerview.ItemType;

public class GameFragmentPresent extends XPresent<GameBasePagerFragment> {

    public void onInitData() {
        Flowable<HttpResultModel<ClassicalResults>> fb = DataService.getGameClassical();
        Flowable<HttpResultModel<CommonResults>> fn = DataService.getGameCommon();

        final Flowable<GameAllResultsData> fa = Flowable.zip(fb, fn, new BiFunction<HttpResultModel<ClassicalResults>, HttpResultModel<CommonResults>, GameAllResultsData>() {
            @Override
            public GameAllResultsData apply(HttpResultModel<ClassicalResults> classicalResultsHttpResultModel, HttpResultModel<CommonResults> commonResultsHttpResultModel) throws Exception {
                if (classicalResultsHttpResultModel.data.isNull()) {
                    Flowable.error(new NetError("fetch classicalResults failed", NetError.NoDataError));
                }
                if (commonResultsHttpResultModel.data.isNull()) {
                    Flowable.error(new NetError("fetch commonResults failed", NetError.NoDataError));
                }
                return new GameAllResultsData(classicalResultsHttpResultModel.data, commonResultsHttpResultModel.data);
            }
        });

        RxLoadingUtils.subscribeWithDialog(getV().getContext(), fa, getV().<GameAllResultsData>bindToLifecycle(), new Consumer<GameAllResultsData>() {
            @Override
            public void accept(GameAllResultsData gameAllResultsData) throws Exception {
                List<ItemType> list = new ArrayList<>();
                list.addAll(gameAllResultsData.classicalResults.list);
                list.addAll(gameAllResultsData.commonResults.list);
                getV().showData(list);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                getV().showError(netError);
            }
        });
    }

    public class GameAllResultsData extends XBaseModel {

        ClassicalResults classicalResults;
        CommonResults commonResults;

        public GameAllResultsData(ClassicalResults classicalResults, CommonResults commonResults) {
            this.classicalResults = classicalResults;
            this.commonResults = commonResults;
        }
    }
}

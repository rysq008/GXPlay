package com.game.helper.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.NotConcernResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.UpdateSignatrueRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.views.EditInputView;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateSignFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = UpdateSignFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.et_sign)
    EditText mSign;
    @BindView(R.id.action_bar_setting)
    RelativeLayout mSetting;
    @BindView(R.id.tv_action)
    TextView mHeadAction;
    @BindView(R.id.iv_action)
    ImageView mHeadActionImg;

    public static UpdateSignFragment newInstance(){
        return new UpdateSignFragment();
    }

    public UpdateSignFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_update_sign;
    }

    private void initView(){
        mHeadTittle.setText(getResources().getString(R.string.common_update_sign));
        mSetting.setVisibility(View.VISIBLE);
        mHeadActionImg.setVisibility(View.GONE);
        mHeadAction.setVisibility(View.VISIBLE);
        mHeadAction.setText("保存");
        mSetting.setOnClickListener(this);
        mHeadBack.setOnClickListener(this);
        getData();
    }

    private void getData() {
        if (getArguments() == null) return;
        String sign = getArguments().getString(TAG);
        if (sign != null && sign.length() != 0) mSign.setText(sign);
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack){
            getActivity().onBackPressed();
        }
        if (v == mSetting) {
            String sign = mSign.getText().toString();
            if (sign.length() == 0) return;
            updateSign(sign);
        }
    }

    private void updateSign(String sign) {
        Flowable<HttpResultModel<NotConcernResults>> fr = DataService.updateSignatrue(new UpdateSignatrueRequestBody(sign));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<NotConcernResults>>() {
            @Override
            public void accept(HttpResultModel<NotConcernResults> notConcernResultsHttpResultModel) throws Exception {
                if (notConcernResultsHttpResultModel.isSucceful()) {
                    getActivity().onBackPressed();
                } else {
                }
                Toast.makeText(getContext(), notConcernResultsHttpResultModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    @Override
    public Object newP() {
        return null;
    }
}

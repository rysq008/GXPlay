package com.game.helper.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
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
import com.game.helper.net.model.UpdateNicknameRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.Utils;
import com.game.helper.views.EditInputView;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateNicknameFragment extends XBaseFragment implements View.OnClickListener {
    public static final String TAG = UpdateNicknameFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.action_bar_setting)
    RelativeLayout mSetting;
    @BindView(R.id.tv_action)
    TextView mHeadAction;
    @BindView(R.id.iv_action)
    ImageView mHeadActionImg;
    @BindView(R.id.et_nickname)
    EditInputView mNickname;

    public static UpdateNicknameFragment newInstance() {
        return new UpdateNicknameFragment();
    }

    public UpdateNicknameFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_update_nickname;
    }

    private void initView() {
        mHeadTittle.setText(getResources().getString(R.string.common_update_nickname));
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
        String nickname = getArguments().getString(TAG);
        if (nickname != null && nickname.length() != 0) mNickname.setText(nickname);
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack) {
            getActivity().onBackPressed();
        }
        if (v == mSetting) {
            String nickname = mNickname.getText().toString();
            if (nickname.length() == 0) return;
            updateNickName(nickname);
        }
    }

    private void updateNickName(String nickname) {
        if (Utils.hasRestrictionString(nickname)) {
            Toast.makeText(getContext(), "字符限制！请检查输入", Toast.LENGTH_SHORT).show();
            return;
        }

        Flowable<HttpResultModel<NotConcernResults>> fr = DataService.updateNickname(new UpdateNicknameRequestBody(nickname));
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

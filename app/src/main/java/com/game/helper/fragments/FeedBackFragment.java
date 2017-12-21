package com.game.helper.fragments;

import android.opengl.ETC1;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.NotConcernResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.FeedbackRequestBody;
import com.game.helper.net.model.UpdateGenderRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.StringUtils;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class FeedBackFragment extends XBaseFragment implements View.OnClickListener{
    private static final String TAG = FeedBackFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.et_content)
    EditText mCntent;
    @BindView(R.id.tv_commit)
    View mSubmit;
    @BindView(R.id.ll_feedback)
    View mFeedBack;

    public static FeedBackFragment newInstance(){
        return new FeedBackFragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_feed_back;
    }

    private void initView(){
        mHeadTittle.setText(getResources().getString(R.string.feedback_tittle));
        mHeadBack.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mFeedBack.setOnClickListener(this);
        mCntent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSubmit.setSelected(StringUtils.isEmpty(s) ? false : true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack){
            getActivity().onBackPressed();
        }
        if (v == mSubmit){
            submit();
        }
        if (v == mFeedBack){
            DetailFragmentsActivity.launch(getContext(),null,FeedBackListFragment.newInstance());
        }
    }

    private void submit(){
        String content = mCntent.getText().toString();
        if (StringUtils.isEmpty(content)){
            Toast.makeText(getContext(), "请输入您的宝贵意见和建议", Toast.LENGTH_SHORT).show();
            return;
        }

        Flowable<HttpResultModel<NotConcernResults>> fr = DataService.feedBack(new FeedbackRequestBody(content));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<NotConcernResults>>() {
            @Override
            public void accept(HttpResultModel<NotConcernResults> notConcernResultsHttpResultModel) throws Exception {
                if (notConcernResultsHttpResultModel.isSucceful()) {
                    mCntent.setText("");
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

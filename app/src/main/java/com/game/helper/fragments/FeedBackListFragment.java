package com.game.helper.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.FeedbackListResults;
import com.game.helper.model.NotConcernResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.FeedbackRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class FeedBackListFragment extends XBaseFragment implements View.OnClickListener {
    private static final String TAG = FeedBackListFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.rc_feedback_list)
    RecyclerView mList;

    private List<FeedbackListResults.FeedbackItem> mData = new ArrayList<>();
    private FeedBackListAdapter adapter;

    public static FeedBackListFragment newInstance() {
        return new FeedBackListFragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_feed_back_list;
    }

    private void initView() {
        mHeadTittle.setText(getResources().getString(R.string.feedback_list_tittle));
        mHeadBack.setOnClickListener(this);

        initList();
        feedbackList();
    }

    private void initList() {
        adapter = new FeedBackListAdapter();
        mList.setAdapter(adapter);
        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.setHasFixedSize(true);
        mList.setItemAnimator(new DefaultItemAnimator());
    }

    private void feedbackList() {
        Flowable<HttpResultModel<FeedbackListResults>> fr = DataService.feedBackList();
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<FeedbackListResults>>() {
            @Override
            public void accept(HttpResultModel<FeedbackListResults> feedbackListResultsHttpResultModel) throws Exception {
                if (feedbackListResultsHttpResultModel.isSucceful()) {
                    adapter.notifyData(feedbackListResultsHttpResultModel.data.list);
                } else {
                    Toast.makeText(getContext(), feedbackListResultsHttpResultModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public Object newP() {
        return null;
    }

    class FeedBackListAdapter extends RecyclerView.Adapter<FeedbackHolder> {

        @Override
        public FeedbackHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater from = LayoutInflater.from(parent.getContext());
            return new FeedbackHolder(from.inflate(R.layout.layout_feed_back_list_item, null, false));
        }

        @Override
        public void onBindViewHolder(FeedbackHolder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public void notifyData(List<FeedbackListResults.FeedbackItem> data) {
            if (data != null && data.size() != 0) {
                mData = data;
                notifyDataSetChanged();
            }
        }
    }

    class FeedbackHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public int position;
        public View rootView;
        public TextView content;
        public TextView reply;
        public View cancel;
        public View confirm;

        public FeedbackHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            content = itemView.findViewById(R.id.tv_content);
            reply = itemView.findViewById(R.id.tv_reply);
            cancel = itemView.findViewById(R.id.tv_cancel);
            confirm = itemView.findViewById(R.id.tv_confirm);
        }

        void onBind(int position) {
            FeedbackListResults.FeedbackItem feedbackItem = mData.get(position);
            content.setText(feedbackItem.content);
            reply.setVisibility(StringUtils.isEmpty(feedbackItem.reply_content) ? View.GONE : View.VISIBLE);
            reply.setText(feedbackItem.reply_content);

            cancel.setOnClickListener(this);
            confirm.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getContext(), "感谢您的反馈！", Toast.LENGTH_SHORT).show();
            if (v == cancel){

            }
            if (v == confirm){

            }
        }
    }
}

package com.game.helper.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.NotConcernResults;
import com.game.helper.model.SystemMessageResults;
import com.game.helper.model.PlatformMessageResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.SinglePageRequestBody;
import com.game.helper.net.model.UpdateMsgStatusRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.StringUtils;
import com.game.helper.views.XReloadableRecyclerContentLayout;
import com.game.helper.views.widget.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends XBaseFragment implements View.OnClickListener {
    public static final String TAG = MessageFragment.class.getSimpleName();
    public static final int Type_Platform = 0;
    public static final int Type_System = 1;

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.iv_platform)
    ImageView mPlatform;
    @BindView(R.id.iv_system)
    ImageView mSystem;

    @BindView(R.id.rc_list)
    XReloadableRecyclerContentLayout mContent;

    private MessageAdapter mAdapter;
    private List mData = new ArrayList();
    private int type = Type_Platform;

    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_message;
    }

    private void initView() {
        mHeadTittle.setText("消息");
        mHeadBack.setOnClickListener(this);
        mPlatform.setOnClickListener(this);
        mSystem.setOnClickListener(this);
        mPlatform.setSelected(true);
        mSystem.setSelected(false);

        mContent.showLoading();
        initList();
        getDataFromNet(1);
    }

    private void initList() {
        mContent.getLoadingView().setVisibility(View.GONE);
        mContent.getRecyclerView().setHasFixedSize(true);
        mContent.getRecyclerView().verticalLayoutManager(context);
        mContent.getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        mContent.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                getDataFromNet(1);
            }

            @Override
            public void onLoadMore(int page) {
                getDataFromNet(page);
            }
        });
        mContent.getRecyclerView().useDefLoadMoreView();
        setmAdapter();
    }

    /**
     * 切换数据显示模式
     */
    private void switchDataType() {
        resetList();
        if (type == Type_Platform) {
            type = Type_System;
        } else if (type == Type_System) {
            type = Type_Platform;
        }
        setmAdapter();
        getDataFromNet(1);
    }

    /**
     * 重设adapter
     */
    private void setmAdapter() {
        mAdapter = null;
        mAdapter = new MessageAdapter(type);
        mContent.getRecyclerView().setAdapter(null);
        mContent.getRecyclerView().setAdapter(mAdapter);
    }

    /**
     * 获取数据
     */
    private void getDataFromNet(final int page) {
        //平台消息
        if (type == Type_System) {
            Flowable<HttpResultModel<SystemMessageResults>> fr = DataService.getSystemMessage(new SinglePageRequestBody(page));
            RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<SystemMessageResults>>() {
                @Override
                public void accept(HttpResultModel<SystemMessageResults> systemMessageResultsHttpResultModel) throws Exception {
                    if (!systemMessageResultsHttpResultModel.isSucceful()) {
                        Toast.makeText(getContext(), systemMessageResultsHttpResultModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (page == 1) mData = systemMessageResultsHttpResultModel.data.list;
                    else mData.addAll(systemMessageResultsHttpResultModel.data.list);
                    notifyData();
                    mContent.getRecyclerView().setPage(systemMessageResultsHttpResultModel.current_page, systemMessageResultsHttpResultModel.total_page);
                }
            }, new Consumer<NetError>() {
                @Override
                public void accept(NetError netError) throws Exception {
                    showError(netError);
                    Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
                }
            });
        }

        //系统消息
        if (type == Type_Platform) {
            Flowable<HttpResultModel<PlatformMessageResults>> fr = DataService.getPlatformMessage(new SinglePageRequestBody(page));
            RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<PlatformMessageResults>>() {
                @Override
                public void accept(HttpResultModel<PlatformMessageResults> platformMessageResultsHttpResultModel) throws Exception {
                    if (!platformMessageResultsHttpResultModel.isSucceful()) {
                        Toast.makeText(getContext(), platformMessageResultsHttpResultModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (page == 1) mData = platformMessageResultsHttpResultModel.data.list;
                    else mData.addAll(platformMessageResultsHttpResultModel.data.list);
                    notifyData();
                    mContent.getRecyclerView().setPage(platformMessageResultsHttpResultModel.current_page, platformMessageResultsHttpResultModel.total_page);
                }
            }, new Consumer<NetError>() {
                @Override
                public void accept(NetError netError) throws Exception {
                    showError(netError);
                    Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
                }
            });
        }
    }

    private void notifyData() {
        mAdapter.notifyDataSetChanged();
        mContent.getLoadingView().setVisibility(View.GONE);
        mContent.refreshState(false);
        if (mAdapter.getItemCount() < 1) {
            mContent.showEmpty();
            return;
        } else {
            mContent.showContent();
        }
    }

    public void showError(NetError error) {
        mContent.getLoadingView().setVisibility(View.GONE);
        mContent.refreshState(false);
        mContent.showError();
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack) {
            getActivity().onBackPressed();
        }
        if (v == mPlatform) {
            if (mPlatform.isSelected()) return;
            mPlatform.setSelected(true);
            mSystem.setSelected(false);
            switchDataType();
        }
        if (v == mSystem) {
            if (mSystem.isSelected()) return;
            mSystem.setSelected(true);
            mPlatform.setSelected(false);
            switchDataType();
        }
    }

    @Override
    public Object newP() {
        return null;
    }

    /**
     * 清除adapter
     */
    private void resetList() {
        mData.clear();
        mAdapter.notifyDataSetChanged();
        mAdapter = null;
    }

    class MessageAdapter extends RecyclerView.Adapter {
        private int mode;
        //存放所有已经打开的菜单
        public List<SwipeLayout> openList = new ArrayList<SwipeLayout>();

        public MessageAdapter(int type) {
            mode = type;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if (mode == Type_Platform) {
                return new PlatformMessageHolder(inflater.inflate(R.layout.item_message_platform, parent, false));
            }
            return new SystemMessageHolder(inflater.inflate(R.layout.item_message_system, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof PlatformMessageHolder) {
                PlatformMessageHolder paltform = (PlatformMessageHolder) holder;
                paltform.onBind(position);
            }
            if (holder instanceof SystemMessageHolder) {
                SystemMessageHolder system = (SystemMessageHolder) holder;
                system.onBind(position);
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class PlatformMessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private View rootView;
            public TextView mTittle;
            public TextView mTime;

            public PlatformMessageHolder(View itemView) {
                super(itemView);
                mTittle = itemView.findViewById(R.id.tv_tittle);
                mTime = itemView.findViewById(R.id.tv_time);
                rootView = itemView;
            }

            void onBind(int position) {
                PlatformMessageResults.PlatformMessageItem results = (PlatformMessageResults.PlatformMessageItem) mData.get(position);
                rootView.setOnClickListener(this);
                rootView.setTag(results);
                mTittle.setText(results.title);
                mTime.setText(results.create_time);
            }

            @Override
            public void onClick(View v) {
                if (v == rootView) {
                    PlatformMessageResults.PlatformMessageItem item = (PlatformMessageResults.PlatformMessageItem) rootView.getTag();
                    Bundle bundle = new Bundle();
                    bundle.putString(MessageDescFragment.TITTLE, item.title);
                    bundle.putString(MessageDescFragment.CONTENT, item.content);
                    MessageDescFragment messageDescFragment = MessageDescFragment.newInstance();
                    messageDescFragment.setArguments(bundle);
                    DetailFragmentsActivity.launch(getContext(), bundle, messageDescFragment);
                }
            }
        }

        class SystemMessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private View rootView;
            private SwipeLayout mSwipeLayout;
            private View mSwipeDelete;
            public View contentView;
            public TextView mTittle;
            public TextView mTime;
            public TextView mContent;
            public ImageView mArrow;
            private int itemPosition;

            public SystemMessageHolder(View itemView) {
                super(itemView);
                rootView = itemView.findViewById(R.id.root_layout);
                mSwipeLayout = itemView.findViewById(R.id.swipeLayout);
                mSwipeDelete = itemView.findViewById(R.id.tv_del);
                mTittle = itemView.findViewById(R.id.tv_tittle);
                mTime = itemView.findViewById(R.id.tv_time);
                mContent = itemView.findViewById(R.id.tv_content);
                contentView = itemView.findViewById(R.id.ll_content);
                mArrow = itemView.findViewById(R.id.iv_arrow);
            }

            void onBind(int position) {
                this.itemPosition = position;
                SystemMessageResults.SystemMessageItem results = (SystemMessageResults.SystemMessageItem) mData.get(position);
                rootView.setOnClickListener(this);
                mTittle.setText(results.title);
                mTime.setText(StringUtils.isEmpty(results.create_time) ? "" : results.create_time);
                mContent.setText(results.content);
                mSwipeDelete.setTag(results);
                mSwipeDelete.setOnClickListener(this);
                mSwipeLayout.setSwipeChangeListener(new SwipeLayout.OnSwipeChangeListener() {

                    @Override
                    public void onStartOpen(SwipeLayout mSwipeLayout) {
                        for (SwipeLayout layout : openList) {
                            layout.close();
                        }
                        openList.clear();
                    }

                    @Override
                    public void onStartClose(SwipeLayout mSwipeLayout) {

                    }

                    @Override
                    public void onOpen(SwipeLayout mSwipeLayout) {
                        openList.add(mSwipeLayout);
                    }

                    @Override
                    public void onDraging(SwipeLayout mSwipeLayout) {

                    }

                    @Override
                    public void onClose(SwipeLayout mSwipeLayout) {
                        openList.remove(mSwipeLayout);
                    }
                });
            }

            @Override
            public void onClick(View v) {
                if (v == rootView) {
                    mSwipeLayout.close();
                    mArrow.setSelected(!mArrow.isSelected());
                    contentView.setVisibility(mArrow.isSelected() ? View.VISIBLE : View.GONE);
                }
                if (v == mSwipeDelete) {
                    mSwipeLayout.close();
                    SystemMessageResults.SystemMessageItem results = (SystemMessageResults.SystemMessageItem) mSwipeDelete.getTag();
                    updateMsgStatus(results.id + "");
                }
            }

            private void updateMsgStatus(String id) {
                Flowable<HttpResultModel<NotConcernResults>> fr = DataService.updateMsgStatus(new UpdateMsgStatusRequestBody(id, UpdateMsgStatusRequestBody.MESSAGE_SYSTEM, UpdateMsgStatusRequestBody.MESSAGE_OPTION_DELETE));
                RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<NotConcernResults>>() {
                    @Override
                    public void accept(HttpResultModel<NotConcernResults> notConcernResultsHttpResultModel) throws Exception {
                        Toast.makeText(getContext(), notConcernResultsHttpResultModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
                        if (notConcernResultsHttpResultModel.isSucceful()) {
                            mData.remove(itemPosition);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }, new Consumer<NetError>() {
                    @Override
                    public void accept(NetError netError) throws Exception {
                        //showError(netError);
                        Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
                    }
                });
            }
        }
    }
}

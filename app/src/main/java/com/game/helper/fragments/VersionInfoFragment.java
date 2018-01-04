package com.game.helper.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.VersionInfoResults;
import com.game.helper.net.DataService;
import com.game.helper.net.api.Api;
import com.game.helper.net.model.SinglePageRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.Utils;
import com.game.helper.views.XReloadableRecyclerContentLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class VersionInfoFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = VersionInfoFragment.class.getSimpleName();
    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.rc_extension_list)
    RecyclerView mContent;
    private VersionHistoryAdapter mAdapter;

    public static VersionInfoFragment newInstance(){
        return new VersionInfoFragment();
    }

    public VersionInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_version_info;
    }

    private void initView(){
        mHeadTittle.setText(getResources().getString(R.string.common_about_version));
        mHeadBack.setOnClickListener(this);
        initList();
        getDataFromNet();
    }

    private void initList(){
        mAdapter = null;
        mAdapter = new VersionHistoryAdapter(getContext(), null);
        mContent.setHasFixedSize(true);
        mContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mContent.setItemAnimator(new DefaultItemAnimator());
        mContent.setAdapter(mAdapter);
    }

    /**
     * 获取数据
     * */
    private void getDataFromNet(){
        Flowable<HttpResultModel<VersionInfoResults>> fr = DataService.getVersionInfo();
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<VersionInfoResults>>() {
            @Override
            public void accept(HttpResultModel<VersionInfoResults> versionInfoResultsHttpResultModel) throws Exception {
                notifyData(versionInfoResultsHttpResultModel.data);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    private void notifyData(VersionInfoResults data){
        mAdapter.setData(data,true);
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack){
            getActivity().onBackPressed();
        }
    }

    class VersionHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List data = new ArrayList();
        private Context context;

        public VersionHistoryAdapter(Context context, List data) {
            this.context = context;
            if (data != null) this.data = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_item_version_info, parent, false);
            return new VersionHistoryHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof VersionHistoryHolder){
                VersionHistoryHolder extensionHistoryHolder = (VersionHistoryHolder) holder;
                extensionHistoryHolder.onBind(position);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void setData(VersionInfoResults data, boolean clear){
            if (data == null) return;
            List list = data.list;
            if (clear) this.data = list;
            else this.data.addAll(list);
            notifyDataSetChanged();
        }

        class VersionHistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private int position = 0;
            private View rootView;
            private TextView name;
            private TextView content;

            public VersionHistoryHolder(View view) {
                super(view);
                rootView = view;
                name = view.findViewById(R.id.tv_name);
                content = view.findViewById(R.id.tv_content);
            }

            public void onBind(int position){
                this.position = position;
                rootView.setOnClickListener(this);
                VersionInfoResults.VersionInfoItem item = (VersionInfoResults.VersionInfoItem) data.get(position);
                name.setText(getResources().getString(R.string.app_name) +"\t"+ item.version);
                content.setText(item.description.replace(" ","\n"));
            }

            @Override
            public void onClick(View v) {
            }
        }
    }
}

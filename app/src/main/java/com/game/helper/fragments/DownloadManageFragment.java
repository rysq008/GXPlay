package com.game.helper.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.DownLoad.DownloadController;
import com.game.helper.model.DownLoad.DownloadItem;
import com.game.helper.net.api.Api;
import com.game.helper.utils.DownLoadReceiveUtils;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.views.XReloadableRecyclerContentLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadEvent;
import zlc.season.rxdownload2.entity.DownloadRecord;
import zlc.season.rxdownload2.entity.DownloadStatus;
import zlc.season.rxdownload2.function.Utils;

import static zlc.season.rxdownload2.function.Utils.empty;

public class DownloadManageFragment extends XBaseFragment {
    private static final String TAG = DownloadManageFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.rc_download_list)
    XReloadableRecyclerContentLayout mContent;

    private RxDownload rxDownload;
    private DownloadAdapter mAdapter;

    public static DownloadManageFragment newInstance() {
        return new DownloadManageFragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_download_mannager;
    }

    private void initView() {
        mHeadTittle.setText(getResources().getString(R.string.mine_name_6));
        initList();
        loadData(true);
    }

    private void initList() {
        rxDownload = RxDownload.getInstance(getActivity());
        if (null == mAdapter)
            mAdapter = new DownloadAdapter(context);
        mContent.getRecyclerView().setHasFixedSize(true);
        mContent.getRecyclerView().verticalLayoutManager(context);
        mContent.getRecyclerView().setAdapter(mAdapter);
        mContent.refreshEnabled(false);
        mContent.getRecyclerView().useDefLoadMoreView();
        mContent.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                loadData(false);
            }

            @Override
            public void onLoadMore(int page) {
                loadData(false);
            }
        });
        ((SimpleItemAnimator) mContent.getRecyclerView().getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    public Object newP() {
        return null;
    }

    @OnClick({R.id.start, R.id.pause, R.id.action_bar_back})
    public void onClick(View view) {
        List<DownloadItem> list = mAdapter.getDataSource();
        switch (view.getId()) {
            case R.id.start:
                for (DownloadItem each : list) {
                    rxDownload.serviceDownload(each.record.getUrl())
                            .subscribe(new Consumer<Object>() {
                                @Override
                                public void accept(Object o) throws Exception {

                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    Utils.log(throwable);
                                }
                            });
                }
                break;
            case R.id.pause:
                for (DownloadItem each : list) {
                    rxDownload.pauseServiceDownload(each.record.getUrl())
                            .subscribe(new Consumer<Object>() {
                                @Override
                                public void accept(Object o) throws Exception {

                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    Utils.log(throwable);
                                }
                            });
                }
                break;
            case R.id.action_bar_back:
                getActivity().onBackPressed();
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        List<DownloadItem> list = mAdapter.getDataSource();
        for (DownloadItem each : list) {
            Utils.dispose(each.disposable);
        }
    }

    private void loadData(boolean showloading) {
        Observable<List<DownloadItem>> observable = rxDownload.getTotalDownloadRecords()
                .map(new Function<List<DownloadRecord>, List<DownloadItem>>() {
                    @Override
                    public List<DownloadItem> apply(List<DownloadRecord> downloadRecords) throws Exception {
                        List<DownloadItem> result = new ArrayList<>();
                        for (DownloadRecord each : downloadRecords) {
                            DownloadItem bean = new DownloadItem();
                            bean.record = each;
                            result.add(bean);
                        }
                        return result;
                    }
                });
        RxLoadingUtils.subscribeWithReloadObserver(mContent, observable, this.bindToLifecycle(), new Consumer<List<DownloadItem>>() {
            @Override
            public void accept(List<DownloadItem> downloadItems) throws Exception {
                mAdapter.setData(downloadItems);
                mContent.getRecyclerView().setPage(1, 1);
                if (mAdapter.getItemCount() < 1) {
                    mContent.showEmpty();
                } else {
                    mContent.showContent();
                }
            }
        }, null, null, showloading);
//        rxDownload.getTotalDownloadRecords()
//                .map(new Function<List<DownloadRecord>, List<DownloadItem>>() {
//                    @Override
//                    public List<DownloadItem> apply(List<DownloadRecord> downloadRecords) throws Exception {
//                        List<DownloadItem> result = new ArrayList<>();
//                        for (DownloadRecord each : downloadRecords) {
//                            DownloadItem bean = new DownloadItem();
//                            bean.record = each;
//                            result.add(bean);
//                        }
//                        return result;
//                    }
//                })
//                .subscribe(new Consumer<List<DownloadItem>>() {
//                    @Override
//                    public void accept(List<DownloadItem> downloadBeen) throws Exception {
//                        mAdapter.addData(downloadBeen);
//                        mContent.showContent();
//                    }
//                });
    }


    class DownloadAdapter extends SimpleRecAdapter<DownloadItem, DownloadAdapter.DownloadHolder> {

        public DownloadAdapter(Context context) {
            super(context);
        }

        @Override
        public DownloadHolder newViewHolder(View itemView) {
            return new DownloadHolder(itemView);
        }

        @Override
        public int getLayoutId() {
            return R.layout.layout_download_list_item;
        }

        @Override
        public void onBindViewHolder(DownloadHolder holder, int position) {
            DownloadItem item = getDataSource().get(position);
            holder.setDisplay(item);
        }

        class DownloadHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.img)
            ImageView mImg;
            @BindView(R.id.percent)
            TextView mPercent;
            @BindView(R.id.progress)
            ProgressBar mProgress;
            @BindView(R.id.size)
            TextView mSize;
            @BindView(R.id.status)
            TextView mStatusText;
            @BindView(R.id.action)
            Button mActionButton;
            @BindView(R.id.name)
            TextView mName;
            @BindView(R.id.more)
            Button mMore;

            private final DownloadController mDownloadController;
            private DownloadItem mData;
            private int flag;

            public DownloadHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                mDownloadController = new DownloadController(mStatusText, mActionButton);
            }

            public void setDisplay(DownloadItem param) {
                this.mData = param;
                if (empty(param.record.getExtra1())) {
                } else {
                    ILFactory.getLoader().loadNet(mImg, Api.API_PAY_OR_IMAGE_URL.concat(mData.record.getExtra1()), null);
                }

                String name = empty(param.record.getExtra2()) ? param.record.getSaveName() : param.record.getExtra2();
                mName.setText(name);

                Utils.log(mData.record.getUrl());
//                Observable<DownloadEvent> replayDownloadStatus = rxDownload.receiveDownloadStatus(mData.record.getUrl())
//                        .replay()
//                        .autoConnect();
//                Observable<DownloadEvent> sampled = replayDownloadStatus
//                        .filter(new Predicate<DownloadEvent>() {
//                            @Override
//                            public boolean test(@NonNull DownloadEvent downloadEvent) throws Exception {
//                                return downloadEvent.getFlag() == DownloadFlag.STARTED;
//                            }
//                        })
//                        .throttleFirst(200, TimeUnit.MILLISECONDS);
//                Observable<DownloadEvent> noProgress = replayDownloadStatus
//                        .filter(new Predicate<DownloadEvent>() {
//                            @Override
//                            public boolean test(@NonNull DownloadEvent downloadEvent) throws Exception {
//                                return downloadEvent.getFlag() != DownloadFlag.STARTED;
//                            }
//                        });
//                mData.disposable = Observable.merge(sampled, noProgress)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<DownloadEvent>() {
//                            @Override
//                            public void accept(@NonNull DownloadEvent downloadEvent) throws Exception {
//                                if (flag != downloadEvent.getFlag()) {
//                                    flag = downloadEvent.getFlag();
//                                    log("all events:" + downloadEvent.getFlag());
//                                }
//                                if (downloadEvent.getFlag() == DownloadFlag.FAILED) {
//                                    Throwable throwable = downloadEvent.getError();
//                                    Log.w("TAG", throwable);
//                                }
//                                mDownloadController.setEvent(downloadEvent);
//                                updateProgressStatus(downloadEvent.getDownloadStatus());
//                            }
//                        });
                DownLoadReceiveUtils.receiveDownloadEvent(context, mData.record.getUrl(), mData.disposable, mDownloadController, new DownLoadReceiveUtils.OnDownloadEventReceiveListener() {
                    @Override
                    public void receiveDownloadEvent(DownloadEvent event, boolean isDisposable) {
                        updateProgressStatus(event.getDownloadStatus());
                        if (isDisposable) {

                        }
                    }
                });
            }

            @OnClick({R.id.action, R.id.more})
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.action:
                        mDownloadController.handleClick(new DownloadController.Callback() {
                            @Override
                            public void startDownload() {
                                DownLoadReceiveUtils.startDownload(context, getRxPermissions(), mData);
                            }

                            @Override
                            public void pauseDownload() {
                                DownLoadReceiveUtils.pauseDownload(context, mData.record.getUrl());
                            }

                            @Override
                            public void cancelDownload() {

                            }

                            @Override
                            public void installApk() {
                                DownLoadReceiveUtils.installApk(context, mData.record.getUrl());
                            }

                            @Override
                            public void openApp() {
                                Toast.makeText(context, "open", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case R.id.more:
                        showPopUpWindow(view);
                        break;
                }
            }

            private void updateProgressStatus(DownloadStatus status) {
                mProgress.setIndeterminate(status.isChunked);
                mProgress.setMax((int) status.getTotalSize());
                mProgress.setProgress((int) status.getDownloadSize());
                mPercent.setText(status.getPercent());
                mSize.setText(status.getFormatStatusString());
            }

            private void showPopUpWindow(View view) {
                final ListPopupWindow listPopupWindow = new ListPopupWindow(context);
                listPopupWindow.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                        new String[]{"删除"}));
                listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int pos, long id) {
                        if (pos == 0) {
                            DownLoadReceiveUtils.deleteDownload(context, mData.disposable, mData.record.getUrl(), new DownLoadReceiveUtils.OnDownloadEventReceiveListener() {
                                @Override
                                public void receiveDownloadEvent(DownloadEvent event, boolean isDisposable) {
                                    mAdapter.removeElement(pos);
                                }
                            });
                            listPopupWindow.dismiss();
                        }
                    }
                });
                listPopupWindow.setWidth(200);
                listPopupWindow.setAnchorView(view);
                listPopupWindow.setModal(false);
                listPopupWindow.show();
            }
        }
    }
}

package com.game.helper.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.BaseFragment.XBaseFragment;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageDescFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = MessageDescFragment.class.getSimpleName();
    public static final String TITTLE = "tittle";
    public static final String CONTENT = "content";

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.webview)
    WebView webView;

    private String tittle;
    private String content;

    public static MessageDescFragment newInstance(){
        return new MessageDescFragment();
    }

    public MessageDescFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_message_desc;
    }

    private void initView(){
        if(getArguments() == null) return;
        tittle = getArguments().getString(TITTLE);
        content = getArguments().getString(CONTENT);

        mHeadTittle.setText(tittle);
        mHeadBack.setOnClickListener(this);

        webView.getSettings().setJavaScriptEnabled(true);
        //加载HTML字符串进行显示
        if(webView!=null) {
            webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack){
            getActivity().onBackPressed();
        }
    }

    @Override
    public Object newP() {
        return null;
    }
}

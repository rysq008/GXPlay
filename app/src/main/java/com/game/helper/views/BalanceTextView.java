package com.game.helper.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.game.helper.R;
import com.game.helper.data.RxConstant;
import com.game.helper.event.BusProvider;
import com.game.helper.event.MsgEvent;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

import static com.game.helper.data.RxConstant.GeneralizeModeType.Generalize_Balance_Amount_type;
import static com.game.helper.data.RxConstant.MineModeType.Mine_Balance_Wallet_type;

/**
 * Created by zr on 2017-10-13.
 */

@SuppressLint("AppCompatCustomView")
public class BalanceTextView extends TextView {

    @BindView(R.id.activity_notice_item_vflipper)
    ViewFlipper viewFlipper;

    private Context mContext;

    public BalanceTextView(Context context) {
        super(context);
        setupView(context);
    }

    public BalanceTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public BalanceTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
    }

    private void setupView(Context context) {
        BusProvider.getBus().receive(MsgEvent.class).subscribe(new Consumer<MsgEvent>() {
            @Override
            public void accept(MsgEvent msgEvent) throws Exception {
                switch (msgEvent.getType()) {
                    case Generalize_Balance_Amount_type://
                        setText(msgEvent.getMsg());
                        break;
                    case RxConstant.GeneralizeModeType.Generalize_Balance_Withdraw_type://
                        setText(msgEvent.getMsg());
                        break;
                    case RxConstant.GeneralizeModeType.Generalize_Balance_Expect_type://
                        setText(msgEvent.getMsg());
                        break;
                    case Mine_Balance_Wallet_type://
                        setText(msgEvent.getMsg());
                        break;
                }
            }
        });
    }

}

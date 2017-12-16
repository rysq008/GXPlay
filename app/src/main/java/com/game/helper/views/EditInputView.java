package com.game.helper.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.game.helper.R;
import com.game.helper.utils.StringUtils;

/**
 * Created by sung on 2017/12/12.
 *
 * 带 清空 && 密码可见 的EditText
 */

public class EditInputView extends RelativeLayout implements View.OnClickListener, View.OnFocusChangeListener {
    private static final String TAG = EditInputView.class.getSimpleName();
    private Context context;
    private EditText mContent;
    private ImageView mClear;
    private ImageView mEyes;

    //args
    public static final int Type_Account = 0;//账号输入模式
    public static final int Type_Password = 1;//密码输入模式
    public static final int Type_Code = 2;//验证码输入模式
    public static final int Type_Trade_Password = 3;//交易密码输入模式

    private boolean text_can_see = false;//内容可见
    private int type;
    private OnEditInputListener onEditInputListener;

    public EditInputView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public EditInputView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditInputView);
        type = typedArray.getInteger(R.styleable.EditInputView_eiv_type, 0);
        String hint = typedArray.getString(R.styleable.EditInputView_eiv_hint);
        int maxLength = typedArray.getInteger(R.styleable.EditInputView_eiv_maxlength,200);
        typedArray.recycle();

        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_edit_input, this);
        mContent = view.findViewById(R.id.et_content);
        mClear = view.findViewById(R.id.iv_clear);
        mEyes = view.findViewById(R.id.iv_eye);

        mEyes.setOnClickListener(this);
        mClear.setOnClickListener(this);
        mClear.setVisibility(GONE);
        mContent.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
        mContent.setHint(hint);
        mContent.setOnFocusChangeListener(this);
        mContent.addTextChangedListener(textWatcher);

        setinputType();
    }

    private void setinputType(){
        //  设置输入模式
        switch (type) {
            case Type_Account:
                this.mContent.setInputType(InputType.TYPE_CLASS_TEXT);
                this.mEyes.setVisibility(GONE);
                break;
            case Type_Password:
                this.mContent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                this.mEyes.setVisibility(VISIBLE);
                this.mEyes.setSelected(false);
                break;
            case Type_Code:
                this.mContent.setInputType(InputType.TYPE_CLASS_NUMBER);
                this.mEyes.setVisibility(GONE);
                break;
            case Type_Trade_Password:
                this.mContent.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                this.mEyes.setVisibility(VISIBLE);
                this.mEyes.setSelected(false);
                break;
        }
    }

    /**
    * 切换文字显示模式
    *
    * 因设置edittext输入模式时用了setInputType() 故此处在切换暗明文时使用setTransformationMethod()防止不必要冲突
    * */
    private void switchTextVisible() {
        if (!text_can_see) {
            mContent.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            mContent.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        this.text_can_see = !text_can_see;
        this.mEyes.setSelected(!text_can_see);
    }

    /**
    * 输入监听
    * */
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (onEditInputListener != null) onEditInputListener.onTextChange(mContent);
            if (s.length() > 0 && mContent.isFocused()) {
                mClear.setVisibility(VISIBLE);
            }else {
                mClear.setVisibility(GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void onClick(View v) {
        if (v == mEyes) {
            switchTextVisible();
        }
        if (v == mClear) {
            mContent.setText("");
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) mClear.setVisibility(GONE);
    }

    public void addOnEditInputListener(OnEditInputListener onEditInputListener){
        this.onEditInputListener = onEditInputListener;
    }

    public interface OnEditInputListener{
        void onTextChange(EditText content);
    }

    /**
    * 模拟 EditText 的 getText()
    * */
    public String getText() {
        return StringUtils.isEmpty(mContent.getText().toString()) ? "" : mContent.getText().toString();
    }

    public void setInputType(int inputType){
        type = inputType;
        setinputType();
    }

    public void setText(String text){
        mContent.setText(text);
    }

    public void setHintText(String text){
        mContent.setHint(text);
    }
}

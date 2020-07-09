package com.zhny.library.presenter.work.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.zhny.library.R;
import com.zhny.library.databinding.DialogViewPictureBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.databinding.DataBindingUtil;


public class ViewPictureDialog extends AppCompatDialogFragment implements CustomImageSwitcher.LeftOrRightScrollListener {


    private Window mWindow;
    private DialogViewPictureBinding binding;
    private List<String> imgUrls;
    private int index;
    private int size, curIndex;

    public void setParams(int index, List<String> imgUrls) {
        this.imgUrls = imgUrls;
        this.index = index >= imgUrls.size() ? 0 : index;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_view_picture, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mWindow = getDialog().getWindow();

        if (mWindow != null) {
            WindowManager.LayoutParams params = mWindow.getAttributes();
            params.gravity = Gravity.CENTER;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            mWindow.setAttributes(params);
        }

        binding.isImgShow.setOnScrollListener(this);
        binding.isImgShow.setInAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        binding.isImgShow.setOutAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
        binding.isImgShow.setFactory(() -> {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return imageView;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (imgUrls != null && imgUrls.size() > 0) {
            curIndex = index;
            size = imgUrls.size();
            switchPic(curIndex);
        }
    }

    @Override
    public void onLeftOrRightScroll(int tag) {
        if (tag == CustomImageSwitcher.LEFT) {
            nextPic();
        } else if (tag == CustomImageSwitcher.RIGHT) {
            prePic();
        } else if (tag == CustomImageSwitcher.CLICK) {
            dismiss();
        }
    }

    //上一张
    private void prePic() {
        if (curIndex > 0) {
            curIndex--;
        } else {
            curIndex = imgUrls.size() - 1;
        }
        switchPic(curIndex);
    }

    //下一张
    private void nextPic() {
        if (curIndex < imgUrls.size() - 1) {
            curIndex++;
        } else {
            curIndex = 0;
        }
        switchPic(curIndex);
    }

    //切换图片
    private void switchPic(int index) {
        String msg = (index + 1) + "/" + size;
        binding.tvImgShow.setText(msg);
        binding.isImgShow.setImageUrl(imgUrls.get(index));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWindow != null) {
            mWindow.setBackgroundDrawableResource(R.color.black);
            mWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (binding != null) binding.unbind();
    }

}

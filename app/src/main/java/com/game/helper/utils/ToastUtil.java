package com.game.helper.utils;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.game.helper.GameMarketApplication;

public class ToastUtil {
	private static Toast toast;
	public static void showToast(String text){
		if(toast==null){
			toast = Toast.makeText(GameMarketApplication.getContext(), text, Toast.LENGTH_SHORT);
		}else {
			//如果toast不为空，则直接更改当前toast的文本
			toast.setText(text);
		}
		toast.show();

	}
	public static void showNetError(){
		showToast("您的网络不给力哦，请稍后再试！");

	}
	public static void showSnackbar(View view, String text){
		Snackbar.make(view,text, Snackbar.LENGTH_LONG).show();
	}



}

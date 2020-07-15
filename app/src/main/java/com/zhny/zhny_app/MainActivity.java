package com.zhny.zhny_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zhny.library.presenter.login.view.LoginActivity;
import com.zhny.zhny_app.activitys.DetailFragmentsActivity;
import com.zhny.zhny_app.fragments.LoginFragment;

import androidx.appcompat.app.AppCompatActivity;

import cn.droidlover.xdroidmvp.router.Router;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(this);
//        toLoginView();
    }


    @Override
    public void onClick(View v) {
        toLoginView();
    }

    private void toLoginView() {
//        Intent intent = new Intent(MainActivity.this, DrawTileActivity.class);
//        startActivity(intent);
        DetailFragmentsActivity.launch(this,null, LoginFragment.newInstance());
        finish();

    }

}

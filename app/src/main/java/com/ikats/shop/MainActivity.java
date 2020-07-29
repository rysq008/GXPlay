package com.ikats.shop;

import android.os.Bundle;
import android.view.View;
import com.ikats.shop.activitys.DetailFragmentsActivity;
import com.ikats.shop.fragments.LoginFragment;

import androidx.appcompat.app.AppCompatActivity;


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

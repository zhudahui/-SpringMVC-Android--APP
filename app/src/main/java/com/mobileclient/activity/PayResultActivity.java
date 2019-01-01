package com.mobileclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mobileclient.util.Utils;

public class PayResultActivity extends Activity {
    private ImageView back,search;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result_activity);
        Utils.setStatusBar(this, false, false);
        back=findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        search=findViewById(R.id.search);
        search.setVisibility(View.GONE);

    }
}

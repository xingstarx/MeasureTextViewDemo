package com.github.star.measuretextviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private Button mButtom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButtom = (Button) findViewById(R.id.show_bottom);
        mButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             BottomFragment bottomFragment = BottomFragment.newInstance();
                bottomFragment.show(getSupportFragmentManager(), BottomFragment.TAG);
            }
        });
    }

}

package com.sumail.shadow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sumail.shadow.widget.XRipple;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XRipple waveView = (XRipple) findViewById(R.id.waveView);
        waveView.setAnimation();
    }
}

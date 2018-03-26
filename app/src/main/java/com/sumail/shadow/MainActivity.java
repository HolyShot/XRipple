package com.sumail.shadow;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;

import com.sumail.shadow.widget.PullZoomView;
import com.sumail.shadow.widget.XRipple;

public class MainActivity extends BaseActivity {

    private String[] strings = {"211","1212","2112","2112","211","1212","2112","2112","211","1212","2112","2112","211","1212","2112","2112"};

        private  PullZoomView pullZoomView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*        XRipple waveView = (XRipple) findViewById(R.id.waveView);
        waveView.setAnimation();*/


        pullZoomView = (PullZoomView)findViewById(R.id.list_view);
        pullZoomView.setDividerHeight(1);
        pullZoomView.setSelector(new ColorDrawable());
        pullZoomView.setCacheColorHint(Color.TRANSPARENT);
        pullZoomView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,strings));
        pullZoomView.startAnimation();

    }


}

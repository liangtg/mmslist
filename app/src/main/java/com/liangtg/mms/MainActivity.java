package com.liangtg.mms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.aitek.app.mms.MmsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
            .add(R.id.mms_fragment_container, new MmsFragment()).commit();
    }
}

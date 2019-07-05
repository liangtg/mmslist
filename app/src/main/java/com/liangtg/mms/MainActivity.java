package com.liangtg.mms;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import com.aitek.app.mms.MmsFragment;

public class MainActivity extends AppCompatActivity {
    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = findViewById(R.id.mms_fragment_container);
        getSupportFragmentManager().beginTransaction()
            .add(R.id.mms_fragment_container, new MmsFragment()).commit();
        container.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override public void onChildViewAdded(View parent, View child) {
                if (child instanceof FilterTouch) return;
                if (container.getChildCount() < 2) return;
                container.addView(new FilterTouch(container.getContext()),
                    container.getChildCount() - 1);
                View last = container.getChildAt(container.getChildCount() - 3);
                AlphaAnimation anim = new AlphaAnimation(1, 0);
                anim.setDuration(300);
                anim.setFillEnabled(true);
                anim.setFillAfter(true);
                last.startAnimation(anim);
            }

            @Override public void onChildViewRemoved(View parent, View child) {
                if (child instanceof FilterTouch) return;
                if (container.getChildCount() < 2) return;
                View view = container.getChildAt(container.getChildCount() - 1);
                if (view instanceof FilterTouch) {
                    container.removeViewAt(container.getChildCount() - 1);
                }
                view = container.getChildAt(container.getChildCount() - 1);
                AlphaAnimation animation = new AlphaAnimation(0, 1);
                animation.setDuration(300);
                view.startAnimation(animation);
            }
        });
    }

    private static class FilterTouch extends View {
        public FilterTouch(Context context) {
            super(context);
        }

        @Override public boolean dispatchTouchEvent(MotionEvent event) {
            return true;
        }
    }
}
